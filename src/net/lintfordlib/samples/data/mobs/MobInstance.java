package net.lintfordlib.samples.data.mobs;

import net.lintfordlib.core.graphics.sprites.SpriteInstance;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.data.entities.CellEntity;

public class MobInstance extends CellEntity {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = -5778125755953549324L;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private MobDefinition mMobDefinition;
	public String mCurrentAnimationName;
	public transient SpriteInstance currentSprite;
	public boolean isPlayerControlled;
	public boolean isLeftFacing;
	public float health;

	// timers
	public float inputCooldownTimerMs;
	public float damageCooldownTimerMs;
	public float attackCooldownTimerMs;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public MobDefinition def() {
		return mMobDefinition;
	}
	
	public boolean isAssigned() {
		return mMobDefinition != null;
	}

	public boolean isInputCooldownElapsed() {
		return inputCooldownTimerMs <= 0.f;
	}

	public boolean isAttackTimerElapsed() {
		return attackCooldownTimerMs <= 0.f;
	}

	public boolean isDamageCooldownElapsed() {
		return damageCooldownTimerMs <= 0;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobInstance(int uid) {
		super(uid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void update(float dt) {
		super.update(dt);

		// 'physics' etc is handled in MobController.
		// here we just update individual mob timers.

		if (inputCooldownTimerMs > 0)
			inputCooldownTimerMs -= dt;
		if (damageCooldownTimerMs > 0)
			damageCooldownTimerMs -= dt;
		if (attackCooldownTimerMs > 0)
			attackCooldownTimerMs -= dt;

		isLeftFacing = vx < 0;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void initialise(MobDefinition definition) {
		mMobDefinition = definition;
		health = definition.maxHealth;

	}

	@SuppressWarnings("unused")
	public void dealDamage(int amt, boolean respectCooldown) {
		if (respectCooldown && !isDamageCooldownElapsed())
			return;

		if (!(ConstantsGame.DEBUG_GOD_MODE && isPlayerControlled))
			health -= amt;

		if (isPlayerControlled)
			damageCooldownTimerMs = 1000.f;
		else
			damageCooldownTimerMs = 200.f;

	}

	public void tryAddHealth(float amt) {
		if (health < mMobDefinition.maxHealth)
			health += amt;

		if (health > mMobDefinition.maxHealth)
			health = mMobDefinition.maxHealth;

	}
}
