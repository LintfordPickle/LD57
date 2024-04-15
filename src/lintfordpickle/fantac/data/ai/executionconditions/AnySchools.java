// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 04/14/2024 21:48:49
// ******************************************************* 
package lintfordpickle.fantac.data.ai.executionconditions;

import lintfordpickle.fantac.controllers.SettlementController;
import lintfordpickle.fantac.data.ai.ConstantsBtContext;
import lintfordpickle.fantac.data.settlements.BaseSettlement;
import lintfordpickle.fantac.data.settlements.SettlementType;
import lintfordpickle.fantac.data.teams.Team;

/** ExecutionCondition class created from MMPM condition AnySchools. */
public class AnySchools extends jbt.execution.task.leaf.condition.ExecutionCondition {

	/**
	 * Constructor. Constructs an instance of AnySchools that is able to run a lintfordpickle.fantac.data.ai.modelconditions.AnySchools.
	 */
	public AnySchools(lintfordpickle.fantac.data.ai.modelconditions.AnySchools modelTask, jbt.execution.core.BTExecutor executor, jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {

		final var team = (Team) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_TEAM_OURS);
		final var settlement = (BaseSettlement) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_SETTLEMENT_OURS);

		final var settlementController = (SettlementController) getContext().getVariable(ConstantsBtContext.CONTEXT_SETTLEMENT_CONTROLLER);

		final var lNumCastles = settlementController.getTotalSettlementOfType(team.teamUid, SettlementType.SETTLEMENT_TYPE_CASTLE);
		final var lNumSchools = settlementController.getTotalSettlementOfType(team.teamUid, SettlementType.SETTLEMENT_TYPE_PENTAGRAM);

		if (lNumCastles > 0 || lNumSchools > 0) {
			final var lClosestSchool = settlementController.getOurClosestSchool(team.teamUid, settlement.x, settlement.y);
			getContext().setVariable(ConstantsBtContext.CONTEXT_VARS_MOVETROOPS_TO_SETTLEMENT_UID, lClosestSchool.uid);
			return jbt.execution.core.ExecutionTask.Status.SUCCESS;
		}

		return jbt.execution.core.ExecutionTask.Status.FAILURE;
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