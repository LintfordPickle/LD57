package net.lintfordpickle.newgame;

import net.lintfordlib.ResourceLoader;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.options.DisplayManager;

public class GameResourceLoader extends ResourceLoader {

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public GameResourceLoader(ResourceManager resourceManager, DisplayManager displayManager) {
		super(resourceManager, displayManager, true);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

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
	}
}
