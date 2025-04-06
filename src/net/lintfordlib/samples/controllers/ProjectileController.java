package net.lintfordlib.samples.controllers;

import java.util.ArrayList;
import java.util.List;

import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.data.GameWorld;
import net.lintfordlib.samples.data.entities.CellEntity;
import net.lintfordlib.samples.data.level.CellLevel;
import net.lintfordlib.samples.data.projectiles.ProjectileInstance;
import net.lintfordlib.samples.data.projectiles.ProjectileManager;

public class ProjectileController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Projectile Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private ProjectileManager mProjectileManager;

	private MobController mMobController;
	private LevelController mLevelController;

	private List<ProjectileInstance> mProjectilesToUpdate = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mProjectileManager != null;
	}

	public ProjectileManager projectileManager() {
		return mProjectileManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ProjectileController(ControllerManager controllerManager, GameWorld gameWorld, int eEntityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, eEntityGroupUid);

		mProjectileManager = gameWorld.projectileManager();
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		mLevelController = (LevelController) core.controllerManager().getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupUid());
		mMobController = (MobController) core.controllerManager().getControllerByNameRequired(MobController.CONTROLLER_NAME, entityGroupUid());
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var dt = (float) core.gameTime().elapsedTimeMilli();
		final var lLevel = mLevelController.cellLevel();

		final var lProjectiles = mProjectileManager.projectiles();

		mProjectilesToUpdate.clear();
		final var lNumProjectiles = lProjectiles.size();
		for (int i = 0; i < lNumProjectiles; i++) {
			mProjectilesToUpdate.add(lProjectiles.get(i));
		}

		// update the projectile positions and kill on touch
		for (int i = 0; i < lNumProjectiles; i++) {
			final var lProjectile = mProjectilesToUpdate.get(i);

			lProjectile.update(dt);

			if (lProjectile.lifeRemaining <= 0) {
				mProjectileManager.removeProjectileInstance(lProjectile);
				continue;
			}

			if (!lProjectile.isSpent)
				updateProjectileMobCollision(core, lLevel, lProjectile);

			updateProjectilePhysics(core, lLevel, lProjectile);
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	private void updateProjectilePhysics(LintfordCore core, CellLevel level, ProjectileInstance projectileInstance) {

		final var lMobRadius = .3f;

		final var lVelocity = projectileInstance.vx * projectileInstance.vx + projectileInstance.vy * projectileInstance.vy;
		final var lMaxVelocity = 8.f;

		// if the velocity is higher than max, apply some drag
		if (lVelocity > lMaxVelocity * lMaxVelocity) {
			projectileInstance.vx *= .9f;
			projectileInstance.vy *= .9f;
		}

		if (projectileInstance.isSpent) {
			projectileInstance.vx *= .1f;
			projectileInstance.vy *= .1f;
		}

		projectileInstance.rx += projectileInstance.vx;
		projectileInstance.ry += projectileInstance.vy;

		// Grid based collision check - stop arrows
//		if (projectileInstance.rx < lMobRadius && level.hasCollision(projectileInstance.cx - 1, projectileInstance.cy)) {
//			projectileInstance.rx = lMobRadius;
//
//			projectileInstance.vx = 0;
//			projectileInstance.vy = 0;
//		}
//
//		if (projectileInstance.rx > 1.f - lMobRadius && level.hasCollision(projectileInstance.cx + 1, projectileInstance.cy)) {
//			projectileInstance.rx = 1.f - lMobRadius;
//
//			projectileInstance.vx = 0;
//			projectileInstance.vy = 0;
//		}
//
//		if (projectileInstance.ry < lMobRadius && level.hasCollision(projectileInstance.cx, projectileInstance.cy - 1)) {
//			projectileInstance.ry = lMobRadius;
//
//			projectileInstance.vx = 0;
//			projectileInstance.vy = 0;
//		}
//
//		if (projectileInstance.ry > 1.f - lMobRadius && level.hasCollision(projectileInstance.cx, projectileInstance.cy + 1)) {
//			projectileInstance.ry = 1.f - lMobRadius;
//
//			projectileInstance.vx = 0;
//			projectileInstance.vy = 0;
//		}

		while (projectileInstance.rx < 0) {
			projectileInstance.rx++;
			projectileInstance.cx--;
		}

		while (projectileInstance.rx > 1) {
			projectileInstance.rx--;
			projectileInstance.cx++;
		}

		while (projectileInstance.ry < 0) {
			projectileInstance.ry++;
			projectileInstance.cy--;
		}

		while (projectileInstance.ry > 1) {
			projectileInstance.ry--;
			projectileInstance.cy++;
		}

		// update mob instance

		projectileInstance.xx = (float) (projectileInstance.cx + projectileInstance.rx) * ConstantsGame.BLOCK_SIZE;
		projectileInstance.yy = (float) (projectileInstance.cy + projectileInstance.ry) * ConstantsGame.BLOCK_SIZE;

		projectileInstance.vx *= .96f;
		projectileInstance.vy *= .96f;
	}

	private void updateProjectileMobCollision(LintfordCore core, CellLevel level, ProjectileInstance projInst) {
		final var lMobManager = mMobController.mobManager();
		final var lMobs = lMobManager.mobs();
		final var lNumMobs = lMobs.size();

		for (int i = 0; i < lNumMobs; i++) {
			final var lMob = lMobs.get(i);

			if (lMob.teamUid == projInst.ownerTeamUid)
				continue;

			if (Math.abs(lMob.cx - projInst.cx) >= 2 || Math.abs(lMob.cy - projInst.cy) >= 2)
				continue;

			final var lDistBetweenMobs = CellEntity.getDistSq(projInst, lMob);

			final float lMinCollisionDist = (projInst.radiusRatio * ConstantsGame.BLOCK_SIZE) + (lMob.radiusRatio * ConstantsGame.BLOCK_SIZE);
			if (lDistBetweenMobs < lMinCollisionDist * lMinCollisionDist) {
				final float lAngle = (float) Math.atan2(lMob.yy - projInst.yy, lMob.xx - projInst.xx);
				final float lRepelPower = 0.03f;

				if (ConstantsGame.DEBUG_ENABLE_ATTACK_KNOCKBACK && !lMob.isPlayerControlled) {
					lMob.vx += Math.cos(lAngle) * lRepelPower;
					lMob.vy += Math.sin(lAngle) * lRepelPower * 0.025f;
				}

				projInst.vx -= Math.cos(lAngle) * lRepelPower;
				projInst.vy -= Math.sin(lAngle) * lRepelPower * 0.025f;

				projInst.showDamageFlash(true);
				projInst.isSpent = true;

				lMob.dealDamage(projInst.damageAmt, true);
			}
		}
	}

	public void shootArrow(float worldX, float worldY, float heading, int ownerTeamUid) {
		final var lNewProjectile = mProjectileManager.getNewProjectileInstance();
		lNewProjectile.initialise(worldX, worldY, heading, ownerTeamUid, 1000, 1);

		final var velocity = .3f;
		lNewProjectile.vx = (float) Math.cos(heading) * velocity;
		lNewProjectile.vy = (float) Math.sin(heading) * velocity;
	}
}
