package lintfordpickle.fantac.controllers;

import lintfordpickle.fantac.data.jobs.JobsManager;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;

public class JobsController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Jobs Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private JobsManager mJobsManager;

	private UnitsController mUnitsController;
	private SettlementsController mSettlementsController;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public JobsManager jobsManager() {
		return mJobsManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public JobsController(ControllerManager controllerManager, JobsManager jobsManager, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mJobsManager = jobsManager;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();
		mSettlementsController = (SettlementsController) lControllerManager.getControllerByNameRequired(SettlementsController.CONTROLLER_NAME, entityGroupUid());
		mUnitsController = (UnitsController) lControllerManager.getControllerByNameRequired(UnitsController.CONTROLLER_NAME, entityGroupUid());
	}

	@Override
	public boolean handleInput(LintfordCore core) {
		return super.handleInput(core);
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var lJobs = mJobsManager.instances();
		final var lNumJob = lJobs.size();
		for (int i = 0; i < lNumJob; i++) {
			final var lJobToProcess = lJobs.get(i);

			if (lJobToProcess.isAssigned() == false)
				continue;

			// lJobToProcess.update(core);
		}
	}

}
