package net.lintfordlib.samples.controllers;

import java.util.ArrayList;
import java.util.List;

import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.maths.MathHelper;
import net.lintfordlib.core.maths.Vector2f;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.data.GameWorld;
import net.lintfordlib.samples.data.level.CellLevel;
import net.lintfordlib.samples.data.mobs.MobDefinition;
import net.lintfordlib.samples.data.mobs.MobInstance;
import net.lintfordlib.samples.data.mobs.MobInstance.MobOrder;
import net.lintfordlib.samples.data.mobs.MobInstance.MobState;
import net.lintfordlib.samples.data.mobs.MobManager;
import net.lintfordlib.samples.data.mobs.MobTypeIndex;

public class MobController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Mob Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private MobManager mMobManager;
	private final List<MobInstance> mMobInstancesToUpdate = new ArrayList<>();

	private LevelController mLevelController;
	private PlayerController mPlayerController;
	private GameStateController mGameStateController;
	private CameraFollowController mCameraFollowController;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public MobManager mobManager() {
		return mMobManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobController(ControllerManager controllerManager, GameWorld gameWorld, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mMobManager = gameWorld.mobManager();
	}

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		mLevelController = (LevelController) core.controllerManager().getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupUid());
		mPlayerController = (PlayerController) core.controllerManager().getControllerByNameRequired(PlayerController.CONTROLLER_NAME, entityGroupUid());
		mCameraFollowController = (CameraFollowController) core.controllerManager().getControllerByNameRequired(CameraFollowController.CONTROLLER_NAME, entityGroupUid());
		mGameStateController = (GameStateController) core.controllerManager().getControllerByNameRequired(GameStateController.CONTROLLER_NAME, entityGroupUid());
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var dt = (float) core.gameTime().elapsedTimeMilli();

		final var lLevel = mLevelController.cellLevel();

		final var lMobList = mMobManager.mobs();
		final int lNumMobs = lMobList.size();

		final var lPlayerCmdr = mPlayerController.commanderInstance();

		mMobInstancesToUpdate.clear();
		for (int i = 0; i < lNumMobs; i++) {
			final var lMobInstance = lMobList.get(i);
			mMobInstancesToUpdate.add(lMobInstance);
		}

		for (int i = 0; i < lNumMobs; i++) {
			final var lMobInstance = mMobInstancesToUpdate.get(i);

			if (lMobInstance.health <= 0) {
				mMobManager.removeMobInstance(lMobInstance);
				continue;
			}

			lMobInstance.update(dt);

			updateMobHoldingGold(lMobInstance);

			if (lMobInstance.teamUid == 0 && lMobInstance.def().typeUid == MobTypeIndex.MOB_TYPE_PLAYER_DIGGER) {

				// TODO: use in-game commands BACK/ATTACK

				final var lDstFromCmdr = Math.abs(lPlayerCmdr.cx - lMobInstance.cx) + Math.abs(lPlayerCmdr.cy - lMobInstance.cy);
				final var lIsDiggerFull = lMobInstance.holdingGoldAmt >= lMobInstance.holdingGoldAmtMax;
				if (lIsDiggerFull || lDstFromCmdr > 4) {
					lMobInstance.order = MobOrder.back;

					lMobInstance.targetTileCoord = -1;
				} else {
					lMobInstance.order = MobOrder.normal;

					updateMobGetCloestsTileTarget(lMobInstance);
				}

			} else {
				updateMobGetClosestTarget(lMobInstance);
				// updateMobGetStrongestTargetInRange(lMobInstance);
			}

			if (lMobInstance.targetMob != null)
				updateMobAttack(core, lLevel, lMobInstance, lMobInstance.targetMob);

			if (lMobInstance.targetTileCoord != -1)
				updateMobDig(core, lLevel, lMobInstance, lMobInstance.targetTileCoord);

			for (int j = i + 1; j < lNumMobs; j++) {
				final var lOtherMob = mMobInstancesToUpdate.get(j);

				updateMobCollision(core, lLevel, lMobInstance, lOtherMob);
			}

			if (!lMobInstance.isPlayerControlled)
				updateMobMovement(core, lLevel, lMobInstance);

			updateMobPhysics(core, lLevel, lMobInstance);
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	private void updateMobHoldingGold(MobInstance mobInstance) {
		if (mobInstance.holdingGoldAmt > 0) {
			final var lEntryTileX = mLevelController.cellLevel().entranceTileX();
			final var lEntryTileY = mLevelController.cellLevel().entranceTileY();

			final var lCmdDistFromEntrance = Math.abs(mobInstance.cx - lEntryTileX) + Math.abs(mobInstance.cy - lEntryTileY);
			if (lCmdDistFromEntrance < 4) {
				// TODO: Cashtill sound

				// TODO: pass over time
				mGameStateController.gameState().credits += mobInstance.holdingGoldAmt;
				mobInstance.holdingGoldAmt = 0;

			}
		}
	}

	// diggers looking for closest value block
	private void updateMobGetCloestsTileTarget(MobInstance ourMob) {
		final var lLevel = mLevelController.cellLevel();
		final var lGridWide = lLevel.tilesWide();
		final var lGridHigh = lLevel.tilesHigh();

		float closestDist = Integer.MAX_VALUE;
		int maxCellDist = 3;

		ourMob.targetTileCoord = -1;

		final var cx = ourMob.cx;
		final var cy = ourMob.cy;
		for (int tx = cx - maxCellDist; tx < cx + maxCellDist; tx++) {
			for (int ty = cy - maxCellDist; ty < cy + maxCellDist; ty++) {
				final var tileCoord = ty * lLevel.tilesWide() + tx;
				final var lBlockType = lLevel.getLevelBlockType(tileCoord);

				// only interested in blocks of value
				if (lBlockType != CellLevel.LEVEL_TILE_INDEX_GOLD && lBlockType != CellLevel.LEVEL_TILE_INDEX_GEMS)
					continue;

				final var tileX = tileCoord % lGridWide;
				final var tileY = tileCoord / lGridHigh;

				// fast manhatten check
				final int manhattenDist = Math.abs(tileX - ourMob.cx) + Math.abs(tileY - ourMob.cy);
				if (manhattenDist > maxCellDist)
					continue;

				if (!gridCheckLineOfSightForDiggers(ourMob.cx, ourMob.cy, tileX, tileY))
					continue;

				if (manhattenDist < closestDist) {
					closestDist = manhattenDist;
					ourMob.targetTileCoord = tileCoord;
					ourMob.targetPosX = tx * ConstantsGame.BLOCK_SIZE + ConstantsGame.BLOCK_SIZE * .5f;
					ourMob.targetPosY = ty * ConstantsGame.BLOCK_SIZE + ConstantsGame.BLOCK_SIZE * .5f;
					ourMob.distManhatten = manhattenDist;
				}
			}
		}

		if (ourMob.targetTileCoord != -1) {
			ourMob.state = MobState.attacking;
		} else {
			ourMob.state = MobState.normal;
		}
	}

	// diggers looking for highest value block in range
	private void updateMobGetHighestTileTarget(MobInstance ourMob) {

	}

	private void updateMobGetClosestTarget(MobInstance ourMob) {
		final var lMobList = mMobManager.mobs();
		final int lNumMobs = lMobList.size();

		float closestDist = Float.MAX_VALUE;
		float maxCellDist = 7;

		ourMob.targetMob = null;

		final var xx = ourMob.xx;
		final var yy = ourMob.yy;

		for (int j = 0; j < lNumMobs; j++) {
			final var lOtherMobInstance = mMobInstancesToUpdate.get(j);

			// only interested in other mobs
			if (ourMob == lOtherMobInstance)
				continue;

			// only interested in mobs on other team
			if (ourMob.teamUid == lOtherMobInstance.teamUid)
				continue;

			// fast manhatten check
			if (Math.abs(lOtherMobInstance.cx - ourMob.cx) >= maxCellDist || Math.abs(lOtherMobInstance.cy - ourMob.cy) >= maxCellDist)
				continue;

			// only interested in visible enemies
			if (!gridCheckLineOfSight(ourMob.cx, ourMob.cy, lOtherMobInstance.cx, lOtherMobInstance.cy))
				continue;

			if (ourMob.teamUid == 0 && ourMob.uid > 0)
				System.out.println(ourMob.uid + "  state: " + ourMob.state);

			final var dst2 = Vector2f.dst2(xx, yy, lOtherMobInstance.xx, lOtherMobInstance.yy);
			if (dst2 < closestDist) {
				closestDist = dst2;
				ourMob.targetMob = lOtherMobInstance;
				ourMob.targetPosX = lOtherMobInstance.xx;
				ourMob.targetPosY = lOtherMobInstance.yy;
				ourMob.dist2Px = dst2;
			}
		}

		if (ourMob.targetMob != null) {
			ourMob.state = MobState.attacking;
		} else {
			ourMob.state = MobState.normal;
		}
	}

	private boolean gridCheckLineOfSight(int x0, int y0, int x1, int y1) {
		final var lLevel = mLevelController.cellLevel();

		final var swapXY = Math.abs(y1 - y0) > Math.abs(x1 - x0);
		int tmp;
		if (swapXY) {
			tmp = x0;
			x0 = y0;
			y0 = tmp;

			tmp = x1;
			x1 = y1;
			y1 = tmp;
		}

		if (x0 > x1) {
			tmp = x0;
			x0 = x1;
			x1 = tmp;

			tmp = y0;
			y0 = y1;
			y1 = tmp;
		}

		int deltaX = x1 - x0;
		int deltaY = (int) Math.abs(y1 - y0);
		int error = (int) (deltaX / 2.f);
		int y = y0;
		int yStep = y0 < y1 ? 1 : -1;

		if (swapXY) {
			// y / x
			for (int x = x0; x < x1 + 1; x++) {
				if (!lLevel.passable(y, x))
					return false;

				error -= deltaY;
				if (error < 0) {
					y = y + yStep;
					error = error + deltaX;
				}
			}
		} else {
			// x / y
			for (int x = x0; x < x1 + 1; x++) {
				if (!lLevel.passable(x, y))
					return false;

				error -= deltaY;
				if (error < 0) {
					y = y + yStep;
					error = error + deltaX;
				}
			}
		}

		return true;
	}

	// hack because checking the last (to dig) tile breaks in the above method
	private boolean gridCheckLineOfSightForDiggers(int x0, int y0, int x1, int y1) {
		final var lLevel = mLevelController.cellLevel();

		final var swapXY = Math.abs(y1 - y0) > Math.abs(x1 - x0);
		int tmp;
		if (swapXY) {
			tmp = x0;
			x0 = y0;
			y0 = tmp;

			tmp = x1;
			x1 = y1;
			y1 = tmp;
		}

		if (x0 > x1) {
			tmp = x0;
			x0 = x1;
			x1 = tmp;

			tmp = y0;
			y0 = y1;
			y1 = tmp;
		}

		int deltaX = x1 - x0;
		int deltaY = (int) Math.abs(y1 - y0);
		int error = (int) (deltaX / 2.f);
		int y = y0;
		int yStep = y0 < y1 ? 1 : -1;

		if (swapXY) {
			// y / x
			for (int x = x0; x < x1 + 1; x++) {
				if (!lLevel.passableForDiggers(y, x))
					return false;

				error -= deltaY;
				if (error < 0) {
					y = y + yStep;
					error = error + deltaX;
				}
			}
		} else {
			// x / y
			for (int x = x0; x < x1 + 1; x++) {
				if (!lLevel.passableForDiggers(x, y))
					return false;

				error -= deltaY;
				if (error < 0) {
					y = y + yStep;
					error = error + deltaX;
				}
			}
		}

		return true;
	}

	private void updateMobPhysics(LintfordCore core, CellLevel pLevel, MobInstance pMobInstance) {

		// TODO: should come from mobs
		final var lMaxVelocity = .05f;
		final var lMobRadius = .3f;

		pMobInstance.vx = MathHelper.clamp(pMobInstance.vx, -lMaxVelocity, lMaxVelocity);
		pMobInstance.vy = MathHelper.clamp(pMobInstance.vy, -lMaxVelocity, lMaxVelocity);

		pMobInstance.rx += pMobInstance.vx;
		pMobInstance.ry += pMobInstance.vy;

		// Grid based collision check.
		if (pMobInstance.rx < lMobRadius && pLevel.hasCollision(pMobInstance.cx - 1, pMobInstance.cy)) {
			pMobInstance.rx = lMobRadius;

			if (pMobInstance.vx < 0)
				pMobInstance.vx = 0;
		}

		if (pMobInstance.rx > 1.f - lMobRadius && pLevel.hasCollision(pMobInstance.cx + 1, pMobInstance.cy)) {
			pMobInstance.rx = 1.f - lMobRadius;

			if (pMobInstance.vx > 0)
				pMobInstance.vx = 0;
		}

		if (pMobInstance.ry < lMobRadius && pLevel.hasCollision(pMobInstance.cx, pMobInstance.cy - 1)) {
			pMobInstance.ry = lMobRadius;

			if (pMobInstance.vy < 0)
				pMobInstance.vy = 0;
		}

		if (pMobInstance.ry > 1.f - lMobRadius && pLevel.hasCollision(pMobInstance.cx, pMobInstance.cy + 1)) {
			pMobInstance.ry = 1.f - lMobRadius;

			if (pMobInstance.vy > 0)
				pMobInstance.vy = 0;

		}

		while (pMobInstance.rx < 0) {
			pMobInstance.rx++;
			pMobInstance.cx--;
		}

		while (pMobInstance.rx > 1) {
			pMobInstance.rx--;
			pMobInstance.cx++;
		}

		while (pMobInstance.ry < 0) {
			pMobInstance.ry++;
			pMobInstance.cy--;
		}

		while (pMobInstance.ry > 1) {
			pMobInstance.ry--;
			pMobInstance.cy++;
		}

		// update mob instance

		pMobInstance.xx = (float) (pMobInstance.cx + pMobInstance.rx) * ConstantsGame.BLOCK_SIZE;
		pMobInstance.yy = (float) (pMobInstance.cy + pMobInstance.ry) * ConstantsGame.BLOCK_SIZE;

		pMobInstance.vx *= .96f;
		pMobInstance.vy *= .96f;
	}

	private static float getDistSq(MobInstance entityA, MobInstance entityB) {
		final float lMobAX = entityA.xx;
		final float lMobAY = entityA.yy;

		final float lMobBX = entityB.xx;
		final float lMobBY = entityB.yy;

		final float lDistSq = Vector2f.dst2(lMobAX, lMobAY, lMobBX, lMobBY);
		return lDistSq;
	}

	private void updateMobDig(LintfordCore core, CellLevel level, MobInstance mobInstanceA, int tilecoord) {
		// check dist to target block, and dig if we're there

		if (mobInstanceA.holdingGoldAmt >= mobInstanceA.holdingGoldAmtMax)
			return; // TODO: toast and let the player know

		if (mobInstanceA.def().digAttackEnabled && mobInstanceA.isAttackTimerElapsed()) {
			final var lSwingRange = mobInstanceA.def().swingRangePx;
			final var dst2 = Vector2f.dst2(mobInstanceA.xx, mobInstanceA.yy, mobInstanceA.targetPosX, mobInstanceA.targetPosY);
			if (dst2 < lSwingRange * lSwingRange) {

				final float lAngle = (float) Math.atan2(mobInstanceA.targetPosY - mobInstanceA.yy, mobInstanceA.targetPosX - mobInstanceA.xx);
				final float lRepelPower = .75f;

				mobInstanceA.vx -= Math.cos(lAngle) * lRepelPower;
				mobInstanceA.vy -= Math.sin(lAngle) * lRepelPower;

				mobInstanceA.rx -= Math.cos(lAngle) * .05f;
				mobInstanceA.ry -= Math.sin(lAngle) * .05f;

				// pick up gold
				level.digBlock(tilecoord, (byte) 1);
				mobInstanceA.holdingGoldAmt++;

				System.out.println("raining gold baby!");

				mobInstanceA.resetAttackTimer();
			}
		}
	}

	private void updateMobAttack(LintfordCore core, CellLevel level, MobInstance mobInstanceA, MobInstance mobInstanceB) {
		if (Math.abs(mobInstanceB.cx - mobInstanceA.cx) >= 2 || Math.abs(mobInstanceB.cy - mobInstanceA.cy) >= 2)
			return;

		final var lDistBetweenMobs = getDistSq(mobInstanceA, mobInstanceB);

		// swing attacks
		if (mobInstanceA.def().swingAttackEnabled && mobInstanceA.isAttackTimerElapsed()) {
			final var lSwingRange = mobInstanceA.def().swingRangePx;
			if (lDistBetweenMobs < lSwingRange * lSwingRange) {

				mobInstanceB.dealDamage(3, true);
				System.out.println("mob swing attack");

				mobInstanceA.resetAttackTimer();
			}
		}

		// range attacks
		if (mobInstanceA.def().rangeAttackEnabled && mobInstanceA.isAttackTimerElapsed()) {
			final var lRange = mobInstanceA.def().rangeRangePx;
			if (lDistBetweenMobs < lRange * lRange) {

				// TODO: shoot projectile

				System.out.println("mob shooting an arrow");

				mobInstanceA.resetAttackTimer();
			}
		}
	}

	// mobs attacking and bumping into each other
	private void updateMobCollision(LintfordCore core, CellLevel level, MobInstance mobInstanceA, MobInstance mobInstanceB) {
		if (Math.abs(mobInstanceB.cx - mobInstanceA.cx) >= 2 || Math.abs(mobInstanceB.cy - mobInstanceA.cy) >= 2)
			return;

		final var lDistBetweenMobs = getDistSq(mobInstanceA, mobInstanceB);

		// TODO: mob b swing attack

		// both collisions

		final float lMinCollisionDist = (mobInstanceA.radiusRatio * ConstantsGame.BLOCK_SIZE) + (mobInstanceB.radiusRatio * ConstantsGame.BLOCK_SIZE);
		if (lDistBetweenMobs < lMinCollisionDist * lMinCollisionDist) {
			final float lAngle = (float) Math.atan2(mobInstanceB.yy - mobInstanceA.yy, mobInstanceB.xx - mobInstanceA.xx);
			final float lRepelPower = 0.03f;

			if (ConstantsGame.DEBUG_ENABLE_ATTACK_KNOCKBACK && !mobInstanceB.isPlayerControlled) {
				mobInstanceB.vx += Math.cos(lAngle) * lRepelPower;
				mobInstanceB.vy += Math.sin(lAngle) * lRepelPower * 0.025f;
			}

			if (ConstantsGame.DEBUG_ENABLE_ATTACK_KNOCKBACK && !mobInstanceA.isPlayerControlled) {
				mobInstanceA.vx -= Math.cos(lAngle) * lRepelPower;
				mobInstanceA.vy -= Math.sin(lAngle) * lRepelPower;
			}

			if (mobInstanceB.teamUid != mobInstanceA.teamUid) {
				if (mobInstanceB.def().damagesOnCollide)
					mobInstanceA.dealDamage(1, true);

				if (mobInstanceA.def().damagesOnCollide)
					mobInstanceB.dealDamage(1, true);
			}
		}
	}

	private void updateMobMovement(LintfordCore core, CellLevel level, MobInstance mobInstance) {
		final var dt = (float) core.gameTime().elapsedTimeMilli();

		final var lCommander = mPlayerController.commanderInstance();
		if (mobInstance == lCommander)
			return;

		if (mobInstance.teamUid == MobInstance.TEAM_ID_PLAYER) {
			if (mobInstance.state == MobState.attacking && mobInstance.targetMob != null) {
				// we have something to attack
				mobInstance.targetX = mobInstance.targetMob.xx;
				mobInstance.targetY = mobInstance.targetMob.yy;
			} else if (mobInstance.state == MobState.attacking && mobInstance.targetTileCoord != CellLevel.LEVEL_TILE_COORD_INVALID) {
				// we have a block to dig/attack
				mobInstance.targetX = mobInstance.targetPosX;
				mobInstance.targetY = mobInstance.targetPosY;
			} else {

				// follow mode (BACK order)

				switch (mobInstance.def().typeUid) {
				case MobTypeIndex.MOB_TYPE_PLAYER_DIGGER:
					mobInstance.targetX = lCommander.auxRearPosX;
					mobInstance.targetY = lCommander.auxRearPosY;

					break;
				case MobTypeIndex.MOB_TYPE_PLAYER_MELEE:
					mobInstance.targetX = lCommander.auxForwardPosX;
					mobInstance.targetY = lCommander.auxForwardPosY;
					break;
				case MobTypeIndex.MOB_TYPE_PLAYER_RANGE:
					mobInstance.targetX = lCommander.auxRearPosX;
					mobInstance.targetY = lCommander.auxRearPosY;
					break;
				}

			}

			// TODO: add a wander effect to all mobs

			// vector to target
			final var xx = mobInstance.targetX - mobInstance.xx;
			final var yy = mobInstance.targetY - mobInstance.yy;

			final var targetHeading = (float) Math.atan2(yy, xx);
			mobInstance.heading += MathHelper.turnToFace(targetHeading, mobInstance.heading, .1f);

//			mobInstance.auxForwardPosX = mobInstance.xx + (float) Math.cos(mobInstance.heading) * 10.f;
//			mobInstance.auxForwardPosY = mobInstance.yy + (float) Math.sin(mobInstance.heading) * 10.f;

			{ // bee line to target, not what we want
				// Get vector towards target

				// move unit towards target
				final var lMovementSpeed = 1f; // TODO: speed from unit
				final var len = (float) Math.sqrt(xx * xx + yy * yy);
				mobInstance.vx += (xx / len) * lMovementSpeed * dt * .001f;
				mobInstance.vy += (yy / len) * lMovementSpeed * dt * .001f;
			}

			// Check for shooting flags

		} else {
			// enemy
			// TODO: 'ai'

		}
	}

	public void startNewGame() {
		addPlayerCommander();

		// do other stuff
	}

	private void addPlayerCommander() {
		final var lPlayerMob = mMobManager.getNewMobInstance();

		// TODO: gotta be using the entrance coords

		lPlayerMob.initialise(MobDefinition.COMMANDER);
		lPlayerMob.isPlayerControlled = true;
		lPlayerMob.setPosition(32.f, 32.f);

		mPlayerController.commanderInstance(lPlayerMob);
		mCameraFollowController.setFollowEntity(lPlayerMob);
	}

	public void addPlayerMob(int mobTypeUid, float worldX, float worldY) {
		final var lPlayerMob = mMobManager.getNewMobInstance();

		lPlayerMob.setPosition(worldX, worldY);
		lPlayerMob.teamUid = MobInstance.TEAM_ID_PLAYER;

		switch (mobTypeUid) {
		default:
		case MobTypeIndex.MOB_TYPE_PLAYER_DIGGER:
			lPlayerMob.initialise(MobDefinition.PLAYER_DIGGER);
			break;

		case MobTypeIndex.MOB_TYPE_PLAYER_MELEE:
			lPlayerMob.initialise(MobDefinition.PLAYER_MELEE);
			break;

		case MobTypeIndex.MOB_TYPE_PLAYER_RANGE:
			lPlayerMob.initialise(MobDefinition.PLAYER_RANGE);
			break;
		}
	}

	public void addEnemyMob(int mobTypeUid, float worldX, float worldY) {
		final var lMobInstance = mMobManager.getNewMobInstance();

		lMobInstance.setPosition(worldX, worldY);
		lMobInstance.teamUid = MobInstance.TEAM_ID_ENEMY;

		switch (mobTypeUid) {
		default:
		case MobTypeIndex.MOB_TYPE_GOBLIN_MELEE:
			lMobInstance.initialise(MobDefinition.GOBLIN_MELEE);
			break;

		case MobTypeIndex.MOB_TYPE_GOBLIN_RANGE:
			lMobInstance.initialise(MobDefinition.GOBLIN_RANGE);
			break;
		}
	}
}
