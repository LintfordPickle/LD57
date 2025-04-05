package net.lintfordlib.samples.renderers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.core.rendering.RenderPass;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManagerBase;
import net.lintfordlib.samples.controllers.GameStateController;
import net.lintfordlib.samples.controllers.LevelController;
import net.lintfordlib.samples.controllers.MobController;
import net.lintfordlib.samples.data.mobs.MobTypeIndex;
import net.lintfordlib.samples.data.textures.GameTextureNames;

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
	private MobController mMobController;

	private SpriteSheetDefinition mGameSpriteSheet;

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
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mGameSpriteSheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_GAME", mEntityGroupUid);
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mGameSpriteSheet = null;
	}

	@Override
	public boolean handleInput(LintfordCore core) {

		if (mGameStateController.playerInLoadoutArea()) {
			// TODO: Need to display key/button on the hud
			if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_X, this)) {
				final var lCreditAvail = 100;

				// TODO: gotta use them coords
				mMobController.addPlayerMob(MobTypeIndex.MOB_TYPE_PLAYER_DIGGER, 32, 32);
			}
			
			if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_C, this)) {
				final var lCreditAvail = 100;

				// TODO: gotta use them coords
				mMobController.addPlayerMob(MobTypeIndex.MOB_TYPE_PLAYER_MELEE, 32, 32);
			}
			
			if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_V, this)) {
				final var lCreditAvail = 100;

				// TODO: gotta use them coords
				mMobController.addPlayerMob(MobTypeIndex.MOB_TYPE_PLAYER_RANGE, 32, 32);
			}
		}

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

		final var lGameState = mGameStateController.gameState();
		final var lHudBounds = core.HUD().boundingRectangle();

		final var lFontBatch = mRendererManager.sharedResources().uiTitleFont();

		if (mGameStateController.playerInLoadoutArea()) {
			final var lSpriteBatch = mRendererManager.sharedResources().uiSpriteBatch();

			final var lPanelFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.WHITE);

			final var lBuyPanelWidth = 650;
			final var lBuyPanelHeight = 220;
			final var lHudLeft = 0 - lBuyPanelWidth * .5f;
			final var lHudTop = lHudBounds.bottom() - lBuyPanelHeight - 20.f;

			lSpriteBatch.setColorRGBA(0.4f, .3f, .34f, 1.f);
			lSpriteBatch.begin(core.HUD());
			lSpriteBatch.draw(mGameSpriteSheet.texture(), lPanelFrame, lHudLeft, lHudTop, lBuyPanelWidth, lBuyPanelHeight, .1f);
			lSpriteBatch.end();

			lFontBatch.begin(core.HUD());
			lFontBatch.drawShadowedText("Your funds: " + lGameState.credits, lHudLeft + 10.f, lHudTop + 50.f, .1f, 1.f, 1.f, 1.f);
			lFontBatch.drawShadowedText("Your goal: " + lGameState.credits, lHudLeft + 10.f, lHudTop + 75.f, .1f, 1.f, 1.f, 1.f);

			lFontBatch.drawShadowedText("Buy Digger: " + lGameState.credits, lHudLeft + 10.f, lHudTop + 125.f, .1f, 1.f, 1.f, 1.f);
			lFontBatch.drawShadowedText("Buy Grunt: " + lGameState.credits, lHudLeft + 10.f, lHudTop + 150.f, .1f, 1.f, 1.f, 1.f);
			lFontBatch.drawShadowedText("Buy Axe Thrower: " + lGameState.credits, lHudLeft + 10.f, lHudTop + 175.f, .1f, 1.f, 1.f, 1.f);
			lFontBatch.end();
		}

		lFontBatch.begin(core.HUD());
		lFontBatch.drawShadowedText("Level Name: " + mLevelController.cellLevel().name() + " - '" + mLevelController.cellLevel().filename() + "'", lHudBounds.left() + 10.f, lHudBounds.top() + 10.f, .1f, 1.f, 1.f, 1.f);
		lFontBatch.drawShadowedText("Commander Health", lHudBounds.left() + 10.f, lHudBounds.top() + 30.f, .1f, 1.f, 1.f, 1.f);

		lFontBatch.drawShadowedText("Credits: " + lGameState.credits, lHudBounds.left() + 10.f, lHudBounds.top() + 50.f, .1f, 1.f, 1.f, 1.f);
		lFontBatch.end();
	}

	@Override
	public boolean allowKeyboardInput() {
		return true;
	}

}
