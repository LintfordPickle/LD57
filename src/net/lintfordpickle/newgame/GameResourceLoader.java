package net.lintfordpickle.newgame;

import net.lintfordlib.ResourceLoader;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.batching.TextureBatchPCT;
import net.lintfordlib.core.graphics.textures.Texture;
import net.lintfordlib.options.DisplayManager;

public class GameResourceLoader extends ResourceLoader {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private TextureBatchPCT mTextureBatch;

	private Texture mLoadingBackgroundTexture;
	private Texture mLoadingTexture;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public GameResourceLoader(ResourceManager resourceManager, DisplayManager displayManager) {
		super(resourceManager, displayManager, true);

		mTextureBatch = new TextureBatchPCT();

	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mTextureBatch.loadResources(resourceManager);
		mLoadingTexture = resourceManager.textureManager().loadTexture("TEXTURE_LOADING_ARROW", "res/textures/textureLoadingArrow.png", ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mLoadingBackgroundTexture = mResourceManager.textureManager().loadTexture("TEXTURE_STARTUP", "res/textures/textureLoadingScreen.png", ConstantsGame.GAME_RESOURCE_GROUP_ID);
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mTextureBatch.unloadResources();
		mLoadingTexture = null;
	}

	private float mRunningTime;

	@Override
	protected void onDraw(LintfordCore core) {
		super.onDraw(core);

		final var lAnimSpeed = 3.f;
		mRunningTime += (float) frameDelta * .001f * lAnimSpeed;
		final var lHudBounds = core.HUD().boundingRectangle();

		mTextureBatch.begin(core.HUD());
		mTextureBatch.draw(mLoadingBackgroundTexture, 0.f, 0.f, 960.f, 540.f, core.HUD().boundingRectangle(), -0.01f, ColorConstants.WHITE);

		final var lDstX = lHudBounds.right() - 6.f - 32.f;
		final var lDstY = lHudBounds.bottom() - 6.f - 32.f;

		mTextureBatch.drawAroundCenter(mLoadingTexture, 0.f, 0.f, 64.f, 64.f, lDstX, lDstY, 64.f, 64f, -0.01f, mRunningTime, 0.f, 0.f, 1.f, ColorConstants.WHITE);
		mTextureBatch.end();

		// You can optionally display a graphic here which is rendered for as long as the resources are being loaded.

	}

	@Override
	protected void resourcesToLoadInBackground() {
		Debug.debugManager().logger().i(getClass().getSimpleName(), "Loading game assets into group: " + ConstantsGame.GAME_RESOURCE_GROUP_ID);

		mResourceManager.addProtectedEntityGroupUid(ConstantsGame.GAME_RESOURCE_GROUP_ID);

		currentStatusMessage("Loading resources");

		mResourceManager.textureManager().loadTexturesFromMetafile("res/textures/_meta.json", ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mResourceManager.spriteSheetManager().loadSpriteSheetFromMeta("res/spritesheets/_meta.json", ConstantsGame.GAME_RESOURCE_GROUP_ID);

		// If you need to override some of the default textures loaded in the LintfordLib.CoreSpritesheetDefinition, it can be done here with the following lines:
		// mResourceManager.textureManager().loadTexture("TEXTURE_CORE", "res/textures/textureCore.png", GL11.GL_NEAREST, true, LintfordCore.CORE_ENTITY_GROUP_ID);
		// mResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetCore.json", LintfordCore.CORE_ENTITY_GROUP_ID);

		mResourceManager.fontManager().loadBitmapFont("FONT_NULSHOCK_12", "res/fonts/fontNulshock12.json");
		mResourceManager.fontManager().loadBitmapFont("FONT_NULSHOCK_16", "res/fonts/fontNulshock16.json");
		mResourceManager.fontManager().loadBitmapFont("FONT_NULSHOCK_22", "res/fonts/fontNulshock22.json");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
