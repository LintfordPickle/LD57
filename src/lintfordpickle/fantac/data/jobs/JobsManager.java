package lintfordpickle.fantac.data.jobs;

import net.lintfordlib.core.entities.instances.EnclosedInstanceManager;

public class JobsManager extends EnclosedInstanceManager<JobAction> {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	// --------------------------------------
	// Properties
	// --------------------------------------

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public JobsManager() {

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void startNewJob(int teamUid, int fromUid, int toUid, int numUnits) {
		final var lJobInstance = getFreeInstanceItem();
		lJobInstance.initialise(teamUid, fromUid, toUid, numUnits);
	}

	@Override
	protected JobAction createPoolObjectInstance() {
		return new JobAction(getNewInstanceUID());
	}
}
