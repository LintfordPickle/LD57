package net.lintfordlib.samples.renderers;

import org.lwjgl.opengl.GL11;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.rendering.RenderPass;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManagerBase;
import net.lintfordlib.samples.controllers.GameStateController;
import net.lintfordlib.samples.controllers.LevelController;

public class HudRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Hud Renderer";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private LevelController mLevelController;
	private GameStateController mGameStateController;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return true;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public HudRenderer(RendererManagerBase rendererManager, int entityGroupUid) {
		super(rendererManager, RENDERER_NAME, entityGroupUid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();

		mLevelController = (LevelController) lControllerManager.getControllerByNameRequired(LevelController.CONTROLLER_NAME, mEntityGroupUid);
		mGameStateController = (GameStateController) lControllerManager.getControllerByNameRequired(GameStateController.CONTROLLER_NAME, mEntityGroupUid);
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		// load hud related game resources
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		// unload hud related game resources
	}

	@Override
	public boolean handleInput(LintfordCore core) {
		return super.handleInput(core);

		// Handle any hud input actions
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		// Update hud elements
	}

	@Override
	public void draw(LintfordCore core, RenderPass renderPass) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		final var lHudBounds = core.HUD().boundingRectangle();

		final var lFontBatch = mRendererManager.sharedResources().uiTitleFont();

		lFontBatch.begin(core.HUD());
		lFontBatch.drawShadowedText("Level Name: " + mLevelController.cellLevel().name() + " - '" + mLevelController.cellLevel().filename() + "'", lHudBounds.left() + 10.f, lHudBounds.top() + 10.f, .1f, 1.f, 1.f, 1.f);
		lFontBatch.drawShadowedText("Commander Health", lHudBounds.left() + 10.f, lHudBounds.top() + 30.f, .1f, 1.f, 1.f, 1.f);

		lFontBatch.drawShadowedText("Credits", lHudBounds.left() + 10.f, lHudBounds.top() + 50.f, .1f, 1.f, 1.f, 1.f);
		lFontBatch.end();
	}
}
