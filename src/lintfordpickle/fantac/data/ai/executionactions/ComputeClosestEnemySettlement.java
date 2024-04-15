// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 04/15/2024 08:39:29
// ******************************************************* 
package lintfordpickle.fantac.data.ai.executionactions;

import lintfordpickle.fantac.controllers.SettlementController;
import lintfordpickle.fantac.data.ai.ConstantsBtContext;
import lintfordpickle.fantac.data.settlements.BaseSettlement;

/**
 * ExecutionAction class created from MMPM action ComputeClosestEnemySettlement.
 */
public class ComputeClosestEnemySettlement extends jbt.execution.task.leaf.action.ExecutionAction {

	/**
	 * Constructor. Constructs an instance of ComputeClosestEnemySettlement that is able to run a lintfordpickle.fantac.data.ai.modelactions.ComputeClosestEnemySettlement.
	 */
	public ComputeClosestEnemySettlement(lintfordpickle.fantac.data.ai.modelactions.ComputeClosestEnemySettlement modelTask, jbt.execution.core.BTExecutor executor, jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		final var lOurSettlement = (BaseSettlement) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_SETTLEMENT_OURS);
		final var lSettlementController = (SettlementController) getContext().getVariable(ConstantsBtContext.CONTEXT_SETTLEMENT_CONTROLLER);
		final var lUnoccupiedSettlement = lSettlementController.getClosestEnemySettlement(lOurSettlement.teamUid, lOurSettlement.x, lOurSettlement.y);

		if (lUnoccupiedSettlement != null) {

			getContext().setVariable(ConstantsBtContext.CONTEXT_VARS_MOVETROOPS_FROM_SETTLEMENT_UID, lOurSettlement.uid);
			getContext().setVariable(ConstantsBtContext.CONTEXT_VARS_MOVETROOPS_TO_SETTLEMENT_UID, lUnoccupiedSettlement.uid);

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