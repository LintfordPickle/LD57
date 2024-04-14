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
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		// Cuurent win/lose condition is control of all buidlings on the map

//		if (mSettlementsController.getBuildingCount(Team.TEAM_1_UID) == 0) {
//			mGameStateListener.onGameLost(Team.TEAM_1_UID);
//		}

//		if (mSettlementsController.getBuildingCount(Team.TEAM_2_UID) == 0) {
//			mGameStateListener.onGameWon(Team.TEAM_1_UID);
//		}
	}
}
