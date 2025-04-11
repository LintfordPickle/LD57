package net.lintfordlib.samples.data.mobs.definitions;

import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.data.mobs.MobDefinition;
import net.lintfordlib.samples.data.mobs.MobTypeIndex;

public class GoblinRange extends MobDefinition {

	public static final int typeId = MobTypeIndex.MOB_TYPE_GOBLIN_RANGE;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GoblinRange() {
		typeUid = MobTypeIndex.MOB_TYPE_GOBLIN_RANGE;

		radius = .15f;
		digAttackEnabled = false;
		damagesOnCollide = false;
		swingAttackEnabled = false;
		rangeAttackEnabled = true;

		swingRangePx = 0.f;
		rangeRangePx = ConstantsGame.BLOCK_SIZE * 4.f;

		maxHealth = 2;
		attackSpeedMs = 4000;
	}
}
