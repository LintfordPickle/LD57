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
		final float lLevelWidth = ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.BLOCK_SIZE;

//		final var lTextureBatch = rendererManager().sharedResources().uiSpriteBatch();
//		lTextureBatch.begin(core.gameCamera());
//		lTextureBatch.setColorWhite();
//
//		final var lHorizontalScale = (lLevelWidth / 256.f) * 256.f;
//		lTextureBatch.draw(mBackgroundTexture, 0, 0, lHorizontalScale, 256, 0, -128, lLevelWidth, 256, -0.9f);
//		lTextureBatch.end();

//		drawBackground(core);
		drawForeground(core);
	}

	private void drawBackground(LintfordCore pCore) {
		final var lLevel = mLevelController.cellLevel();

		if (lLevel == null)
			return;

		final var lTextureBatch = rendererManager().sharedResources().uiSpriteBatch();

		lTextureBatch.begin(pCore.gameCamera());

		final float lBlockSize = ConstantsGame.BLOCK_SIZE;
		final int floorHeight = 3;

		for (int y = floorHeight; y < ConstantsGame.LEVEL_TILES_HIGH; y++) {

			final float lModAmt = 1.f - (float) ((float) (y + 5.f) / (float) ConstantsGame.LEVEL_TILES_HIGH);
			lTextureBatch.setColor(ColorConstants.getColor(lModAmt, lModAmt, lModAmt, 1.f));

			for (int x = 0; x < ConstantsGame.LEVEL_TILES_WIDE; x++) {
//				if (y == floorHeight) {
//					lTextureBatch.draw(mLevelTexture, BACKGROUND_TOP_FILL_SRC_RECT, x * lBlockSize, y * lBlockSize, lBlockSize, lBlockSize, .01f);
//				} else {
//					lTextureBatch.draw(mLevelTexture, BACKGROUND_FILL_SRC_RECT, x * lBlockSize, y * lBlockSize, lBlockSize, lBlockSize, .01f);
//				}
			}
		}

		lTextureBatch.end();
	}

	private void drawForeground(LintfordCore pCore) {
		final var lLevel = mLevelController.cellLevel();

		if (lLevel == null)
			return;

		final var lTextureBatch = mRendererManager.sharedResources().uiSpriteBatch();
		lTextureBatch.begin(pCore.gameCamera());

		final float lBlockSize = ConstantsGame.BLOCK_SIZE;

		var lSpriteFrame = (SpriteFrame) null;
		var lSpriteSheetTexture = mGameSpriteSheet.texture();
		
		for (int y = 0; y < ConstantsGame.LEVEL_TILES_HIGH; y++) {
			final float lModAmt = 1.f - (float) ((float) y / (float) ConstantsGame.LEVEL_TILES_HIGH * 0.5f);
			final var lDepthColorMod = ColorConstants.getColor(lModAmt, lModAmt, lModAmt, 1.f);
			lTextureBatch.setColor(lDepthColorMod);

			for (int x = 0; x < ConstantsGame.LEVEL_TILES_WIDE; x++) {
				final int lBlockTypeIndex = lLevel.getLevelBlockType(x, y);
				if (lBlockTypeIndex == CellLevel.LEVEL_TILE_INDEX_NOTHING)
					continue;

				final int lTileIndex = lLevel.getLevelTileCoord(x, y);
				
				switch (lBlockTypeIndex) {
				case CellLevel.LEVEL_TILE_INDEX_DIRT:
					lSpriteFrame = mGameSpriteSheet.getSpriteFrame(GameTextureNames.DIRT);
					break;
				}

				// TODO: resovle the source positions from the tilemap/spritesheet/texture

				final var sx = 0;
				final var sy = 0;
				final var sw = 0;
				final var sh = 0;

				lTextureBatch.draw(lSpriteSheetTexture, lSpriteFrame, (int) (x * lBlockSize), (int) (y * lBlockSize), 16, 16, .01f);
			}
		}

		lTextureBatch.end();
	}
}
