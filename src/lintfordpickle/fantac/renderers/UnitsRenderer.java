package lintfordpickle.fantac.renderers;

import lintfordpickle.fantac.ConstantsGame;
import lintfordpickle.fantac.controllers.TeamController;
import lintfordpickle.fantac.controllers.UnitController;
import lintfordpickle.fantac.data.teams.TeamRace;
import lintfordpickle.fantac.data.units.UnitDefinitions;
import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.sprites.SpriteFrame;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.core.maths.RandomNumbers;
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

	private UnitController mUnitController;
	private TeamController mTeamController;
	private SpriteSheetDefinition mGameSpritesheet;

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

	public UnitsRenderer(RendererManager rendererManager, int entityGroupUid) {
		super(rendererManager, RENDERER_NAME, entityGroupUid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {

		final var lControllerManager = core.controllerManager();
		mUnitController = (UnitController) lControllerManager.getControllerByNameRequired(UnitController.CONTROLLER_NAME, entityGroupID());

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

		final var lUnitsManager = mUnitController.unitsManager();
		final var lUnits = lUnitsManager.unitsInField;
		final var lNumUnitInstance = lUnits.size();
		for (int i = 0; i < lNumUnitInstance; i++) {
			final var u = lUnits.get(i);

			final var lSpriteFrame = getSpriteFrame(u.raceUid, u.unitTypeUid);
			final var lWidth = lSpriteFrame.width() * 2.f;
			final var lHeight = lSpriteFrame.height() * 2.f;

			if (u.stimer > 0.f)
				u.stimer -= core.gameTime().elapsedTimeMilli();
			else
				u.highStep = false;

			if (u.highStep == false && u.stimer <= 0.f) {
				u.highStep = RandomNumbers.getRandomChance(30);
				u.stimer = 100.f;
			}

			final var xx = u.x;
			final var yy = u.y - (u.highStep ? 3.f : 0.f);

			lSpriteBatch.drawAroundCenter(mGameSpritesheet, lSpriteFrame, xx, yy, lWidth, lHeight, 0.f, 0.f, 0.f, -0.1f, ColorConstants.WHITE);
		}

		lSpriteBatch.end();
	}

	private SpriteFrame getSpriteFrame(int raceUid, int unitTypeUid) {
		switch (raceUid) {
		case TeamRace.RACE_HUMANS:
			switch (unitTypeUid) {
			case UnitDefinitions.UNIT_WORKER_UID:
				return mGameSpritesheet.getSpriteFrame("WORKER");
			case UnitDefinitions.UNIT_SOLDIER_UID:
				return mGameSpritesheet.getSpriteFrame("SOLDIER");
			}
			break;

		case TeamRace.RACE_DEMONS:
			switch (unitTypeUid) {
			case UnitDefinitions.UNIT_WORKER_UID:
				return mGameSpritesheet.getSpriteFrame("PEON");
			case UnitDefinitions.UNIT_SOLDIER_UID:
				return mGameSpritesheet.getSpriteFrame("DEMON");
			}
			break;
		}

		return null;
	}

}
