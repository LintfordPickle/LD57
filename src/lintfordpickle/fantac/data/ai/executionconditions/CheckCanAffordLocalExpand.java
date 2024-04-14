// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 04/14/2024 15:02:25
// ******************************************************* 
package lintfordpickle.fantac.data.ai.executionconditions;

import lintfordpickle.fantac.data.ai.ConstantsBtContext;
import lintfordpickle.fantac.data.settlements.BaseSettlement;

/**
 * ExecutionCondition class created from MMPM condition CheckCanAffordLocalExpand.
 */
public class CheckCanAffordLocalExpand extends jbt.execution.task.leaf.condition.ExecutionCondition {

	/**
	 * Constructor. Constructs an instance of CheckCanAffordLocalExpand that is able to run a lintfordpickle.fantac.data.ai.modelconditions.CheckCanAffordLocalExpand.
	 */
	public CheckCanAffordLocalExpand(lintfordpickle.fantac.data.ai.modelconditions.CheckCanAffordLocalExpand modelTask, jbt.execution.core.BTExecutor executor, jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {

		final var lSettlement = (BaseSettlement) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_SETTLEMENT_OURS);
		if (lSettlement == null)
			return jbt.execution.core.ExecutionTask.Status.FAILURE;

		// TODO: Hard coded number - should be based on expansion aggression factor

		final var lNumSoldiers = lSettlement.numSoldiers;
		final var lNumWorkers = lSettlement.numWorkers;

		if (lNumWorkers - lNumSoldiers >= lSettlement.numSoldiers)
			return jbt.execution.core.ExecutionTask.Status.SUCCESS;

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