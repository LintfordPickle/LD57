package net.lintfordlib.samples.data.entities;

import net.lintfordlib.core.entities.instances.OpenPooledBaseData;
import net.lintfordlib.samples.ConstantsGame;

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

	public float radiusRatio;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public CellEntity(int uid) {
		super(uid);

		radiusRatio = 8.f;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void setPosition(float x, float y) {
		xx = x;
		yy = y;
		cx = (int) (x / ConstantsGame.BLOCK_SIZE);
		cy = (int) (y / ConstantsGame.BLOCK_SIZE);
		rx = (xx - cx * ConstantsGame.BLOCK_SIZE) / ConstantsGame.BLOCK_SIZE;
		ry = (xx - cx * ConstantsGame.BLOCK_SIZE) / ConstantsGame.BLOCK_SIZE;
	}

}