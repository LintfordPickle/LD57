package lintfordpickle.fantac.controllers;

import lintfordpickle.fantac.data.teams.Team;
import lintfordpickle.fantac.data.teams.TeamManager;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;

public class TeamController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Team Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private TeamManager mTeamManager;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public TeamManager teamManager() {
		return mTeamManager;
	}

	public Team getTeamByUid(int uid) {
		return mTeamManager.getTeamByUid(uid);
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public TeamController(ControllerManager controllerManager, TeamManager teamManager, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mTeamManager = teamManager;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void update(LintfordCore core) {
		super.update(core);
	}
}
