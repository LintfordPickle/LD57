// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 04/14/2024 21:48:49
// ******************************************************* 
package lintfordpickle.fantac.data.ai.modelconditions;

/** ModelCondition class created from MMPM condition AnySchools. */
public class AnySchools extends jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of AnySchools. */
	public AnySchools(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a lintfordpickle.fantac.data.ai.executionconditions.AnySchools
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new lintfordpickle.fantac.data.ai.executionconditions.AnySchools(
				this, executor, parent);
	}
}