package net.lintfordlib.samples.controllers;

import org.lwjgl.glfw.GLFW;

import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.controllers.core.particles.ParticleFrameworkController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.input.mouse.IInputProcessor;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.data.GameWorld;
import net.lintfordlib.samples.data.level.CellLevel;

public class LevelController extends BaseController implements IInputProcessor {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Level Controller";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private GameStateController mGameStateController;
	private ParticleFrameworkController mParticleFrameworkController;

	private CellLevel mLevel;
	private float mMouseCooldownTimer;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	@Override
	public boolean isCoolDownElapsed() {
		return mMouseCooldownTimer <= 0;
	}

	@Override
	public void resetCoolDownTimer() {
		mMouseCooldownTimer = 200;

	}

	public CellLevel cellLevel() {
		return mLevel;
	}

	@Override
	public boolean isInitialized() {
		return mGameStateController != null;

	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public LevelController(ControllerManager controllerManager, GameWorld gameWorld, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mLevel = gameWorld.level();
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		mGameStateController = (GameStateController) core.controllerManager().getControllerByNameRequired(GameStateController.CONTROLLER_NAME, entityGroupUid());
		mParticleFrameworkController = (ParticleFrameworkController) core.controllerManager().getControllerByNameRequired(ParticleFrameworkController.CONTROLLER_NAME, entityGroupUid());

	}

	@Override
	public boolean handleInput(LintfordCore core) {

		if (ConstantsGame.IS_DEBUG_MODE) {
			if (core.input().mouse().isMouseLeftButtonDownTimed(this)) {
				final int lMouseTileX = (int) (core.gameCamera().getMouseWorldSpaceX() / ConstantsGame.BLOCK_SIZE);
				final int lMouseTileY = (int) (core.gameCamera().getMouseWorldSpaceY() / ConstantsGame.BLOCK_SIZE);

				if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {

					// place block
					mLevel.placeBlock(lMouseTileX, lMouseTileX, CellLevel.LEVEL_TILE_INDEX_DIRT, (byte) 2);

				} else {
					mLevel.digBlock(lMouseTileX, lMouseTileX, (byte) 50);
				}

			}

		}

		return super.handleInput(core);

	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		if (mMouseCooldownTimer > 0) {
			mMouseCooldownTimer -= core.gameTime().elapsedTimeMilli();

		}
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void loadLevelFromFile(String pFilename) {
		mLevel.loadLevel();
	}

	public void startNewGame(int pLevelNumber) {
		mLevel.loadLevel();
	}
}