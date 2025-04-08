package net.lintfordlib.samples.data.mobs.definitions;

import net.lintfordlib.samples.data.mobs.MobDefinition;
import net.lintfordlib.samples.data.mobs.MobTypeIndex;

public class PlayerDigger extends MobDefinition {

	public static final int typeId = MobTypeIndex.MOB_TYPE_PLAYER_DIGGER;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public PlayerDigger() {
		typeUid = MobTypeIndex.MOB_TYPE_PLAYER_DIGGER;

		radius = .3f;
		digAttackEnabled = true;
		damagesOnCollide = false;
		swingAttackEnabled = true;
		rangeAttackEnabled = false;

		swingRangePx = 16.f;
		rangeRangePx = 0.f;

		maxHealth = 2;
		cost = 12;
		maxCarryAmt = 8;
		attackSpeedMs = 900;

		maxAloneTravelDistInTiles = 3;
		targetSightRangeTiles = 3; // GOLD / GEMS
		distanceToDropOffGold = 5; // Drop off gold / Gems at home point
	}
}
