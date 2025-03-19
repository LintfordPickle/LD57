package net.lintfordlib.samples.renderers;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.controllers.core.FafAnimationController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.rendering.RenderPass;
import net.lintfordlib.renderers.RendererManagerBase;
import net.lintfordlib.renderers.sprites.FafAnimationRenderer;
import net.lintfordlib.samples.ConstantsGame;

public class AnimationRenderer extends FafAnimationRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Animation Renderer";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public AnimationRenderer(RendererManagerBase rendererManager, FafAnimationController animationController, int entityGroupUid) {
		super(rendererManager, RENDERER_NAME, animationController, entityGroupUid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		final var lGameSpritesheetDef = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_GAME", ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mFafAnimationController.spritesheetDefinition(lGameSpritesheetDef);
	}

	@Override
	public void draw(LintfordCore core, RenderPass renderPass) {
		if (!isInitialized())
			return;

		final var lSpritesheetDefinition = mFafAnimationController.spritesheetDefintion();

		if (lSpritesheetDefinition == null) {
			return;
		}

		final var lSpriteBatch = rendererManager().sharedResources().uiSpriteBatch();
		lSpriteBatch.begin(core.gameCamera());
		lSpriteBatch.setColorWhite();

		final int lNumAnimations = animationUpdateList.size();
		for (int i = 0; i < lNumAnimations; i++) {

			final var lAnimInstance = animationUpdateList.get(i);

			final float lDstW = lAnimInstance.width() * 2.f;
			final float lDstH = lAnimInstance.height() * 2.f;
			final float lDstX = lAnimInstance.x() - lDstW * .5f;
			final float lDstY = lAnimInstance.y() - lDstH * .5f;

			lSpriteBatch.draw(lSpritesheetDefinition, lAnimInstance.currentSpriteFrame(), lDstX, lDstY, lDstW, lDstH, 0.4f);
		}

		lSpriteBatch.end();
	}
}
