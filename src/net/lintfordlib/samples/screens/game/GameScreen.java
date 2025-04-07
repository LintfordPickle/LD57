package net.lintfordlib.samples.screens.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.controllers.core.particles.ParticleFrameworkController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.particles.ParticleFrameworkData;
import net.lintfordlib.core.particles.particleemitters.ParticleEmitterInstance;
import net.lintfordlib.core.particles.particlesystems.ParticleSystemInstance;
import net.lintfordlib.data.DataManager;
import net.lintfordlib.renderers.SimpleRendererManager;
import net.lintfordlib.renderers.particles.ParticleFrameworkRenderer;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.NewGameKeyActions;
import net.lintfordlib.samples.controllers.AnimationController;
import net.lintfordlib.samples.controllers.CameraFollowController;
import net.lintfordlib.samples.controllers.GameStateController;
import net.lintfordlib.samples.controllers.LevelController;
import net.lintfordlib.samples.controllers.MobController;
import net.lintfordlib.samples.controllers.PlayerController;
import net.lintfordlib.samples.controllers.ProjectileController;
import net.lintfordlib.samples.data.GameOptions;
import net.lintfordlib.samples.data.GameState;
import net.lintfordlib.samples.data.GameWorld;
import net.lintfordlib.samples.data.IGameStateListener;
import net.lintfordlib.samples.data.SampleSceneHeader;
import net.lintfordlib.samples.data.projectiles.ProjectileManager;
import net.lintfordlib.samples.renderers.AnimationRenderer;
import net.lintfordlib.samples.renderers.HudRenderer;
import net.lintfordlib.samples.renderers.LevelRenderer;
import net.lintfordlib.samples.renderers.MobRenderer;
import net.lintfordlib.samples.renderers.ProjectileRenderer;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.screens.BaseGameScreen;
import net.lintfordlib.screenmanager.screens.LoadingScreen;

public class GameScreen extends BaseGameScreen implements IGameStateListener {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SampleSceneHeader mSceneHeader;
	private GameOptions mGameOptions;

	// Data
	private GameState mGameState;
	private GameWorld mGameWorld; // reference to data related to the scene
	private ParticleFrameworkData mParticleData;
	private ProjectileManager mProjetileManager;

	private ParticleEmitterInstance mWispEmitter;

	// Controllers
	private GameStateController mGameStateController;
	private AnimationController mAnimationController;
	private LevelController mLevelController;
	private MobController mMobController;
	private PlayerController mPlayerController;
	private CameraFollowController mCameraFollowController;
	private ParticleFrameworkController mParticleFrameworkController;
	private ProjectileController mProjectileController;

	// Renderers
	private LevelRenderer mLevelRenderer;
	private AnimationRenderer mAnimationRenderer;
	private HudRenderer mHudRenderer;
	private ParticleFrameworkRenderer mParticleFrameworkRenderer;
	private MobRenderer mMobRenderer;
	private ProjectileRenderer mProjectileRenderer;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameScreen(ScreenManager screenManager, SampleSceneHeader sceneHeader, GameOptions options) {
		super(screenManager, new SimpleRendererManager(screenManager.core(), ConstantsGame.GAME_RESOURCE_GROUP_ID));

		mSceneHeader = sceneHeader;
		mGameOptions = options;
		
		mShowBackgroundScreens = true;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize() {
		super.initialize();

		// by this stage, all the controller should be fully initialized themselves ..
		if (mSceneHeader.levelNumber == -1) {
			mLevelController.startEmptyLevel();
		} else {
			mLevelController.loadLevelFromFile(mSceneHeader.levelNumber);
		}

		final var lGridSize = ConstantsGame.BLOCK_SIZE;
		final var lStartX = mLevelController.cellLevel().entranceTileX() * lGridSize + lGridSize * .5f;
		final var lStartY = mLevelController.cellLevel().entranceTileY() * lGridSize + lGridSize * .5f;

		mMobController.startNewGame(lStartX, lStartY);
		mGameStateController.startNewGame(20);
	}

	@Override
	public void handleInput(LintfordCore core) {
		super.handleInput(core);

		if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_ESCAPE, this) || core.input().gamepads().isGamepadButtonDownTimed(GLFW.GLFW_GAMEPAD_BUTTON_START, this)) {
			// final var lGameScreen = new GameScreen(screenManager, mSceneHeader, mGameOptions);
			// screenManager.createLoadingScreen(new LoadingScreen(screenManager, true, true, lGameScreen));
			screenManager.addScreen(new PauseScreen(screenManager, mSceneHeader, mGameOptions));
			return;
		}
	}

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);

		if (core.input().eventActionManager().getCurrentControlActionStateTimed(NewGameKeyActions.KEY_BINDING_PRIMARY_FIRE)) {
			screenManager.toastManager().addMessage(getClass().getSimpleName(), "PRIMARY FIRE", 1500);
		}

	}

	@Override
	public void draw(LintfordCore core) {
		GL11.glClearColor(0.08f, .02f, 0.03f, 1.f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		mGameCamera.setZoomFactor(2.f);

		super.draw(core);
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	// DATA ----------------------------------------

	@Override
	protected void createData(DataManager dataManager) {
		mGameState = new GameState();
		mGameWorld = new GameWorld();
		mParticleData = new ParticleFrameworkData(dataManager, entityGroupUid());
		mParticleData.loadFromMetaFiles();

		mWispEmitter = mParticleData.particleEmitterManager().createNewParticleEmitterFromDefinitionName("PS_WISP");
		mWispEmitter.triggerEmission(); // is timed, so should keep going ?

		mProjetileManager = new ProjectileManager();
	}

	// CONTROLLERS ---------------------------------

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		mGameStateController = new GameStateController(controllerManager, mGameState, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mAnimationController = new AnimationController(controllerManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mLevelController = new LevelController(controllerManager, mGameWorld, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mParticleFrameworkController = new ParticleFrameworkController(controllerManager, mParticleData, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mMobController = new MobController(controllerManager, mGameWorld, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mProjectileController = new ProjectileController(controllerManager, mGameWorld, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mPlayerController = new PlayerController(controllerManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mCameraFollowController = new CameraFollowController(controllerManager, mGameCamera, ConstantsGame.GAME_RESOURCE_GROUP_ID);

		mGameStateController.setGameStateListener(this);
	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		mGameStateController.initialize(core);
		mAnimationController.initialize(core);
		mMobController.initialize(core);
		mPlayerController.initialize(core);
		mProjectileController.initialize(core);
		mParticleFrameworkController.initialize(core);
		mCameraFollowController.initialize(core);
		mLevelController.initialize(core);
	}

	// RENDERERS -----------------------------------

	@Override
	protected void createRenderers(LintfordCore core) {

		// TODO: this is the order of rendering, not the structure below.

		mAnimationRenderer = new AnimationRenderer(mRendererManager, mAnimationController, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mLevelRenderer = new LevelRenderer(mRendererManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mParticleFrameworkRenderer = new ParticleFrameworkRenderer(mRendererManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mMobRenderer = new MobRenderer(mRendererManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mHudRenderer = new HudRenderer(mRendererManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mProjectileRenderer = new ProjectileRenderer(mRendererManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
	}

	@Override
	protected void createRendererStructure(LintfordCore core) {
		mRendererManager.addRenderer(mAnimationRenderer);
		mRendererManager.addRenderer(mLevelRenderer);
		mRendererManager.addRenderer(mParticleFrameworkRenderer);
		mRendererManager.addRenderer(mMobRenderer);
		mRendererManager.addRenderer(mProjectileRenderer);
		mRendererManager.addRenderer(mHudRenderer);
	}

	// --------------------------------------
	// Callback-Methods
	// --------------------------------------

	@Override
	public void onGameWon() {
		screenManager.addScreen(new WonScreen(screenManager, mSceneHeader, mGameOptions));
	}

	@Override
	public void onGameLost() {
		screenManager.addScreen(new LostScreen(screenManager, mSceneHeader, mGameOptions));
	}
}
