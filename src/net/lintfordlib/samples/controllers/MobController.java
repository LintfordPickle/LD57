package net.lintfordlib.samples.controllers;

import java.util.ArrayList;
import java.util.List;

import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.controllers.core.particles.ParticleFrameworkController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.maths.MathHelper;
import net.lintfordlib.core.maths.RandomNumbers;
import net.lintfordlib.core.maths.Vector2f;
import net.lintfordlib.core.particles.particleemitters.ParticleEmitterInstance;
import net.lintfordlib.core.particles.particlesystems.ParticleSystemInstance;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.data.GameWorld;
import net.lintfordlib.samples.data.entities.CellEntity;
import net.lintfordlib.samples.data.level.CellLevel;
import net.lintfordlib.samples.data.mobs.MobDefinition;
import net.lintfordlib.samples.data.mobs.MobInstance;
import net.lintfordlib.samples.data.mobs.MobInstance.MobOrder;
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
	private ProjectileController mProjectileController;
	private CameraFollowController mCameraFollowController;
	private ParticleFrameworkController mParticleFrameworkController;
	private SoundfxController mSoundfxController;

	private ParticleSystemInstance mFootstepParticles;
	private ParticleEmitterInstance mBlockEmitter;

	private float mCashoutSoundCooldownTimer;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public MobManager mobManager() {
		return mMobManager;
	}

	public int numEnemyMobs() {
		int result = 0;

		final int lNumMobs = mMobManager.mobs().size();
		for (int i = 0; i < lNumMobs; i++) {
			if (mMobManager.mobs().get(i).teamUid > 0)
				result++;
		}

		return result;
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
		mProjectileController = (ProjectileController) core.controllerManager().getControllerByNameRequired(ProjectileController.CONTROLLER_NAME, entityGroupUid());
		mParticleFrameworkController = (ParticleFrameworkController) core.controllerManager().getControllerByNameRequired(ParticleFrameworkController.CONTROLLER_NAME, entityGroupUid());
		mSoundfxController = (SoundfxController) core.controllerManager().getControllerByNameRequired(SoundfxController.CONTROLLER_NAME, LintfordCore.CORE_ENTITY_GROUP_ID);

		final var lParticleSystemManager = mParticleFrameworkController.particleFrameworkData().particleSystemManager();
		final var lParticleEmitterManager = mParticleFrameworkController.particleFrameworkData().particleEmitterManager();

		mFootstepParticles = lParticleSystemManager.getParticleSystemByName("PS_FOOTSTEPS");
		mBlockEmitter = lParticleEmitterManager.createNewParticleEmitterFromDefinitionName("BLOCK");

	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var dt = (float) core.gameTime().elapsedTimeMilli();

		if (mCashoutSoundCooldownTimer > 0.f)
			mCashoutSoundCooldownTimer -= dt;

		final var lLevel = mLevelController.cellLevel();

		final var lMobList = mMobManager.mobs();
		final int lNumMobs = lMobList.size();

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

			// footsteps on player mobs
			if (lMobInstance.teamUid == 0 && lMobInstance.pstimer <= 0.f) {
				if (lMobInstance.def().typeUid == MobDefinition.COMMANDER.typeUid) {
					final var lIsMoving = (lMobInstance.vx * lMobInstance.vx + lMobInstance.vy * lMobInstance.vy) > 0.02f;
					if (lIsMoving)
						mSoundfxController.playSound(SoundfxController.SOUND_FOOTSTEP);
				}

				final var lHeadingX = (float) Math.cos(lMobInstance.heading);
				final var lHeadingY = (float) Math.sin(lMobInstance.heading);

				lMobInstance.pstimer = RandomNumbers.random(150, 180);

				final var stepOffsetX = lMobInstance.lstep ? lHeadingY * 2.f : -lHeadingY * 2.f;
				final var stepOffsetY = lMobInstance.lstep ? lHeadingX * 2.f : -lHeadingX * 2.f;

				mFootstepParticles.spawnParticle(lMobInstance.xx + stepOffsetX, lMobInstance.yy + stepOffsetY, .2f, lMobInstance.vx, lMobInstance.vy);
				lMobInstance.lstep = !lMobInstance.lstep;
			}

			updateMobHoldingGold(lMobInstance);

			updateMobDistFromHome(lMobInstance);

			if (lMobInstance.teamUid == 0 && lMobInstance.def().typeUid == MobTypeIndex.MOB_TYPE_PLAYER_DIGGER) {

				if (lMobInstance.order != MobOrder.retreat)
					updateMobTargetGetClosestValueTile(lMobInstance);

			} else {
				if (lMobInstance.order != MobOrder.retreat)
					updateMobGetClosestTarget(lMobInstance);

			}

			if (lMobInstance.targetMob != null)
				updateMobAttack(core, lLevel, lMobInstance, lMobInstance.targetMob);

			if (lMobInstance.targetTileCoord != -1 && lMobInstance.order == MobOrder.attack)
				updateMobDig(core, lLevel, lMobInstance, lMobInstance.targetTileCoord);

			for (int j = i + 1; j < lNumMobs; j++) {
				final var lOtherMob = mMobInstancesToUpdate.get(j);

				updateMobCollision(core, lLevel, lMobInstance, lOtherMob);
			}

			if (!lMobInstance.isPlayerControlled) {
				if (lMobInstance.teamUid == 0)
					updatePlayerMobMovement(core, lLevel, lMobInstance);
				else
					updateEnemyMobMovement(core, lLevel, lMobInstance);

			}

			updateMobPhysics(core, lLevel, lMobInstance);
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	private void updateMobDistFromHome(MobInstance mobInstance) {
		if (mobInstance.teamUid == 0) {
			final var lPlayerCmdr = mPlayerController.commanderInstance();

			final var lDstFromCmdr = Math.abs(lPlayerCmdr.cx - mobInstance.cx) + Math.abs(lPlayerCmdr.cy - mobInstance.cy);
			final var lIsDiggerFull = mobInstance.def().typeUid == MobTypeIndex.MOB_TYPE_PLAYER_DIGGER && mobInstance.holdingGoldAmt >= mobInstance.def().maxCarryAmt;
			final var lIsAttacking = mobInstance.order == MobOrder.attack;

			if (lIsDiggerFull || (lIsAttacking && lDstFromCmdr > mobInstance.def().maxAloneTravelDistInTiles)) {
				mobInstance.order = MobOrder.retreat; // return to cmdr

				mobInstance.targetTileCoord = -1;
				mobInstance.targetMob = null;

			} else if (mobInstance.order == MobOrder.retreat && !lIsDiggerFull && lDstFromCmdr < mobInstance.def().maxAloneTravelDistInTiles) {
				mobInstance.order = MobOrder.normal;
			}

		} else {
			// enemies
			final var lLevelTilesWide = mLevelController.cellLevel().tilesWide();

			final var hx = mobInstance.homeTileCoord % lLevelTilesWide;
			final var hy = mobInstance.homeTileCoord / lLevelTilesWide;

			final var lDstFromHome = Math.abs(hx - mobInstance.cx) + Math.abs(hy - mobInstance.cy);

			final var lIsAttacking = mobInstance.order == MobOrder.attack;
			final var lDstHomePx2 = (lDstFromHome * ConstantsGame.BLOCK_SIZE) * (lDstFromHome * ConstantsGame.BLOCK_SIZE);
			final var lCloserToAttacker = mobInstance.dist2Px < lDstHomePx2 * .5f;

			if (mobInstance.order == MobOrder.retreat && lDstFromHome > 2) {
				// retreat until within 2 blocks
			} else if (lDstFromHome > mobInstance.def().maxAloneTravelDistInTiles && !(lIsAttacking && lCloserToAttacker)) {
				mobInstance.order = MobOrder.retreat;

				mobInstance.targetTileCoord = -1;
				mobInstance.targetMob = null;

				mobInstance.targetX = hx * ConstantsGame.BLOCK_SIZE + ConstantsGame.BLOCK_SIZE * .5f;
				mobInstance.targetY = hy * ConstantsGame.BLOCK_SIZE + ConstantsGame.BLOCK_SIZE * .5f;
			} else {
				mobInstance.order = MobOrder.normal;
			}
		}
	}

	private void updateMobHoldingGold(MobInstance mobInstance) {
		if (mobInstance.holdingGoldAmt <= 0)
			return;

		final var lEntryTileX = mLevelController.cellLevel().entranceTileX();
		final var lEntryTileY = mLevelController.cellLevel().entranceTileY();

		final var lCmdDistFromEntrance = Math.max(Math.abs(mobInstance.cx - lEntryTileX), Math.abs(mobInstance.cy - lEntryTileY));
		if (lCmdDistFromEntrance > 2)
			return;

		if (mobInstance.goldDropTimer > 0)
			return;

		if (mCashoutSoundCooldownTimer <= 0) {
			mSoundfxController.playSound(SoundfxController.SOUND_CASHOUT);
			mCashoutSoundCooldownTimer = 1000;
		}

		// TODO: pass over time
		mGameStateController.gameState().credits++;
		mobInstance.holdingGoldAmt--;

		mobInstance.goldDropTimer = 500;
	}

	private void updateMobTargetGetClosestValueTile(MobInstance ourMob) {
		if (ourMob.order == MobOrder.retreat)
			return; // don't change retreat order

		final var lLevel = mLevelController.cellLevel();
		final var lGridWide = lLevel.tilesWide();
		final var lGridHigh = lLevel.tilesHigh();

		float closestDist = Integer.MAX_VALUE;
		final var lMaxCellDist = ourMob.def().targetSightRangeTiles;

		ourMob.targetTileCoord = -1;

		final var cx = ourMob.cx;
		final var cy = ourMob.cy;

		final var localxMin = MathHelper.clampi(cx - lMaxCellDist, 0, lGridWide);
		final var localxMax = MathHelper.clampi(cx + lMaxCellDist, localxMin, lGridWide);
		final var localyMin = MathHelper.clampi(cy - lMaxCellDist, 0, lGridWide);
		final var localyMax = MathHelper.clampi(cy + lMaxCellDist, localxMin, lGridHigh);

		for (int tx = localxMin; tx < localxMax; tx++) {
			for (int ty = localyMin; ty < localyMax; ty++) {
				final var tileCoord = ty * lLevel.tilesWide() + tx;
				final var lBlockType = lLevel.getLevelBlockType(tileCoord);

				// only interested in blocks of value
				if (lBlockType != CellLevel.LEVEL_TILE_INDEX_GOLD && lBlockType != CellLevel.LEVEL_TILE_INDEX_GEMS)
					continue;

				final var tileX = tileCoord % lGridWide;
				final var tileY = tileCoord / lGridWide;

				if (!gridCheckLineOfSightForDiggers(ourMob.cx, ourMob.cy, tileX, tileY))
					continue;

				final int chebyshevDistTiles = Math.max(Math.abs(tileX - ourMob.cx), Math.abs(tileY - ourMob.cy));
				if (chebyshevDistTiles < closestDist) {
					closestDist = chebyshevDistTiles;
					ourMob.targetTileCoord = tileCoord;
					ourMob.targetPosX = tx * ConstantsGame.BLOCK_SIZE + ConstantsGame.BLOCK_SIZE * .5f;
					ourMob.targetPosY = ty * ConstantsGame.BLOCK_SIZE + ConstantsGame.BLOCK_SIZE * .5f;
					ourMob.chebyshevDistTiles = chebyshevDistTiles;
				}
			}
		}

		if (ourMob.targetTileCoord != -1) {
			ourMob.order = MobOrder.attack;
		} else {
			if (ourMob.def().typeUid == MobTypeIndex.MOB_TYPE_PLAYER_DIGGER && ourMob.holdingGoldAmt > 0) {
				// for diggers, see if they can drop off gold at home
				updateMobTargetWhenEntranceWithinRange(ourMob);
				return;
			}

			ourMob.order = MobOrder.normal;
		}
	}

	private void updateMobTargetWhenEntranceWithinRange(MobInstance ourMob) {
		if (ourMob.order == MobOrder.retreat)
			return; // don't change retreat order

		final var lLevel = mLevelController.cellLevel();
		final var lGridWide = lLevel.tilesWide();

		ourMob.targetTileCoord = -1;

		final var lEntranceTileCoord = mLevelController.cellLevel().entranceTileCoord();
		if (lEntranceTileCoord == -1)
			return;

		final var tileX = lEntranceTileCoord % lGridWide;
		final var tileY = lEntranceTileCoord / lGridWide;

		if (!gridCheckLineOfSightForDiggers(ourMob.cx, ourMob.cy, tileX, tileY))
			return;

		// chebyshev distance (diag allowed)
		final int chebyshevDistTiles = Math.max(Math.abs(tileX - ourMob.cx), Math.abs(tileY - ourMob.cy));
		if (chebyshevDistTiles <= ourMob.def().distanceToDropOffGold) {
			ourMob.targetTileCoord = lEntranceTileCoord;
			ourMob.targetPosX = mLevelController.startWorldX();
			ourMob.targetPosY = mLevelController.startWorldY();
			ourMob.chebyshevDistTiles = chebyshevDistTiles;
		}

		if (ourMob.targetTileCoord != -1) {
			ourMob.order = MobOrder.home;
		} else {
			ourMob.order = MobOrder.normal;
		}
	}

	private void updateMobTargetGetClosestSpawnerTile(MobInstance ourMob) {
		if (ourMob.order == MobOrder.retreat)
			return; // don't change retreat order

		if (ourMob.def().typeUid != MobTypeIndex.MOB_TYPE_PLAYER_MELEE)
			return; // only search for melee

		final var lLevel = mLevelController.cellLevel();
		final var lGridWide = lLevel.tilesWide();
		final var lGridHigh = lLevel.tilesHigh();

		float lClosestDist = Integer.MAX_VALUE;
		final var lMaxCellDist = ourMob.def().targetSightRangeTiles;

		ourMob.targetTileCoord = -1;

		final var cx = ourMob.cx;
		final var cy = ourMob.cy;

		final var localxMin = MathHelper.clampi(cx - lMaxCellDist, 0, lGridWide);
		final var localxMax = MathHelper.clampi(cx + lMaxCellDist, localxMin, lGridWide);
		final var localyMin = MathHelper.clampi(cy - lMaxCellDist, 0, lGridWide);
		final var localyMax = MathHelper.clampi(cy + lMaxCellDist, localxMin, lGridHigh);

		for (int tx = localxMin; tx < localxMax; tx++) {
			for (int ty = localyMin; ty < localyMax; ty++) {

				// This is incorrect
				final var tileCoord = ty * lLevel.tilesWide() + tx;
				final var lItemTypeUid = lLevel.getItemTypeUid(tileCoord);

				// only interested in spawners
				if (lItemTypeUid != CellLevel.LEVEL_ITEMS_SPAWNER)
					continue;

				final var tileX = tileCoord % lGridWide;
				final var tileY = tileCoord / lGridWide;

				if (!gridCheckLineOfSightForDiggers(ourMob.cx, ourMob.cy, tileX, tileY))
					continue;

				final int chebyshevDistTiles = Math.max(Math.abs(tileX - ourMob.cx), Math.abs(tileY - ourMob.cy));
				if (chebyshevDistTiles < lClosestDist) {
					lClosestDist = chebyshevDistTiles;

					ourMob.targetTileCoord = tileCoord;
					ourMob.targetPosX = tx * ConstantsGame.BLOCK_SIZE + ConstantsGame.BLOCK_SIZE * .5f;
					ourMob.targetPosY = ty * ConstantsGame.BLOCK_SIZE + ConstantsGame.BLOCK_SIZE * .5f;

					ourMob.chebyshevDistTiles = chebyshevDistTiles;
				}
			}
		}

		if (ourMob.targetTileCoord != -1) {
			ourMob.order = MobOrder.attack;
		} else {
			ourMob.order = MobOrder.normal;
		}
	}

	private void updateMobGetClosestTarget(MobInstance ourMob) {
		if (ourMob.order == MobOrder.retreat)
			return; // dont change retreat order

		final var lMobList = mMobManager.mobs();
		final int lNumMobs = lMobList.size();

		float closestDist = Float.MAX_VALUE;
		float maxCellDist = 10;

		ourMob.targetMob = null;
		ourMob.targetTileCoord = -1;

		final var xx = ourMob.xx;
		final var yy = ourMob.yy;

		boolean mobFound = false;

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

			final var dst2 = Vector2f.dst2(xx, yy, lOtherMobInstance.xx, lOtherMobInstance.yy);
			if (dst2 < closestDist) {
				closestDist = dst2;
				ourMob.targetMob = lOtherMobInstance;
				ourMob.targetPosX = lOtherMobInstance.xx;
				ourMob.targetPosY = lOtherMobInstance.yy;
				ourMob.dist2Px = dst2;

				mobFound = true;
			}
		}

		if (ourMob.teamUid == 0 && mobFound == false) {
			// nothing found, see if we can target a spawn point (item)
			updateMobTargetGetClosestSpawnerTile(ourMob);
		}

		if (ourMob.def().swingAttackEnabled) {
			// TODO: this would be sight range - because the units need to run towards each other
			// final var lSwingRange = ourMob.def().swingRangePx;
			if ((ourMob.targetMob != null || ourMob.targetTileCoord != -1) /* && closestDist < lSwingRange * lSwingRange */) {
				ourMob.order = MobOrder.attack;
			} else {
				ourMob.order = MobOrder.normal;
			}
		}

		if (ourMob.def().rangeAttackEnabled) {
			final var lRangeRange = ourMob.def().rangeRangePx;
			if (ourMob.targetMob != null && closestDist < lRangeRange * lRangeRange) {
				ourMob.order = MobOrder.attack;
			} else {
				ourMob.order = MobOrder.normal;
			}
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

		final var lVelocity = pMobInstance.vx * pMobInstance.vx + pMobInstance.vy * pMobInstance.vy;

		// if the velocity is higher than max, apply some drag
		if (lVelocity > lMaxVelocity * lMaxVelocity) {
			pMobInstance.vx *= .03f;
			pMobInstance.vy *= .03f;
		}

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

	private void updateMobDig(LintfordCore core, CellLevel level, MobInstance mobInstance, int tilecoord) {
		// check dist to target block, and dig if we're there

		if (mobInstance.def().typeUid == MobTypeIndex.MOB_TYPE_PLAYER_DIGGER) {
			if (mobInstance.holdingGoldAmt >= mobInstance.def().maxCarryAmt)
				return;

			if (mobInstance.isAttackTimerElapsed()) {
				final var lSwingRange = mobInstance.def().swingRangePx;
				final var dst2 = Vector2f.dst2(mobInstance.xx, mobInstance.yy, mobInstance.targetPosX, mobInstance.targetPosY);
				if (dst2 < lSwingRange * lSwingRange) {

					final float lAngle = (float) Math.atan2(mobInstance.targetPosY - mobInstance.yy, mobInstance.targetPosX - mobInstance.xx);
					final float lRepelPower = .75f;

					mobInstance.vx -= Math.cos(lAngle) * lRepelPower;
					mobInstance.vy -= Math.sin(lAngle) * lRepelPower;

					mobInstance.rx -= Math.cos(lAngle) * .05f;
					mobInstance.ry -= Math.sin(lAngle) * .05f;

					// pick up gold
					level.digBlock(tilecoord, (byte) 1);
					mobInstance.holdingGoldAmt++;

					mSoundfxController.playSound(SoundfxController.SOUND_DIG_BLOCK0);

					mBlockEmitter.globalRotRads = (float) Math.toRadians(180);
					mBlockEmitter.aabb.set(mobInstance.xx, mobInstance.yy, 2, 2);
					mBlockEmitter.triggerEmission();

					mobInstance.resetAttackTimer();
				}
			}
		}

		if (mobInstance.def().typeUid == MobTypeIndex.MOB_TYPE_PLAYER_MELEE) {
			if (mobInstance.isAttackTimerElapsed()) {
				final var lSwingRange = mobInstance.def().swingRangePx;
				final var dst2 = Vector2f.dst2(mobInstance.xx, mobInstance.yy, mobInstance.targetPosX, mobInstance.targetPosY);
				if (dst2 < lSwingRange * lSwingRange) {

					final float lAngle = (float) Math.atan2(mobInstance.targetPosY - mobInstance.yy, mobInstance.targetPosX - mobInstance.xx);
					final float lRepelPower = .75f;

					mobInstance.vx -= Math.cos(lAngle) * lRepelPower;
					mobInstance.vy -= Math.sin(lAngle) * lRepelPower;

					mobInstance.rx -= Math.cos(lAngle) * .05f;
					mobInstance.ry -= Math.sin(lAngle) * .05f;

					mSoundfxController.playSound(SoundfxController.SOUND_COLLAPSE);

					// remove spawner and place destroyed one
					level.removeItem(tilecoord);
					level.placeItem(tilecoord, CellLevel.LEVEL_ITEMS_SPAWNER_DESTROYED);

					mobInstance.targetTileCoord = -1;

					mobInstance.resetAttackTimer();
				}
			}
		}
	}

	private void updateMobAttack(LintfordCore core, CellLevel level, MobInstance mobInstanceA, MobInstance mobInstanceB) {

		// TODO: use actual mob ranges

		// swing attacks
		if (mobInstanceA.def().swingAttackEnabled) {
			if (Math.abs(mobInstanceB.cx - mobInstanceA.cx) >= 2 || Math.abs(mobInstanceB.cy - mobInstanceA.cy) >= 2)
				return;

			final var lDistBetweenMobs = CellEntity.getDistSq(mobInstanceA, mobInstanceB);

			// swing attacks
			if (mobInstanceA.isAttackTimerElapsed()) {
				final var lSwingRange = mobInstanceA.def().swingRangePx;
				if (lDistBetweenMobs < lSwingRange * lSwingRange) {
					// chance to block
					if (!RandomNumbers.getRandomChance((1.f - mobInstanceB.def().blockChance) * 100.f)) {
						// block

					} else {

						mSoundfxController.playSound(SoundfxController.SOUND_DIG_ATTACK0);

						mobInstanceB.dealDamage(1, true);

						final var lHurtSample = RandomNumbers.random(0, 6);
						switch (lHurtSample) {
						case 0:
							mSoundfxController.playSound(SoundfxController.SOUND_DIG_HURT0);
							break;

						case 1:
							mSoundfxController.playSound(SoundfxController.SOUND_DIG_HURT1);
							break;

						case 2:
							mSoundfxController.playSound(SoundfxController.SOUND_DIG_HURT2);
							break;

						case 3:
							mSoundfxController.playSound(SoundfxController.SOUND_DIG_HURT3);
							break;

						case 4:
							mSoundfxController.playSound(SoundfxController.SOUND_DIG_HURT4);
							break;

						case 5:
							mSoundfxController.playSound(SoundfxController.SOUND_DIG_HURT5);
							break;

						case 6:
							mSoundfxController.playSound(SoundfxController.SOUND_DIG_HURT6);
							break;
						}

					}

					mobInstanceA.resetAttackTimer();
				}
			}
		}

		// range attacks
		if (mobInstanceA.def().rangeAttackEnabled) {
			if (Math.abs(mobInstanceB.cx - mobInstanceA.cx) >= 6 || Math.abs(mobInstanceB.cy - mobInstanceA.cy) >= 6)
				return;

			if (mobInstanceA.isAttackTimerElapsed()) {
				var xx = mobInstanceB.xx - mobInstanceA.xx;
				var yy = mobInstanceB.yy - mobInstanceA.yy;

				final var len = (float) Math.sqrt(xx * xx + yy * yy);

				final var lHeading = (float) Math.atan2(yy / len, xx / len);

				mProjectileController.shootArrow(mobInstanceA.xx, mobInstanceA.yy, lHeading, mobInstanceA.teamUid);

				final var lArrowSample = RandomNumbers.random(0, 2);
				switch (lArrowSample) {
				case 0:
					mSoundfxController.playSound(SoundfxController.SOUND_DIG_ARROW0);
					break;

				case 1:
					mSoundfxController.playSound(SoundfxController.SOUND_DIG_ARROW1);
					break;

				case 2:
					mSoundfxController.playSound(SoundfxController.SOUND_DIG_ARROW2);
					break;
				}

				mobInstanceA.resetAttackTimer();
			}
		}
	}

	// mobs attacking and bumping into each other
	private void updateMobCollision(LintfordCore core, CellLevel level, MobInstance mobInstanceA, MobInstance mobInstanceB) {
		if (Math.abs(mobInstanceB.cx - mobInstanceA.cx) >= 2 || Math.abs(mobInstanceB.cy - mobInstanceA.cy) >= 2)
			return;

		final var lDistBetweenMobs = CellEntity.getDistSq(mobInstanceA, mobInstanceB);

		final float lMinCollisionDist = (mobInstanceA.radiusRatio * ConstantsGame.BLOCK_SIZE) + (mobInstanceB.radiusRatio * ConstantsGame.BLOCK_SIZE);
		if (lDistBetweenMobs < lMinCollisionDist * lMinCollisionDist) {
			final float lAngle = (float) Math.atan2(mobInstanceB.yy - mobInstanceA.yy, mobInstanceB.xx - mobInstanceA.xx);
			final float lRepelPower = 0.03f;

			if (!mobInstanceB.isPlayerControlled) {
				mobInstanceB.vx += Math.cos(lAngle) * lRepelPower;
				mobInstanceB.vy += Math.sin(lAngle) * lRepelPower * 0.025f;
			}

			if (!mobInstanceA.isPlayerControlled) {
				mobInstanceA.vx -= Math.cos(lAngle) * lRepelPower;
				mobInstanceA.vy -= Math.sin(lAngle) * lRepelPower;
			}
		}
	}

	private void updatePlayerMobMovement(LintfordCore core, CellLevel level, MobInstance mobInstance) {
		final var dt = (float) core.gameTime().elapsedTimeMilli();

		final var lCommander = mPlayerController.commanderInstance();
		if (mobInstance == lCommander)
			return;

		boolean toBeMoved = true;

		if (mobInstance.order == MobOrder.retreat) {
			toBeMoved = true;
			mobInstance.targetX = lCommander.xx;
			mobInstance.targetY = lCommander.yy;

		} else if (mobInstance.order == MobOrder.attack && mobInstance.targetMob != null) {
			// we have something to attack
			if (mobInstance.def().typeUid == MobTypeIndex.MOB_TYPE_PLAYER_MELEE || mobInstance.def().typeUid == MobTypeIndex.MOB_TYPE_PLAYER_DIGGER) {
				mobInstance.targetX = mobInstance.targetMob.xx;
				mobInstance.targetY = mobInstance.targetMob.yy;
			} else {
				toBeMoved = false; // ranged
			}

		} else if (mobInstance.targetTileCoord != CellLevel.LEVEL_TILE_COORD_INVALID) {
			// we have a block to dig/attack
			mobInstance.targetX = mobInstance.targetPosX;
			mobInstance.targetY = mobInstance.targetPosY;

		} else {

			// order normal (formation)

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

		if (toBeMoved) {
			final var xx = mobInstance.targetX - mobInstance.xx;
			final var yy = mobInstance.targetY - mobInstance.yy;

			final var targetHeading = (float) Math.atan2(yy, xx);
			mobInstance.heading += MathHelper.turnToFace(targetHeading, mobInstance.heading, .1f);

			// move unit towards target
			final var lMovementSpeed = mobInstance.movementSpeedMod;
			final var len = (float) Math.sqrt(xx * xx + yy * yy);
			mobInstance.vx += (xx / len) * lMovementSpeed * dt * .001f;
			mobInstance.vy += (yy / len) * lMovementSpeed * dt * .001f;
		}

	}

	private void updateEnemyMobMovement(LintfordCore core, CellLevel level, MobInstance mobInstance) {
		final var dt = (float) core.gameTime().elapsedTimeMilli();

		if (mobInstance.order == MobOrder.retreat) {

			final var xx = mobInstance.targetX - mobInstance.xx;
			final var yy = mobInstance.targetY - mobInstance.yy;

			// move unit towards target
			final var lMovementSpeed = mobInstance.movementSpeedMod;
			final var len = (float) Math.sqrt(xx * xx + yy * yy);
			mobInstance.vx += (xx / len) * lMovementSpeed * dt * .001f;
			mobInstance.vy += (yy / len) * lMovementSpeed * dt * .001f;

		} else if (mobInstance.order == MobOrder.attack && mobInstance.targetMob != null) {
			// we have something to attack
			if (mobInstance.def().typeUid == MobTypeIndex.MOB_TYPE_GOBLIN_MELEE) {
				mobInstance.targetX = mobInstance.targetMob.xx;
				mobInstance.targetY = mobInstance.targetMob.yy;

			}

			final var xx = mobInstance.targetX - mobInstance.xx;
			final var yy = mobInstance.targetY - mobInstance.yy;

			// move unit towards target
			final var lMovementSpeed = mobInstance.movementSpeedMod;
			final var len = (float) Math.sqrt(xx * xx + yy * yy);
			mobInstance.vx += (xx / len) * lMovementSpeed * dt * .001f;
			mobInstance.vy += (yy / len) * lMovementSpeed * dt * .001f;

		} else {

			// wandering mechanic

			final float lMaxRange = (float) Math.PI * 2f;
			mobInstance.heading += RandomNumbers.random(-1f, 1.f) * .1f;

			mobInstance.heading %= lMaxRange;

			// move unit towards target
			final var lMovementSpeed = mobInstance.movementSpeedMod;
			mobInstance.vx += Math.cos(mobInstance.heading) * lMovementSpeed;
			mobInstance.vy += Math.sin(mobInstance.heading) * lMovementSpeed;

			return;
		}
	}

	public void startNewGame(float startX, float startY) {
		addPlayerCommander(startX, startY);

		addPlayerMob(MobTypeIndex.MOB_TYPE_PLAYER_DIGGER, startX, startY);
		addPlayerMob(MobTypeIndex.MOB_TYPE_PLAYER_MELEE, startX, startY);

	}

	private void addPlayerCommander(float worldX, float worldY) {
		final var lPlayerMob = mMobManager.getNewMobInstance();

		// TODO: gotta be using the entrance coords

		lPlayerMob.initialise(MobDefinition.COMMANDER);
		lPlayerMob.isPlayerControlled = true;
		lPlayerMob.setPosition(worldX, worldY);

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

	public void addEnemyMob(int mobTypeUid, float worldX, float worldY, int homeTileCoord) {
		final var lMobInstance = mMobManager.getNewMobInstance();

		lMobInstance.homeTileCoord = homeTileCoord;
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
