package net.lintfordlib.samples.data.mobs;

// @formatter:off

public class MobDefinition {

	public static final MobDefinition COMMANDER = new MobDefinition(MobTypeIndex.MOB_TYPE_PLAYER_COMANDER, .3f, false, false, false, false, 0, 0, 10);
	
	public static final MobDefinition PLAYER_MELEE = new MobDefinition(MobTypeIndex.MOB_TYPE_PLAYER_MELEE, .3f, true, false, true, false, 16.f, 0, 10);
	public static final MobDefinition PLAYER_RANGE = new MobDefinition(MobTypeIndex.MOB_TYPE_PLAYER_RANGE, .3f, false, false,false, true, 0, 64.f, 10);
	public static final MobDefinition PLAYER_DIGGER = new MobDefinition(MobTypeIndex.MOB_TYPE_PLAYER_DIGGER, .3f, true, true,false, false, 16.f, 0, 10);
	
	public static final MobDefinition GOBLIN_MELEE = new MobDefinition(MobTypeIndex.MOB_TYPE_GOBLIN_MELEE, .3f, true, false, true, false, 16.f, 0, 10);
	public static final MobDefinition GOBLIN_RANGE = new MobDefinition(MobTypeIndex.MOB_TYPE_GOBLIN_RANGE, .3f, false, false, false, true, 0, 48.f, 10);

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final int typeUid;
	public final float radius;

	public final boolean damagesOnCollide;
	public final boolean digAttackEnabled;
	public final boolean swingAttackEnabled;
	public final boolean rangeAttackEnabled;

	public final float swingRangePx;
	public final float rangeRangePx;

	public final float maxHealth;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	private MobDefinition(
			int mobTypeIndex, 
			float radius,
			boolean damagesOnCollide,
			boolean digAttackEnabled,
			boolean swingAttackEnabled,
			boolean rangeAttackEnabled,
			float swingRange,
			float rangeRange,
			float maxHealth) {
		
		typeUid = mobTypeIndex;

		this.radius = radius;
		this.digAttackEnabled = digAttackEnabled;
		this.damagesOnCollide = damagesOnCollide;
		this.swingAttackEnabled = swingAttackEnabled;
		this.rangeAttackEnabled = rangeAttackEnabled;

		this.swingRangePx = swingRange;
		this.rangeRangePx = rangeRange;

		this.maxHealth = maxHealth;
		
	}
}
