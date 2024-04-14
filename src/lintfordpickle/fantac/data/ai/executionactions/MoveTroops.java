// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 04/14/2024 14:45:12
// ******************************************************* 
package lintfordpickle.fantac.data.ai.executionactions;

import lintfordpickle.fantac.controllers.JobController;
import lintfordpickle.fantac.controllers.SettlementController;
import lintfordpickle.fantac.data.ai.ConstantsBtContext;
import lintfordpickle.fantac.data.settlements.BaseSettlement;
import lintfordpickle.fantac.data.teams.Team;
import lintfordpickle.fantac.data.units.UnitDefinitions;

/** ExecutionAction class created from MMPM action MoveTroops. */
public class MoveTroops extends jbt.execution.task.leaf.action.ExecutionAction {
	/**
	 * Value of the parameter "fromSettlementUid" in case its value is specified at construction time. null otherwise.
	 */
	private java.lang.Integer fromSettlementUid;
	/**
	 * Location, in the context, of the parameter "fromSettlementUid" in case its value is not specified at construction time. null otherwise.
	 */
	private java.lang.String fromSettlementUidLoc;
	/**
	 * Value of the parameter "toSettlementUid" in case its value is specified at construction time. null otherwise.
	 */
	private java.lang.Integer toSettlementUid;
	/**
	 * Location, in the context, of the parameter "toSettlementUid" in case its value is not specified at construction time. null otherwise.
	 */
	private java.lang.String toSettlementUidLoc;
	/**
	 * Value of the parameter "numUnits" in case its value is specified at construction time. null otherwise.
	 */
	private java.lang.Integer numUnits;
	/**
	 * Location, in the context, of the parameter "numUnits" in case its value is not specified at construction time. null otherwise.
	 */
	private java.lang.String numUnitsLoc;
	/**
	 * Value of the parameter "unitType" in case its value is specified at construction time. null otherwise.
	 */
	private java.lang.Integer unitType;
	/**
	 * Location, in the context, of the parameter "unitType" in case its value is not specified at construction time. null otherwise.
	 */
	private java.lang.String unitTypeLoc;

	/**
	 * Constructor. Constructs an instance of MoveTroops that is able to run a lintfordpickle.fantac.data.ai.modelactions.MoveTroops.
	 * 
	 * @param fromSettlementUid    value of the parameter "fromSettlementUid", or null in case it should be read from the context. If null, <code>fromSettlementUidLoc<code> cannot be null.
	 * @param fromSettlementUidLoc
	 *            in case <code>fromSettlementUid</code> is null, this variable represents the place in the context where the parameter's value will be retrieved from.
	 * @param toSettlementUid      value of the parameter "toSettlementUid", or null in case it should be read from the context. If null, <code>toSettlementUidLoc<code> cannot be null.
	 * @param toSettlementUidLoc
	 *            in case <code>toSettlementUid</code> is null, this variable represents the place in the context where the parameter's value will be retrieved from.
	 * @param numUnits             value of the parameter "numUnits", or null in case it should be read from the context. If null, <code>numUnitsLoc<code> cannot be null.
	 * @param numUnitsLoc
	 *            in case <code>numUnits</code> is null, this variable represents the place in the context where the parameter's value will be retrieved from.
	 * @param unitType             value of the parameter "unitType", or null in case it should be read from the context. If null, <code>unitTypeLoc<code> cannot be null.
	 * @param unitTypeLoc
	 *            in case <code>unitType</code> is null, this variable represents the place in the context where the parameter's value will be retrieved from.
	 */
	public MoveTroops(lintfordpickle.fantac.data.ai.modelactions.MoveTroops modelTask, jbt.execution.core.BTExecutor executor, jbt.execution.core.ExecutionTask parent, java.lang.Integer fromSettlementUid, java.lang.String fromSettlementUidLoc, java.lang.Integer toSettlementUid, java.lang.String toSettlementUidLoc,
			java.lang.Integer numUnits, java.lang.String numUnitsLoc, java.lang.Integer unitType, java.lang.String unitTypeLoc) {
		super(modelTask, executor, parent);

		this.fromSettlementUid = fromSettlementUid;
		this.fromSettlementUidLoc = fromSettlementUidLoc;
		this.toSettlementUid = toSettlementUid;
		this.toSettlementUidLoc = toSettlementUidLoc;
		this.numUnits = numUnits;
		this.numUnitsLoc = numUnitsLoc;
		this.unitType = unitType;
		this.unitTypeLoc = unitTypeLoc;
	}

	/**
	 * Returns the value of the parameter "fromSettlementUid", or null in case it has not been specified or it cannot be found in the context.
	 */
	public java.lang.Integer getFromSettlementUid() {
		if (this.fromSettlementUid != null) {
			return this.fromSettlementUid;
		} else {
			return (java.lang.Integer) this.getContext().getVariable(this.fromSettlementUidLoc);
		}
	}

	/**
	 * Returns the value of the parameter "toSettlementUid", or null in case it has not been specified or it cannot be found in the context.
	 */
	public java.lang.Integer getToSettlementUid() {
		if (this.toSettlementUid != null) {
			return this.toSettlementUid;
		} else {
			return (java.lang.Integer) this.getContext().getVariable(this.toSettlementUidLoc);
		}
	}

	/**
	 * Returns the value of the parameter "numUnits", or null in case it has not been specified or it cannot be found in the context.
	 */
	public java.lang.Integer getNumUnits() {
		if (this.numUnits != null) {
			return this.numUnits;
		} else {
			return (java.lang.Integer) this.getContext().getVariable(this.numUnitsLoc);
		}
	}

	/**
	 * Returns the value of the parameter "unitType", or null in case it has not been specified or it cannot be found in the context.
	 */
	public java.lang.Integer getUnitType() {
		if (this.unitType != null) {
			return this.unitType;
		} else {
			return (java.lang.Integer) this.getContext().getVariable(this.unitTypeLoc);
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);

		final var lOurTeam = (Team) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_TEAM_OURS);
		final var lOurSettlement = (BaseSettlement) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_SETTLEMENT_OURS);

		final var lJobController = (JobController) getContext().getVariable(ConstantsBtContext.CONTEXT_JOBACTION_CONTROLLER);
		final var lSettlementController = (SettlementController) getContext().getVariable(ConstantsBtContext.CONTEXT_SETTLEMENT_CONTROLLER);

		final var lGoToSettlementUid = getToSettlementUid();
		final var lToSettlement = lSettlementController.getSettlementByUid(lGoToSettlementUid);

		lJobController.sendArmy(lOurTeam.teamUid, lOurTeam.raceUid, UnitDefinitions.UNIT_WORKER_UID, lOurSettlement, lToSettlement);
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {

		// sendArmy is instant

		return jbt.execution.core.ExecutionTask.Status.SUCCESS;
	}

	protected void internalTerminate() {
	}

	protected void restoreState(jbt.execution.core.ITaskState state) {
	}

	protected jbt.execution.core.ITaskState storeState() {
		return null;
	}

	protected jbt.execution.core.ITaskState storeTerminationState() {
		return null;
	}
}