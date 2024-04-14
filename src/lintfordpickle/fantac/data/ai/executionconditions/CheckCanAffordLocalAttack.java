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
 * ExecutionCondition class created from MMPM condition CheckCanAffordLocalAttack.
 */
public class CheckCanAffordLocalAttack extends jbt.execution.task.leaf.condition.ExecutionCondition {

	/**
	 * Constructor. Constructs an instance of CheckCanAffordLocalAttack that is able to run a lintfordpickle.fantac.data.ai.modelconditions.CheckCanAffordLocalAttack.
	 */
	public CheckCanAffordLocalAttack(lintfordpickle.fantac.data.ai.modelconditions.CheckCanAffordLocalAttack modelTask, jbt.execution.core.BTExecutor executor, jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {

		final var lSettlement = (BaseSettlement) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_SETTLEMENT_OURS);
		if (lSettlement == null)
			return jbt.execution.core.ExecutionTask.Status.FAILURE;

		// TODO: Hard coded number - should be based on attack aggression factor

		if (lSettlement.numSoldiers > 4)
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