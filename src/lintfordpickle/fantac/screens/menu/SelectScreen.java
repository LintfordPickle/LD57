package lintfordpickle.fantac.screens.menu;

import lintfordpickle.fantac.data.GameOptions;
import lintfordpickle.fantac.screens.game.GameScreen;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.data.scene.SceneHeader;
import net.lintfordlib.screenmanager.MenuEntry;
import net.lintfordlib.screenmanager.MenuScreen;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.ScreenManagerConstants.FILLTYPE;
import net.lintfordlib.screenmanager.ScreenManagerConstants.LAYOUT_ALIGNMENT;
import net.lintfordlib.screenmanager.ScreenManagerConstants.LAYOUT_WIDTH;
import net.lintfordlib.screenmanager.entries.MenuEnumEntry;
import net.lintfordlib.screenmanager.entries.MenuSliderEntry;
import net.lintfordlib.screenmanager.layouts.ListLayout;
import net.lintfordlib.screenmanager.screens.LoadingScreen;

public class SelectScreen extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final int BUTTON_PLAY = 11;
	private static final int BUTTON_BACK = 12;

	private final MenuSliderEntry mNumPlayersSlider;
	private final MenuEnumEntry mPlayerResources;
	private final MenuEnumEntry mDifficulty;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public SelectScreen(ScreenManager pScreenManager) {
		super(pScreenManager, null);

		final var lLayout = new ListLayout(this);
		lLayout.setDrawBackground(true, ColorConstants.WHITE);
		lLayout.layoutWidth(LAYOUT_WIDTH.HALF);
		lLayout.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);

		lLayout.showTitle(true);
		lLayout.title("Select Parameters");
		lLayout.cropPaddingTop(10.f);
		lLayout.cropPaddingBottom(10.f);

		mNumPlayersSlider = new MenuSliderEntry(pScreenManager, this);
		mNumPlayersSlider.label("Players");

		mNumPlayersSlider.setBounds(2, 4, 1);
		mNumPlayersSlider.setValue(2);
		mNumPlayersSlider.showValue(true);
		mNumPlayersSlider.showValueUnit(false);
		mNumPlayersSlider.showValueGuides(false);
		mNumPlayersSlider.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mPlayerResources = new MenuEnumEntry(pScreenManager, this, "Resources");
		mPlayerResources.addItems("Low", "Medium", "High");
		mPlayerResources.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		mPlayerResources.setButtonsEnabled(true);
		mPlayerResources.showInfoButton(true);
		mPlayerResources.setToolTip("Sets the amount of workers the player begins with");

		mDifficulty = new MenuEnumEntry(pScreenManager, this, "Difficulty");
		mDifficulty.addItems("Easy", "Hard");
		mDifficulty.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		mDifficulty.setButtonsEnabled(true);
		mDifficulty.showInfoButton(true);
		mDifficulty.setToolTip("Controls the likelyhood of empty settlements spawning near you");

		final var lStartGameEntry = new MenuEntry(mScreenManager, this, "Play");
		lStartGameEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lStartGameEntry.registerClickListener(this, BUTTON_PLAY);

		final var lBackEntry = new MenuEntry(mScreenManager, this, "Back");
		lBackEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lBackEntry.registerClickListener(this, BUTTON_BACK);

		lLayout.addMenuEntry(mNumPlayersSlider);
		lLayout.addMenuEntry(mPlayerResources);
		lLayout.addMenuEntry(mDifficulty);
		lLayout.addMenuEntry(MenuEntry.menuSeparator());
		lLayout.addMenuEntry(lStartGameEntry);
		lLayout.addMenuEntry(MenuEntry.menuSeparator());
		lLayout.addMenuEntry(lBackEntry);

		mScreenPaddingTop = 30.f;
		mLayoutPaddingHorizontal = 50.f;
		mLayoutAlignment = LAYOUT_ALIGNMENT.LEFT;

		mShowBackgroundScreens = false;

		mLayouts.add(lLayout);

	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);
	}

	@Override
	public void draw(LintfordCore core) {
		super.draw(core);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case BUTTON_PLAY:

			final var gameOptions = new GameOptions();
			gameOptions.numberOfPlayers = mNumPlayersSlider.getCurrentValue();
			gameOptions.difficulty = mDifficulty.selectedEntry(); // this isn't actually correct, index is happentance the id
			gameOptions.resources = mPlayerResources.selectedEntry(); // this isn't actually correct, index is happentance the id

			final var lNewSceneHeader = new SceneHeader(null);

			final var lLoadingScreen = new LoadingScreen(screenManager(), true, new GameScreen(screenManager(), lNewSceneHeader, gameOptions));
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
			break;

		case BUTTON_BACK:
			exitScreen();
			break;
		}
	}
}
