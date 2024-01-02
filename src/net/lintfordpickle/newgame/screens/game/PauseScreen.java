package net.lintfordpickle.newgame.screens.game;

import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.data.scene.SceneHeader;
import net.lintfordlib.screenmanager.MenuEntry;
import net.lintfordlib.screenmanager.MenuScreen;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.ScreenManagerConstants.FILLTYPE;
import net.lintfordlib.screenmanager.layouts.ListLayout;
import net.lintfordlib.screenmanager.screens.LoadingScreen;
import net.lintfordpickle.newgame.screens.MainMenu;
import net.lintfordpickle.newgame.screens.menu.CreditsScreen;

public class PauseScreen extends MenuScreen {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final int SCREEN_BUTTON_CONTINUE = 10;
	private static final int SCREEN_BUTTON_RESTART = 11;
	private static final int SCREEN_BUTTON_EXIT = 12;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SceneHeader mSceneHeader;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public PauseScreen(ScreenManager screenManager, SceneHeader sceneHeader) {
		super(screenManager, null);

		mSceneHeader = sceneHeader;

		final var lLayout = new ListLayout(this);
		lLayout.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);
		lLayout.setDrawBackground(true, ColorConstants.WHITE);
		lLayout.showTitle(true);
		lLayout.title("Paused");

		final var lContinueEntry = new MenuEntry(mScreenManager, this, "Continue");
		lContinueEntry.registerClickListener(this, SCREEN_BUTTON_CONTINUE);

		final var lRestartEntry = new MenuEntry(mScreenManager, this, "Restart");
		lRestartEntry.registerClickListener(this, SCREEN_BUTTON_RESTART);

		final var lExitToMenuEntry = new MenuEntry(mScreenManager, this, "Exit");
		lExitToMenuEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

		lLayout.addMenuEntry(lContinueEntry);
		lLayout.addMenuEntry(lRestartEntry);
		lLayout.addMenuEntry(lExitToMenuEntry);

		mLayouts.add(lLayout);

		mIsPopup = true;
		mShowBackgroundScreens = true;

		mBlockGamepadInputInBackground = true;
		mBlockKeyboardInputInBackground = true;
		mBlockMouseInputInBackground = true;

		mShowContextualKeyHints = false;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case SCREEN_BUTTON_CONTINUE:
			exitScreen();
			return;

		case SCREEN_BUTTON_RESTART:
			final var lLoadingScreen = new LoadingScreen(screenManager(), true, new GameScreen(screenManager(), mSceneHeader));
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
			break;

		case SCREEN_BUTTON_EXIT:
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), false, new CreditsScreen(mScreenManager), new MainMenu(screenManager())));
			break;

		}
	}
}
