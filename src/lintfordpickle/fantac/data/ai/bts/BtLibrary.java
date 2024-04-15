// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 04/15/2024 21:57:58
// ******************************************************* 
package lintfordpickle.fantac.data.ai.bts;

/**
 * BT library that includes the trees read from the following files:
 * <ul>
 * <li>D:\dev\java\ludumdare\55 -
 * Summoning\projects\FanTac\res\def\jbt\jbtSettlement.xbt</li>
 * <li>D:\dev\java\ludumdare\55 -
 * Summoning\projects\FanTac\res\def\jbt\jbtTeam.xbt</li>
 * </ul>
 */
public class BtLibrary implements jbt.execution.core.IBTLibrary {
	/**
	 * Tree generated from file D:\dev\java\ludumdare\55 -
	 * Summoning\projects\FanTac\res\def\jbt\jbtSettlement.xbt.
	 */
	private static jbt.model.core.ModelTask BtSettlement;
	/**
	 * Tree generated from file D:\dev\java\ludumdare\55 -
	 * Summoning\projects\FanTac\res\def\jbt\jbtTeam.xbt.
	 */
	private static jbt.model.core.ModelTask BtTeam;

	/* Static initialization of all the trees. */
	static {
		BtSettlement = new jbt.model.task.composite.ModelSequence(
				null,
				new jbt.model.task.decorator.ModelRepeat(
						null,
						new jbt.model.task.composite.ModelSelector(
								null,
								new jbt.model.task.composite.ModelSequence(
										null,
										new lintfordpickle.fantac.data.ai.modelconditions.AnyUnoccupiedSettlements(
												null),
										new lintfordpickle.fantac.data.ai.modelconditions.CheckGlobalExpand(
												null),
										new lintfordpickle.fantac.data.ai.modelactions.ComputeClosestNeutralSettlement(
												null),
										new jbt.model.task.composite.ModelSelector(
												null,
												new jbt.model.task.composite.ModelSequence(
														null,
														new lintfordpickle.fantac.data.ai.modelconditions.CheckCanAffordLocalExpand(
																null),
														new lintfordpickle.fantac.data.ai.modelactions.MoveWorkers(
																null,
																null,
																"CONTEXT_VARS_MOVETROOPS_FROM_SETTLEMENT_UID",
																null,
																"CONTEXT_VARS_MOVETROOPS_TO_SETTLEMENT_UID",
																null,
																"CONTEXT_VARS_MOVETROOPS_UNIT_NUM_UID"),
														new lintfordpickle.fantac.data.ai.modelactions.ResetGlobalExpand(
																null)),
												new lintfordpickle.fantac.data.ai.modelactions.SetRequestPopToExpand(
														null))),
								new jbt.model.task.composite.ModelSequence(
										null,
										new jbt.model.task.decorator.ModelInverter(
												null,
												new lintfordpickle.fantac.data.ai.modelconditions.AnySchools(
														null)),
										new jbt.model.task.composite.ModelSelector(
												null,
												new jbt.model.task.decorator.ModelInverter(
														null,
														new lintfordpickle.fantac.data.ai.modelactions.ComputeClosestSchool(
																null)),
												new jbt.model.task.composite.ModelSequence(
														null,
														new lintfordpickle.fantac.data.ai.modelconditions.CheckGlobalAttackOverride(
																null),
														new lintfordpickle.fantac.data.ai.modelactions.MoveTroops(
																null,
																null,
																"CONTEXT_VARS_MOVETROOPS_FROM_SETTLEMENT_UID",
																null,
																"CONTEXT_VARS_MOVETROOPS_TO_SETTLEMENT_UID",
																null,
																"CONTEXT_VARS_MOVETROOPS_UNIT_NUM_UID")),
												new jbt.model.task.composite.ModelSequence(
														null,
														new lintfordpickle.fantac.data.ai.modelconditions.CheckGlobalExpandOverride(
																null),
														new lintfordpickle.fantac.data.ai.modelactions.MoveWorkers(
																null,
																null,
																"CONTEXT_VARS_MOVETROOPS_FROM_SETTLEMENT_UID",
																null,
																"CONTEXT_VARS_MOVETROOPS_TO_SETTLEMENT_UID",
																null,
																"CONTEXT_VARS_MOVETROOPS_UNIT_NUM_UID")))),
								new jbt.model.task.composite.ModelSequence(
										null,
										new lintfordpickle.fantac.data.ai.modelconditions.CheckGlobalAttack(
												null),
										new lintfordpickle.fantac.data.ai.modelconditions.CheckCanAffordLocalAttack(
												null),
										new jbt.model.task.composite.ModelSelector(
												null,
												new jbt.model.task.composite.ModelSequence(
														null,
														new lintfordpickle.fantac.data.ai.modelconditions.CheckCanAffordLocalAttack(
																null),
														new jbt.model.task.composite.ModelRandomSelector(
																null,
																new lintfordpickle.fantac.data.ai.modelactions.ComputeWeakestEnemySettlement(
																		null),
																new lintfordpickle.fantac.data.ai.modelactions.ComputeClosestEnemySettlement(
																		null)),
														new lintfordpickle.fantac.data.ai.modelactions.MoveTroops(
																null,
																null,
																"CONTEXT_VARS_MOVETROOPS_FROM_SETTLEMENT_UID",
																null,
																"CONTEXT_VARS_MOVETROOPS_TO_SETTLEMENT_UID",
																null,
																"CONTEXT_VARS_MOVETROOPS_UNIT_NUM_UID"),
														new lintfordpickle.fantac.data.ai.modelactions.ResetGlobalAttack(
																null)),
												new lintfordpickle.fantac.data.ai.modelactions.SetRequestPopToAttack(
														null))),
								new jbt.model.task.composite.ModelSequence(
										null,
										new lintfordpickle.fantac.data.ai.modelconditions.CheckRequestPopExpandSet(
												null),
										new jbt.model.task.composite.ModelSelector(
												null,
												new lintfordpickle.fantac.data.ai.modelconditions.CheckGlobalExpand(
														null),
												new lintfordpickle.fantac.data.ai.modelconditions.CheckGlobalExpandOverride(
														null)),
										new lintfordpickle.fantac.data.ai.modelconditions.CheckCanAffordLocalExpand(
												null),
										new lintfordpickle.fantac.data.ai.modelactions.MoveWorkers(
												null,
												null,
												"CONTEXT_VARS_MOVETROOPS_FROM_SETTLEMENT_UID",
												null,
												"CONTEXT_VARS_MOVETROOPS_TO_SETTLEMENT_UID",
												null,
												"CONTEXT_VARS_MOVETROOPS_UNIT_NUM_UID")),
								new jbt.model.task.composite.ModelSequence(
										null,
										new lintfordpickle.fantac.data.ai.modelconditions.CheckRequestPopAttackSet(
												null),
										new lintfordpickle.fantac.data.ai.modelconditions.CheckGlobalAttack(
												null),
										new lintfordpickle.fantac.data.ai.modelconditions.CheckCanAffordLocalAttack(
												null),
										new lintfordpickle.fantac.data.ai.modelactions.MoveTroops(
												null,
												null,
												"CONTEXT_VARS_MOVETROOPS_FROM_SETTLEMENT_UID",
												null,
												"CONTEXT_VARS_MOVETROOPS_TO_SETTLEMENT_UID",
												null,
												"CONTEXT_VARS_MOVETROOPS_UNIT_NUM_UID")),
								new jbt.model.task.composite.ModelSequence(
										null,
										new lintfordpickle.fantac.data.ai.modelconditions.AnySchools(
												null),
										new jbt.model.task.composite.ModelSelector(
												null,
												new lintfordpickle.fantac.data.ai.modelconditions.CheckGlobalExpand(
														null),
												new lintfordpickle.fantac.data.ai.modelconditions.CheckGlobalExpandOverride(
														null)),
										new lintfordpickle.fantac.data.ai.modelconditions.CheckCanAffordLocalExpand(
												null),
										new lintfordpickle.fantac.data.ai.modelactions.MoveWorkers(
												null,
												null,
												"CONTEXT_VARS_MOVETROOPS_FROM_SETTLEMENT_UID",
												null,
												"CONTEXT_VARS_MOVETROOPS_TO_SETTLEMENT_UID",
												null,
												"CONTEXT_VARS_MOVETROOPS_UNIT_NUM_UID")))));

		BtTeam = new jbt.model.task.decorator.ModelRepeat(
				null,
				new jbt.model.task.composite.ModelSequence(
						null,
						new lintfordpickle.fantac.data.ai.modelactions.ComputeAggression(
								null),
						new lintfordpickle.fantac.data.ai.modelactions.ComputeCanAffordGlobalExpand(
								null),
						new lintfordpickle.fantac.data.ai.modelactions.ComputeCanAffordGlobalAttack(
								null)));

	}

	/**
	 * Returns a behaviour tree by its name, or null in case it cannot be found.
	 * It must be noted that the trees that are retrieved belong to the class,
	 * not to the instance (that is, the trees are static members of the class),
	 * so they are shared among all the instances of this class.
	 */
	public jbt.model.core.ModelTask getBT(java.lang.String name) {
		if (name.equals("BtSettlement")) {
			return BtSettlement;
		}
		if (name.equals("BtTeam")) {
			return BtTeam;
		}
		return null;
	}

	/**
	 * Returns an Iterator that is able to iterate through all the elements in
	 * the library. It must be noted that the iterator does not support the
	 * "remove()" operation. It must be noted that the trees that are retrieved
	 * belong to the class, not to the instance (that is, the trees are static
	 * members of the class), so they are shared among all the instances of this
	 * class.
	 */
	public java.util.Iterator<jbt.util.Pair<java.lang.String, jbt.model.core.ModelTask>> iterator() {
		return new BTLibraryIterator();
	}

	private class BTLibraryIterator
			implements
			java.util.Iterator<jbt.util.Pair<java.lang.String, jbt.model.core.ModelTask>> {
		static final long numTrees = 2;
		long currentTree = 0;

		public boolean hasNext() {
			return this.currentTree < numTrees;
		}

		public jbt.util.Pair<java.lang.String, jbt.model.core.ModelTask> next() {
			this.currentTree++;

			if ((this.currentTree - 1) == 0) {
				return new jbt.util.Pair<java.lang.String, jbt.model.core.ModelTask>(
						"BtSettlement", BtSettlement);
			}

			if ((this.currentTree - 1) == 1) {
				return new jbt.util.Pair<java.lang.String, jbt.model.core.ModelTask>(
						"BtTeam", BtTeam);
			}

			throw new java.util.NoSuchElementException();
		}

		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}
	}
}
