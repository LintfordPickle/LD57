package lintfordpickle.fantac.renderers;

import lintfordpickle.fantac.ConstantsGame;
import lintfordpickle.fantac.controllers.UnitsController;
import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManager;

public class UnitsRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Units Renderer";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private UnitsController mUnitsController;

	private SpriteSheetDefinition mGameSpritesheet;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mUnitsController != null;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public UnitsRenderer(RendererManager rendererManager, int entityGroupUid) {
		super(rendererManager, RENDERER_NAME, entityGroupUid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {

		final var lControllerManager = core.controllerManager();
		mUnitsController = (UnitsController) lControllerManager.getControllerByNameRequired(UnitsController.CONTROLLER_NAME, entityGroupID());

	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mGameSpritesheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_GAME", ConstantsGame.GAME_RESOURCE_GROUP_ID);

	}

	@Override
	public boolean handleInput(LintfordCore core) {

		return super.handleInput(core);
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

	}

	@Override
	public void draw(LintfordCore core) {
		final var lSpriteBatch = mRendererManager.uiSpriteBatch();

		lSpriteBatch.begin(core.gameCamera());

		final var lUnitsManager = mUnitsController.unitsManager();
		final var lUnits = lUnitsManager.unitsInField;
		final var lNumUnitInstance = lUnits.size();
		for (int i = 0; i < lNumUnitInstance; i++) {
			final var lUnitInstance = lUnits.get(i);

			final var lSpriteFrame = mGameSpritesheet.getSpriteFrame("PEON");
			final var lWidth = lSpriteFrame.width() * 2.f;
			final var lHeight = lSpriteFrame.height() * 2.f;

			final var xx = lUnitInstance.x;
			final var yy = lUnitInstance.y;

			lSpriteBatch.drawAroundCenter(mGameSpritesheet, mGameSpritesheet.getSpriteFrame("PEON"), xx, yy, lWidth, lHeight, 0.f, 0.f, 0.f, -0.1f, ColorConstants.WHITE);

//			if (ConstantsGame.IS_DEBUG_RENDERING_MODE) {
//				Debug.debugManager().drawers().drawCircleImmediate(core.gameCamera(), xx, yy, lUnitInstance.radius);
//			}
		}

		lSpriteBatch.end();

	}

}
