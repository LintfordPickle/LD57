package lintfordpickle.fantac.data;

import lintfordpickle.fantac.data.jobs.JobsManager;
import lintfordpickle.fantac.data.settlements.SettlementsManager;
import lintfordpickle.fantac.data.teams.TeamManager;
import lintfordpickle.fantac.data.units.UnitsManager;

public class GameWorld {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private TeamManager mTeamManager;
	private JobsManager mJobsManager;
	private UnitsManager mUnitsManager;
	private SettlementsManager mSettlementsManager;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public TeamManager team() {
		return mTeamManager;
	}

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
		mTeamManager = new TeamManager();
		mJobsManager = new JobsManager();
		mUnitsManager = new UnitsManager();
		mSettlementsManager = new SettlementsManager();
	}

}
