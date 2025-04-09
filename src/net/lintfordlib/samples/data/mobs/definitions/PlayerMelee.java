package net.lintfordlib.samples.data.mobs.definitions;

import net.lintfordlib.samples.data.mobs.MobDefinition;
import net.lintfordlib.samples.data.mobs.MobTypeIndex;

public class PlayerMelee extends MobDefinition {

	public static final int typeId = MobTypeIndex.MOB_TYPE_PLAYER_MELEE;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public PlayerMelee() {
		typeUid = MobTypeIndex.MOB_TYPE_PLAYER_MELEE;

		radius = .15f;
		digAttackEnabled = false;
		damagesOnCollide = true;
		swingAttackEnabled = true;
		rangeAttackEnabled = false;

		swingRangePx = 16.f;
		rangeRangePx = 0;
		blockChance = 0.4f;

		maxHealth = 6;
		cost = 10;
		attackSpeedMs = 500;

		maxAloneTravelDistInTiles = 7;
		targetSightRangeTiles = 5; // SPAWNERS
	}
}
