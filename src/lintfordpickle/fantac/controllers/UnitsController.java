package lintfordpickle.fantac.controllers;

import lintfordpickle.fantac.data.units.UnitsManager;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;

public class UnitsController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Units Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private UnitsManager mUnitsManager;

	private SettlementsController mSettlementController;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public UnitsManager unitsManager() {
		return mUnitsManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public UnitsController(ControllerManager controllerManager, UnitsManager unitManager, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mUnitsManager = unitManager;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();
		mSettlementController = (SettlementsController) lControllerManager.getControllerByNameRequired(SettlementsController.CONTROLLER_NAME, entityGroupUid());
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var lUnitInstances = mUnitsManager.unitsInField;
		final var lNumUnits = lUnitInstances.size();
		for (int i = 0; i < lNumUnits; i++) {
			final var lUnit = lUnitInstances.get(i);

			// move unit towards target

			// process on destination reached
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

}
