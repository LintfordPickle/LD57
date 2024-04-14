package lintfordpickle.fantac.controllers;

import lintfordpickle.fantac.data.IGameStateListener;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;

public class GameStateController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "GameState Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private TeamController mTeamController;
	private UnitController mUnitController;
	private JobController mJobController;
	private SettlementController mSettlementController;

	private IGameStateListener mGameStateListener;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public void setGameStateListener(IGameStateListener listener) {
		mGameStateListener = listener;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameStateController(ControllerManager controllerManager, int entityGroupUId) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUId);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();
		mSettlementController = (SettlementController) lControllerManager.getControllerByNameRequired(SettlementController.CONTROLLER_NAME, entityGroupUid());
		mUnitController = (UnitController) lControllerManager.getControllerByNameRequired(UnitController.CONTROLLER_NAME, entityGroupUid());
		mTeamController = (TeamController) lControllerManager.getControllerByNameRequired(TeamController.CONTROLLER_NAME, entityGroupUid());
		mJobController = (JobController) lControllerManager.getControllerByNameRequired(JobController.CONTROLLER_NAME, entityGroupUid());
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		// Cuurent win/lose condition is control of all buidlings on the map
		final var lTeamManager = mTeamController.teamManager();
		final var lTeams = lTeamManager.teams;
		final var lNumTeams = lTeams.size();
		for (int i = 0; i < lNumTeams; i++) {
			final var lTeam = lTeams.get(i);
			final var lNumSettlements = mSettlementController.getTotalSettlements(lTeam.teamUid);

			if (lNumSettlements <= 0) {
				// mTeamController.setTeamLost();
				mUnitController.removeAllUnits(lTeam.teamUid);
				mSettlementController.removeAllSettlements(lTeam.teamUid);
				mJobController.removeAllJobs(lTeam.teamUid);
			}

		}

//		if (mSettlementsController.getBuildingCount(Team.TEAM_1_UID) == 0) {
//			mGameStateListener.onGameLost(Team.TEAM_1_UID);
//		}

//		if (mTeamController.getNumberOfTeamsPlaying() <= 1) {
//			mGameStateListener.onGameWon(Team.TEAM_1_UID);
//		}
	}
}
