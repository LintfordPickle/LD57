package lintfordpickle.fantac.data.jobs;

import lintfordpickle.fantac.data.settlements.BaseSettlement;
import net.lintfordlib.core.entities.instances.ClosedPooledBaseData;

// This is just the move job. The actual movement is done later by the individual units.

public class JobAction extends ClosedPooledBaseData {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public boolean isComplete;

	public BaseSettlement fromSettlement;
	public BaseSettlement toSettlement;

	public int teamUid;
	public int unitType;
	public int raceUid;
	public int numUnits;

	public float deployTimer;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public JobAction(int uid) {
		super(uid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void initialise(int teamUid, int raceUid, int unitType, BaseSettlement from, BaseSettlement to, int numUnits) {
		fromSettlement = from;
		toSettlement = to;

		this.teamUid = teamUid;
		this.raceUid = raceUid;
		this.unitType = unitType;
		this.numUnits = numUnits;

		isComplete = false;

	}
}
