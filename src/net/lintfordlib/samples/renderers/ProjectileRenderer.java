package net.lintfordlib.samples.renderers;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.core.rendering.RenderPass;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManagerBase;
import net.lintfordlib.samples.controllers.ProjectileController;
import net.lintfordlib.samples.data.textures.GameTextureNames;

public class ProjectileRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Projectile Renderer";

	private static final float FULL_FLASH_DUR = 150;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private ProjectileController mProjectileController;

	private SpriteSheetDefinition mGameSpriteSheet;

	// --------------------------------------
	// Property
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mProjectileController != null;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ProjectileRenderer(RendererManagerBase rendererManager, int entityGroupUid) {
		super(rendererManager, RENDERER_NAME, entityGroupUid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();
		mProjectileController = (ProjectileController) lControllerManager.getControllerByNameRequired(ProjectileController.CONTROLLER_NAME, mEntityGroupUid);
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mGameSpriteSheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_GAME", entityGroupID());
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mGameSpriteSheet = null;
	}

	@Override
	public void draw(LintfordCore core, RenderPass renderPass) {
		final var lProjectileManager = mProjectileController.projectileManager();
		final var lProjectiles = lProjectileManager.projectiles();

		final var lSpriteBatch = rendererManager().sharedResources().uiSpriteBatch();

		final var lSpriteFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.ARROW);

		lSpriteBatch.begin(core.gameCamera());

		final int lProjectileCount = lProjectiles.size();
		for (int i = 0; i < lProjectileCount; i++) {
			final var lProj = lProjectiles.get(i);

			var lTintColor = ColorConstants.WHITE();
			if (!lProj.isDamageCooldownElapsed() && lProj.damageCooldownTimerMs % FULL_FLASH_DUR < FULL_FLASH_DUR * .5f)
				lTintColor = ColorConstants.getColor(100, 100, 100, 1);

			lSpriteBatch.setColor(lTintColor);

			lSpriteBatch.drawAroundCenter(mGameSpriteSheet, lSpriteFrame, lProj.xx, lProj.yy, 16.f, 16.f, lProj.heading, 0, 0, .1f);
		}

		lSpriteBatch.end();
	}
}
