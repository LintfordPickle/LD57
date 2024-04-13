package lintfordpickle.fantac.data.settlements;

import net.lintfordlib.core.entities.instances.EnclosedInstanceManager;

public class SettlementsManager extends EnclosedInstanceManager<BaseSettlement> {

	// --------------------------------------
	// Properties
	// --------------------------------------

	public BaseSettlement getSettlementByUid(int uid) {
		final int lNumInstances = mInstances.size();
		for (int i = 0; i < lNumInstances; i++) {
			if (mInstances.get(i).isAssigned()) {
				if (mInstances.get(i).uid == uid)
					return mInstances.get(i);
			}
		}

		return null;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SettlementsManager() {

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public BaseSettlement addNewSettlement(int teamUid, int typeUid, float worldX, float worldY) {
		final var lNewSettlement = getFreeInstanceItem();

		lNewSettlement.initialise(teamUid, typeUid, worldX, worldY);
		return lNewSettlement;
	}

	@Override
	protected BaseSettlement createPoolObjectInstance() {
		return new BaseSettlement(getNewInstanceUID());
	}
}
