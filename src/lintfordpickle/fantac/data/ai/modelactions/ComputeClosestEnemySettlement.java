// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 04/15/2024 08:39:29
// ******************************************************* 
package lintfordpickle.fantac.data.ai.modelactions;

/** ModelAction class created from MMPM action ComputeClosestEnemySettlement. */
public class ComputeClosestEnemySettlement extends
		jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of ComputeClosestEnemySettlement. */
	public ComputeClosestEnemySettlement(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a lintfordpickle.fantac.data.ai.executionactions.
	 * ComputeClosestEnemySettlement task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new lintfordpickle.fantac.data.ai.executionactions.ComputeClosestEnemySettlement(
				this, executor, parent);
	}
}