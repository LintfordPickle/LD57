package lintfordpickle.fantac.screens.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lintfordpickle.fantac.NewGameKeyActions;
import lintfordpickle.fantac.controllers.AnimationController;
import lintfordpickle.fantac.controllers.JobsController;
import lintfordpickle.fantac.controllers.SettlementsController;
import lintfordpickle.fantac.controllers.UnitsController;
import lintfordpickle.fantac.data.GameWorld;
import lintfordpickle.fantac.data.Teams;
import lintfordpickle.fantac.data.settlements.SettlementType;
import lintfordpickle.fantac.renderers.AnimationRenderer;
import lintfordpickle.fantac.renderers.SettlementsRenderer;
import lintfordpickle.fantac.renderers.UnitsRenderer;
import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.data.DataManager;
import net.lintfordlib.data.scene.SceneHeader;
import net.lintfordlib.renderers.sprites.FafAnimationRenderer;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.screens.BaseGameScreen;

public class GameScreen extends BaseGameScreen {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SceneHeader mSceneHeader;

	// Data
	private GameWorld mGameWorld;

	// Controllers
	private SettlementsController mSettlementsController;
	private UnitsController mUnitsController;
	private JobsController mJobsController;
	private AnimationController mAnimationController;

	// Renderers
	private SettlementsRenderer mSettlementRenderer;
	private UnitsRenderer mUnitsRenderer;
	private FafAnimationRenderer mFafAnimationRenderer;
	private AnimationRenderer mAnimationRenderer;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameScreen(ScreenManager screenManager, SceneHeader sceneHeader) {
		super(screenManager);

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

	@Override
	protected void createData(DataManager dataManager) {
		mGameWorld = new GameWorld();

		// team 1 - humans
		// team 2 - demons

		// setup settlements
		final var lSettlement00 = mGameWorld.settlements().addNewSettlement(Teams.TEAM_1_UID, SettlementType.SETTLEMENT_TYPE_TOWN, -300, -200);
		final var lSettlement02 = mGameWorld.settlements().addNewSettlement(Teams.TEAM_1_UID, SettlementType.SETTLEMENT_TYPE_TOWN, -330, 170);
		final var lSettlement06 = mGameWorld.settlements().addNewSettlement(Teams.TEAM_NONE_UID, SettlementType.SETTLEMENT_TYPE_CASTLE, -150, -50);

		lSettlement00.numWorkers = 10;
		lSettlement02.numWorkers = 10;

		final var lSettlement03 = mGameWorld.settlements().addNewSettlement(Teams.TEAM_2_UID, SettlementType.SETTLEMENT_TYPE_TOWN, +300, -200);
		final var lSettlement04 = mGameWorld.settlements().addNewSettlement(Teams.TEAM_2_UID, SettlementType.SETTLEMENT_TYPE_SCHOOL, +350, -0);
		final var lSettlement05 = mGameWorld.settlements().addNewSettlement(Teams.TEAM_2_UID, SettlementType.SETTLEMENT_TYPE_TOWN, +270, 170);
		lSettlement03.numWorkers = 7;
		lSettlement05.numWorkers = 7;

	}

	// CONTROLLERS ---------------------------------

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		mSettlementsController = new SettlementsController(controllerManager, mGameWorld.settlements(), entityGroupUid());
		mUnitsController = new UnitsController(controllerManager, mGameWorld.units(), entityGroupUid());
		mJobsController = new JobsController(controllerManager, mGameWorld.jobs(), entityGroupUid());
		mAnimationController = new AnimationController(controllerManager, entityGroupUid());
	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		mSettlementsController.initialize(core);
		mUnitsController.initialize(core);
		mJobsController.initialize(core);
		mAnimationController.initialize(core);
	}

	// RENDERERS -----------------------------------

	@Override
	protected void createRenderers(LintfordCore core) {
		mUnitsRenderer = new UnitsRenderer(mRendererManager, entityGroupUid());
		mSettlementRenderer = new SettlementsRenderer(mRendererManager, entityGroupUid());
		mAnimationRenderer = new AnimationRenderer(mRendererManager, mAnimationController, entityGroupUid());
	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		mSettlementRenderer.initialize(core);
		mUnitsRenderer.initialize(core);
		mAnimationRenderer.initialize(core);
	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {
		mSettlementRenderer.loadResources(resourceManager);
		mUnitsRenderer.loadResources(resourceManager);
		mAnimationRenderer.loadResources(resourceManager);

	}
}
