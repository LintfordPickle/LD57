package net.lintfordlib.samples.data.mobs;

import net.lintfordlib.samples.data.mobs.definitions.Commander;
import net.lintfordlib.samples.data.mobs.definitions.GoblinMelee;
import net.lintfordlib.samples.data.mobs.definitions.GoblinRange;
import net.lintfordlib.samples.data.mobs.definitions.PlayerDigger;
import net.lintfordlib.samples.data.mobs.definitions.PlayerMelee;
import net.lintfordlib.samples.data.mobs.definitions.PlayerRange;

// @formatter:off

public abstract class MobDefinition {

	public static final MobDefinition COMMANDER = new Commander();
	
	public static final MobDefinition PLAYER_MELEE = new PlayerMelee();
	public static final MobDefinition PLAYER_RANGE = new PlayerRange();
	public static final MobDefinition PLAYER_DIGGER = new PlayerDigger();
	
	public static final MobDefinition GOBLIN_MELEE = new GoblinMelee();
	public static final MobDefinition GOBLIN_RANGE = new GoblinRange();

	// --------------------------------------
	// Variables
	// --------------------------------------

	public int typeUid;
	public float radius;

	public boolean damagesOnCollide;
	public boolean digAttackEnabled;
	public boolean swingAttackEnabled;
	
	public boolean rangeAttackEnabled;
	
	public int maxAloneTravelDistInTiles;

	public float blockChance;
	public float swingRangePx;
	public float rangeRangePx;
	
	public float attackSpeedMs;

	public float maxHealth;
	public int cost;
	public int maxCarryAmt;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	protected MobDefinition() {
		// default for all
		radius = .3f;
		maxHealth = 1;
		cost = 1;
		maxCarryAmt = 0;
		blockChance = 0;
		
		maxAloneTravelDistInTiles = 5;
	}
}
