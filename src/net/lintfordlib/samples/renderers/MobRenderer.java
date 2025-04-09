package net.lintfordlib.samples.renderers;

import org.lwjgl.opengl.GL11;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.batching.SpriteBatch;
import net.lintfordlib.core.graphics.sprites.SpriteFrame;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.core.rendering.RenderPass;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManagerBase;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.controllers.LevelController;
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
	private LevelController mLevelController;

	private SpriteSheetDefinition mMobSpriteSheet;
	private SpriteFrame mCoinFrame;
	private SpriteFrame mHeartFrame;
	private SpriteFrame mShadowFrame;

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
		mLevelController = (LevelController) lControllerManager.getControllerByNameRequired(LevelController.CONTROLLER_NAME, mEntityGroupUid);

	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mMobSpriteSheet = resourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetMobs.json", entityGroupID());

		mCoinFrame = mMobSpriteSheet.getSpriteFrame("COIN");
		mHeartFrame = mMobSpriteSheet.getSpriteFrame("HEART");
		mShadowFrame = mMobSpriteSheet.getSpriteFrame("SHADOW");
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mMobSpriteSheet = null;
	}

	@Override
	public void draw(LintfordCore core, RenderPass renderPass) {
		final var lMobManager = mMobController.mobManager();
		final var lDepthValues = mLevelController.cellLevel().tileDepth();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);

		final var lMobList = lMobManager.mobs();
		if (lMobList == null || lMobList.size() == 0)
			return;

		final var lSpriteBatch = rendererManager().sharedResources().uiSpriteBatch();

		lSpriteBatch.begin(core.gameCamera());

		final int lMobCount = lMobList.size();
		for (int i = 0; i < lMobCount; i++) {
			final var lMobInstance = (MobInstance) lMobList.get(i);

			final var step = (lMobInstance.highStep ? 2.f : 0.f);
			final float lMobWorldPositionX = lMobInstance.xx;
			final float lMobWorldPositionY = lMobInstance.yy - step;

			updateMobSpriteInstance(lMobInstance);
			final var lMobSpriteInstance = lMobInstance.currentSprite;

			if (lMobSpriteInstance == null)
				continue;

			lMobSpriteInstance.setCenterPosition(lMobWorldPositionX, lMobWorldPositionY);
			lMobSpriteInstance.update(core);

			final float lHalfWidth = 8.f;
			final float lHalfHeight = 8.f;
			final float lMobWidth = lMobInstance.currentSprite.width();

			final var lTileCoord = lMobInstance.cy * ConstantsGame.LEVEL_TILES_WIDE + lMobInstance.cx;
			final var lTileDepth = lDepthValues[lTileCoord];

			final var lDepthTolerance = 1.5f;
			final var lInvDepth = 1.f - (lTileDepth / (float) ConstantsGame.LEVEL_TILES_WIDE / lDepthTolerance);
			final var lDepthColorMod = ColorConstants.getColor(lInvDepth, lInvDepth, lInvDepth, 1.f);

			lSpriteBatch.setColorA(.5f);
			// lSpriteBatch.draw(mMobSpriteSheet, mShadowFrame, lMobWorldPositionX + lHalfWidth, lMobWorldPositionY - lHalfHeight, -lMobWidth, 16, .7f);

			var lTintColor = lDepthColorMod;
			if (!lMobInstance.isDamageCooldownElapsed() && lMobInstance.damageCooldownTimerMs % FULL_FLASH_DUR < FULL_FLASH_DUR * .5f)
				lTintColor = ColorConstants.getColor(100, 100, 100, 1);

			lSpriteBatch.setColor(lTintColor);

			if (lMobInstance.isLeftFacing)
				lSpriteBatch.draw(mMobSpriteSheet, lMobSpriteInstance.currentSpriteFrame(), lMobWorldPositionX + lHalfWidth, lMobWorldPositionY - lHalfHeight, -lMobWidth, 16, .1f);
			else
				lSpriteBatch.draw(mMobSpriteSheet, lMobSpriteInstance.currentSpriteFrame(), lMobWorldPositionX - lHalfWidth, lMobWorldPositionY - lHalfHeight, lMobWidth, 16, .1f);

			if (lMobInstance.def().typeUid != MobTypeIndex.MOB_TYPE_PLAYER_COMANDER)
				drawMobHealthBar(lSpriteBatch, lMobInstance, lMobWorldPositionX, lMobWorldPositionY);

			if (lMobInstance.def().typeUid == MobTypeIndex.MOB_TYPE_PLAYER_DIGGER)
				drawMobCarryBar(lSpriteBatch, lMobInstance, lMobWorldPositionX, lMobWorldPositionY);

			GL11.glPointSize(5);
			Debug.debugManager().drawers().drawPointImmediate(core.gameCamera(), lMobInstance.auxForwardPosX, lMobInstance.auxForwardPosY);

			if (ConstantsGame.DEBUG_DRAW_MOB_COLLISION_RADIUS) {
				var r = lMobInstance.teamUid == 0 ? 1.f : 1.f;
				var g = lMobInstance.teamUid == 0 ? 1.f : 0.f;
				var b = lMobInstance.teamUid == 0 ? 0.f : 0.f;

				Debug.debugManager().drawers().drawCircleImmediate(core.gameCamera(), lMobInstance.xx, lMobInstance.yy, lMobInstance.radiusRatio * ConstantsGame.BLOCK_SIZE, 12, GL11.GL_LINE_STRIP, 2, r, g, b);
			}
		}

		lSpriteBatch.end();
		GL11.glDepthMask(true);

		if (ConstantsGame.DEBUG_DRAW_MOB_COLLISION_RADIUS)
			drawMobCollisionRadius(core);

	}

	private void drawMobCollisionRadius(LintfordCore core) {
		final var lMobManager = mMobController.mobManager();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);

		final var lMobList = lMobManager.mobs();
		if (lMobList == null || lMobList.size() == 0)
			return;


		final int lMobCount = lMobList.size();
		for (int i = 0; i < lMobCount; i++) {
			Debug.debugManager().drawers().beginLineRenderer(core.gameCamera());
			final var lMobInstance = (MobInstance) lMobList.get(i);

			var r = lMobInstance.teamUid == 0 ? 1.f : 1.f;
			var g = lMobInstance.teamUid == 0 ? 1.f : 0.f;
			var b = lMobInstance.teamUid == 0 ? 0.f : 0.f;

			Debug.debugManager().drawers().drawCircle(lMobInstance.xx, lMobInstance.yy, lMobInstance.radiusRatio * ConstantsGame.BLOCK_SIZE, lMobInstance.heading, 12, GL11.GL_LINES, r, g, b);
			Debug.debugManager().drawers().endLineRenderer();
		}

	}

	private void drawMobHealthBar(SpriteBatch lSpriteBatch, MobInstance lMobInstance, float lMobWorldPositionX, float lMobWorldPositionY) {
		final var lHalfWidth = 8.f;
		final var lHalfHeight = 8.f;

		var xx = lMobWorldPositionX - lHalfWidth;
		var yy = lMobWorldPositionY + lHalfHeight - 3;

		final var lHeartSize = 2;

		for (int j = 0; j < lMobInstance.health; j++) {
			lSpriteBatch.setColorA(.5f);
			lSpriteBatch.draw(mMobSpriteSheet, mHeartFrame, xx, yy, lHeartSize, lHeartSize, .1f);
			yy -= (lHeartSize + 1);
		}
	}

	private void drawMobCarryBar(SpriteBatch lSpriteBatch, MobInstance lMobInstance, float lMobWorldPositionX, float lMobWorldPositionY) {
		final var lHalfWidth = 8.f;
		final var lHalfHeight = 8.f;

		final var xx = lMobWorldPositionX - lHalfWidth;
		final var yy = lMobWorldPositionY + lHalfHeight;

		final var lHeartSize = 2;

		final var lCarryingNum = lMobInstance.holdingGoldAmt;
		for (int j = 0; j < lCarryingNum; j++) {

			var xxx = xx + (j % 5) * (lHeartSize + 1);
			var yyy = yy + (j / 5) * (lHeartSize + 1);

			lSpriteBatch.setColorA(.5f);
			lSpriteBatch.draw(mMobSpriteSheet, mCoinFrame, xxx, yyy, lHeartSize, lHeartSize, .1f);
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	private void updateMobSpriteInstance(MobInstance mob) {
		if (mob == null)
			return;

		String lNextSpriteName = null; // "P1_COMMANDER";

		switch (mob.def().typeUid) {
		case MobTypeIndex.MOB_TYPE_PLAYER_COMANDER:
			lNextSpriteName = "commander_idle";
			break;

		case MobTypeIndex.MOB_TYPE_PLAYER_DIGGER:
			lNextSpriteName = "digger_idle";
			break;

		case MobTypeIndex.MOB_TYPE_PLAYER_MELEE:
			lNextSpriteName = "melee_idle";
			break;

		case MobTypeIndex.MOB_TYPE_PLAYER_RANGE:
			lNextSpriteName = "range_idle";
			break;

		case MobTypeIndex.MOB_TYPE_GOBLIN_MELEE:
			lNextSpriteName = "gob_melee_idle";
			break;

		case MobTypeIndex.MOB_TYPE_GOBLIN_RANGE:
			lNextSpriteName = "gob_range_idle";
			break;
		}

//		if (mob.swingingFlag && mob.swingAttackEnabled) {
//			lCurrentAnimationName = mob.mobTypeName() + "_SWING";
//
//		}

		if (mob.mCurrentAnimationName == null || !mob.mCurrentAnimationName.equals(lNextSpriteName)) {
			mob.currentSprite = mMobSpriteSheet.getSpriteInstance(lNextSpriteName);
			if (mob.currentSprite != null) {
				mob.mCurrentAnimationName = lNextSpriteName;
			}
		}
	}
}
