package net.lintfordpickle.newgame.screens.menu;

import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.screenmanager.MenuScreen;
import net.lintfordlib.screenmanager.ScreenManager;

public class CreditsScreen extends MenuScreen {

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public CreditsScreen(ScreenManager screenManager) {
		super(screenManager, null);

		mIsPopup = false;
		mShowBackgroundScreens = true;
		mESCBackEnabled = false;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void draw(LintfordCore core) {
		super.draw(core);

		final var lHudBounds = core.HUD().boundingRectangle();

		final var lFontUnit = mRendererManager.uiTextFont();
		lFontUnit.begin(core.HUD());
		final var lTextPadding = 2.f;
		lFontUnit.drawText("New Game Template", lHudBounds.left() + lTextPadding, lHudBounds.bottom() - lFontUnit.fontHeight() - lTextPadding, -0.01f, ColorConstants.WHITE, 1.f);
		lFontUnit.end();

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	protected void handleOnClick() {
		// ignored
	}
}
