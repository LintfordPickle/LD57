package net.lintfordpickle.newgame.screens.menu;

import org.lwjgl.opengl.GL11;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.textures.Texture;
import net.lintfordlib.screenmanager.Screen;
import net.lintfordlib.screenmanager.ScreenManager;

public class MainMenuBackground extends Screen {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private Texture mBackgroundTexture;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public MainMenuBackground(ScreenManager screenManager) {
		super(screenManager);

		mScreenManager.core().createNewGameCamera();
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mBackgroundTexture = resourceManager.textureManager().loadTexture("TEXTURE_MENU_BACKGROUND", "res/textures/textureMainMenuScreen.png", entityGroupUid());

		mCoreSpritesheet = resourceManager.spriteSheetManager().coreSpritesheet();
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mBackgroundTexture = null;
		mCoreSpritesheet = null;
	}

	@Override
	public void draw(LintfordCore core) {
		super.draw(core);

		final var lCanvasBox = core.gameCamera().boundingRectangle();
		final var lTextureBatch = rendererManager().uiSpriteBatch();

		GL11.glEnable(GL11.GL_DEPTH_TEST);

		core.gameCamera().update(core);
		core.config().display().reapplyGlViewport();

		lTextureBatch.begin(core.gameCamera());
		lTextureBatch.draw(mBackgroundTexture, 0, 0, 960, 540, lCanvasBox.left(), lCanvasBox.top(), lCanvasBox.width(), lCanvasBox.height(), -0.85f, ColorConstants.WHITE);
		lTextureBatch.end();

	}
}
