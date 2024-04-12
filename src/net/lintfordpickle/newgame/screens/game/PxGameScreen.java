package net.lintfordpickle.newgame.screens.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.core.geometry.Rectangle;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.rendertarget.RTCamera;
import net.lintfordlib.core.graphics.rendertarget.RenderTarget;
import net.lintfordlib.core.graphics.textures.Texture;
import net.lintfordlib.data.DataManager;
import net.lintfordlib.data.scene.SceneHeader;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.screens.BaseGameScreen;
import net.lintfordpickle.newgame.ConstantsGame;
import net.lintfordpickle.newgame.NewGameKeyActions;

public class PxGameScreen extends BaseGameScreen {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final int CANVAS_PIXEL_SIZE = 4;

	public static final int VIEWPORT_WIDTH = 960;
	public static final int VIEWPORT_HEIGHT = 576;
	public static final int CANVAS_WIDTH = VIEWPORT_WIDTH / CANVAS_PIXEL_SIZE;
	public static final int CANVAS_HEIGHT = VIEWPORT_HEIGHT / CANVAS_PIXEL_SIZE;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SceneHeader mSceneHeader;
	private RenderTarget mRenderTarget;
	private Texture mOverlayTexture;
	private Texture mTextureDave;
	private RTCamera mRTCamera;
	private final Rectangle mViewport = new Rectangle();

	// --------------------------------------
	// Properties
	// --------------------------------------

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public PxGameScreen(ScreenManager screenManager, SceneHeader sceneHeader) {
		super(screenManager);

		mSceneHeader = sceneHeader;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mRTCamera = new RTCamera(CANVAS_WIDTH, CANVAS_HEIGHT);
		mRenderTarget = mRendererManager.createRenderTarget("PxGameScreenRT", CANVAS_WIDTH, CANVAS_HEIGHT, 1, GL11.GL_NEAREST, false);
		mViewport.setDimensions(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

		mOverlayTexture = resourceManager.textureManager().loadTexture("TEXTURE_OVERLAY", "res/textures/texturePxOverlay940x576.png", ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mTextureDave = resourceManager.textureManager().loadTexture("TEXTURE_DAVE", "res/textures/textureDave.png", ConstantsGame.GAME_RESOURCE_GROUP_ID);
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mOverlayTexture = null;
		mRendererManager.unloadRenderTarget(mRenderTarget);

	}

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

	int daveX, daveY;

	@Override
	public void draw(LintfordCore core) {
		final var lGameCam = mGameCamera;

		mRenderTarget.bind();

		GL11.glClearColor(21f / 255f, 19f / 255f, 24f / 255f, 1.f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		super.draw(core);

		daveX = (int) (Math.sin(core.gameTime().totalTimeSeconds() * .1f) * 100);
		daveY = (int) 0;

		spriteBatch().begin(mRTCamera);
		spriteBatch().draw(mTextureDave, 0, 0, 32, 32, daveX, 0, 32, 32, -0.1f, ColorConstants.WHITE);
		spriteBatch().end();

		mRenderTarget.unbind();

		// restore core
		core.setActiveGameCamera(lGameCam);
		core.config().display().reapplyGlViewport();

		GL11.glClearColor(0.98f, .27f, 0.11f, 1.f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		mViewport.set(-VIEWPORT_WIDTH / 2, -VIEWPORT_HEIGHT / 2, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		// Render out Px effect to window
		Debug.debugManager().drawers().drawRenderTargetImmediate(core, mViewport, -10f, mRenderTarget);

		spriteBatch().begin(core.gameCamera());
		spriteBatch().draw(mOverlayTexture, 0, 0, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, -VIEWPORT_WIDTH / 2, -VIEWPORT_HEIGHT / 2, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, -0.1f, ColorConstants.WHITE);
		spriteBatch().end();
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
