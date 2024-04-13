package lintfordpickle.fantac.controllers;

import lintfordpickle.fantac.data.jobs.JobsManager;
import lintfordpickle.fantac.data.settlements.BaseSettlement;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.input.mouse.IInputProcessor;
import net.lintfordlib.core.maths.RandomNumbers;

public class JobsController extends BaseController implements IInputProcessor {

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

	private float mMouseCooldownTimer;
	private boolean mProcessingInput;
	private BaseSettlement mFoundFromSettlement;
	private BaseSettlement mFoundToSettlement;

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
		if (core.input().mouse().tryAcquireMouseLeftClickTimed(hashCode(), this)) {
			final var lMouseX = core.gameCamera().getMouseWorldSpaceX();
			final var lMouseY = core.gameCamera().getMouseWorldSpaceY();

			mFoundFromSettlement = mSettlementsController.getSettlementAtPosition(lMouseX, lMouseY, 4);
			if (mFoundFromSettlement != null) {
				mProcessingInput = true;

				return true;
			}
		}

		if (mProcessingInput) {
			if (core.input().mouse().isMouseLeftButtonDown() == false) {
				// check if a settlement was selected (under the mouse)
				final var lMouseX = core.gameCamera().getMouseWorldSpaceX();
				final var lMouseY = core.gameCamera().getMouseWorldSpaceY();

				mFoundToSettlement = mSettlementsController.getSettlementAtPosition(lMouseX, lMouseY, 4);
				if (mFoundToSettlement != null) {

					if (mFoundFromSettlement != mFoundToSettlement)
						sendArmy(mFoundFromSettlement.teamUid, mFoundFromSettlement, mFoundToSettlement);
				}

				mProcessingInput = false;
				mFoundFromSettlement = null;
				mFoundToSettlement = null;

			} else {
				// Still working on it
			}
		}

		return super.handleInput(core);
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		if (mMouseCooldownTimer > 0.f)
			mMouseCooldownTimer -= core.gameTime().elapsedTimeMilli();

		final var lJobs = mJobsManager.instances();
		final var lNumJobs = lJobs.size();

		for (int i = 0; i < lNumJobs; i++) {
			final var lJobToProcess = lJobs.get(i);

			if (lJobToProcess.isAssigned() == false)
				continue;

			lJobToProcess.deployTimer += core.gameTime().elapsedTimeMilli();
			final float emitTimeMs = 5;
			if (lJobToProcess.numUnits > 0 && lJobToProcess.deployTimer > emitTimeMs) {
				final var lFrom = lJobToProcess.fromSettlement;
				final var lTo = lJobToProcess.toSettlement;

				final var launchFor = .01f;
				final var rvx = RandomNumbers.random(-(float) Math.PI, (float) Math.PI);
				final var rvy = RandomNumbers.random(-(float) Math.PI, (float) Math.PI);

				mUnitsController.unitsManager().addNewUnit(lJobToProcess.teamUid, lFrom, lTo, lFrom.x, lFrom.y, rvx * launchFor, rvy * launchFor);

				lJobToProcess.deployTimer = 0;
				lJobToProcess.numUnits--;
			}

			if (lJobToProcess.numUnits == 0)
				lJobToProcess.setFree();
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	private void sendArmy(int teamUid, BaseSettlement from, BaseSettlement to) {
		final var lNewJob = mJobsManager.getFreeInstanceItem();

		// Get and reduce the settlement numbers
		final var lTotalWorkers = from.numWorkers / 2;
		from.numWorkers -= lTotalWorkers;

		lNewJob.initialise(teamUid, from, to, lTotalWorkers);
	}

	// --------------------------------------
	// Inherited-Methods
	// --------------------------------------

	@Override
	public boolean isCoolDownElapsed() {
		return mMouseCooldownTimer <= 0.f;
	}

	@Override
	public void resetCoolDownTimer(float cooldownInMs) {
		mMouseCooldownTimer = cooldownInMs;
	}

	@Override
	public boolean allowKeyboardInput() {
		return false;
	}

	@Override
	public boolean allowGamepadInput() {
		return false;
	}

	@Override
	public boolean allowMouseInput() {
		return true;
	}

}
