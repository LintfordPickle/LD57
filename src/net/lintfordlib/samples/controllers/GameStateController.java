package net.lintfordlib.samples.controllers;

import org.lwjgl.glfw.GLFW;

import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.data.GameState;
import net.lintfordlib.samples.data.IGameStateListener;

public class GameStateController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "GameState Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private GameState mGameState;
	private IGameStateListener mGameStateListener;

	private PlayerController mPlayerController;
	private LevelController mLevelController;

	private boolean mPlayerInLoadoutArea;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public boolean playerInLoadoutArea() {
		return mPlayerInLoadoutArea;
	}

	public void setGameStateListener(IGameStateListener listener) {
		mGameStateListener = listener;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameStateController(ControllerManager controllerManager, GameState gameState, int entityGroupUId) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUId);

		mGameState = gameState;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();
		mPlayerController = (PlayerController) lControllerManager.getControllerByNameRequired(PlayerController.CONTROLLER_NAME, entityGroupUid());
	}

	@Override
	public boolean handleInput(LintfordCore core) {

		if (ConstantsGame.IS_DEBUG_MODE) {
			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_O, this)) {
				Debug.debugManager().logger().i(getClass().getSimpleName(), "Skipping level");
				mGameStateListener.onGameWon();
			}
			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_P, this)) {
				Debug.debugManager().logger().i(getClass().getSimpleName(), "losing level");
				mGameStateListener.onGameLost();
			}
		}

		return super.handleInput(core);
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		// Check for win/lose conditions
		if (mPlayerController.commanderHealth() < 0) {
			mGameStateListener.onGameLost();
		}

		final var lWinCreditsAmount = 2500;
		if (mGameState.credits > lWinCreditsAmount) {
			mGameStateListener.onGameWon();
		}
	}
}
