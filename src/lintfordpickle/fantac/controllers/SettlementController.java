package lintfordpickle.fantac.controllers;

import org.lwjgl.glfw.GLFW;

import lintfordpickle.fantac.data.settlements.SettlementsManager;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;

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

			System.out.println("(" + lSettlement.uid + ") num workers: " + lSettlement.numWorkers);
		}
	}

}
