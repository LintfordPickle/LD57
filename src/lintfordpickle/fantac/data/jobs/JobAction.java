package lintfordpickle.fantac.data.jobs;

import net.lintfordlib.core.entities.instances.ClosedPooledBaseData;

// This is just the move job. The actual movement is done later by the individual units.

public class JobAction extends ClosedPooledBaseData {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	public boolean isComplete;

	public int fromSettlementUid;
	public int toSettlementUid;

	public int teamUid;
	public int numUnits;

	// --------------------------------------
	// Properties
	// --------------------------------------

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public JobAction(int uid) {
		super(uid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void initialise(int teamUid, int fromUid, int toUid, int numUnits) {
		fromSettlementUid = fromUid;
		toSettlementUid = toUid;

		this.teamUid = teamUid;
		this.numUnits = numUnits;

		isComplete = false;

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

}
