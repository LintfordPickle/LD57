package lintfordpickle.fantac.data.units;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.fantac.data.settlements.BaseSettlement;
import net.lintfordlib.core.entities.instances.OpenPoolInstanceManager;

public class UnitsManager extends OpenPoolInstanceManager<Unit> {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final List<Unit> unitsInField = new ArrayList<>();

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public UnitsManager() {

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void addNewUnit(int teamUid, int raceUid, int unitType, BaseSettlement from, BaseSettlement to, float worldX, float worldY, float vx, float vy) {
		final var lNewUnitInstance = getFreePooledItem();
		lNewUnitInstance.initialise(teamUid, raceUid, unitType, from, to, worldX, worldY, vx, vy);

		unitsInField.add(lNewUnitInstance);
	}

	@Override
	protected Unit createPoolObjectInstance() {
		return new Unit(getNewInstanceUID());
	}
}
