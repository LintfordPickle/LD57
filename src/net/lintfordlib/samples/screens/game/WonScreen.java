package net.lintfordlib.samples.screens.game;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.data.GameOptions;
import net.lintfordlib.samples.data.SampleSceneHeader;
import net.lintfordlib.samples.screens.MainMenu;
import net.lintfordlib.samples.screens.menu.MainMenuBackground;
import net.lintfordlib.screenmanager.MenuEntry;
import net.lintfordlib.screenmanager.MenuScreen;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.ScreenManagerConstants.FILLTYPE;
import net.lintfordlib.screenmanager.layouts.ListLayout;
import net.lintfordlib.screenmanager.screens.LoadingScreen;

public class WonScreen extends MenuScreen {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final int SCREEN_BUTTON_CONTINUE = 10;
	private static final int SCREEN_BUTTON_RESTART = 12;
	private static final int SCREEN_BUTTON_EXIT = 13;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SampleSceneHeader mSceneHeader;
	private GameOptions mGameOptions;
	private SpriteSheetDefinition mGameSpritesheetDef;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public WonScreen(ScreenManager screenManager, SampleSceneHeader sceneHeader, GameOptions gameOptions) {
		super(screenManager, null);

		mSceneHeader = sceneHeader;
		mGameOptions = gameOptions;

		final var lLayout = new ListLayout(this);
		lLayout.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);
		lLayout.setDrawBackground(true, ColorConstants.WHITE());
		lLayout.showTitle(true);

		sceneHeader.levelNumber++;
		if (sceneHeader.levelNumber > ConstantsGame.NUM_LEVELS) {
			lLayout.title("You've won the game!");

			final var lExitToMenuEntry = new MenuEntry(screenManager, this, "Exit");
			lExitToMenuEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

			lLayout.addMenuEntry(lExitToMenuEntry);
		} else {
			lLayout.title("You've won the level");

			final var lContinueEntry = new MenuEntry(screenManager, this, "Next");
			lContinueEntry.registerClickListener(this, SCREEN_BUTTON_CONTINUE);

			final var lRestartEntry = new MenuEntry(screenManager, this, "Restart");
			lRestartEntry.registerClickListener(this, SCREEN_BUTTON_RESTART);

			lLayout.addMenuEntry(lContinueEntry);
			lLayout.addMenuEntry(lRestartEntry);
		}

		final var lExitToMenuEntry = new MenuEntry(screenManager, this, "Exit");
		lExitToMenuEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

		lLayout.addMenuEntry(MenuEntry.menuSeparator());
		lLayout.addMenuEntry(lExitToMenuEntry);

		mLayouts.add(lLayout);

		mIsPopup = false;
		mShowBackgroundScreens = true;

		mBlockGamepadInputInBackground = true;
		mBlockKeyboardInputInBackground = true;
		mBlockMouseInputInBackground = true;

		mShowContextualKeyHints = false;

		mScreenPaddingTop = 120;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mGameSpritesheetDef = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_GAME", ConstantsGame.GAME_RESOURCE_GROUP_ID);
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mGameSpritesheetDef = null;
	}

	@Override
	public void draw(LintfordCore core) {
		super.draw(core);

		if (mGameSpritesheetDef == null)
			return;

		final var lSpriteFrame = mGameSpritesheetDef.getSpriteFrame("WONTEXT");

		if (lSpriteFrame != null) {
			final var lTextureBatch = mRendererManager.sharedResources().uiSpriteBatch();
			lTextureBatch.setColorWhite();

			lTextureBatch.begin(core.gameCamera());
			lTextureBatch.draw(mGameSpritesheetDef, lSpriteFrame, -lSpriteFrame.width() * .5f, core.gameCamera().boundingRectangle().top() + 32, lSpriteFrame.width(), lSpriteFrame.height(), .1f);
			lTextureBatch.end();
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case SCREEN_BUTTON_CONTINUE:
			final var lNewGameScreen = new GameScreen(screenManager, mSceneHeader, mGameOptions);
			screenManager.createLoadingScreen(new LoadingScreen(screenManager, true, true, lNewGameScreen));
			return;

		case SCREEN_BUTTON_RESTART:
			// TODO: the restart won't work like this because we already incremented the level index
			final var lLoadingScreen = new GameScreen(screenManager, mSceneHeader, mGameOptions);
			screenManager.createLoadingScreen(new LoadingScreen(screenManager, true, true, lLoadingScreen));
			break;

		case SCREEN_BUTTON_EXIT:
			final var lMenuBackgroundScreen = new MainMenuBackground(screenManager);
			final var lMainMenuScreen = new MainMenu(screenManager);
			screenManager.createLoadingScreen(new LoadingScreen(screenManager, false, false, lMenuBackgroundScreen, lMainMenuScreen));
			break;

		}
	}
}
