package net.lintfordlib.samples.renderers;

import org.lwjgl.opengl.GL11;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.core.rendering.RenderPass;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManagerBase;
import net.lintfordlib.samples.controllers.MobController;
import net.lintfordlib.samples.data.mobs.MobInstance;
import net.lintfordlib.samples.data.mobs.MobTypeIndex;

public class MobRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Cell Entity Renderer";

	private static final float FULL_FLASH_DUR = 150;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private MobController mMobController;

	private SpriteSheetDefinition mMobSpriteSheet;

	// --------------------------------------
	// Property
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobRenderer(RendererManagerBase rendererManager, int entityGroupUid) {
		super(rendererManager, RENDERER_NAME, entityGroupUid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();
		mMobController = (MobController) lControllerManager.getControllerByNameRequired(MobController.CONTROLLER_NAME, mEntityGroupUid);

	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mMobSpriteSheet = resourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetMobs.json", entityGroupID());
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mMobSpriteSheet = null;
	}

	@Override
	public void draw(LintfordCore core, RenderPass renderPass) {
		final var lMobManager = mMobController.mobManager();

		final var lMobList = lMobManager.mobs();
		if (lMobList == null || lMobList.size() == 0)
			return;

		final var lSpriteBatch = rendererManager().sharedResources().uiSpriteBatch();

		lSpriteBatch.begin(core.gameCamera());

		final int lMobCount = lMobList.size();
		for (int i = 0; i < lMobCount; i++) {
			final var lMobInstance = (MobInstance) lMobList.get(i);

			final float lMobWorldPositionX = lMobInstance.xx;
			final float lMobWorldPositionY = lMobInstance.yy;

			updateMobSpriteInstance(lMobInstance);
			final var lMobSpriteInstance = lMobInstance.currentSprite;

			if (lMobSpriteInstance == null)
				continue;

			lMobSpriteInstance.setCenterPosition(lMobWorldPositionX, lMobWorldPositionY);
			lMobSpriteInstance.update(core);

			final float lHalfWidth = 8.f;
			final float lMobWidth = lMobInstance.currentSprite.width();

			var lTintColor = ColorConstants.WHITE();
			if (!lMobInstance.isDamageCooldownElapsed() && lMobInstance.damageCooldownTimerMs % FULL_FLASH_DUR < FULL_FLASH_DUR * .5f)
				lTintColor = ColorConstants.getColor(100, 100, 100, 1);

			lSpriteBatch.setColor(lTintColor);

			if (lMobInstance.isLeftFacing)
				lSpriteBatch.draw(mMobSpriteSheet, lMobSpriteInstance.currentSpriteFrame(), lMobWorldPositionX + lHalfWidth, lMobWorldPositionY - lHalfWidth, -lMobWidth, 16, .1f);
			else
				lSpriteBatch.draw(mMobSpriteSheet, lMobSpriteInstance.currentSpriteFrame(), lMobWorldPositionX - lHalfWidth, lMobWorldPositionY - lHalfWidth, lMobWidth, 16, .1f);

			GL11.glPointSize(5);
			Debug.debugManager().drawers().drawPointImmediate(core.gameCamera(), lMobInstance.auxForwardPosX, lMobInstance.auxForwardPosY);

		}

		lSpriteBatch.end();
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	private void updateMobSpriteInstance(MobInstance mob) {
		String lCurrentAnimationName = null; // "P1_COMMANDER";

		switch (mob.def().typeUid) {
		case MobTypeIndex.MOB_TYPE_PLAYER_COMANDER:
			lCurrentAnimationName = "P1_COMMANDER";
			break;

		case MobTypeIndex.MOB_TYPE_PLAYER_DIGGER:
			lCurrentAnimationName = "P1_DIGGER";
			break;

		case MobTypeIndex.MOB_TYPE_PLAYER_MELEE:
			lCurrentAnimationName = "P1_MELEE";
			break;

		case MobTypeIndex.MOB_TYPE_PLAYER_RANGE:
			lCurrentAnimationName = "P1_RANGE";
			break;

		case MobTypeIndex.MOB_TYPE_GOBLIN_MELEE:
			lCurrentAnimationName = "GOB_MELEE";
			break;

		case MobTypeIndex.MOB_TYPE_GOBLIN_RANGE:
			lCurrentAnimationName = "GOB_RANGE";
			break;
		}

//		if (mob.swingingFlag && mob.swingAttackEnabled) {
//			lCurrentAnimationName = mob.mobTypeName() + "_SWING";
//
//		}

		if (mob == null || mob.mCurrentAnimationName == null || !mob.mCurrentAnimationName.equals(lCurrentAnimationName)) {
			mob.currentSprite = mMobSpriteSheet.getSpriteInstance(lCurrentAnimationName);
			if (mob.currentSprite != null) {
				mob.mCurrentAnimationName = lCurrentAnimationName;
			}
		}
	}
}
