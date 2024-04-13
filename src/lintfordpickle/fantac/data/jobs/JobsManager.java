package lintfordpickle.fantac.data.jobs;

import net.lintfordlib.core.entities.instances.EnclosedInstanceManager;

public class JobsManager extends EnclosedInstanceManager<JobAction> {

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public JobsManager() {

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	protected JobAction createPoolObjectInstance() {
		return new JobAction(getNewInstanceUID());
	}
}
