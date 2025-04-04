package net.lintfordlib.samples.data;

import net.lintfordlib.samples.data.level.CellLevel;
import net.lintfordlib.samples.data.mobs.MobManager;

public class GameWorld {
	// --------------------------------------
	// Variables
	// --------------------------------------

	private MobManager mMobManager;
	private CellLevel mLevel;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public MobManager mobManager() {
		return mMobManager;
	}

	public CellLevel level() {
		return mLevel;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameWorld() {
		mMobManager = new MobManager();
		mLevel = new CellLevel();
	}
}
