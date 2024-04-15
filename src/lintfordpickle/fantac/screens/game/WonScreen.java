package lintfordpickle.fantac.screens.game;

import lintfordpickle.fantac.ConstantsGame;
import lintfordpickle.fantac.screens.MainMenu;
import lintfordpickle.fantac.screens.menu.CreditsScreen;
import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.data.scene.SceneHeader;
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

	private SceneHeader mSceneHeader;

	private SpriteSheetDefinition mGameSpritesheetDef;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public WonScreen(ScreenManager screenManager, SceneHeader sceneHeader) {
		super(screenManager, null);

		mSceneHeader = sceneHeader;

		final var lLayout = new ListLayout(this);
		lLayout.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);
		lLayout.setDrawBackground(true, ColorConstants.WHITE);
		lLayout.showTitle(true);
		lLayout.title("You love to see it!");

		final var lContinueEntry = new MenuEntry(mScreenManager, this, "Continue");
		lContinueEntry.registerClickListener(this, SCREEN_BUTTON_CONTINUE);

		final var lRestartEntry = new MenuEntry(mScreenManager, this, "Restart");
		lRestartEntry.registerClickListener(this, SCREEN_BUTTON_RESTART);

		final var lExitToMenuEntry = new MenuEntry(mScreenManager, this, "Exit");
		lExitToMenuEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

		lLayout.addMenuEntry(lContinueEntry);
		lLayout.addMenuEntry(lRestartEntry);
		lLayout.addMenuEntry(MenuEntry.menuSeparator());
		lLayout.addMenuEntry(lExitToMenuEntry);

		mLayouts.add(lLayout);

		mIsPopup = false;
		mShowBackgroundScreens = true;

		mBlockGamepadInputInBackground = true;
		mBlockKeyboardInputInBackground = true;
		mBlockMouseInputInBackground = true;

		mShowContextualKeyHints = false;

		mScreenPaddingTop = 50;
	}

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

		final var s = mRendererManager.uiSpriteBatch();

		s.begin(core.gameCamera());
		final var sf = mGameSpritesheetDef.getSpriteFrame("WONTEXT");
		s.draw(mGameSpritesheetDef, sf, -sf.width() * .5f, core.gameCamera().boundingRectangle().top(), sf.width(), sf.height(), -0.1f, ColorConstants.WHITE);
		s.end();

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
