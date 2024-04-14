// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 04/14/2024 15:00:06
// ******************************************************* 
package lintfordpickle.fantac.data.ai.modelconditions;

/** ModelCondition class created from MMPM condition CheckGlobalExpand. */
public class CheckGlobalExpand extends
		jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of CheckGlobalExpand. */
	public CheckGlobalExpand(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a
	 * lintfordpickle.fantac.data.ai.executionconditions.CheckGlobalExpand task
	 * that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new lintfordpickle.fantac.data.ai.executionconditions.CheckGlobalExpand(
				this, executor, parent);
	}
}