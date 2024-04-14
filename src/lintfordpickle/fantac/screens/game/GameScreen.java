package lintfordpickle.fantac.screens.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lintfordpickle.fantac.ConstantsGame;
import lintfordpickle.fantac.NewGameKeyActions;
import lintfordpickle.fantac.controllers.AiController;
import lintfordpickle.fantac.controllers.AnimationController;
import lintfordpickle.fantac.controllers.GameStateController;
import lintfordpickle.fantac.controllers.JobController;
import lintfordpickle.fantac.controllers.SettlementController;
import lintfordpickle.fantac.controllers.TeamController;
import lintfordpickle.fantac.controllers.UnitController;
import lintfordpickle.fantac.data.GameWorld;
import lintfordpickle.fantac.data.IGameStateListener;
import lintfordpickle.fantac.data.settlements.SettlementType;
import lintfordpickle.fantac.data.teams.TeamManager;
import lintfordpickle.fantac.data.teams.TeamRace;
import lintfordpickle.fantac.renderers.AnimationRenderer;
import lintfordpickle.fantac.renderers.SettlementsRenderer;
import lintfordpickle.fantac.renderers.UnitsRenderer;
import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.controllers.core.particles.ParticleFrameworkController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.particles.ParticleFrameworkData;
import net.lintfordlib.data.DataManager;
import net.lintfordlib.data.scene.SceneHeader;
import net.lintfordlib.renderers.RendererManager;
import net.lintfordlib.renderers.particles.ParticleFrameworkRenderer;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.screens.BaseGameScreen;

public class GameScreen extends BaseGameScreen implements IGameStateListener {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SceneHeader mSceneHeader;

	// Data
	private GameWorld mGameWorld;
	private ParticleFrameworkData mParticleFrameworkData;

	// Controllers
	private GameStateController mGameStateController;
	private SettlementController mSettlementController;
	private UnitController mUnitController;
	private JobController mJobController;
	private AnimationController mAnimationController;
	private ParticleFrameworkController mParticleFrameworkController;
	private AiController mAiController;
	private TeamController mTeamController;

	// Renderers
	private SettlementsRenderer mSettlementRenderer;
	private UnitsRenderer mUnitsRenderer;
	private AnimationRenderer mAnimationRenderer;
	private ParticleFrameworkRenderer mParticleFrameworkRenderer;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameScreen(ScreenManager screenManager, SceneHeader sceneHeader) {
		super(screenManager, new RendererManager(screenManager.core(), ConstantsGame.GAME_RESOURCE_GROUP_ID));

		mSceneHeader = sceneHeader;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void handleInput(LintfordCore core) {
		super.handleInput(core);

		if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_ESCAPE, this) || core.input().gamepads().isGamepadButtonDownTimed(GLFW.GLFW_GAMEPAD_BUTTON_START, this)) {
			screenManager().addScreen(new PauseScreen(screenManager(), mSceneHeader));
			return;
		}

		// For simple games you could add code to handle the player input here.
		// However usually, components would be updated in dedicated BaseControllers (see the CONTROLLERS Section below).
	}

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);

		// For simple games you could add code to perform the game update logic here.
		// However usually, components would be updated in dedicated BaseControllers (see the CONTROLLERS Section below).

		if (core.input().eventActionManager().getCurrentControlActionStateTimed(NewGameKeyActions.KEY_BINDING_PRIMARY_FIRE)) {
			screenManager().toastManager().addMessage(getClass().getSimpleName(), "PRIMARY FIRE", 1500);
		}

	}

	@Override
	public void draw(LintfordCore core) {

		GL11.glClearColor(0.08f, .02f, 0.03f, 1.f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		super.draw(core);

		// For simple games you could add code to render a basic scene directly here.
		// However usually, the rendering would be performed by a specific BaseRenderer (see the RENDERERS section below).

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	// DATA ----------------------------------------

	@SuppressWarnings("unused")
	@Override
	protected void createData(DataManager dataManager) {
		mParticleFrameworkData = new ParticleFrameworkData(dataManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mParticleFrameworkData.loadFromMetaFiles();

		mGameWorld = new GameWorld();

		// TODO:
		final var lPlayerTeam = mGameWorld.team().addTeam(true, TeamRace.RACE_DEMONS);
		final var lNpcTeam = mGameWorld.team().addTeam(false, TeamRace.RACE_HUMANS);

		// team 1 - humans
		// team 2 - demons

		// setup settlements
		final var lSettlement00 = mGameWorld.settlements().addNewSettlement(lPlayerTeam.teamUid, SettlementType.SETTLEMENT_TYPE_TOWN, -300, -200);
		final var lSettlement01 = mGameWorld.settlements().addNewSettlement(lPlayerTeam.teamUid, SettlementType.SETTLEMENT_TYPE_TOWN, -330, 170);

		lSettlement00.numWorkers = 8;
		lSettlement01.numWorkers = 12;

		final var lSettlement02 = mGameWorld.settlements().addNewSettlement(lNpcTeam.teamUid, SettlementType.SETTLEMENT_TYPE_TOWN, +300, -200);
		final var lSettlement03 = mGameWorld.settlements().addNewSettlement(lNpcTeam.teamUid, SettlementType.SETTLEMENT_TYPE_TOWN, +270, 170);
		lSettlement02.numWorkers = 7;
		lSettlement03.numWorkers = 15;

		mGameWorld.settlements().addNewSettlement(TeamManager.CONTROLLED_NONE, SettlementType.SETTLEMENT_TYPE_CASTLE, -150, -50);
		mGameWorld.settlements().addNewSettlement(TeamManager.CONTROLLED_NONE, SettlementType.SETTLEMENT_TYPE_SCHOOL, +350, -0);

	}

	// CONTROLLERS ---------------------------------

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		mGameStateController = new GameStateController(controllerManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mTeamController = new TeamController(controllerManager, mGameWorld.team(), ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mSettlementController = new SettlementController(controllerManager, mGameWorld.settlements(), ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mUnitController = new UnitController(controllerManager, mGameWorld.units(), ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mJobController = new JobController(controllerManager, mGameWorld.jobs(), ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mAnimationController = new AnimationController(controllerManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mParticleFrameworkController = new ParticleFrameworkController(controllerManager, mParticleFrameworkData, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mAiController = new AiController(controllerManager, mGameWorld.team(), ConstantsGame.GAME_RESOURCE_GROUP_ID);

		mGameStateController.setGameStateListener(this);
	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		mTeamController.initialize(core);
		mGameStateController.initialize(core);
		mSettlementController.initialize(core);
		mUnitController.initialize(core);
		mJobController.initialize(core);
		mAnimationController.initialize(core);
		mParticleFrameworkController.initialize(core);
		mAiController.initialize(core);
	}

	// RENDERERS -----------------------------------

	@Override
	protected void createRenderers(LintfordCore core) {
		mUnitsRenderer = new UnitsRenderer(mRendererManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mSettlementRenderer = new SettlementsRenderer(mRendererManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mAnimationRenderer = new AnimationRenderer(mRendererManager, mAnimationController, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mParticleFrameworkRenderer = new ParticleFrameworkRenderer(mRendererManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		mSettlementRenderer.initialize(core);
		mUnitsRenderer.initialize(core);
		mAnimationRenderer.initialize(core);
		mParticleFrameworkRenderer.initialize(core);
	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {
		mSettlementRenderer.loadResources(resourceManager);
		mUnitsRenderer.loadResources(resourceManager);
		mAnimationRenderer.loadResources(resourceManager);
		mParticleFrameworkRenderer.loadResources(resourceManager);

	}

	// --------------------------------------
	// Callback-Methods
	// --------------------------------------

	@Override
	public void onGameWon(int teamUid) {
		mScreenManager.addScreen(new WonScreen(mScreenManager, mSceneHeader));

	}

	@Override
	public void onGameLost(int teamUid) {
		mScreenManager.addScreen(new LostScreen(mScreenManager, mSceneHeader));

	}
}
