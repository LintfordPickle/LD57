// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 04/14/2024 20:35:42
// ******************************************************* 
package lintfordpickle.fantac.data.ai.executionactions;

/** ExecutionAction class created from MMPM action SetRequestPopToExpand. */
public class SetRequestPopToExpand extends jbt.execution.task.leaf.action.ExecutionAction {

	/**
	 * Constructor. Constructs an instance of SetRequestPopToExpand that is able to run a lintfordpickle.fantac.data.ai.modelactions.SetRequestPopToExpand.
	 */
	public SetRequestPopToExpand(lintfordpickle.fantac.data.ai.modelactions.SetRequestPopToExpand modelTask, jbt.execution.core.BTExecutor executor, jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
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