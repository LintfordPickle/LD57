package net.lintfordlib.samples.data;

import net.lintfordlib.samples.data.level.CellLevel;
import net.lintfordlib.samples.data.mobs.MobManager;
import net.lintfordlib.samples.data.projectiles.ProjectileManager;

public class GameWorld {
	// --------------------------------------
	// Variables
	// --------------------------------------

	private MobManager mMobManager;
	private CellLevel mLevel;
	private ProjectileManager mProjectileManager;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public MobManager mobManager() {
		return mMobManager;
	}

	public CellLevel level() {
		return mLevel;
	}

	public ProjectileManager projectileManager() {
		return mProjectileManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameWorld() {
		mMobManager = new MobManager();
		mLevel = new CellLevel();
		mProjectileManager = new ProjectileManager();
	}
}
