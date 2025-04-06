package net.lintfordlib.samples.data.mobs.definitions;

import net.lintfordlib.samples.data.mobs.MobDefinition;
import net.lintfordlib.samples.data.mobs.MobTypeIndex;

public class GoblinMelee extends MobDefinition {

	public static final int typeId = MobTypeIndex.MOB_TYPE_GOBLIN_MELEE;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GoblinMelee() {
		typeUid = MobTypeIndex.MOB_TYPE_GOBLIN_MELEE;

		radius = .3f;
		digAttackEnabled = false;
		damagesOnCollide = true;
		swingAttackEnabled = true;
		rangeAttackEnabled = false;

		swingRangePx = 16.f;
		rangeRangePx = 0;
		blockChance = 0.1f;

		maxAloneTravelDistInTiles = 7;

		maxHealth = 3;
		attackSpeedMs = 500;
	}
}
