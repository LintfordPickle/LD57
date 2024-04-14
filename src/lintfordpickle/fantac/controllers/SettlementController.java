package lintfordpickle.fantac.controllers;

import org.lwjgl.glfw.GLFW;

import lintfordpickle.fantac.data.settlements.BaseSettlement;
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

	// --------------------------------------
	// Properties
	// --------------------------------------

	public SettlementsManager settlementsManager() {
		return mSettlementsManager;
	}

	public int getBuildingCount(int teamUid) {
		int result = 0;
		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			if (lSettlements.get(i).isAssigned() == false)
				continue;

			if (lSettlements.get(i).teamUid == teamUid)
				result++;

		}
		return result;
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

	public BaseSettlement getWeakestEnemySettlement(int ourTeamUid) {
		var result = (BaseSettlement) null;
		int currentWeakest = Integer.MAX_VALUE;
		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var s = lSettlements.get(i);
			if (s.isAssigned() == false)
				continue;

			// TOOD: Incomplete

			if (s.teamUid != ourTeamUid && s.numWorkers < currentWeakest) {
				result = s;
				currentWeakest = s.numWorkers;
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
	public void update(LintfordCore core) {
		super.update(core);

		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			final var lSettlement = lSettlements.get(i);

			if (lSettlement.isAssigned() == false)
				continue;

			lSettlement.update(core);

			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_G))
				lSettlement.reset();
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

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

		// TODO: Attacking / Defending calcs

		boolean deducted = false;
		if (settlement.numSoldiers > 0) {
			// atacking soldiers
			settlement.numSoldiers--;
			deducted = true;
		} else if (settlement.numWorkers > 0) {
			// attacking workers
			settlement.numWorkers--;
			deducted = true;
		}

		if (settlement.numSoldiers <= 0 && settlement.numWorkers <= 0) {
			settlement.teamUid = unit.teamUid; // conquered

			if (deducted == false) {
				if (unit.unitTypeUid == UnitDefinitions.UNIT_WORKER_UID) {
					settlement.numWorkers++;
				} else if (unit.unitTypeUid == UnitDefinitions.UNIT_SOLDIER_UID) {
					settlement.numSoldiers++;
				}
			}

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
