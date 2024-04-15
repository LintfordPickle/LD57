package lintfordpickle.fantac.renderers;

import lintfordpickle.fantac.ConstantsGame;
import lintfordpickle.fantac.controllers.JobController;
import lintfordpickle.fantac.controllers.SettlementController;
import lintfordpickle.fantac.controllers.TeamController;
import lintfordpickle.fantac.controllers.UnitController;
import lintfordpickle.fantac.data.teams.TeamRace;
import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.geometry.Rectangle;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.sprites.SpriteFrame;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManager;

public class HudRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Hud Renderer";

	private static final float HUD_PANEL_WIDTH = 120.f * 2.f;
	private static final float HUD_PANEL_HEIGHT = 48.f * 2.f;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private UnitController mUnitController;
	private SettlementController mSettlementController;
	private TeamController mTeamController;
	private JobController mJobController;

	private SpriteSheetDefinition mGameSpritesheet;

	private final Rectangle pos1HudRect = new Rectangle();
	private final Rectangle pos2HudRect = new Rectangle();
	private final Rectangle pos3HudRect = new Rectangle();
	private final Rectangle pos4HudRect = new Rectangle();

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mUnitController != null;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public HudRenderer(RendererManager rendererManager, int entityGroupUid) {
		super(rendererManager, RENDERER_NAME, entityGroupUid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {

		final var lControllerManager = core.controllerManager();
		mUnitController = (UnitController) lControllerManager.getControllerByNameRequired(UnitController.CONTROLLER_NAME, entityGroupID());
		mTeamController = (TeamController) lControllerManager.getControllerByNameRequired(TeamController.CONTROLLER_NAME, entityGroupID());
		mSettlementController = (SettlementController) lControllerManager.getControllerByNameRequired(SettlementController.CONTROLLER_NAME, entityGroupID());
		mJobController = (JobController) lControllerManager.getControllerByNameRequired(JobController.CONTROLLER_NAME, entityGroupID());

		final var lHudBounds = core.HUD().boundingRectangle();

		final var xx = lHudBounds.left();
		final var yy = lHudBounds.bottom() - HUD_PANEL_HEIGHT;

		pos1HudRect.set(xx + HUD_PANEL_WIDTH * 0, yy, HUD_PANEL_WIDTH, HUD_PANEL_HEIGHT);
		pos2HudRect.set(xx + HUD_PANEL_WIDTH * 1, yy, HUD_PANEL_WIDTH, HUD_PANEL_HEIGHT);
		pos3HudRect.set(xx + HUD_PANEL_WIDTH * 2, yy, HUD_PANEL_WIDTH, HUD_PANEL_HEIGHT);
		pos4HudRect.set(xx + HUD_PANEL_WIDTH * 3, yy, HUD_PANEL_WIDTH, HUD_PANEL_HEIGHT);
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

		final var lHudBounds = core.gameCamera().boundingRectangle();
		final var xx = lHudBounds.left();
		final var yy = lHudBounds.bottom() - HUD_PANEL_HEIGHT;

		final var lHorizontalDist = lHudBounds.width() / 4;
		pos1HudRect.set(xx + lHorizontalDist * 0, yy, HUD_PANEL_WIDTH, HUD_PANEL_HEIGHT);
		pos2HudRect.set(xx + lHorizontalDist * 1, yy, HUD_PANEL_WIDTH, HUD_PANEL_HEIGHT);
		pos3HudRect.set(xx + lHorizontalDist * 2, yy, HUD_PANEL_WIDTH, HUD_PANEL_HEIGHT);
		pos4HudRect.set(xx + lHorizontalDist * 3, yy, HUD_PANEL_WIDTH, HUD_PANEL_HEIGHT);
	}

	@Override
	public void draw(LintfordCore core) {
		final var lSpriteBatch = mRendererManager.uiSpriteBatch();
		final var lFontBatch = mRendererManager.uiHeaderFont();

		lSpriteBatch.begin(core.gameCamera());
		lFontBatch.begin(core.gameCamera());

		final var lTeamManager = mTeamController.teamManager();
		final var lTeams = lTeamManager.teams;
		final var lNumTeams = lTeams.size();
		for (int i = 1; i < lNumTeams; i++) {
			final var t = lTeams.get(i);

			final var lTeamIndexOffset = i - 1; // not counting UNOCCUPIED
			final var lHudRect = getTeamHudRectangle(lTeamIndexOffset, lNumTeams - 1);

			var lTeamColor = ColorConstants.WHITE;
			if (lTeamIndexOffset == 0)
				lTeamColor = ColorConstants.RED;
			if (lTeamIndexOffset == 1)
				lTeamColor = ColorConstants.BLUE_SKY;

			final var lSpriteFrame = mGameSpritesheet.getSpriteFrame("HUDTEAMPANEL");
			lSpriteBatch.draw(mGameSpritesheet, lSpriteFrame, lHudRect, -0.2f, lTeamColor);

			final var s_wor = mSettlementController.getTotalWorkers(t.teamUid);
			final var u_wor = mUnitController.getTotalWorkers(t.teamUid);
			final var j_wor = mJobController.getTotalWorkers(t.teamUid);
			final var lTotalWorkers = s_wor + u_wor + j_wor;

			final var s_sol = mSettlementController.getTotalSoldiers(t.teamUid);
			final var u_sol = mUnitController.getTotalSoldiers(t.teamUid);
			final var j_sol = mJobController.getTotalSoldiers(t.teamUid);
			final var lTotalSoldiers = s_sol + u_sol + j_sol;

			lFontBatch.drawText("PLAYER " + i, lHudRect.x() + 16, lHudRect.y() + 16, -0.1f, ColorConstants.WHITE, 1.f);
			lFontBatch.drawText("W: " + lTotalWorkers, lHudRect.x() + 16, lHudRect.y() + 40, -0.1f, ColorConstants.WHITE, 1.f);
			lFontBatch.drawText("S: " + lTotalSoldiers, lHudRect.x() + 16, lHudRect.y() + 60, -0.1f, ColorConstants.WHITE, 1.f);

			// portrait
			SpriteFrame portraitFrame = null;
			if (t.raceUid == TeamRace.RACE_HUMANS) {
				portraitFrame = mGameSpritesheet.getSpriteFrame("PORTRAITHUMAN");
			} else {
				portraitFrame = mGameSpritesheet.getSpriteFrame("PORTRAITDEMON");
			}

			if (portraitFrame != null) {
				lSpriteBatch.draw(mGameSpritesheet, portraitFrame, lHudRect.x() + 165, lHudRect.y() + 20, 29 * 2, 29 * 2, -0.2f, ColorConstants.WHITE);
			}
		}

		lSpriteBatch.end();
		lFontBatch.end();
	}

	private Rectangle getTeamHudRectangle(int teamNum, int numOfTeams) {
		if (teamNum == 0) {
			return pos1HudRect;
		}

		// player 2 with 2 teams is thje exception
		if (teamNum == 1 && numOfTeams == 2)
			return pos4HudRect;

		if (teamNum == 1)
			return pos2HudRect;

		if (teamNum == 2)
			return pos3HudRect;

		if (teamNum == 3)
			return pos4HudRect;

		return null;

	}

}
