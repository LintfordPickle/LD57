package lintfordpickle.fantac.renderers;

import lintfordpickle.fantac.ConstantsGame;
import lintfordpickle.fantac.controllers.SettlementsController;
import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManager;

public class SettlementsRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Settlement Renderer";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SettlementsController mSettlementController;
	private SpriteSheetDefinition mGameSpritesheet;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mSettlementController != null;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SettlementsRenderer(RendererManager rendererManager, int entityGroupUid) {
		super(rendererManager, RENDERER_NAME, entityGroupUid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {

		final var lControllerManager = core.controllerManager();
		mSettlementController = (SettlementsController) lControllerManager.getControllerByNameRequired(SettlementsController.CONTROLLER_NAME, entityGroupID());

	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mGameSpritesheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_GAME", ConstantsGame.GAME_RESOURCE_GROUP_ID);

	}

	@Override
	public boolean handleInput(LintfordCore core) {

		if (core.input().mouse().tryAcquireMouseLeftClickTimed(hashCode(), this)) {
			final var lMouseX = core.gameCamera().getMouseWorldSpaceX();
			final var lMouseY = core.gameCamera().getMouseWorldSpaceY();

			final var lSettlement = mSettlementController.getSettlementAtPosition(lMouseX, lMouseY, 4);
			if (lSettlement != null) {
				System.out.println("Settlement selected: " + lSettlement.uid);
				return true;
			}

		}

		return super.handleInput(core);
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

	}

	@Override
	public void draw(LintfordCore core) {
		final var lSpriteBatch = mRendererManager.uiSpriteBatch();
		final var lFontUnit = mRendererManager.uiTextFont();

		lSpriteBatch.begin(core.gameCamera());
		lFontUnit.begin(core.gameCamera());

		final var lSettlementManager = mSettlementController.settlementsManager();
		final var lSettlements = lSettlementManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var lSettlement = lSettlements.get(i);
			if (lSettlement.isAssigned() == false)
				continue;

			// TODO: Draw the settlements
			final var lSpriteFrame = mGameSpritesheet.getSpriteFrame("CASTLE");
			final var lWidth = lSpriteFrame.width() * 2.f;
			final var lHeight = lSpriteFrame.height() * 2.f;

			final var xx = lSettlement.x;
			final var yy = lSettlement.y;

			lSpriteBatch.drawAroundCenter(mGameSpritesheet, mGameSpritesheet.getSpriteFrame("CASTLE"), xx, yy, lWidth, lHeight, 0.f, 0.f, 0.f, -0.1f, ColorConstants.WHITE);

			final var lNumWorkersTextWidth = lFontUnit.getStringWidth("" + lSettlement.numWorkers);
			lFontUnit.drawText("" + lSettlement.numWorkers, xx - lNumWorkersTextWidth * .5f, yy - lWidth * .5f, -0.1f, 1.f);

			if (ConstantsGame.IS_DEBUG_RENDERING_MODE) {
				Debug.debugManager().drawers().drawCircleImmediate(core.gameCamera(), xx, yy, lSettlement.radius);
			}

		}

		lSpriteBatch.end();
		lFontUnit.end();

	}

}
