package net.lintfordlib.samples.renderers;

import org.lwjgl.opengl.GL11;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.sprites.SpriteFrame;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.core.graphics.textures.Texture;
import net.lintfordlib.core.rendering.RenderPass;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManagerBase;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.controllers.LevelController;
import net.lintfordlib.samples.data.level.CellLevel;
import net.lintfordlib.samples.data.textures.GameTextureNames;

public class LevelRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Level Renderer";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private LevelController mLevelController;
	private Texture mLevelTexture;
	private SpriteSheetDefinition mGameSpriteSheet;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public LevelRenderer(RendererManagerBase rendererManager, int entityGroupUid) {
		super(rendererManager, RENDERER_NAME, entityGroupUid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mLevelController != null;
	}

	@Override
	public void initialize(LintfordCore pCore) {
		mLevelController = (LevelController) pCore.controllerManager().getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupID());

	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mLevelTexture = resourceManager.textureManager().loadTexture("TEXTURE_LEVEL", "res/textures/textureLevel.png", GL11.GL_LINEAR, entityGroupID());
		mGameSpriteSheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_GAME", entityGroupID());
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mLevelTexture = null;
		mGameSpriteSheet = null;
	}

	@Override
	public void draw(LintfordCore core, RenderPass renderPass) {

		drawBackground(core);
		drawForeground(core);

		drawItems(core);
		// drawDebugDepth(core);
	}

	// --------------------------------------

	private void drawBackground(LintfordCore pCore) {
		final var lLevel = mLevelController.cellLevel();
		final var lDepthValues = lLevel.tileDepth();
		final var lTileVariationOffsets = lLevel.tileVariations();

		final var lTextureBatch = rendererManager().sharedResources().uiSpriteBatch();

		lTextureBatch.begin(pCore.gameCamera());

		final float lBlockSize = ConstantsGame.BLOCK_SIZE;

		var lSpriteSheetTexture = mGameSpriteSheet.texture();
		var lSpriteFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.FLOOR00);

		for (int y = 0; y < ConstantsGame.LEVEL_TILES_HIGH; y++) {
			for (int x = 0; x < ConstantsGame.LEVEL_TILES_WIDE; x++) {

				final var lTileCoord = lLevel.getLevelTileCoord(x, y);
				final var lTileDepth = lDepthValues[lTileCoord];
				final var lTileVariation = lTileVariationOffsets[lTileCoord];

				final var lDepthTolerance = 1.25f;
				final var lInvDepth = 1.f - (lTileDepth / (float) ConstantsGame.LEVEL_TILES_WIDE / lDepthTolerance);
				final var lDepthColorMod = ColorConstants.getColor(lInvDepth, lInvDepth, lInvDepth, 1.f);
				lTextureBatch.setColor(lDepthColorMod);

				switch (lTileVariation) {
				default:
				case 0:
					lSpriteFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.FLOOR00);
					break;

				case 1:
					lSpriteFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.FLOOR01);
					break;

				case 2:
					lSpriteFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.FLOOR02);
					break;

				case 3:
					lSpriteFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.FLOOR03);
					break;
				}

				lTextureBatch.draw(lSpriteSheetTexture, lSpriteFrame, (int) (x * lBlockSize), (int) (y * lBlockSize), 16, 16, .01f);
			}
		}

		lTextureBatch.end();
	}

	private void drawForeground(LintfordCore pCore) {
		final var lLevel = mLevelController.cellLevel();
		final var lDepthValues = lLevel.tileDepth();

		
		final var lTextureBatch = mRendererManager.sharedResources().uiSpriteBatch();
		lTextureBatch.useHalfPixelCorrection(true);
		lTextureBatch.begin(pCore.gameCamera());

		final float lBlockSize = ConstantsGame.BLOCK_SIZE;

		var lSpriteFrame = (SpriteFrame) null;
		var lSpriteSheetTexture = mGameSpriteSheet.texture();

		for (int y = 0; y < ConstantsGame.LEVEL_TILES_HIGH; y++) {
			for (int x = 0; x < ConstantsGame.LEVEL_TILES_WIDE; x++) {

				final var lTileCoord = lLevel.getLevelTileCoord(x, y);
				final var lTileDepth = lDepthValues[lTileCoord];

				final var lDepthTolerance = 1.5f;
				final var lInvDepth = 1.f - (lTileDepth / (float) ConstantsGame.LEVEL_TILES_WIDE / lDepthTolerance);
				final var lDepthColorMod = ColorConstants.getColor(lInvDepth, lInvDepth, lInvDepth, 1.f);
				lTextureBatch.setColor(lDepthColorMod);

				final int lBlockTypeIndex = lLevel.getLevelBlockType(x, y);
				if (lBlockTypeIndex == CellLevel.LEVEL_TILE_INDEX_NOTHING)
					continue;

				switch (lBlockTypeIndex) {
				case CellLevel.LEVEL_TILE_INDEX_DIRT:
					lSpriteFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.DIRT);
					break;

				case CellLevel.LEVEL_TILE_INDEX_GOLD:
					lSpriteFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.GOLD);
					break;

				case CellLevel.LEVEL_TILE_INDEX_GEMS:
					lSpriteFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.GEMS);
					break;
				}

				lTextureBatch.draw(lSpriteSheetTexture, lSpriteFrame, (int) (x * lBlockSize), (int) (y * lBlockSize), 16, 16, .01f);
			}
		}

		lTextureBatch.end();
	}

	private void drawItems(LintfordCore pCore) {
		final var lLevel = mLevelController.cellLevel();
		final var lDepthValues = lLevel.tileDepth();

		final var lTextureBatch = mRendererManager.sharedResources().uiSpriteBatch();
		lTextureBatch.begin(pCore.gameCamera());

		final float lBlockSize = ConstantsGame.BLOCK_SIZE;

		var lSpriteFrame = (SpriteFrame) null;
		var lSpriteSheetTexture = mGameSpriteSheet.texture();

		for (int y = 0; y < ConstantsGame.LEVEL_TILES_HIGH; y++) {
			for (int x = 0; x < ConstantsGame.LEVEL_TILES_WIDE; x++) {
				final int lItemIndex = lLevel.getItemTypeUid(x, y);
				if (lItemIndex == CellLevel.LEVEL_ITEMS_NOTHING)
					continue;

				final var lTileCoord = lLevel.getLevelTileCoord(x, y);
				final var lTileDepth = lDepthValues[lTileCoord];

				final var lDepthTolerance = 2.f; // so the darkest is only max half way to black
				final var lInvDepth = 1.f - (lTileDepth / (float) ConstantsGame.LEVEL_TILES_WIDE / lDepthTolerance);
				final var lDepthColorMod = ColorConstants.getColor(lInvDepth, lInvDepth, lInvDepth, 1.f);
				lTextureBatch.setColor(lDepthColorMod);

				switch (lItemIndex) {
				case CellLevel.LEVEL_ITEMS_SPAWNER:
					lSpriteFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.SPAWNER_FLOOR_1);
					break;

				case CellLevel.LEVEL_ITEMS_TREASURE:
					lSpriteFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.CHEST);
					break;

				case CellLevel.LEVEL_ITEMS_ENTRANCE:
					lSpriteFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.ENTRANCE);
					break;

				}

				lTextureBatch.draw(lSpriteSheetTexture, lSpriteFrame, (int) (x * lBlockSize), (int) (y * lBlockSize), 16, 16, .01f);
			}
		}

		lTextureBatch.end();
	}

	private void drawDebugDepth(LintfordCore core) {
		final var lLevel = mLevelController.cellLevel();
		final var lDepths = lLevel.tileDepth();

		final var lFontUnit = mRendererManager.sharedResources().uiTextFont();
		lFontUnit.begin(core.gameCamera());
		lFontUnit.setTextColorWhite();

		for (int y = 0; y < ConstantsGame.LEVEL_TILES_HIGH; y++) {
			for (int x = 0; x < ConstantsGame.LEVEL_TILES_WIDE; x++) {
				final int lTileIndex = lLevel.getLevelTileCoord(x, y);

				final var xx = x * ConstantsGame.BLOCK_SIZE;
				final var yy = y * ConstantsGame.BLOCK_SIZE;

				lFontUnit.drawText("" + lDepths[lTileIndex], xx, yy, .01f, .2f);
			}
		}

		lFontUnit.end();
	}
}
