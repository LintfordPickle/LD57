package lintfordpickle.fantac.controllers;

import lintfordpickle.fantac.data.jobs.JobsManager;
import lintfordpickle.fantac.data.settlements.BaseSettlement;
import lintfordpickle.fantac.data.teams.TeamRace;
import lintfordpickle.fantac.data.units.UnitDefinitions;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.input.mouse.IInputProcessor;
import net.lintfordlib.core.maths.RandomNumbers;

public class JobController extends BaseController implements IInputProcessor {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Jobs Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private JobsManager mJobsManager;

	private TeamController mTeamController;
	private UnitController mUnitController;
	private SettlementController mSettlementController;

	private float mMouseCooldownTimer;
	private boolean mProcessingInput;
	private int mProcessingArmyUnitType;
	private BaseSettlement mFoundFromSettlement;
	private BaseSettlement mFoundToSettlement;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public JobsManager jobsManager() {
		return mJobsManager;
	}

	public int getTotalWorkers(int teamUid) {
		var result = 0;
		final var lJobInstances = mJobsManager.instances();
		final var lNumJobs = lJobInstances.size();
		for (int i = 0; i < lNumJobs; i++) {
			final var s = lJobInstances.get(i);
			if (s.isAssigned() == false)
				continue;

			if (s.teamUid == teamUid && s.unitType == UnitDefinitions.UNIT_WORKER_UID)
				result += s.numUnits;

		}
		return result;
	}

	public int getTotalSoldiers(int teamUid) {
		var result = 0;
		final var lJobInstances = mJobsManager.instances();
		final var lNumJobs = lJobInstances.size();
		for (int i = 0; i < lNumJobs; i++) {
			final var s = lJobInstances.get(i);
			if (s.isAssigned() == false)
				continue;

			if (s.teamUid == teamUid && s.unitType == UnitDefinitions.UNIT_SOLDIER_UID)
				result += s.numUnits;

		}
		return result;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public JobController(ControllerManager controllerManager, JobsManager jobsManager, int entityGroupUid) {
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

		mSettlementController = (SettlementController) lControllerManager.getControllerByNameRequired(SettlementController.CONTROLLER_NAME, entityGroupUid());
		mUnitController = (UnitController) lControllerManager.getControllerByNameRequired(UnitController.CONTROLLER_NAME, entityGroupUid());
		mTeamController = (TeamController) lControllerManager.getControllerByNameRequired(TeamController.CONTROLLER_NAME, entityGroupUid());
	}

	@Override
	public boolean handleInput(LintfordCore core) {
		if (!mProcessingInput && core.input().mouse().tryAcquireMouseLeftClickTimed(hashCode(), this)) {
			mProcessingArmyUnitType = UnitDefinitions.UNIT_WORKER_UID;

			final var lMouseX = core.gameCamera().getMouseWorldSpaceX();
			final var lMouseY = core.gameCamera().getMouseWorldSpaceY();

			mFoundFromSettlement = mSettlementController.getSettlementAtPosition(lMouseX, lMouseY, 4);
			if (mFoundFromSettlement != null) {
				final var lOwner = mTeamController.getTeamByUid(mFoundFromSettlement.teamUid);
				if (lOwner != null && lOwner.playerControlled) {
					mProcessingInput = true;
					return true;
				}
			}
		}

		if (!mProcessingInput && core.input().mouse().tryAcquireMouseRightClickTimed(hashCode(), this)) {
			mProcessingArmyUnitType = UnitDefinitions.UNIT_SOLDIER_UID;

			final var lMouseX = core.gameCamera().getMouseWorldSpaceX();
			final var lMouseY = core.gameCamera().getMouseWorldSpaceY();

			mFoundFromSettlement = mSettlementController.getSettlementAtPosition(lMouseX, lMouseY, 4);
			if (mFoundFromSettlement != null) {
				mProcessingInput = true;

				return true;
			}
		}

		if (mProcessingInput) {
			if (core.input().mouse().isMouseLeftButtonDown() == false && core.input().mouse().isMouseRightButtonDown() == false) {
				// check if a settlement was selected (under the mouse)
				final var lMouseX = core.gameCamera().getMouseWorldSpaceX();
				final var lMouseY = core.gameCamera().getMouseWorldSpaceY();

				mFoundToSettlement = mSettlementController.getSettlementAtPosition(lMouseX, lMouseY, 4);
				if (mFoundToSettlement != null) {

					// TODO: This isn't using the correct race - we are just assuming the player is playing as demons
					if (mFoundFromSettlement != mFoundToSettlement) {
						sendArmy(mFoundFromSettlement.teamUid, TeamRace.RACE_DEMONS, mProcessingArmyUnitType, mFoundFromSettlement, mFoundToSettlement);
					}
				}

				mProcessingInput = false;
				mFoundFromSettlement = null;
				mFoundToSettlement = null;
				mProcessingArmyUnitType = -1;

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

				mUnitController.unitsManager().addNewUnit(lJobToProcess.teamUid, lJobToProcess.raceUid, lJobToProcess.unitType, lFrom, lTo, lFrom.x, lFrom.y, rvx * launchFor, rvy * launchFor, lJobToProcess.spd);

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

	// initalises a new move job. instantly reduces settlement pop. count, but slowly releases units over time.
	public void sendArmy(int teamUid, int raceUid, int unitType, BaseSettlement from, BaseSettlement to) {
		if (from == null || to == null)
			return;

		if (from.uid == to.uid)
			return; // TODO: One of the AI paths tries to send (soldiers) units to itself.

		final var lNewJob = mJobsManager.getFreeInstanceItem();
		final var spd = UnitDefinitions.getUnitDefByUid(unitType).speed;

		int numToSend = 0;

		switch (unitType) {
		case UnitDefinitions.UNIT_WORKER_UID:
			numToSend = from.numWorkers / 2;
			from.numWorkers -= numToSend;
			break;
		case UnitDefinitions.UNIT_SOLDIER_UID:
			numToSend = from.numSoldiers / 2;
			from.numSoldiers -= numToSend;
			break;
		}

		lNewJob.initialise(teamUid, raceUid, unitType, from, to, numToSend, spd);
	}

	public void removeAllJobs(int teamUid) {
		// TODO: Unimplemented method
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
