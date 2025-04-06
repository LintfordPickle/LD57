package net.lintfordlib.samples.data.projectiles;

import java.util.ArrayList;
import java.util.List;

public class ProjectileManager {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private static int entityUidCounter;

	public static int getNewEntityUid() {
		return entityUidCounter++;
	}

	protected final List<ProjectileInstance> mProjectilesInstances = new ArrayList<>();
	protected final List<ProjectileInstance> mPoolInstances = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public List<ProjectileInstance> projectiles() {
		return mProjectilesInstances;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ProjectileManager() {
		entityUidCounter = 0;

		final int lNumInitialPool = 64;
		for (int i = 0; i < lNumInitialPool; i++) {
			mPoolInstances.add(new ProjectileInstance(getNewEntityUid()));
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void removeProjectileInstance(ProjectileInstance proj) {
		if (mProjectilesInstances.contains(proj))
			mProjectilesInstances.remove(proj);

		if (!mPoolInstances.contains(proj))
			mPoolInstances.add(proj);

	}

	public ProjectileInstance getNewProjectileInstance() {
		if (mPoolInstances.size() > 0) {
			final var lProjectileInst = mPoolInstances.removeLast();
			mProjectilesInstances.add(lProjectileInst);
			return lProjectileInst;
		}

		final var lNewProjectileInstance = new ProjectileInstance(getNewEntityUid());
		mProjectilesInstances.add(lNewProjectileInstance);
		return lNewProjectileInstance;
	}
}
