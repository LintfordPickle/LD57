package net.lintfordpickle.newgame.screens.game;

import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.data.DataManager;
import net.lintfordlib.data.scene.SceneHeader;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.screens.BaseGameScreen;

public class GameScreen extends BaseGameScreen {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SceneHeader mSceneHeader;

	// --------------------------------------
	// Properties
	// --------------------------------------

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
	public void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		// TODO Auto-generated method stub
		super.loadResources(resourceManager);
	}

	@Override
	public void unloadResources() {
		// TODO Auto-generated method stub
		super.unloadResources();
	}

	@Override
	public void handleInput(LintfordCore core) {
		// TODO Auto-generated method stub
		super.handleInput(core);
	}

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		// TODO Auto-generated method stub
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);
	}

	@Override
	public void draw(LintfordCore core) {
		// TODO Auto-generated method stub
		super.draw(core);
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	// DATA ----------------------------------------

	@Override
	protected void createData(DataManager dataManager) {
		// TODO Auto-generated method stub

	}

	// CONTROLLERS ---------------------------------

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		// TODO Auto-generated method stub

	}

	// RENDERERS -----------------------------------

	@Override
	protected void createRenderers(LintfordCore core) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {
		// TODO Auto-generated method stub

	}
}
