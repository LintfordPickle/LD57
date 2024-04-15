package lintfordpickle.fantac.screens.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lintfordpickle.fantac.ConstantsGame;
import lintfordpickle.fantac.NewGameKeyActions;
import lintfordpickle.fantac.controllers.AiController;
import lintfordpickle.fantac.controllers.AnimationController;
import lintfordpickle.fantac.controllers.GameStateController;
import lintfordpickle.fantac.controllers.JobController;
import lintfordpickle.fantac.controllers.SettlementController;
import lintfordpickle.fantac.controllers.TeamController;
import lintfordpickle.fantac.controllers.UnitController;
import lintfordpickle.fantac.data.GameWorld;
import lintfordpickle.fantac.data.IGameStateListener;
import lintfordpickle.fantac.data.settlements.SettlementType;
import lintfordpickle.fantac.data.teams.TeamManager;
import lintfordpickle.fantac.data.teams.TeamRace;
import lintfordpickle.fantac.renderers.AnimationRenderer;
import lintfordpickle.fantac.renderers.SettlementsRenderer;
import lintfordpickle.fantac.renderers.UnitsRenderer;
import net.lintfordlib.assets.ResourceManager;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.controllers.core.particles.ParticleFrameworkController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.textures.Texture;
import net.lintfordlib.core.maths.RandomNumbers;
import net.lintfordlib.core.particles.ParticleFrameworkData;
import net.lintfordlib.data.DataManager;
import net.lintfordlib.data.scene.SceneHeader;
import net.lintfordlib.renderers.RendererManager;
import net.lintfordlib.renderers.particles.ParticleFrameworkRenderer;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.screens.BaseGameScreen;

public class GameScreen extends BaseGameScreen implements IGameStateListener {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SceneHeader mSceneHeader;

	// Data
	private GameWorld mGameWorld;
	private ParticleFrameworkData mParticleFrameworkData;

	private Texture mGameBackgroundTexture;

	// Controllers
	private GameStateController mGameStateController;
	private SettlementController mSettlementController;
	private UnitController mUnitController;
	private JobController mJobController;
	private AnimationController mAnimationController;
	private ParticleFrameworkController mParticleFrameworkController;
	private AiController mAiController;
	private TeamController mTeamController;

	// Renderers
	private SettlementsRenderer mSettlementRenderer;
	private UnitsRenderer mUnitsRenderer;
	private AnimationRenderer mAnimationRenderer;
	private ParticleFrameworkRenderer mParticleFrameworkRenderer;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameScreen(ScreenManager screenManager, SceneHeader sceneHeader) {
		super(screenManager, new RendererManager(screenManager.core(), ConstantsGame.GAME_RESOURCE_GROUP_ID));

		mSceneHeader = sceneHeader;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void handleInput(LintfordCore core) {
		super.handleInput(core);

		if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_ESCAPE, this) || core.input().gamepads().isGamepadButtonDownTimed(GLFW.GLFW_GAMEPAD_BUTTON_START, this)) {
			screenManager().addScreen(new PauseScreen(screenManager(), mSceneHeader));
			return;
		}

		// For simple games you could add code to handle the player input here.
		// However usually, components would be updated in dedicated BaseControllers (see the CONTROLLERS Section below).
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mGameBackgroundTexture = resourceManager.textureManager().getTexture("TEXTURE_GAME_BACKGROUND", ConstantsGame.GAME_RESOURCE_GROUP_ID);

	}

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);

		// For simple games you could add code to perform the game update logic here.
		// However usually, components would be updated in dedicated BaseControllers (see the CONTROLLERS Section below).

		if (core.input().eventActionManager().getCurrentControlActionStateTimed(NewGameKeyActions.KEY_BINDING_PRIMARY_FIRE)) {
			screenManager().toastManager().addMessage(getClass().getSimpleName(), "PRIMARY FIRE", 1500);
		}

	}

	@Override
	public void draw(LintfordCore core) {

		GL11.glClearColor(0.08f, .02f, 0.03f, 1.f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		spriteBatch().begin(core.gameCamera());
		spriteBatch().draw(mGameBackgroundTexture, 0, 0, 960 / 2, 576 / 2, -960 / 2, -576 / 2, 960, 576, -0.1f, ColorConstants.WHITE);
		spriteBatch().end();

		super.draw(core);

		// For simple games you could add code to render a basic scene directly here.
		// However usually, the rendering would be performed by a specific BaseRenderer (see the RENDERERS section below).

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	// DATA ----------------------------------------

	@SuppressWarnings("unused")
	@Override
	protected void createData(DataManager dataManager) {
		mParticleFrameworkData = new ParticleFrameworkData(dataManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mParticleFrameworkData.loadFromMetaFiles();

		mGameWorld = new GameWorld();

		// TODO:
		setup2Players();

	}

	private void setup2Players() {

		final var lRandomPlayer1VillageChange = 40;
		final var lRandomPlayer2VillageChange = 60;

		{
			final var lPlayerTeam = mGameWorld.team().addTeam(true, TeamRace.RACE_DEMONS);
			final var lNpcTeam = mGameWorld.team().addTeam(false, TeamRace.RACE_HUMANS);

			// team 1 - humans
			// team 2 - demons

			// setup settlements
			final var lSettlement00 = mGameWorld.settlements().addNewSettlement(lPlayerTeam.teamUid, SettlementType.SETTLEMENT_TYPE_BADTOWN, -360, -180);
			final var lSettlement01 = mGameWorld.settlements().addNewSettlement(lPlayerTeam.teamUid, SettlementType.SETTLEMENT_TYPE_BADTOWN, -280, -20);

			if (RandomNumbers.getRandomChance(lRandomPlayer1VillageChange))
				mGameWorld.settlements().addNewSettlement(TeamManager.CONTROLLED_NONE, SettlementType.SETTLEMENT_TYPE_VILLAGE, -100, -50);
			if (RandomNumbers.getRandomChance(lRandomPlayer1VillageChange))
				mGameWorld.settlements().addNewSettlement(TeamManager.CONTROLLED_NONE, SettlementType.SETTLEMENT_TYPE_VILLAGE, -170, 200);
			if (RandomNumbers.getRandomChance(lRandomPlayer2VillageChange))
				mGameWorld.settlements().addNewSettlement(TeamManager.CONTROLLED_NONE, SettlementType.SETTLEMENT_TYPE_VILLAGE, 170, 160);
			if (RandomNumbers.getRandomChance(lRandomPlayer2VillageChange))
				mGameWorld.settlements().addNewSettlement(TeamManager.CONTROLLED_NONE, SettlementType.SETTLEMENT_TYPE_VILLAGE, 120, -140);

			lSettlement00.numWorkers = 8;
			lSettlement01.numWorkers = 12;

			final var lSettlement02 = mGameWorld.settlements().addNewSettlement(lNpcTeam.teamUid, SettlementType.SETTLEMENT_TYPE_TOWN, +300, -200);
			final var lSettlement03 = mGameWorld.settlements().addNewSettlement(lNpcTeam.teamUid, SettlementType.SETTLEMENT_TYPE_TOWN, +270, 170);
			lSettlement02.numWorkers = 7;
			lSettlement03.numWorkers = 15;

			mGameWorld.settlements().addNewSettlement(TeamManager.CONTROLLED_NONE, SettlementType.SETTLEMENT_TYPE_PENTAGRAM, -10, -230); // pentagram
			mGameWorld.settlements().addNewSettlement(TeamManager.CONTROLLED_NONE, SettlementType.SETTLEMENT_TYPE_CASTLE, +350, -0);
		}
	}

	private void setup3Players() {
		final var lPlayerTeam = mGameWorld.team().addTeam(true, TeamRace.RACE_DEMONS);
		final var lNpcTeam0 = mGameWorld.team().addTeam(false, TeamRace.RACE_HUMANS);
		final var lNpcTeam1 = mGameWorld.team().addTeam(false, TeamRace.RACE_HUMANS);

		{

			// setup settlements
			final var lSettlement00 = mGameWorld.settlements().addNewSettlement(lPlayerTeam.teamUid, SettlementType.SETTLEMENT_TYPE_TOWN, -350, -200);
			final var lSettlement01 = mGameWorld.settlements().addNewSettlement(lPlayerTeam.teamUid, SettlementType.SETTLEMENT_TYPE_TOWN, -290, -140);
			lSettlement00.numWorkers = 8;
			lSettlement01.numWorkers = 12;
			mGameWorld.settlements().addNewSettlement(TeamManager.CONTROLLED_NONE, SettlementType.SETTLEMENT_TYPE_PENTAGRAM, -150, -150); // pentagram

			final var lSettlement02 = mGameWorld.settlements().addNewSettlement(lNpcTeam0.teamUid, SettlementType.SETTLEMENT_TYPE_TOWN, +300, 220);
			final var lSettlement03 = mGameWorld.settlements().addNewSettlement(lNpcTeam0.teamUid, SettlementType.SETTLEMENT_TYPE_TOWN, +270, 170);
			lSettlement02.numWorkers = 7;
			lSettlement03.numWorkers = 15;
			mGameWorld.settlements().addNewSettlement(TeamManager.CONTROLLED_NONE, SettlementType.SETTLEMENT_TYPE_CASTLE, +350, -0);

			final int numRandomTowns = RandomNumbers.random(2, 3);
			for (int i = 0; i < numRandomTowns; i++) {
				final var xx = RandomNumbers.random(-150, 200);
				final var yy = RandomNumbers.random(-150, 200);

				final var s = mGameWorld.settlements().addNewSettlement(lNpcTeam1.teamUid, SettlementType.SETTLEMENT_TYPE_TOWN, xx, yy);
				s.numWorkers = RandomNumbers.random(5, 15);
			}

			final int numUnoccupiedTowns = RandomNumbers.random(2, 4);
			for (int i = 0; i < numUnoccupiedTowns; i++) {
				final var xx = RandomNumbers.random(-150, 200);
				final var yy = RandomNumbers.random(-150, 200);

				mGameWorld.settlements().addNewSettlement(TeamManager.CONTROLLED_NONE, SettlementType.SETTLEMENT_TYPE_VILLAGE, xx, yy);
			}

		}

	}

	// CONTROLLERS ---------------------------------

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		mGameStateController = new GameStateController(controllerManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mTeamController = new TeamController(controllerManager, mGameWorld.team(), ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mSettlementController = new SettlementController(controllerManager, mGameWorld.settlements(), ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mUnitController = new UnitController(controllerManager, mGameWorld.units(), ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mJobController = new JobController(controllerManager, mGameWorld.jobs(), ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mAnimationController = new AnimationController(controllerManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mParticleFrameworkController = new ParticleFrameworkController(controllerManager, mParticleFrameworkData, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mAiController = new AiController(controllerManager, mGameWorld.team(), ConstantsGame.GAME_RESOURCE_GROUP_ID);

		mGameStateController.setGameStateListener(this);
	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		mTeamController.initialize(core);
		mGameStateController.initialize(core);
		mSettlementController.initialize(core);
		mUnitController.initialize(core);
		mJobController.initialize(core);
		mAnimationController.initialize(core);
		mParticleFrameworkController.initialize(core);
		mAiController.initialize(core);
	}

	// RENDERERS -----------------------------------

	@Override
	protected void createRenderers(LintfordCore core) {
		mParticleFrameworkRenderer = new ParticleFrameworkRenderer(mRendererManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mUnitsRenderer = new UnitsRenderer(mRendererManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mSettlementRenderer = new SettlementsRenderer(mRendererManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mAnimationRenderer = new AnimationRenderer(mRendererManager, mAnimationController, ConstantsGame.GAME_RESOURCE_GROUP_ID);
	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		mSettlementRenderer.initialize(core);
		mUnitsRenderer.initialize(core);
		mAnimationRenderer.initialize(core);
		mParticleFrameworkRenderer.initialize(core);
	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {
		mSettlementRenderer.loadResources(resourceManager);
		mUnitsRenderer.loadResources(resourceManager);
		mAnimationRenderer.loadResources(resourceManager);
		mParticleFrameworkRenderer.loadResources(resourceManager);

	}

	// --------------------------------------
	// Callback-Methods
	// --------------------------------------

	@Override
	public void onGameWon(int teamUid) {
		mScreenManager.addScreen(new WonScreen(mScreenManager, mSceneHeader));

	}

	@Override
	public void onGameLost(int teamUid) {
		mScreenManager.addScreen(new LostScreen(mScreenManager, mSceneHeader));

	}
}
