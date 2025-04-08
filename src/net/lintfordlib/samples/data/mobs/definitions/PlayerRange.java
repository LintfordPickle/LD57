package net.lintfordlib.samples.data.mobs.definitions;

import net.lintfordlib.samples.data.mobs.MobDefinition;
import net.lintfordlib.samples.data.mobs.MobTypeIndex;

public class PlayerRange extends MobDefinition {

	public static final int typeId = MobTypeIndex.MOB_TYPE_PLAYER_RANGE;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public PlayerRange() {
		typeUid = MobTypeIndex.MOB_TYPE_PLAYER_RANGE;

		radius = .3f;
		digAttackEnabled = false;
		damagesOnCollide = false;
		swingAttackEnabled = false;
		rangeAttackEnabled = true;

		swingRangePx = 0.f;
		rangeRangePx = 80.f;

		maxHealth = 2;
		cost = 15;
		attackSpeedMs = 1750;
	}
}
