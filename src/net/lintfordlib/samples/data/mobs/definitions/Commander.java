package net.lintfordlib.samples.data.mobs.definitions;

import net.lintfordlib.samples.data.mobs.MobDefinition;
import net.lintfordlib.samples.data.mobs.MobTypeIndex;

public class Commander extends MobDefinition {

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public Commander() {
		typeUid = MobTypeIndex.MOB_TYPE_PLAYER_COMANDER;

		radius = .3f;
		digAttackEnabled = false;
		damagesOnCollide = true;
		swingAttackEnabled = false;
		rangeAttackEnabled = false;

		swingRangePx = 16.f;
		rangeRangePx = 0;

		maxHealth = 6;
	}
}
