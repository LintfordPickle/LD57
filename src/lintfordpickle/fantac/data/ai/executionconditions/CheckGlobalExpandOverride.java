// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 04/15/2024 08:28:37
// ******************************************************* 
package lintfordpickle.fantac.data.ai.executionconditions;

import lintfordpickle.fantac.data.ai.ConstantsBtContext;
import lintfordpickle.fantac.data.settlements.BaseSettlement;
import lintfordpickle.fantac.data.teams.Team;

/**
 * ExecutionCondition class created from MMPM condition CheckGlobalExpandOverride.
 */
public class CheckGlobalExpandOverride extends jbt.execution.task.leaf.condition.ExecutionCondition {

	/**
	 * Constructor. Constructs an instance of CheckGlobalExpandOverride that is able to run a lintfordpickle.fantac.data.ai.modelconditions.CheckGlobalExpandOverride.
	 */
	public CheckGlobalExpandOverride(lintfordpickle.fantac.data.ai.modelconditions.CheckGlobalExpandOverride modelTask, jbt.execution.core.BTExecutor executor, jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		final var lTeam = (Team) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_TEAM_OURS);
		final var lSettlement = (BaseSettlement) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_SETTLEMENT_OURS);

		if (lTeam.isExpanding == false) {// this means the avg. global pop is down

			// but if our settlement pop is higher than avg + 1.5x, then go for it
			// TODO: Hard-coded AI value
			// TODO: We need to check that soliders are in-need, so we don't delete our settlements
//			if (lSettlement.numWorkers > 10) {
//				return jbt.execution.core.ExecutionTask.Status.SUCCESS;
//			}

			return jbt.execution.core.ExecutionTask.Status.FAILURE;
		}

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