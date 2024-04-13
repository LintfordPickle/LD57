package lintfordpickle.fantac.data.settlements;

import lintfordpickle.fantac.data.BaseSettlement;
import net.lintfordlib.core.entities.instances.EnclosedInstanceManager;

public class SettlementsManager extends EnclosedInstanceManager<BaseSettlement> {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	// --------------------------------------
	// Properties
	// --------------------------------------

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SettlementsManager() {

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void addNewSettlement(float worldX, float worldY) {
		final var lNewSettlement = getFreeInstanceItem();

		lNewSettlement.initialise(worldX, worldY);
	}

	@Override
	protected BaseSettlement createPoolObjectInstance() {
		return new BaseSettlement(getNewInstanceUID());
	}
}
