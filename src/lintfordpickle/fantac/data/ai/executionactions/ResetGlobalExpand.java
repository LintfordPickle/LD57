// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 04/15/2024 08:31:43
// ******************************************************* 
package lintfordpickle.fantac.data.ai.executionactions;

import lintfordpickle.fantac.data.ai.ConstantsBtContext;
import lintfordpickle.fantac.data.teams.Team;

/** ExecutionAction class created from MMPM action ResetGlobalExpand. */
public class ResetGlobalExpand extends jbt.execution.task.leaf.action.ExecutionAction {

	/**
	 * Constructor. Constructs an instance of ResetGlobalExpand that is able to run a lintfordpickle.fantac.data.ai.modelactions.ResetGlobalExpand.
	 */
	public ResetGlobalExpand(lintfordpickle.fantac.data.ai.modelactions.ResetGlobalExpand modelTask, jbt.execution.core.BTExecutor executor, jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		final var lTeam = (Team) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_TEAM_OURS);
		lTeam.isExpanding = false;
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