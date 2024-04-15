// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 04/15/2024 08:28:37
// ******************************************************* 
package lintfordpickle.fantac.data.ai.modelconditions;

/** ModelCondition class created from MMPM condition CheckGlobalAttackOverride. */
public class CheckGlobalAttackOverride extends
		jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of CheckGlobalAttackOverride. */
	public CheckGlobalAttackOverride(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a lintfordpickle.fantac.data.ai.executionconditions.
	 * CheckGlobalAttackOverride task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new lintfordpickle.fantac.data.ai.executionconditions.CheckGlobalAttackOverride(
				this, executor, parent);
	}
}