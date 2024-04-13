package lintfordpickle.fantac.data.units;

import java.util.ArrayList;
import java.util.List;

import net.lintfordlib.core.entities.instances.OpenPoolInstanceManager;

public class UnitsManager extends OpenPoolInstanceManager<Unit> {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final List<Unit> unitsInField = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public UnitsManager() {

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void addNewUnit(int teamUid, int fromSettlementUid, int toSettlementUid, float worldX, float worldY) {
		final var lNewUnitInstance = getFreePooledItem();
		lNewUnitInstance.initialise(teamUid, fromSettlementUid, toSettlementUid, worldX, worldY);

		unitsInField.add(lNewUnitInstance);
	}

	@Override
	protected Unit createPoolObjectInstance() {
		return new Unit(getNewInstanceUID());
	}
}
