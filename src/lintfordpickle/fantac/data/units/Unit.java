package lintfordpickle.fantac.data.units;

import lintfordpickle.fantac.data.settlements.BaseSettlement;
import net.lintfordlib.core.entities.instances.OpenPooledBaseData;

public class Unit extends OpenPooledBaseData {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	public int teamUid;
	public int raceUid;

	public int unitTypeUid;

	public BaseSettlement from;
	public BaseSettlement to;

	public float x;
	public float y;
	public final float radius = 4;

	public float vx;
	public float vy;

	public float spd;

	// flags
	public boolean highStep;

	public float stimer; // step
	public float ptimer; // particle

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public Unit(int uid) {
		super(uid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void initialise(int teamUid, int raceUid, int unitTypeUid, BaseSettlement from, BaseSettlement to, float worldX, float worldY, float vx, float vy, float spd) {
		this.teamUid = teamUid;
		this.raceUid = raceUid;
		this.unitTypeUid = unitTypeUid;

		this.from = from;
		this.to = to;

		this.x = worldX;
		this.y = worldY;
		this.vx = vx;
		this.vy = vy;
		this.spd = spd;
	}

	public void reset() {
		this.teamUid = -1;

		this.from = null;
		this.to = null;

		this.x = 0;
		this.y = 0;
		this.vx = 0;
		this.vy = 0;
		this.spd = 0;
	}
}
