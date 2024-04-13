package lintfordpickle.fantac.data;

import lintfordpickle.fantac.data.jobs.JobsManager;
import lintfordpickle.fantac.data.settlements.SettlementsManager;
import lintfordpickle.fantac.data.units.UnitsManager;

public class GameWorld {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	private JobsManager mJobsManager;
	private UnitsManager mUnitsManager;
	private SettlementsManager mSettlementsManager;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public JobsManager jobs() {
		return mJobsManager;
	}

	public UnitsManager units() {
		return mUnitsManager;
	}

	public SettlementsManager settlements() {
		return mSettlementsManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameWorld() {
		mJobsManager = new JobsManager();
		mUnitsManager = new UnitsManager();
		mSettlementsManager = new SettlementsManager();
	}

}
