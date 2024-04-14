// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 04/14/2024 15:02:25
// ******************************************************* 
package lintfordpickle.fantac.data.ai.executionconditions;

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

		System.out.println(this.getClass().getCanonicalName() + " spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
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