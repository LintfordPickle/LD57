// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 04/14/2024 20:35:42
// ******************************************************* 
package lintfordpickle.fantac.data.ai.modelactions;

/** ModelAction class created from MMPM action SetRequestPopToExpand. */
public class SetRequestPopToExpand extends
		jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of SetRequestPopToExpand. */
	public SetRequestPopToExpand(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a
	 * lintfordpickle.fantac.data.ai.executionactions.SetRequestPopToExpand task
	 * that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new lintfordpickle.fantac.data.ai.executionactions.SetRequestPopToExpand(
				this, executor, parent);
	}
}