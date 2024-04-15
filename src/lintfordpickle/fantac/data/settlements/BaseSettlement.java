package lintfordpickle.fantac.data.settlements;

import jbt.execution.core.IBTExecutor;
import lintfordpickle.fantac.data.teams.TeamManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.entities.instances.ClosedPooledBaseData;
import net.lintfordlib.core.graphics.sprites.SpriteInstance;
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
	public double rengenSoldierPool;

	public float rengenRateMod = .01f;

	protected float mBaseRegMod;

	// TODO: This needs a better solution after the LD. The Settlements shouldn't own a reference to the executor
	public IBTExecutor btExecutor;

	// TODO: This needs a better solution after the LD. The Settlements shouldn't own a reference to the executor
	public SpriteInstance mFlagSpriteInstance;
	public SpriteInstance mSettlementInstance;

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
		case SettlementType.SETTLEMENT_TYPE_BADTOWN:
		case SettlementType.SETTLEMENT_TYPE_TOWN:
			updateTown(core);
			break;

		case SettlementType.SETTLEMENT_TYPE_CASTLE:
		case SettlementType.SETTLEMENT_TYPE_PENTAGRAM:
			updateSchool(core);
			break;

		case SettlementType.SETTLEMENT_TYPE_VILLAGE:
		default:
			updateVillage(core);
			break;
		}
	}

	private void updateTown(LintfordCore core) {
		final double t = core.gameTime().elapsedTimeMilli() * .0001f;

		final double neutral_neuter_mod = teamUid == TeamManager.CONTROLLED_NONE ? 0.3 : 1.;

		final double worker_baseReg = 1.5f * neutral_neuter_mod;
		final double soldier_baseReg = .5f * neutral_neuter_mod;

		final double workerMod = .075;
		final double workForceBonus = MathHelper.clampd((t * workerMod * numWorkers), 0., 10.);

		final double soldierMod = .005;
		final double soldierForceBonus = MathHelper.clampd((t * soldier_baseReg), 0., 10.);

		final double dt_worker = t + (t * worker_baseReg) + workForceBonus;
		final double dt_soldier = (t * soldierMod) + soldierForceBonus;

		rengenWorkerPool += dt_worker;
		rengenSoldierPool += dt_soldier;

		final var lFullWorkers = (int) Math.floor(rengenWorkerPool);
		if (lFullWorkers > 0) {
			numWorkers += lFullWorkers;
			rengenWorkerPool -= lFullWorkers;
		}

		final var lFullSoldiers = (int) Math.floor(rengenSoldierPool);
		if (lFullSoldiers > 0) {
			numSoldiers += lFullSoldiers;
			rengenSoldierPool -= lFullSoldiers;
		}

		if (rengenWorkerPool < 0)
			rengenWorkerPool = 0.;

		if (rengenSoldierPool < 0)
			rengenSoldierPool = 0.;
	}

	// like a town, but no knights
	private void updateVillage(LintfordCore core) {
		final double t = core.gameTime().elapsedTimeMilli() * .0001f;

		final double neutral_neuter_mod = teamUid == TeamManager.CONTROLLED_NONE ? 0.3 : 1.;

		final double worker_baseReg = 1.5f * neutral_neuter_mod;

		final double workerMod = .075;
		final double workForceBonus = MathHelper.clampd((t * workerMod * numWorkers), 0., 10.);

		final double dt_worker = t + (t * worker_baseReg) + workForceBonus;

		rengenWorkerPool += dt_worker;

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

		final double workerMod = .7;
		final double workForceBonus = MathHelper.clampd((t * workerMod * numWorkers * .8f), 0., 10.);
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
