// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 04/14/2024 14:45:12
// ******************************************************* 
package lintfordpickle.fantac.data.ai.executionactions;

import lintfordpickle.fantac.controllers.SettlementController;
import lintfordpickle.fantac.data.ai.ConstantsBtContext;
import lintfordpickle.fantac.data.teams.Team;

/** ExecutionAction class created from MMPM action ComputeCanAffordGlobalExpand. */
public class ComputeCanAffordGlobalExpand extends jbt.execution.task.leaf.action.ExecutionAction {

	/**
	 * Constructor. Constructs an instance of ComputeCanAffordGlobalExpand that is able to run a lintfordpickle.fantac.data.ai.modelactions.ComputeCanAffordGlobalExpand.
	 */
	public ComputeCanAffordGlobalExpand(lintfordpickle.fantac.data.ai.modelactions.ComputeCanAffordGlobalExpand modelTask, jbt.execution.core.BTExecutor executor, jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);

		final var settlementController = (SettlementController) getContext().getVariable(ConstantsBtContext.CONTEXT_SETTLEMENT_CONTROLLER);
		final var team = (Team) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_TEAM_OURS);

		final var totalSettlements = settlementController.getTotalSettlements(team.teamUid);
		final var totalPop = settlementController.getTotalWorkers(team.teamUid);

		if (totalSettlements > 0) {
			final float avg = (float) totalPop / (float) totalSettlements;

			// TODO: Hard-coded AI value for minimum pop count before expansion (global).

			if (avg > 10) {
				team.isExpanding = true;
				System.out.println("[" + getClass().getSimpleName() + "] team " + team.teamUid + " is expanding.");
			} else {
				System.out.println("[" + getClass().getSimpleName() + "] team " + team.teamUid + " cannot expand.");
				team.isExpanding = false;
			}

			// team.isExpanding = true;
		} else {
			System.out.println("[" + getClass().getSimpleName() + "] team " + team.teamUid + " cannot expand.");
			team.isExpanding = false;
		}
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