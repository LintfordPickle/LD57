package lintfordpickle.fantac.data.settlements;

import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.entities.instances.ClosedPooledBaseData;
import net.lintfordlib.core.maths.MathHelper;

public class BaseSettlement extends ClosedPooledBaseData {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public int settlementTypeUid;
	public int teamUid;

	public float x;
	public float y;
	public final float radius = 38.f;

	public int numWorkers;
	public int numSoldiers; // knights / demons

	public double rengenWorkerPool;
	public float rengenRateMod = .01f;

	protected float mBaseRegMod;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public BaseSettlement(int uid) {
		super(uid);

	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void initialise(int teamUid, int typeUid, float worldX, float worldY) {
		this.teamUid = teamUid;

		this.settlementTypeUid = typeUid;

		this.x = worldX;
		this.y = worldY;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void update(LintfordCore core) {

		switch (settlementTypeUid) {
		case SettlementType.SETTLEMENT_TYPE_TOWN:
			updateTown(core);
			break;

		case SettlementType.SETTLEMENT_TYPE_CASTLE:
		case SettlementType.SETTLEMENT_TYPE_SCHOOL:
			updateSchool(core);
			break;
		}
	}

	private void updateTown(LintfordCore core) {
		final double t = core.gameTime().elapsedTimeMilli() * .0001f;

		final double baseReg = .1f;
		final double workerMod = .02;
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

	private void updateSchool(LintfordCore core) {
		if (numWorkers <= 0)
			return;

		final double t = core.gameTime().elapsedTimeMilli() * .0001f;

		final double workerMod = .9;
		final double workForceBonus = MathHelper.clampd((t * workerMod * numWorkers), 0., 10.);
		final double a = workForceBonus;

		rengenWorkerPool += a;

		final var lFullSoldiers = (int) Math.floor(rengenWorkerPool);
		if (lFullSoldiers > 0) {
			numSoldiers += lFullSoldiers;
			rengenWorkerPool -= lFullSoldiers;
			numWorkers--;
		}

		if (rengenWorkerPool < 0)
			rengenWorkerPool = 0.;
	}

	public void reset() {
		rengenWorkerPool = 0.;
		numWorkers = 5;
		settlementTypeUid = SettlementType.SETTLEMENT_TYPE_NONE;

	}
}
