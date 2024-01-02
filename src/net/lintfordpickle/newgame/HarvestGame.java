package net.lintfordpickle.newgame;

import net.lintfordlib.GameInfo;
import net.lintfordlib.ResourceLoader;
import net.lintfordlib.controllers.music.MusicController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.fonts.BitmapFontManager;
import net.lintfordlib.core.maths.RandomNumbers;
import net.lintfordlib.renderers.RendererManager;
import net.lintfordlib.screenmanager.IMenuAction;
import net.lintfordlib.screenmanager.Screen;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.screens.TimedIntroScreen;
import net.lintfordlib.screenmanager.toast.ToastManager;
import net.lintfordpickle.newgame.screens.MainMenu;
import net.lintfordpickle.newgame.screens.menu.CreditsScreen;
import net.lintfordpickle.newgame.screens.menu.MainMenuBackground;

public abstract class HarvestGame extends LintfordCore {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	protected int mEntityGroupID;

	protected ResourceLoader mGameResourceLoader;
	protected ScreenManager mScreenManager;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public ScreenManager screenManager() {
		return mScreenManager;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public HarvestGame(GameInfo pGameInfo, String[] pArgs) {
		super(pGameInfo, pArgs, false);

		mEntityGroupID = RandomNumbers.RANDOM.nextInt();
		mIsFixedTimeStep = true;

		mScreenManager = new ScreenManager(this);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	protected void showStartUpLogo(long windowHandle) {

		// You can optionally choose to display a static resource here to be displayed while the program performs the initialization and resource loading in the background.
		// Remember to call glfwSwapBuffers(windowHandle) to commit the changes to the buffer before leaving the method.

		// Also, if you use the bacgrkound ResourceLoader (see GameResourceLoader.java), you can forgo this step, as it has its own (non-static) onDraw

//		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
// 		...		
//		glfwSwapBuffers(windowHandle);

	}

	@Override
	protected void onInitializeBitmapFontSources(BitmapFontManager fontManager) {
		super.onInitializeBitmapFontSources(fontManager);

		ScreenManager.ScreenManagerFonts.AddOrUpdate(ScreenManager.FONT_MENU_TOOLTIP_NAME, "res/fonts/fontNulshock12.json");
		ScreenManager.ScreenManagerFonts.AddOrUpdate(ScreenManager.FONT_MENU_ENTRY_NAME, "res/fonts/fontNulshock16.json");
		ScreenManager.ScreenManagerFonts.AddOrUpdate(ScreenManager.FONT_MENU_BOLD_ENTRY_NAME, "res/fonts/fontNulshock16.json");
		ScreenManager.ScreenManagerFonts.AddOrUpdate(ScreenManager.FONT_MENU_TITLE_NAME, "res/fonts/fontNulshock22.json");

		ScreenManager.ScreenManagerFonts.AddOrUpdate(ToastManager.FONT_TOAST_NAME, "res/fonts/fontNulshock16.json");

		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.HUD_FONT_TEXT_BOLD_SMALL_NAME, "res/fonts/fontBarlow14.json");

		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.UI_FONT_TEXT_NAME, "res/fonts/fontBarlow14.json");
		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.UI_FONT_TEXT_BOLD_NAME, "res/fonts/fontBarlow14.json");
		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.UI_FONT_HEADER_NAME, "res/fonts/fontNulshock16.json");
		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.UI_FONT_TITLE_NAME, "res/fonts/fontNulshock22.json");
	}

	@Override
	protected void onInitializeApp() {
		super.onInitializeApp();

		mScreenManager.initialize();
	}

	@Override
	protected void onLoadResources() {
		super.onLoadResources();

		mGameResourceLoader = new GameResourceLoader(mResourceManager, config().display());

		mGameResourceLoader.loadResources(mResourceManager);
		mGameResourceLoader.setMinimumTimeToShowLogosMs(ConstantsGame.IS_DEBUG_MODE ? 0 : 2000);
		mGameResourceLoader.loadResourcesInBackground(this);

		mResourceManager.audioManager().loadAudioFilesFromMetafile("res/audio/_meta.json");
		mResourceManager.musicManager().loadMusicFromMetaFile("res/music/meta.json");

		var lMusic = new MusicController(mControllerManager, mResourceManager.musicManager(), LintfordCore.CORE_ENTITY_GROUP_ID);
		lMusic.playFromGroup(0, "menu");

		mScreenManager.loadResources(mResourceManager);
	}

	@Override
	protected void finializeAppSetup() {
		final var lSplashScreen = new TimedIntroScreen(mScreenManager, "res/textures/textureSplashScreen.png", 1000, 3000);
		lSplashScreen.stretchBackgroundToFit(true);

		lSplashScreen.setTimerFinishedCallback(new IMenuAction() {
			@Override
			public void TimerFinished(Screen pScreen) {
				mScreenManager.addScreen(new MainMenuBackground(mScreenManager));
				mScreenManager.addScreen(new CreditsScreen(mScreenManager));
				mScreenManager.addScreen(new MainMenu(mScreenManager));
			}
		});

		mScreenManager.addScreen(lSplashScreen);
	}

	@Override
	protected void onUnloadResources() {
		super.onUnloadResources();

		mScreenManager.unloadResources();
	}

	@Override
	protected void onHandleInput() {
		super.onHandleInput();

		gameCamera().handleInput(this);
		mScreenManager.handleInput(this);
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();

		mScreenManager.update(this);
	}

	@Override
	protected void onDraw() {
		super.onDraw();

		mScreenManager.draw(this);
	}

}
