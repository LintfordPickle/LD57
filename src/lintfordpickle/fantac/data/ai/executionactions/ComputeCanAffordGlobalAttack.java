// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 04/14/2024 14:45:12
// ******************************************************* 
package lintfordpickle.fantac.data.ai.executionactions;

import lintfordpickle.fantac.data.ai.ConstantsBtContext;
import lintfordpickle.fantac.data.teams.Team;

/** ExecutionAction class created from MMPM action ComputeCanAffordGlobalAttack. */
public class ComputeCanAffordGlobalAttack extends jbt.execution.task.leaf.action.ExecutionAction {

	/**
	 * Constructor. Constructs an instance of ComputeCanAffordGlobalAttack that is able to run a lintfordpickle.fantac.data.ai.modelactions.ComputeCanAffordGlobalAttack.
	 */
	public ComputeCanAffordGlobalAttack(lintfordpickle.fantac.data.ai.modelactions.ComputeCanAffordGlobalAttack modelTask, jbt.execution.core.BTExecutor executor, jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);

		// TODO: Actually do something

		getContext().setVariable(ConstantsBtContext.GLOBAL_ATTACK, true);
		final var lOurTeam = (Team) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_TEAM_OURS);
		lOurTeam.isAttacking = true;

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