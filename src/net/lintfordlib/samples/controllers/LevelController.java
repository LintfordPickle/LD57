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
import net.lintfordlib.core.maths.RandomNumbers;
import net.lintfordlib.core.storage.FileUtils;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.data.GameWorld;
import net.lintfordlib.samples.data.level.CellLevel;
import net.lintfordlib.samples.data.level.LevelSaveDefinition;
import net.lintfordlib.samples.data.mobs.MobTypeIndex;

public class LevelController extends BaseController implements IInputProcessor {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Level Controller";

	// this should be a per level basis - and an army strength, not a count of units
	public static final int MAX_ENEMY_MOBS = 80;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private GameStateController mGameStateController;
	private ParticleFrameworkController mParticleFrameworkController;
	private MobController mMobController;
	private LevelController mLevelController;

	private CellLevel mLevel;
	private float mMouseCooldownTimer;
	private float mSpawnTimerModifier;

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

	public float startWorldX() {
		return mLevel.entranceTileX() * ConstantsGame.BLOCK_SIZE + ConstantsGame.BLOCK_SIZE * .5f;
	}

	public float startWorldY() {
		return mLevel.entranceTileY() * ConstantsGame.BLOCK_SIZE + ConstantsGame.BLOCK_SIZE * .5f;
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
		mMobController = (MobController) core.controllerManager().getControllerByNameRequired(MobController.CONTROLLER_NAME, entityGroupUid());
		mLevelController = (LevelController) core.controllerManager().getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupUid());
	}

	@Override
	public boolean handleInput(LintfordCore core) {
		if (ConstantsGame.IS_LEVEL_EDIT_MODE) {
			final int lMouseTileX = (int) (core.gameCamera().getMouseWorldSpaceX() / ConstantsGame.BLOCK_SIZE);
			final int lMouseTileY = (int) (core.gameCamera().getMouseWorldSpaceY() / ConstantsGame.BLOCK_SIZE);

			if (core.input().mouse().isMouseLeftButtonDownTimed(this, 50)) {
				if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
					if (!mLevel.removeItem(lMouseTileX, lMouseTileY)) {
						mLevel.digBlock(lMouseTileX, lMouseTileY, (byte) 50);
					}
				} else {
					mLevel.placeBlock(lMouseTileX, lMouseTileY, CellLevel.LEVEL_TILE_INDEX_DIRT, (byte) 2);
				}
			}

			if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_1, this)) {
				mLevel.setCaveEntrance(lMouseTileX, lMouseTileY);
			}

			if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_2, this)) {
				mLevel.placeItem(lMouseTileX, lMouseTileY, CellLevel.LEVEL_ITEMS_SPAWNER);
			}

			if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_3, this)) {
				mLevel.placeBlock(lMouseTileX, lMouseTileY, CellLevel.LEVEL_TILE_INDEX_GOLD, (byte) 10);
			}

			if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_4, this)) {
				mLevel.placeBlock(lMouseTileX, lMouseTileY, CellLevel.LEVEL_TILE_INDEX_GEMS, (byte) 4);
			}

			if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_5, this)) {
				mLevel.placeItem(lMouseTileX, lMouseTileY, CellLevel.LEVEL_ITEMS_TREASURE);
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

		updateSpawners(core);
	}

	private void updateSpawners(LintfordCore core) {
		if (ConstantsGame.DEBUG_ENABLE_ENEMY_MOBS)
			return;

		final var lItemTimers = mLevel.itemTimers();
		final var lSpawnerIndices = mLevel.spawnerIndices();

		var lNumEnemyMobs = mMobController.numEnemyMobs();

		if (lNumEnemyMobs >= LevelController.MAX_ENEMY_MOBS)
			return; // max reached

		final var lFullModifierAmt = 10;
		mSpawnTimerModifier = 1.f + (lNumEnemyMobs / LevelController.MAX_ENEMY_MOBS) * lFullModifierAmt;

		final int lNumSpawners = lSpawnerIndices.size();
		for (int i = 0; i < lNumSpawners; i++) {
			final var lSpawnerIndex = lSpawnerIndices.get(i);

			lItemTimers[lSpawnerIndex] -= core.gameTime().elapsedTimeMilli();

			if (lItemTimers[lSpawnerIndex] < 0.f) {
				lItemTimers[lSpawnerIndex] = (10000.f + RandomNumbers.random(0, 10000)) * mSpawnTimerModifier;

				final var xx = lSpawnerIndex % mLevelController.cellLevel().tilesWide() * ConstantsGame.BLOCK_SIZE;
				final var yy = lSpawnerIndex / mLevelController.cellLevel().tilesHigh() * ConstantsGame.BLOCK_SIZE;

				if (RandomNumbers.getRandomChance(30.f))
					mMobController.addEnemyMob(MobTypeIndex.MOB_TYPE_GOBLIN_RANGE, xx, yy, lSpawnerIndex);
				else
					mMobController.addEnemyMob(MobTypeIndex.MOB_TYPE_GOBLIN_MELEE, xx, yy, lSpawnerIndex);

				lNumEnemyMobs++;

				if (lNumEnemyMobs >= LevelController.MAX_ENEMY_MOBS)
					return; // max reached
			}
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