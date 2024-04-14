// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 04/14/2024 20:35:42
// ******************************************************* 
package lintfordpickle.fantac.data.ai.executionconditions;

import lintfordpickle.fantac.controllers.SettlementController;
import lintfordpickle.fantac.data.ai.ConstantsBtContext;
import lintfordpickle.fantac.data.teams.Team;

/**
 * ExecutionCondition class created from MMPM condition CheckRequestPopAttackSet.
 */
public class CheckRequestPopAttackSet extends jbt.execution.task.leaf.condition.ExecutionCondition {

	/**
	 * Constructor. Constructs an instance of CheckRequestPopAttackSet that is able to run a lintfordpickle.fantac.data.ai.modelconditions.CheckRequestPopAttackSet.
	 */
	public CheckRequestPopAttackSet(lintfordpickle.fantac.data.ai.modelconditions.CheckRequestPopAttackSet modelTask, jbt.execution.core.BTExecutor executor, jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		final var lTeam = (Team) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_TEAM_OURS);

		final var lSettlementUidToExpand = lTeam.requestPopExpandSettlementUid;
		if (lSettlementUidToExpand == -1) {
			return jbt.execution.core.ExecutionTask.Status.FAILURE;
		}

		final var settlementController = (SettlementController) getContext().getVariable(ConstantsBtContext.CONTEXT_SETTLEMENT_CONTROLLER);
		final var settlementToExpand = settlementController.getSettlementByUid(lSettlementUidToExpand);

		if (settlementToExpand != null) {
			getContext().setVariable(ConstantsBtContext.CONTEXT_VARS_MOVETROOPS_TO_SETTLEMENT_UID, settlementToExpand.uid);
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