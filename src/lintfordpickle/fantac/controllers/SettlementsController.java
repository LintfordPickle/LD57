package lintfordpickle.fantac.controllers;

import org.lwjgl.glfw.GLFW;

import lintfordpickle.fantac.data.settlements.BaseSettlement;
import lintfordpickle.fantac.data.settlements.SettlementsManager;
import lintfordpickle.fantac.data.units.Unit;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.maths.CollisionExtensions;

public class SettlementsController extends BaseController {

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

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SettlementsController(ControllerManager controllerManager, SettlementsManager settlements, int entityGroupUid) {
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
			settlement.numWorkers--;
		} else {
			// attacking workers
			settlement.numWorkers--;
		}

		if (settlement.numSoldiers <= 0 && settlement.numWorkers <= 0) {
			settlement.teamUid = unit.teamUid; // conquered
		}
	}
}
