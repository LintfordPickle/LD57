package lintfordpickle.fantac.renderers;

import lintfordpickle.fantac.ConstantsGame;
import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.controllers.core.FafAnimationController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.renderers.RendererManager;
import net.lintfordlib.renderers.sprites.FafAnimationRenderer;

public class AnimationRenderer extends FafAnimationRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Animation Renderer";

	// --------------------------------------
	// Variables
	// --------------------------------------

	// --------------------------------------
	// Properties
	// --------------------------------------

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public AnimationRenderer(RendererManager rendererManager, FafAnimationController animationController, int entityGroupUid) {
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
	public void draw(LintfordCore core) {
		if (!isInitialized())
			return;

		final var lSpritesheetDefinition = mFafAnimationController.spritesheetDefintion();

		if (lSpritesheetDefinition == null) {
			return;
		}

		final var lSpriteBatch = rendererManager().uiSpriteBatch();
		lSpriteBatch.begin(core.gameCamera());
		final int lNumAnimations = animationUpdateList.size();
		for (int i = 0; i < lNumAnimations; i++) {

			final var lAnimInstance = animationUpdateList.get(i);

			final float lDstW = lAnimInstance.width() * 2.f;
			final float lDstH = lAnimInstance.height() * 2.f;
			final float lDstX = lAnimInstance.x() - lDstW * .5f;
			final float lDstY = lAnimInstance.y() - lDstH * .5f;

			lSpriteBatch.draw(lSpritesheetDefinition, lAnimInstance.currentSpriteFrame(), lDstX, lDstY, lDstW, lDstH, -0.4f, ColorConstants.WHITE);
		}

		lSpriteBatch.end();
	}
	
	// --------------------------------------
	// Methods
	// --------------------------------------
}
