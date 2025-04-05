package net.lintfordlib.samples.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;

import org.lwjgl.glfw.GLFW;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.controllers.core.particles.ParticleFrameworkController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.core.input.mouse.IInputProcessor;
import net.lintfordlib.core.storage.FileUtils;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.data.GameWorld;
import net.lintfordlib.samples.data.level.CellLevel;
import net.lintfordlib.samples.data.level.LevelSaveDefinition;

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

		if (ConstantsGame.IS_LEVEL_EDIT_MODE) {
			if (core.input().mouse().isMouseLeftButtonDownTimed(this)) {
				final int lMouseTileX = (int) (core.gameCamera().getMouseWorldSpaceX() / ConstantsGame.BLOCK_SIZE);
				final int lMouseTileY = (int) (core.gameCamera().getMouseWorldSpaceY() / ConstantsGame.BLOCK_SIZE);

				if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL))
					mLevel.placeBlock(lMouseTileX, lMouseTileY, CellLevel.LEVEL_TILE_INDEX_DIRT, (byte) 2);

				else
					mLevel.digBlock(lMouseTileX, lMouseTileY, (byte) 50);

			}

			if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_F5, this)) {
				final var levelFilePath = Paths.get(ConstantsGame.LEVEL_DIRECTORY, "_temp" + ConstantsGame.LEVEL_EXTENSION);
				final var levelFileName = mLevel.filename() == null ? levelFilePath.toString() : mLevel.filename();

				saveLevelIntoFile(levelFileName);
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

	public void loadLevelFromFile(int levelNumber) {
		final var lCompletePath = Paths.get(ConstantsGame.LEVEL_DIRECTORY, levelNumber + ConstantsGame.LEVEL_EXTENSION);

		final var lFileName = lCompletePath.toString();

		String lLevelRawFileContents = null;
		LevelSaveDefinition lLevelToLoad = null;

		final var lGson = new GsonBuilder().create();

		try {
			lLevelRawFileContents = FileUtils.loadString(lFileName);
			lLevelToLoad = lGson.fromJson(lLevelRawFileContents, LevelSaveDefinition.class);

			lLevelToLoad.fileName = lFileName;
			mLevel.loadLevel(lLevelToLoad);

		} catch (JsonSyntaxException ex) {
			Debug.debugManager().logger().printException(getClass().getSimpleName(), ex);
		}
	}

	public void saveLevelIntoFile(String fileName) {
		final var lLevelToSave = mLevel.saveLevel();
		final var lFile = new File(fileName);

		final var lParentFile = lFile.getParentFile();
		if (!lParentFile.exists()) {
			lParentFile.mkdirs();
		}

		try (Writer writer = new FileWriter(fileName)) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(lLevelToSave, writer);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void startEmptyLevel() {
		mLevel.createTestLevel();
	}
}