// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 04/14/2024 14:45:12
// ******************************************************* 
package lintfordpickle.fantac.data.ai.modelactions;

/** ModelAction class created from MMPM action MoveTroops. */
public class MoveTroops extends jbt.model.task.leaf.action.ModelAction {
	/**
	 * Value of the parameter "fromSettlementUid" in case its value is specified at construction time. null otherwise.
	 */
	private java.lang.Integer fromSettlementUid;
	/**
	 * Location, in the context, of the parameter "fromSettlementUid" in case its value is not specified at construction time. null otherwise.
	 */
	private java.lang.String fromSettlementUidLoc;
	/**
	 * Value of the parameter "toSettlementUid" in case its value is specified at construction time. null otherwise.
	 */
	private java.lang.Integer toSettlementUid;
	/**
	 * Location, in the context, of the parameter "toSettlementUid" in case its value is not specified at construction time. null otherwise.
	 */
	private java.lang.String toSettlementUidLoc;
	/**
	 * Value of the parameter "numUnits" in case its value is specified at construction time. null otherwise.
	 */
	private java.lang.Integer numUnits;
	/**
	 * Location, in the context, of the parameter "numUnits" in case its value is not specified at construction time. null otherwise.
	 */
	private java.lang.String numUnitsLoc;

	/**
	 * Constructor. Constructs an instance of MoveTroops.
	 * 
	 * @param fromSettlementUid    value of the parameter "fromSettlementUid", or null in case it should be read from the context. If null, <code>fromSettlementUidLoc</code> cannot be null.
	 * @param fromSettlementUidLoc in case <code>fromSettlementUid</code> is null, this variable represents the place in the context where the parameter's value will be retrieved from.
	 * @param toSettlementUid      value of the parameter "toSettlementUid", or null in case it should be read from the context. If null, <code>toSettlementUidLoc</code> cannot be null.
	 * @param toSettlementUidLoc   in case <code>toSettlementUid</code> is null, this variable represents the place in the context where the parameter's value will be retrieved from.
	 * @param numUnits             value of the parameter "numUnits", or null in case it should be read from the context. If null, <code>numUnitsLoc</code> cannot be null.
	 * @param numUnitsLoc          in case <code>numUnits</code> is null, this variable represents the place in the context where the parameter's value will be retrieved from.
	 * @param unitType             value of the parameter "unitType", or null in case it should be read from the context. If null, <code>unitTypeLoc</code> cannot be null.
	 * @param unitTypeLoc          in case <code>unitType</code> is null, this variable represents the place in the context where the parameter's value will be retrieved from.
	 */
	public MoveTroops(jbt.model.core.ModelTask guard, java.lang.Integer fromSettlementUid, java.lang.String fromSettlementUidLoc, java.lang.Integer toSettlementUid, java.lang.String toSettlementUidLoc, java.lang.Integer numUnits, java.lang.String numUnitsLoc) {
		super(guard);
		this.fromSettlementUid = fromSettlementUid;
		this.fromSettlementUidLoc = fromSettlementUidLoc;
		this.toSettlementUid = toSettlementUid;
		this.toSettlementUidLoc = toSettlementUidLoc;
		this.numUnits = numUnits;
		this.numUnitsLoc = numUnitsLoc;
	}

	/**
	 * Returns a lintfordpickle.fantac.data.ai.executionactions.MoveTroops task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(jbt.execution.core.BTExecutor executor, jbt.execution.core.ExecutionTask parent) {
		return new lintfordpickle.fantac.data.ai.executionactions.MoveTroops(this, executor, parent, this.fromSettlementUid, this.fromSettlementUidLoc, this.toSettlementUid, this.toSettlementUidLoc, this.numUnits, this.numUnitsLoc);
	}
}