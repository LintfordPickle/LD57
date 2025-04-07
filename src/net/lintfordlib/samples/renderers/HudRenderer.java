package net.lintfordlib.samples.renderers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.geometry.Rectangle;
import net.lintfordlib.core.graphics.sprites.SpriteFrame;
import net.lintfordlib.core.graphics.sprites.SpriteInstance;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.core.maths.MathHelper;
import net.lintfordlib.core.rendering.RenderPass;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManagerBase;
import net.lintfordlib.samples.controllers.GameStateController;
import net.lintfordlib.samples.controllers.LevelController;
import net.lintfordlib.samples.controllers.MobController;
import net.lintfordlib.samples.controllers.PlayerController;
import net.lintfordlib.samples.data.mobs.MobDefinition;
import net.lintfordlib.samples.data.mobs.definitions.PlayerDigger;
import net.lintfordlib.samples.data.mobs.definitions.PlayerMelee;
import net.lintfordlib.samples.data.mobs.definitions.PlayerRange;

public class HudRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Hud Renderer";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private LevelController mLevelController;
	private PlayerController mPlayerController;
	private GameStateController mGameStateController;
	private MobController mMobController;

	private SpriteSheetDefinition mGameSpriteSheet;
	private SpriteSheetDefinition mHudSpriteSheet;
	private SpriteInstance mCoinSprite;
	private SpriteFrame mHeartSprite;
	private SpriteFrame mTopPanelFrame;
	private SpriteFrame mHomePanelFrame;

	private final Rectangle mKeyXRectangle = new Rectangle();
	private final Rectangle mKeyCRectangle = new Rectangle();
	private final Rectangle mKeyVRectangle = new Rectangle();

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
		mMobController = (MobController) lControllerManager.getControllerByNameRequired(MobController.CONTROLLER_NAME, mEntityGroupUid);
		mPlayerController = (PlayerController) lControllerManager.getControllerByNameRequired(PlayerController.CONTROLLER_NAME, mEntityGroupUid);
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mGameSpriteSheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_GAME", mEntityGroupUid);
		mHudSpriteSheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_HUD", mEntityGroupUid);
		mCoinSprite = mHudSpriteSheet.getSpriteInstance("coin");
		mCoinSprite.animSpeedMod(.4f);
		mHeartSprite = mHudSpriteSheet.getSpriteFrame("HEART");

		mHomePanelFrame = mHudSpriteSheet.getSpriteFrame("HUDPANELHOME");
		mTopPanelFrame = mHudSpriteSheet.getSpriteFrame("HUDPANELTOP");
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mGameSpriteSheet = null;
		mHudSpriteSheet = null;
	}

	@Override
	public boolean handleInput(LintfordCore core) {

		if (mGameStateController.playerInLoadoutArea()) {
			// TODO: Need to display key/button on the hud
			if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_X, this)) {
				final var mobCost = MobDefinition.PLAYER_DIGGER.cost;
				if (mGameStateController.deduct(mobCost)) {

					final var startX = mLevelController.startWorldX();
					final var startY = mLevelController.startWorldY();

					mMobController.addPlayerMob(PlayerDigger.typeId, startX, startY);
				}
			}

			if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_C, this)) {
				final var mobCost = MobDefinition.PLAYER_MELEE.cost;
				if (mGameStateController.deduct(mobCost)) {

					final var startX = mLevelController.startWorldX();
					final var startY = mLevelController.startWorldY();

					mMobController.addPlayerMob(PlayerMelee.typeId, startX, startY);
				}
			}

			if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_V, this)) {
				final var mobCost = MobDefinition.PLAYER_RANGE.cost;
				if (mGameStateController.deduct(mobCost)) {

					final var startX = mLevelController.startWorldX();
					final var startY = mLevelController.startWorldY();

					mMobController.addPlayerMob(PlayerRange.typeId, startX, startY);
				}
			}
		}

		return super.handleInput(core);

		// Handle any hud input actions
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

	}

	@Override
	public void draw(LintfordCore core, RenderPass renderPass) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		if (mGameStateController.playerInLoadoutArea())
			drawHomePanel(core);

		drawHudTopPanel(core);
		drawHudInfo(core);

	}

	private void drawHomePanel(LintfordCore core) {
		final var lGameState = mGameStateController.gameState();
		final var lHudBounds = core.HUD().boundingRectangle();

		final var lScaleX = MathHelper.scaleToRange(1.f, 0, 960, 0, lHudBounds.width());
		final var lScaleY = MathHelper.scaleToRange(1.f, 0, 540, 0, lHudBounds.height());

		final var lSpriteBatch = mRendererManager.sharedResources().uiSpriteBatch();
		final var lFontBatch = mRendererManager.sharedResources().uiHeaderFont();

		final var lBuyPanelWidth = mHomePanelFrame.width() * lScaleX;
		final var lBuyPanelHeight = mHomePanelFrame.height() * lScaleY;
		final var lHudLeft = 0 - lBuyPanelWidth * .5f;
		final var lHudTop = lHudBounds.bottom() - lBuyPanelHeight - 20.f;

		{
			final var lKeyButtonSize = 32.f;
			mKeyXRectangle.set(lHudLeft + 440.f * lScaleX, lHudTop + (125 - 8) * lScaleY, lKeyButtonSize * lScaleX, lKeyButtonSize * lScaleY);
			mKeyCRectangle.set(lHudLeft + 440.f * lScaleX, lHudTop + (160 - 8) * lScaleY, lKeyButtonSize * lScaleX, lKeyButtonSize * lScaleY);
			mKeyVRectangle.set(lHudLeft + 440.f * lScaleX, lHudTop + (195 - 8) * lScaleY, lKeyButtonSize * lScaleX, lKeyButtonSize * lScaleY);
		}

		lSpriteBatch.begin(core.HUD());
		lSpriteBatch.setColorWhite();
		lSpriteBatch.setColorA(.8f);
		lSpriteBatch.draw(mHudSpriteSheet.texture(), mHomePanelFrame, lHudLeft, lHudTop, lBuyPanelWidth, lBuyPanelHeight, .1f);
		final var lHeaderLogo = mHudSpriteSheet.getSpriteFrame("HUDPANELHOMEHEADER");

		lSpriteBatch.draw(mHudSpriteSheet.texture(), lHeaderLogo, lHudLeft, lHudTop, lHeaderLogo.width(), lHeaderLogo.height(), .1f);

		lFontBatch.begin(core.HUD());
		//  @formatter:off
		lFontBatch.setTextColorWhite();
		lFontBatch.drawShadowedText("Your funds:", lHudLeft + 10.f * lScaleX, lHudTop + 50.f * lScaleY, .1f, lScaleX, lScaleY, lScaleX);
		lFontBatch.drawShadowedText(lGameState.credits+"c", lHudLeft + 320.f * lScaleX, lHudTop + 50.f * lScaleY, .1f, lScaleX, lScaleY, lScaleX);
		
		lFontBatch.drawShadowedText("Your goal:", lHudLeft + 10.f * lScaleX, lHudTop + 75.f * lScaleY, .1f, lScaleX, lScaleY, lScaleX);
		lFontBatch.drawShadowedText(mGameStateController.goalCreditsAmt() + "c", lHudLeft + 320.f * lScaleX, lHudTop + 75.f * lScaleY, .1f, lScaleX, lScaleY, lScaleX);
		
		// digger
		lFontBatch.setTextColorWhite();
		lFontBatch.drawShadowedText("Buy Digger:", 							lHudLeft + 10.f * lScaleX, 		lHudTop + 125.f * lScaleY, .1f, lScaleX, lScaleY, lScaleX);
		if(lGameState.credits < MobDefinition.PLAYER_DIGGER.cost)
			lFontBatch.setTextColorRGB(.6f, .15f, .16f);
		else 
			lFontBatch.setTextColorWhite();
		
		lFontBatch.drawShadowedText(MobDefinition.PLAYER_DIGGER.cost + "c", lHudLeft + 320.f * lScaleX, 	lHudTop + 125.f * lScaleY, .1f, lScaleX, lScaleY, lScaleX);
		lSpriteBatch.setColorWhite();
		lSpriteBatch.draw(mHudSpriteSheet, mHudSpriteSheet.getSpriteFrame("KEY_X"), mKeyXRectangle, .1f);
		
		// melee
		lFontBatch.setTextColorWhite();
		lFontBatch.drawShadowedText("Buy Grunt:", 							lHudLeft + 10.f * lScaleX, 		lHudTop + 160.f * lScaleY, .1f, lScaleX, lScaleY, lScaleX);
		if(lGameState.credits < MobDefinition.PLAYER_MELEE.cost)
			lFontBatch.setTextColorRGB(.6f, .15f, .16f);
		else 
			lFontBatch.setTextColorWhite();
		
		lFontBatch.drawShadowedText(MobDefinition.PLAYER_MELEE.cost + "c", 	lHudLeft + 320.f * lScaleX, 	lHudTop + 160.f * lScaleY, .1f, lScaleX, lScaleY, lScaleX);
		lSpriteBatch.draw(mHudSpriteSheet, mHudSpriteSheet.getSpriteFrame("KEY_C"), mKeyCRectangle, .1f);
		
		// range
		lFontBatch.setTextColorWhite();
		lFontBatch.drawShadowedText("Buy Archer:", 					lHudLeft + 10.f * lScaleX, 		lHudTop + 195.f * lScaleY, .1f, lScaleX, lScaleY, lScaleX);
		if(lGameState.credits < MobDefinition.PLAYER_RANGE.cost)
			lFontBatch.setTextColorRGB(.6f, .15f, .16f);
		else 
			lFontBatch.setTextColorWhite();
		
		lFontBatch.drawShadowedText(MobDefinition.PLAYER_RANGE.cost + "c", 	lHudLeft + 320.f * lScaleX, 	lHudTop + 195.f * lScaleY, .1f, lScaleX, lScaleY, lScaleX);
		lSpriteBatch.draw(mHudSpriteSheet, mHudSpriteSheet.getSpriteFrame("KEY_V"), mKeyVRectangle, .1f);
		// @formatter:on

		lSpriteBatch.end();
		lFontBatch.end();
	}

	private void drawHudTopPanel(LintfordCore core) {
		final var lHudBounds = core.HUD().boundingRectangle();

		final var lSpriteBatch = mRendererManager.sharedResources().uiSpriteBatch();

		lSpriteBatch.setColorWhite();
		lSpriteBatch.begin(core.HUD());

		final var sf_w = MathHelper.scaleToRange(256.f, 0, 960, 0, lHudBounds.width());
		final var sf_h = MathHelper.scaleToRange(64.f, 0, 540, 0, lHudBounds.height());

		final var yy = lHudBounds.top();

		for (float xx = lHudBounds.left(); xx <= lHudBounds.right(); xx += sf_w) {
			lSpriteBatch.draw(mHudSpriteSheet, mTopPanelFrame, xx, yy - sf_h * .25f, sf_w, sf_h, .01f);
		}

		lSpriteBatch.end();
	}

	private void drawHudInfo(LintfordCore core) {
		final var lGameState = mGameStateController.gameState();
		final var lHudBounds = core.HUD().boundingRectangle();

		final var lSpriteBatch = mRendererManager.sharedResources().uiSpriteBatch();
		final var lFontBatch = mRendererManager.sharedResources().uiHeaderFont();

		final var lScaleX = MathHelper.scaleToRange(1.f, 0, 960, 0, lHudBounds.width());
		final var lScaleY = MathHelper.scaleToRange(1.f, 0, 540, 0, lHudBounds.height());

		lSpriteBatch.setColorWhite();
		lSpriteBatch.begin(core.HUD());

		final var lCmdrHealthText = "Commander Health: ";
		final var lCmdrHealthTextWidth = lFontBatch.getStringWidth(lCmdrHealthText, lScaleX);

		lFontBatch.setTextColorWhite();
		lFontBatch.begin(core.HUD());
//		lFontBatch.drawShadowedText("Level Name: " + mLevelController.cellLevel().name() + " - '" + mLevelController.cellLevel().filename() + "'", lHudBounds.left() + 5.f, lHudBounds.top() + 5.f, .1f, lScaleX, lScaleY, lScaleX);
		lFontBatch.drawShadowedText(lCmdrHealthText, lHudBounds.left() + 10.f * lScaleX, lHudBounds.top() + 10.f * lScaleY, .1f, lScaleX, lScaleY, lScaleX);
		{
			lSpriteBatch.setColorWhite();
			final var lCmdrHealth = mPlayerController.commanderHealth();
			var hxx = lHudBounds.left() + 10.f + lCmdrHealthTextWidth;
			var hyy = lHudBounds.top() + 2 * lScaleY;
			final var lHeartSize = 32.f;
			for (int i = 0; i < lCmdrHealth; i++) {
				lSpriteBatch.draw(mHudSpriteSheet, mHeartSprite, hxx, hyy, lHeartSize * lScaleX, lHeartSize * lScaleY, .01f);
				hxx += lHeartSize * lScaleX + 2.f;
			}
		}

		lSpriteBatch.draw(mHudSpriteSheet, mCoinSprite, mCoinSprite, 1.f, .01f);
		lSpriteBatch.end();

		final var lCreditsText = lGameState.credits + "/" + mGameStateController.goalCreditsAmt();
		final var lCreditsTextWidth = lFontBatch.getStringWidth(lCreditsText, lScaleX);

		final var xx = lHudBounds.right() - 10.f - lCreditsTextWidth;

		mCoinSprite.update(core);
		final var lCoinSize = 32.f;

		mCoinSprite.set(lHudBounds.right() - 10.f - lCoinSize * lScaleX, lHudBounds.top() + 2 * lScaleY, lCoinSize * lScaleX, lCoinSize * lScaleY);

		lSpriteBatch.draw(mHudSpriteSheet, mCoinSprite, mCoinSprite, 1.f, .01f);
		lSpriteBatch.end();

		lFontBatch.drawShadowedText(lCreditsText, xx - lCoinSize * lScaleX - 5.f, lHudBounds.top() + 10.f * lScaleY, .1f, 1.f * lScaleX, 1.f * lScaleY, lScaleX);
		lFontBatch.end();

	}

	@Override
	public boolean allowKeyboardInput() {
		return true;
	}

}
