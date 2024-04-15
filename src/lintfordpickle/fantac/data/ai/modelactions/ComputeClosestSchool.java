// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 04/15/2024 21:27:19
// ******************************************************* 
package lintfordpickle.fantac.data.ai.modelactions;

/** ModelAction class created from MMPM action ComputeClosestSchool. */
public class ComputeClosestSchool extends
		jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of ComputeClosestSchool. */
	public ComputeClosestSchool(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a
	 * lintfordpickle.fantac.data.ai.executionactions.ComputeClosestSchool task
	 * that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new lintfordpickle.fantac.data.ai.executionactions.ComputeClosestSchool(
				this, executor, parent);
	}
}