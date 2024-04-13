package lintfordpickle.fantac.data;

import lintfordpickle.fantac.data.settlements.SettlementsManager;

public class GameWorld {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SettlementsManager mSettlementsManager;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public SettlementsManager settlements() {
		return mSettlementsManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameWorld() {
		mSettlementsManager = new SettlementsManager();
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	// --------------------------------------
	// Methods
	// --------------------------------------
}
