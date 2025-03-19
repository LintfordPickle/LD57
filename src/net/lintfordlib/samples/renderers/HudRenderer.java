package net.lintfordlib.samples.renderers;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.core.rendering.RenderPass;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManagerBase;
import net.lintfordlib.samples.ConstantsGame;

public class HudRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Hud Renderer";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SpriteSheetDefinition mHudSpritesheet;

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

		// Get any controllers or renderers created with the game screen.

	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mHudSpritesheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_HUD", ConstantsGame.GAME_RESOURCE_GROUP_ID);
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
		final var lSpriteBatch = mRendererManager.sharedResources().uiSpriteBatch();
		final var lFontBatch = mRendererManager.sharedResources().uiHeaderFont();

		lSpriteBatch.begin(core.gameCamera());
		lFontBatch.begin(core.gameCamera());

		lSpriteBatch.end();
		lFontBatch.end();
	}
}
