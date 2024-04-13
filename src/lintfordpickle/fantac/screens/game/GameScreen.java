package lintfordpickle.fantac.screens.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lintfordpickle.fantac.NewGameKeyActions;
import lintfordpickle.fantac.controllers.JobsController;
import lintfordpickle.fantac.controllers.SettlementsController;
import lintfordpickle.fantac.controllers.UnitsController;
import lintfordpickle.fantac.data.GameWorld;
import lintfordpickle.fantac.renderers.SettlementsRenderer;
import lintfordpickle.fantac.renderers.UnitsRenderer;
import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.data.DataManager;
import net.lintfordlib.data.scene.SceneHeader;
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

	// Renderers
	private SettlementsRenderer mSettlementRenderer;
	private UnitsRenderer mUnitsRenderer;

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

		// setup settlements
		mGameWorld.settlements().addNewSettlement(0, -130);
		mGameWorld.settlements().instances().get(0).numWorkers = 20;

		mGameWorld.settlements().addNewSettlement(-250, 0);
		mGameWorld.settlements().instances().get(1).numWorkers = 50;

		mGameWorld.settlements().addNewSettlement(250, 0);

		mGameWorld.units().addNewUnit(0, 0, 0, 50, 50);

	}

	// CONTROLLERS ---------------------------------

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		mSettlementsController = new SettlementsController(controllerManager, mGameWorld.settlements(), entityGroupUid());
		mUnitsController = new UnitsController(controllerManager, mGameWorld.units(), entityGroupUid());
		mJobsController = new JobsController(controllerManager, mGameWorld.jobs(), entityGroupUid());
	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		mSettlementsController.initialize(core);
		mUnitsController.initialize(core);
		mJobsController.initialize(core);
	}

	// RENDERERS -----------------------------------

	@Override
	protected void createRenderers(LintfordCore core) {
		mSettlementRenderer = new SettlementsRenderer(mRendererManager, entityGroupUid());
		mUnitsRenderer = new UnitsRenderer(mRendererManager, entityGroupUid());
	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		mSettlementRenderer.initialize(core);
		mUnitsRenderer.initialize(core);
	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {
		mSettlementRenderer.loadResources(resourceManager);
		mUnitsRenderer.loadResources(resourceManager);

	}
}
