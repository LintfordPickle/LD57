package lintfordpickle.fantac.controllers;

import lintfordpickle.fantac.ConstantsGame;
import lintfordpickle.fantac.data.settlements.BaseSettlement;
import lintfordpickle.fantac.data.settlements.SettlementType;
import lintfordpickle.fantac.data.settlements.SettlementsManager;
import lintfordpickle.fantac.data.teams.TeamManager;
import lintfordpickle.fantac.data.units.Unit;
import lintfordpickle.fantac.data.units.UnitDefinitions;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.maths.CollisionExtensions;

public class SettlementController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Settlement Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SettlementsManager mSettlementsManager;
	private AiController mAiController;
	private TeamController mTeamController;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public SettlementsManager settlementsManager() {
		return mSettlementsManager;
	}

	public BaseSettlement getClosestUnoccupiedSettlement(float x, float y) {
		float dist = Float.MAX_VALUE;
		BaseSettlement result = null;

		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var s = lSettlements.get(i);
			if (s.isAssigned() == false)
				continue;

			if (s.teamUid != TeamManager.CONTROLLED_NONE)
				continue;

			final var xx = s.x - x;
			final var yy = s.y - y;
			float d = xx * xx + yy * yy;
			if (d < dist) {
				dist = d;
				result = s;
			}

		}
		return result;
	}

	public BaseSettlement getClosestEnemySettlement(int teamUid, float x, float y) {
		float dist = Float.MAX_VALUE;
		BaseSettlement result = null;

		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var s = lSettlements.get(i);
			if (s.isAssigned() == false)
				continue;

			if (s.teamUid != teamUid)
				continue;

			final var xx = s.x - x;
			final var yy = s.y - y;
			float d = xx * xx + yy * yy;
			if (d < dist) {
				dist = d;
				result = s;
			}

		}
		return result;
	}

	public BaseSettlement getOurClosestSchool(int teamUid, float x, float y) {
		float dist = Float.MAX_VALUE;
		BaseSettlement result = null;

		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var s = lSettlements.get(i);
			if (s.isAssigned() == false)
				continue;

			if (s.teamUid != teamUid)
				continue;

			if (s.settlementTypeUid != SettlementType.SETTLEMENT_TYPE_PENTAGRAM && s.settlementTypeUid != SettlementType.SETTLEMENT_TYPE_CASTLE)
				continue;

			final var xx = s.x - x;
			final var yy = s.y - y;
			float d = xx * xx + yy * yy;
			if (d < dist) {
				dist = d;
				result = s;
			}

		}
		return result;
	}

	public BaseSettlement getClosestSchool(float x, float y) {
		float dist = Float.MAX_VALUE;
		BaseSettlement result = null;

		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var s = lSettlements.get(i);
			if (s.isAssigned() == false)
				continue;

			if (s.settlementTypeUid != SettlementType.SETTLEMENT_TYPE_PENTAGRAM && s.settlementTypeUid != SettlementType.SETTLEMENT_TYPE_CASTLE)
				continue;

			final var xx = s.x - x;
			final var yy = s.y - y;
			float d = xx * xx + yy * yy;
			if (d < dist) {
				dist = d;
				result = s;
			}

		}
		return result;
	}

	public BaseSettlement getWeakestEnemySettlement(int ourTeamUid) {
		var result = (BaseSettlement) null;
		var currentWeakest = Integer.MAX_VALUE;

		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var s = lSettlements.get(i);
			if (s.isAssigned() == false)
				continue;

			final var workersDef = s.numWorkers * UnitDefinitions.worker.defense;
			final var soldiersDef = s.numSoldiers * UnitDefinitions.worker.defense;

			if (s.teamUid != ourTeamUid && (workersDef + soldiersDef) < currentWeakest) {
				result = s;
				currentWeakest = (workersDef + soldiersDef);
			}

		}
		return result;
	}

	public int getNumUnoccupiedSettlements() {
		int result = 0;
		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			if (lSettlements.get(i).isAssigned() == false)
				continue;

			if (lSettlements.get(i).teamUid == -TeamManager.CONTROLLED_NONE)
				result++;

		}
		return result;
	}

	public BaseSettlement getSettlementByUid(int uid) {
		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			if (lSettlements.get(i).isAssigned() == false)
				continue;

			if (lSettlements.get(i).uid == uid)
				return lSettlements.get(i);

		}
		return null;
	}

	public int getTotalSettlements(int teamUid) {
		var result = 0;
		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var s = lSettlements.get(i);
			if (s.isAssigned() == false)
				continue;

			if (s.teamUid == teamUid)
				result++;

		}
		return result;
	}

	public int getTotalSettlementOfType(int teamUid, int settlementTypeUid) {
		var result = 0;
		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var s = lSettlements.get(i);
			if (s.isAssigned() == false)
				continue;

			if (s.teamUid == teamUid && s.settlementTypeUid == settlementTypeUid)
				result++;

		}
		return result;
	}

	public int getTotalWorkers(int teamUid) {
		var result = 0;
		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var s = lSettlements.get(i);
			if (s.isAssigned() == false)
				continue;

			if (s.teamUid == teamUid)
				result += s.numWorkers;

		}
		return result;
	}

	public int getTotalSoldiers(int teamUid) {
		var result = 0;
		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var s = lSettlements.get(i);
			if (s.isAssigned() == false)
				continue;

			if (s.teamUid == teamUid)
				result += s.numSoldiers;

		}
		return result;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SettlementController(ControllerManager controllerManager, SettlementsManager settlements, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mSettlementsManager = settlements;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();

		mAiController = (AiController) lControllerManager.getControllerByNameRequired(AiController.CONTROLLER_NAME, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mTeamController = (TeamController) lControllerManager.getControllerByNameRequired(TeamController.CONTROLLER_NAME, ConstantsGame.GAME_RESOURCE_GROUP_ID);

	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var lSettlement = lSettlements.get(i);

			if (lSettlement.isAssigned() == false)
				continue;

			lSettlement.update(core);
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void removeAllSettlements(int teamUid) {
		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var s = lSettlements.get(i);
			if (s.isAssigned() == false)
				continue;

			if (s.teamUid == teamUid)
				s.teamUid = TeamManager.CONTROLLED_NONE;

		}
	}

	public BaseSettlement getSettlementAtPosition(float worldX, float worldY, float radius) {
		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var lSettlement = lSettlements.get(i);

			if (lSettlement.isAssigned() == false)
				continue;

			if (CollisionExtensions.intersectsCircleCircle(lSettlement.x, lSettlement.y, lSettlement.radius, worldX, worldY, radius))
				return lSettlement;
		}

		return null;
	}

	public void attackSettlement(BaseSettlement settlement, Unit unit) {
		if (settlement.teamUid == unit.teamUid)
			return;

		// 1 soldier takes out 1 soldier
		// 1 soldier takes out 2 workers
		// 1 worker reduces 1 soldier to 2 workers
		// 1 worker takes out 1 worker

		var isAttackerASoldier = unit.unitTypeUid == UnitDefinitions.UNIT_SOLDIER_UID;
		var livesToExpend = isAttackerASoldier ? 3 : 1;

		// we are attacking a soldier
		if (settlement.numSoldiers > 0) {
			if (isAttackerASoldier) {
				// soldier => soldier
				settlement.numSoldiers--;
				livesToExpend = 0;
			} else {
				// worker => soldier
				// [old] settlement.numWorkers += 2;

				settlement.soldDmgCounter++;
				if (settlement.soldDmgCounter >= 3) {
					settlement.numSoldiers--;
					settlement.soldDmgCounter = 0;
				}

				livesToExpend = 0;
			}
		}

		if (livesToExpend > 0 && settlement.numWorkers > 0) {
			if (isAttackerASoldier) {
				// soldier => worker
				settlement.numWorkers -= 3;
				livesToExpend = 0;
			} else {
				// worker => worker
				settlement.numWorkers--;
				livesToExpend = 0;
			}
		}

		if (settlement.numWorkers < 0)
			settlement.numWorkers = 0;

		if (settlement.numSoldiers <= 0 && settlement.numWorkers <= 0) {
			takeOverSettlement(settlement, unit.teamUid);

			if (livesToExpend > 0) {
				if (unit.unitTypeUid == UnitDefinitions.UNIT_WORKER_UID) {
					settlement.numWorkers++;
				} else if (unit.unitTypeUid == UnitDefinitions.UNIT_SOLDIER_UID) {
					settlement.numSoldiers++;
				}
			}
		}
	}

	private void takeOverSettlement(BaseSettlement settlement, int teamUid) {
		if (settlement.teamUid == teamUid)
			return;

		settlement.teamUid = teamUid; // conquered
		settlement.soldDmgCounter = 0;

		final var lNewOwner = mTeamController.getTeamByUid(teamUid);
		if (lNewOwner.playerControlled) {
			// Try and remove
			if (settlement.btExecutor != null) {
				mAiController.removeBtExecutor(settlement);
			}
		}

		if (lNewOwner.playerControlled == false) {
			// Try and add
			mAiController.assignNewBtExecutor(settlement);
		}
	}

	public boolean getIsSettlementLowDanger(int settlementUid) {
		// TODO: Unimplemented method
		return false;
	}

	public boolean getIsSettlementHighDanger(int settlementUid) {
		// TODO: Unimplemented method
		return false;
	}

}
