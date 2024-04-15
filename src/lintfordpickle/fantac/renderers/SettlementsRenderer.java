package lintfordpickle.fantac.renderers;

import lintfordpickle.fantac.ConstantsGame;
import lintfordpickle.fantac.controllers.SettlementController;
import lintfordpickle.fantac.data.settlements.SettlementType;
import lintfordpickle.fantac.data.teams.TeamManager;
import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
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

	private SettlementController mSettlementController;
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
		mSettlementController = (SettlementController) lControllerManager.getControllerByNameRequired(SettlementController.CONTROLLER_NAME, entityGroupID());

	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mGameSpritesheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_GAME", ConstantsGame.GAME_RESOURCE_GROUP_ID);

	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var lSettlementManager = mSettlementController.settlementsManager();
		final var lSettlements = lSettlementManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var s = lSettlements.get(i);
			if (s.mFlagSpriteInstance == null) {
				s.mFlagSpriteInstance = mGameSpritesheet.getSpriteInstance("flag");
			}

			if (s.mSettlementInstance == null) {
				if (s.settlementTypeUid == SettlementType.SETTLEMENT_TYPE_TOWN) {
					s.mSettlementInstance = mGameSpritesheet.getSpriteInstance("TOWN");
				} else if (s.settlementTypeUid == SettlementType.SETTLEMENT_TYPE_BADTOWN) {
					s.mSettlementInstance = mGameSpritesheet.getSpriteInstance("BADTOWN");
				} else if (s.settlementTypeUid == SettlementType.SETTLEMENT_TYPE_CASTLE) {
					s.mSettlementInstance = mGameSpritesheet.getSpriteInstance("CASTLE");
				} else if (s.settlementTypeUid == SettlementType.SETTLEMENT_TYPE_PENTAGRAM) {
					s.mSettlementInstance = mGameSpritesheet.getSpriteInstance("pentagram");
				} else {
					s.mSettlementInstance = mGameSpritesheet.getSpriteInstance("VILLAGE");
				}
			}

			if (s.mSettlementInstance != null)
				s.mSettlementInstance.update(core);

			if (s.mFlagSpriteInstance != null)
				s.mFlagSpriteInstance.update(core);
		}

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

			if (lSettlement.mSettlementInstance == null)
				continue;

			final var lSettlementSpriteFrame = lSettlement.mSettlementInstance.currentSpriteFrame();
			final var lWidth = lSettlementSpriteFrame.width() * 2.f;
			final var lHeight = lSettlementSpriteFrame.height() * 2.f;

			final var xx = lSettlement.x;
			final var yy = lSettlement.y;

			// Draw settlement base

			lSpriteBatch.drawAroundCenter(mGameSpritesheet, lSettlementSpriteFrame, xx, yy, lWidth, lHeight, 0.f, 0.f, 0.f, -0.1f, ColorConstants.WHITE);

			// Draw team flag
			if (lSettlement.teamUid > TeamManager.CONTROLLED_NONE && lSettlement.mFlagSpriteInstance != null) {
				final var lFlagSpriteFrame = lSettlement.mFlagSpriteInstance.currentSpriteFrame();
				if (lFlagSpriteFrame != null) {

					var lTeamColor = ColorConstants.WHITE;
					if (lSettlement.teamUid == 1)
						lTeamColor = ColorConstants.RED;
					if (lSettlement.teamUid == 2)
						lTeamColor = ColorConstants.BLUE_SKY;

					lSpriteBatch.drawAroundCenter(mGameSpritesheet, lFlagSpriteFrame, xx, yy - lHeight * .5f, lFlagSpriteFrame.width() * 2.f, lFlagSpriteFrame.height() * 2.f, 0.f, 0.f, 0.f, -0.1f, lTeamColor);
				}
			}

			// Draw workers / peons
			final var lNumWorkersTextWidth = lFontUnit.getStringWidth("" + lSettlement.numWorkers);
			lFontUnit.drawText("" + lSettlement.numWorkers, xx - lNumWorkersTextWidth * .5f, yy + lWidth * .5f, -0.1f, 1.f);

			final var lNumSoldiersTextWidth = lFontUnit.getStringWidth("" + lSettlement.numSoldiers);

			lFontUnit.drawText("" + lSettlement.numSoldiers, xx - lNumSoldiersTextWidth * .5f, yy + lWidth * .5f + lFontUnit.fontHeight() + 2, -0.1f, ColorConstants.BLUE, 1.f);

//			if (ConstantsGame.IS_DEBUG_RENDERING_MODE) {
//				Debug.debugManager().drawers().drawCircleImmediate(core.gameCamera(), xx, yy, lSettlement.radius);
//			}

		}

		lSpriteBatch.end();
		lFontUnit.end();

	}

}
