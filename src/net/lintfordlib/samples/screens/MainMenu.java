package net.lintfordlib.samples.screens;

import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.samples.data.GameOptions;
import net.lintfordlib.samples.data.SampleSceneHeader;
import net.lintfordlib.samples.screens.game.GameScreen;
import net.lintfordlib.samples.screens.menu.OptionsScreen;
import net.lintfordlib.screenmanager.MenuEntry;
import net.lintfordlib.screenmanager.MenuScreen;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.ScreenManagerConstants.FILLTYPE;
import net.lintfordlib.screenmanager.ScreenManagerConstants.LAYOUT_ALIGNMENT;
import net.lintfordlib.screenmanager.ScreenManagerConstants.LAYOUT_WIDTH;
import net.lintfordlib.screenmanager.layouts.ListLayout;
import net.lintfordlib.screenmanager.screens.LoadingScreen;

public class MainMenu extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final String TITLE = null;
	private static final boolean OPTIONS_ENABLED = false;

	private static final int SCREEN_BUTTON_PLAY = 11;
	private static final int SCREEN_BUTTON_OPTIONS = 14;
	private static final int SCREEN_BUTTON_HELP = 15;
	private static final int SCREEN_BUTTON_EXIT = 16;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private ListLayout mMainMenuListBox;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public MainMenu(ScreenManager pScreenManager) {
		super(pScreenManager, TITLE);

		mLayoutAlignment = LAYOUT_ALIGNMENT.LEFT;

		mMainMenuListBox = new ListLayout(this);
		mMainMenuListBox.setDrawBackground(true, ColorConstants.getColor(.7f, .3f, .7f, .5f));
		mMainMenuListBox.layoutWidth(LAYOUT_WIDTH.HALF);
		mMainMenuListBox.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);

		final var lStartGameEntry = new MenuEntry(screenManager, this, "Start Game");
		lStartGameEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lStartGameEntry.registerClickListener(this, SCREEN_BUTTON_PLAY);

		final var lOptionsEntry = new MenuEntry(screenManager, this, "Options");
		lOptionsEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lOptionsEntry.registerClickListener(this, SCREEN_BUTTON_OPTIONS);

		final var lExitEntry = new MenuEntry(screenManager, this, "Exit");
		lExitEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lExitEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

		mMainMenuListBox.addMenuEntry(lStartGameEntry);
		if (OPTIONS_ENABLED) {
			mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
			mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
			mMainMenuListBox.addMenuEntry(lOptionsEntry);
		}

		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(lExitEntry);

		mLayouts.add(mMainMenuListBox);

		mSelectedLayoutIndex = mLayouts.size() - 1;
		mSelectedEntryIndex = 0;

		mScreenPaddingTop = 40.f;
		mLayoutPaddingHorizontal = 50.f;

		mIsPopup = false;
		mShowBackgroundScreens = true;
		mESCBackEnabled = false;

		screenManager.contextHintManager().drawContextBackground(true);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize() {
		super.initialize();

	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	protected void handleOnClick() {

		switch (mClickAction.consume()) {
		case SCREEN_BUTTON_PLAY: {
			// screenManager.addScreen(new SelectScreen(screenManager));
			startLevel1();
			break;
		}

		case SCREEN_BUTTON_OPTIONS: {
			screenManager.addScreen(new OptionsScreen(screenManager));
			break;
		}

		case SCREEN_BUTTON_EXIT:
			screenManager.exitGame();
			break;
		}
	}

	private void startLevel1() {
		final var gameOptions = new GameOptions();

		final var lNewSceneHeader = new SampleSceneHeader("Sample Game");
		lNewSceneHeader.levelNumber = 1;

		final var lNewGameScreen = new GameScreen(screenManager, lNewSceneHeader, gameOptions);
		screenManager.createLoadingScreen(new LoadingScreen(screenManager, true, true, lNewGameScreen));
	}
}
