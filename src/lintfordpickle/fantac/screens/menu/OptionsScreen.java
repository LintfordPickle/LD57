package lintfordpickle.fantac.screens.menu;

import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.screenmanager.MenuEntry;
import net.lintfordlib.screenmanager.MenuScreen;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.ScreenManagerConstants.FILLTYPE;
import net.lintfordlib.screenmanager.ScreenManagerConstants.LAYOUT_ALIGNMENT;
import net.lintfordlib.screenmanager.ScreenManagerConstants.LAYOUT_WIDTH;
import net.lintfordlib.screenmanager.layouts.ListLayout;
import net.lintfordlib.screenmanager.screens.AudioOptionsScreen;
import net.lintfordlib.screenmanager.screens.KeyBindOptionsScreen;
import net.lintfordlib.screenmanager.screens.VideoOptionsScreen;

public class OptionsScreen extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final int BUTTON_AUDIO = 10;
	private static final int BUTTON_VIDEO = 11;
	private static final int BUTTON_KEY_BINDS = 12;
	private static final int BUTTON_BACK = 30;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public OptionsScreen(ScreenManager pScreenManager) {
		super(pScreenManager, null);

		final var lLayout = new ListLayout(this);
		lLayout.setDrawBackground(true, ColorConstants.WHITE);
		lLayout.layoutWidth(LAYOUT_WIDTH.HALF);
		lLayout.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);

		lLayout.showTitle(true);
		lLayout.title("Options");
		lLayout.cropPaddingTop(10.f);
		lLayout.cropPaddingBottom(10.f);

		final var lKeyBindsEntry = new MenuEntry(mScreenManager, this, "Key Binds");
		lKeyBindsEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lKeyBindsEntry.registerClickListener(this, BUTTON_KEY_BINDS);

		final var lVideoEntry = new MenuEntry(mScreenManager, this, "Video");
		lVideoEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lVideoEntry.registerClickListener(this, BUTTON_VIDEO);

		final var lAudioSettingsEntry = new MenuEntry(mScreenManager, this, "Audio");
		lAudioSettingsEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lAudioSettingsEntry.registerClickListener(this, BUTTON_AUDIO);

		final var lBackEntry = new MenuEntry(mScreenManager, this, "Back");
		lBackEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lBackEntry.registerClickListener(this, BUTTON_BACK);

		lLayout.addMenuEntry(lVideoEntry);
		lLayout.addMenuEntry(lAudioSettingsEntry);
//		lLayout.addMenuEntry(lKeyBindsEntry);
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
		case BUTTON_AUDIO:
			screenManager().addScreen(new AudioOptionsScreen(mScreenManager));
			break;

		case BUTTON_VIDEO:
			screenManager().addScreen(new VideoOptionsScreen(mScreenManager));
			break;

		case BUTTON_KEY_BINDS:
			screenManager().addScreen(new KeyBindOptionsScreen(mScreenManager));
			break;

		case BUTTON_BACK:
			exitScreen();
			break;
		}
	}
}
