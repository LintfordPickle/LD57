package net.lintfordlib.samples.data.mobs;

import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.core.maths.MathHelper;

// @formatter:off

public class MobDefinition {

	public static final MobDefinition COMMANDER = new MobDefinition(MobTypeIndex.MOB_TYPE_PLAYER_COMANDER, .3f, false, false, false, 0, 0, 10);
	
	public static final MobDefinition PLAYER_MELEE = new MobDefinition(MobTypeIndex.MOB_TYPE_PLAYER_MELEE, .3f, true, false, false, 1, 0, 10);
	public static final MobDefinition PLAYER_RANGE = new MobDefinition(MobTypeIndex.MOB_TYPE_PLAYER_RANGE, .3f, false, false, true, 0, 4, 10);
	public static final MobDefinition PLAYER_DIGGER = new MobDefinition(MobTypeIndex.MOB_TYPE_PLAYER_DIGGER, .3f, false, false, false, 0, 0, 10);
	
	public static final MobDefinition GOBLIN_MELEE = new MobDefinition(MobTypeIndex.MOB_TYPE_GOBLIN_MELEE, .3f, true, true, false, 1, 0, 10);
	public static final MobDefinition GOBLIN_RANGE = new MobDefinition(MobTypeIndex.MOB_TYPE_GOBLIN_RANGE, .3f, true, false, true, 0, 3, 10);
	
	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final float MIN_RADIUS = 0.f;
	public static final float MAX_RADIUS = 1.f;

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final float radius;

	public final boolean damagesOnCollide;
	public final boolean swingAttackEnabled;
	public final boolean rangeAttackEnabled;

	public final float swingRange;
	public final float rangeRange;

	public final float maxHealth;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	private MobDefinition(
			int mobTypeIndex, 
			float radius,
			boolean damagesOnCollide,
			boolean swingAttackEnabled,
			boolean rangeAttackEnabled,
			float swingRange,
			float rangeRange,
			float maxHealth) {
		
		if(radius < 0.f || radius > 1.f)
			Debug.debugManager().logger().w(getClass().getSimpleName(), "Mob radius needs to be within [0,1]. TypeIndex: "+mobTypeIndex+"Passed radius: " + radius);
		
		this.radius = MathHelper.clamp(radius, MIN_RADIUS, MAX_RADIUS);
		this.damagesOnCollide = damagesOnCollide;
		this.swingAttackEnabled = swingAttackEnabled;
		this.rangeAttackEnabled = rangeAttackEnabled;

		this.swingRange = swingRange;
		this.rangeRange = rangeRange;

		this.maxHealth = maxHealth;
		
	}
}
