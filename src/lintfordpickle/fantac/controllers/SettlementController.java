package lintfordpickle.fantac.controllers;

import org.lwjgl.glfw.GLFW;

import lintfordpickle.fantac.data.settlements.BaseSettlement;
import lintfordpickle.fantac.data.settlements.SettlementsManager;
import lintfordpickle.fantac.data.teams.TeamManager;
import lintfordpickle.fantac.data.units.Unit;
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
		final var lSettlements = mSettlementsManager.instances();
		final var lNumSettlements = lSettlements.size();
		for (int i = 0; i < lNumSettlements; i++) {
			if (lSettlements.get(i).isAssigned() == false)
				continue;

			// TODO: unimplmented method

			if (lSettlements.get(i).teamUid == TeamManager.CONTROLLED_NONE)
				return lSettlements.get(i);

		}
		return null;
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

		if (settlement.numSoldiers > 0) {
			// atacking soldiers
			settlement.numSoldiers--;
		} else {
			// attacking workers
			settlement.numWorkers--;
		}

		if (settlement.numSoldiers <= 0 && settlement.numWorkers <= 0) {
			settlement.teamUid = unit.teamUid; // conquered
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
