package net.lintfordlib.samples.data.mobs;

import java.util.ArrayList;
import java.util.List;

public class MobManager {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	private static int entityUidCounter;

	public static int getNewEntityUid() {
		return entityUidCounter++;
	}

	protected final List<MobInstance> mMobInstances = new ArrayList<>();
	protected final List<MobInstance> mPoolInstances = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public List<MobInstance> mobs() {
		return mMobInstances;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobManager() {
		entityUidCounter = 0;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void removeMobInstance(MobInstance mob) {
		if (mMobInstances.contains(mob))
			mMobInstances.remove(mob);

		if (!mPoolInstances.contains(mob))
			mPoolInstances.add(mob);

	}

	public MobInstance getNewMobInstance() {

		if (mPoolInstances.size() > 0) {
			final var lMobInst = mPoolInstances.removeLast();
			mMobInstances.add(lMobInst);
			return lMobInst;
		}

		final var lNewMobInstance = new MobInstance(getNewEntityUid());
		mMobInstances.add(lNewMobInstance);
		return lNewMobInstance;
	}
}
