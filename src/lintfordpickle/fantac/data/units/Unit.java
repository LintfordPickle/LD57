package lintfordpickle.fantac.data.units;

import net.lintfordlib.core.entities.instances.OpenPooledBaseData;

public class Unit extends OpenPooledBaseData {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	public int teamUid;

	public int fromUid;
	public int toUid;

	public float x;
	public float y;
	public final float radius = 4;

	// --------------------------------------
	// Properties
	// --------------------------------------

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public Unit(int uid) {
		super(uid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void initialise(int teamUid, int fromUid, int toUid, float worldX, float worldY) {
		this.teamUid = teamUid;

		this.fromUid = fromUid;
		this.toUid = toUid;

		this.x = worldX;
		this.y = worldY;
	}

	public void reset() {
		teamUid = -1;

		fromUid = -1;
		toUid = -1;

		x = 0;
		y = 0;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------
}
