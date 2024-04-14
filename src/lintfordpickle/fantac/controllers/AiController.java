package lintfordpickle.fantac.controllers;

import java.util.ArrayList;
import java.util.List;

import jbt.execution.core.BTExecutorFactory;
import jbt.execution.core.ContextFactory;
import jbt.execution.core.IBTExecutor;
import jbt.execution.core.IContext;
import lintfordpickle.fantac.ConstantsGame;
import lintfordpickle.fantac.data.ai.ConstantsBtContext;
import lintfordpickle.fantac.data.ai.bts.BtLibrary;
import lintfordpickle.fantac.data.settlements.BaseSettlement;
import lintfordpickle.fantac.data.teams.Team;
import lintfordpickle.fantac.data.teams.TeamManager;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.debug.Debug;

public class AiController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public final static String CONTROLLER_NAME = "Ai Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	protected final List<IBTExecutor> mExecutors = new ArrayList<>();

	private TeamManager mTeamManager;
	private BtLibrary mBtLibrary;

	private SettlementController mSettlementController;
	private UnitController mUnitController;
	private JobController mJobController;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public AiController(ControllerManager controllerManager, TeamManager teamManager, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mTeamManager = teamManager;
		mBtLibrary = new BtLibrary();

	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();
		mSettlementController = (SettlementController) lControllerManager.getControllerByNameRequired(SettlementController.CONTROLLER_NAME, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mUnitController = (UnitController) lControllerManager.getControllerByNameRequired(UnitController.CONTROLLER_NAME, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mJobController = (JobController) lControllerManager.getControllerByNameRequired(JobController.CONTROLLER_NAME, ConstantsGame.GAME_RESOURCE_GROUP_ID);

		final var lNumTeams = mTeamManager.teams.size();
		for (int i = 0; i < lNumTeams; i++) {
			final var lTeam = mTeamManager.teams.get(i);
			if (lTeam.teamUid == TeamManager.CONTROLLED_NONE || lTeam.teamUid == TeamManager.CONTROLLED_PLAYER)
				continue;

			assignNewBtExecutor(lTeam);
		}

		final var lSettlementManager = mSettlementController.settlementsManager();
		final var lSettlements = lSettlementManager.instances();
		final var lNumInstances = lSettlements.size();
		for (int i = 0; i < lNumInstances; i++) {
			final var lSettlement = lSettlements.get(i);
			if (lSettlement.isAssigned() == false)
				continue;

			assignNewBtExecutor(lSettlement);

		}
	}

	@Override
	public void unloadController() {
		super.unloadController();

	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final int lNumBtExecutorsToUpdate = mExecutors.size();
		for (int i = 0; i < lNumBtExecutorsToUpdate; i++) {
			final var lMobBtExecutor = mExecutors.get(i);

			lMobBtExecutor.tick();
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void assignNewBtExecutor(Team team) {
		if (team == null)
			return;

		final var lBtTreeName = "BtTeam";
		final var lBehaviourTree = mBtLibrary.getBT(lBtTreeName);

		if (lBehaviourTree == null) {
			Debug.debugManager().logger().e(getClass().getSimpleName(), "Couldn't resolve Jbt Tree 'BtTeam'.");
			return;
		}

		final var lContext = getContext();

		// Set vars
		lContext.setVariable(ConstantsBtContext.CONTEXT_VARS_TEAM_OURS, team);

		final var lBtExecutor = BTExecutorFactory.createBTExecutor(lBehaviourTree, lContext);

		mExecutors.add(lBtExecutor);

	}

	public void assignNewBtExecutor(BaseSettlement settlement) {
		if (settlement == null)
			return;

		final var lBtTreeName = "BtSettlement";
		final var lBehaviourTree = mBtLibrary.getBT(lBtTreeName);

		if (lBehaviourTree == null) {
			Debug.debugManager().logger().e(getClass().getSimpleName(), "Couldn't resolve Jbt Tree 'BtSettlement'.");
			return;
		}

		final var lContext = getContext();

		// Set vars
		lContext.setVariable(ConstantsBtContext.CONTEXT_VARS_SETTLEMENT_OURS, settlement);

		final var lTeamToSetInContext = mTeamManager.getTeamByUid(settlement.teamUid);
		lContext.setVariable(ConstantsBtContext.CONTEXT_VARS_TEAM_OURS, lTeamToSetInContext);

		final var lBtExecutor = BTExecutorFactory.createBTExecutor(lBehaviourTree, lContext);

		mExecutors.add(lBtExecutor);

	}

	private IContext getContext() {
		final var lContext = ContextFactory.createContext();

		lContext.setVariable(ConstantsBtContext.CONTEXT_JOBACTION_CONTROLLER, mJobController);
		lContext.setVariable(ConstantsBtContext.CONTEXT_SETTLEMENT_CONTROLLER, mSettlementController);
		lContext.setVariable(ConstantsBtContext.CONTEXT_UNITACTION_CONTROLLER, mUnitController);

		return lContext;
	}

	public void removeBtExecutor(IBTExecutor btExecutor) {
		if (btExecutor == null)
			return;

		btExecutor.terminate();

		if (mExecutors.contains(btExecutor)) {
			mExecutors.remove(btExecutor);
		}
	}

}
