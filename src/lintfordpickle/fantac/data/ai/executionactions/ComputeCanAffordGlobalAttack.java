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

		final var settlementController = (SettlementController) getContext().getVariable(ConstantsBtContext.CONTEXT_SETTLEMENT_CONTROLLER);
		final var team = (Team) getContext().getVariable(ConstantsBtContext.CONTEXT_VARS_TEAM_OURS);

		final var totalSettlements = settlementController.getTotalSettlements(team.teamUid);
		final var totalSoldiers = settlementController.getTotalSoldiers(team.teamUid);

		if (totalSettlements > 0) {
			final float avg = (float) totalSoldiers / (float) totalSettlements;

			// TODO: TODO: Hard-coded AI value for minimum soldier count before attack (global).

//			if (avg > 5) {
//				team.isAttacking = true;
//				System.out.println("[" + getClass().getSimpleName() + "] team " + team.teamUid + " is setting for attack.");
//			} else {
//				System.out.println("[" + getClass().getSimpleName() + "] team " + team.teamUid + " cannot attack.");
//				team.isAttacking = false;
//			}
			// TODO: Check that we are properly defended before attacking

			team.isAttacking = true;

		} else {
			team.isAttacking = false;
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