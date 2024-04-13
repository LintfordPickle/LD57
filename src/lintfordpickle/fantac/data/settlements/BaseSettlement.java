package lintfordpickle.fantac.data.settlements;

import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.entities.instances.ClosedPooledBaseData;
import net.lintfordlib.core.maths.MathHelper;

public class BaseSettlement extends ClosedPooledBaseData {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public int numWorkers;

	public int teamUid;

	public float x;
	public float y;
	public final float radius = 38.f;

	public double rengenWorkerPool;
	public float rengenRateMod = .01f;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public BaseSettlement(int uid) {
		super(uid);

	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void initialise(int teamUid, float worldX, float worldY) {
		this.teamUid = teamUid;

		this.x = worldX;
		this.y = worldY;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void update(LintfordCore core) {
		final double t = core.gameTime().elapsedTimeMilli() * .0001f;

		final double baseReg = 0.1;
		final double workerMod = .2;
		final double workForceBonus = MathHelper.clampd((t * workerMod * numWorkers), 0., 10.);
		final double a = t + (t * baseReg) + workForceBonus;

		rengenWorkerPool += a;

		final var lFullWorkers = (int) Math.floor(rengenWorkerPool);
		if (lFullWorkers > 0) {
			numWorkers += lFullWorkers;
			rengenWorkerPool -= lFullWorkers;
		}

		if (rengenWorkerPool < 0)
			rengenWorkerPool = 0.;
	}

	public void reset() {
		rengenWorkerPool = 0.;
		numWorkers = 5;

	}
}
