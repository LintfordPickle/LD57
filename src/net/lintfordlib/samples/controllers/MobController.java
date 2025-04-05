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
import net.lintfordlib.samples.data.mobs.MobManager;

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
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var dt = (float) core.gameTime().elapsedTimeMilli() * .001f;

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

			lMobInstance.update(dt);

			if (!lMobInstance.isPlayerControlled) {
				updateEnemyAi(core, lLevel, lMobInstance);
			}

			for (int j = i + 0; j < lNumMobs; j++) {
				final var lOtherMobInstance = mMobInstancesToUpdate.get(j);

				if (lMobInstance == lOtherMobInstance)
					continue;

				updateEnemyPlayerCollision(core, lLevel, lMobInstance, lOtherMobInstance);

			}

			updateMobPhysics(core, lLevel, lMobInstance);
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	private void updateMobPhysics(LintfordCore pCore, CellLevel pLevel, MobInstance pMobInstance) {

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

	private static float getDistSq(MobInstance pEntityA, MobInstance pEntityB) {
		final float lMobAX = pEntityA.xx;
		final float lMobAY = pEntityA.yy;

		final float lMobBX = pEntityB.xx;
		final float lMobBY = pEntityB.yy;

		final float lDistSq = Vector2f.dst2(lMobAX, lMobAY, lMobBX, lMobBY);

		return lDistSq;
	}

	private void updateEnemyPlayerCollision(LintfordCore pCore, CellLevel pLevel, MobInstance pMobInstanceA, MobInstance pMobInstanceB) {
		if (Math.abs(pMobInstanceB.cx - pMobInstanceA.cx) >= 2 || Math.abs(pMobInstanceB.cy - pMobInstanceA.cy) >= 2)
			return;

		final float lMaxDist = pMobInstanceA.radius + pMobInstanceB.radius;

		if (getDistSq(pMobInstanceA, pMobInstanceB) < lMaxDist * lMaxDist) {
			final float lAngle = (float) Math.atan2(pMobInstanceB.yy - pMobInstanceA.yy, pMobInstanceB.xx - pMobInstanceA.xx);
			final float lRepelPower = 0.03f;

			if (ConstantsGame.DEBUG_ENABLE_ATTACK_KNOCKBACK && !pMobInstanceB.isPlayerControlled) {
				pMobInstanceB.vx += Math.cos(lAngle) * lRepelPower;
				pMobInstanceB.vy += Math.sin(lAngle) * lRepelPower * 0.025f;
			}

			if (ConstantsGame.DEBUG_ENABLE_ATTACK_KNOCKBACK && !pMobInstanceA.isPlayerControlled) {
				pMobInstanceA.vx -= Math.cos(lAngle) * lRepelPower;
				pMobInstanceA.vy -= Math.sin(lAngle) * lRepelPower;
			}

			// assumes the player is always the first mob index (probably correct)
			if (pMobInstanceA.isPlayerControlled) {
				if (pMobInstanceB.def().damagesOnCollide)
					pMobInstanceA.dealDamage(1, true);

				if (pMobInstanceA.def().damagesOnCollide)
					pMobInstanceB.dealDamage(1, true);
			}
		}
	}

	private void updateEnemyAi(LintfordCore core, CellLevel level, MobInstance mobInstance) {
		final var lPlayerMobInstance = mPlayerController.commanderInstance();

		// TODO: enemy ai

	}

	public void startNewGame() {
		addPlayerCommander();

		// do other stuff
	}

	private void addPlayerCommander() {
		final var lPlayerMob = mMobManager.getNewMobInstance();

		lPlayerMob.initialise(MobDefinition.COMMANDER);
		lPlayerMob.isPlayerControlled = true;
		lPlayerMob.setPosition(32.f, 32.f);

		mPlayerController.commanderInstance(lPlayerMob);
		mCameraFollowController.setFollowEntity(lPlayerMob);
	}

}
