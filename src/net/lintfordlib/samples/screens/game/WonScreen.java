package net.lintfordlib.samples.screens.game;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.core.maths.MathHelper;
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
	private boolean mWonGame;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public WonScreen(ScreenManager screenManager, SampleSceneHeader sceneHeader, GameOptions gameOptions) {
		super(screenManager, null);

		mSceneHeader = sceneHeader;
		mGameOptions = gameOptions;

		final var lLayout = new ListLayout(this);
		lLayout.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);

		if (sceneHeader.levelNumber + 1 > ConstantsGame.NUM_LEVELS) {
			lLayout.title("You've won the game!");
			mWonGame = true;

			final var lExitToMenuEntry = new MenuEntry(screenManager, this, "Exit");
			lExitToMenuEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

			lLayout.addMenuEntry(lExitToMenuEntry);
		} else {
			lLayout.title("You've won the level, and got enough funds to buy more rum and ale! Uragh!.");
			mWonGame = false;

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
		mESCBackEnabled = false;

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

		final var lTextureBatch = mRendererManager.sharedResources().uiSpriteBatch();
		lTextureBatch.setColorWhite();
		lTextureBatch.begin(core.HUD());

		if (mWonGame) {
			final var lSpriteFramef = mGameSpritesheetDef.getSpriteFrame("WONGAMETEXT");

			final var lScale = 2.f;
			final var lDstWidth = lSpriteFramef.width() * lScale;
			final var lDstHeight = lSpriteFramef.height() * lScale;
			lTextureBatch.draw(mGameSpritesheetDef, lSpriteFramef, -lDstWidth * .5f, core.HUD().boundingRectangle().top(), lDstWidth, lDstHeight, .1f);
		} else {
			final var lSpriteFramef = mGameSpritesheetDef.getSpriteFrame("WONLEVELTEXT");

			final var lScale = 2.f;
			final var lDstWidth = lSpriteFramef.width() * lScale;
			final var lDstHeight = lSpriteFramef.height() * lScale;
			lTextureBatch.draw(mGameSpritesheetDef, lSpriteFramef, -lDstWidth * .5f, core.HUD().boundingRectangle().top(), lDstWidth, lDstHeight, .1f);
		}
		;
		lTextureBatch.end();
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case SCREEN_BUTTON_CONTINUE:
			mSceneHeader.levelNumber = MathHelper.clampi(mSceneHeader.levelNumber++, 0, ConstantsGame.NUM_LEVELS);
			final var lNewGameScreen = new GameScreen(screenManager, mSceneHeader, mGameOptions);
			screenManager.createLoadingScreen(new LoadingScreen(screenManager, true, true, lNewGameScreen));
			return;

		case SCREEN_BUTTON_RESTART:
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
