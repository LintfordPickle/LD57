package net.lintfordlib.samples.data.entities;

import net.lintfordlib.core.entities.instances.OpenPooledBaseData;

public class CellEntity extends OpenPooledBaseData {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = -7190347118082878526L;

	public static final float drag = .97f;
	public static final float entitySize = .3f; // must be less than 1 cell

	// --------------------------------------
	// Variables
	// --------------------------------------

	public int cx; // cell y
	public int cy; // cell x
	public float rx; // ratio x
	public float ry; // ratio y

	public float xx; // result
	public float yy; // result

	public float vx;
	public float vy;

	public float radius;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public CellEntity(int uid) {
		super(uid);

		radius = 16.f;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void update(float dt) {

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void setPosition(float x, float y) {
		xx = x;
		yy = y;
		cx = (int) (x / 16.f);
		cy = (int) (y / 16.f);
		rx = (xx - cx * 16.f) / 16;
		ry = (xx - cx * 16.f) / 16;
	}

	public static boolean hasCollision(int cx, int cy) {
		return false; // TODO:
	}

}
