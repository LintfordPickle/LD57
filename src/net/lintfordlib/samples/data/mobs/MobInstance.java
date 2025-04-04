package net.lintfordlib.samples.data.mobs;

import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.sprites.SpriteInstance;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.data.entities.CellEntity;

public class MobInstance extends CellEntity {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = -5778125755953549324L;

	public static final int MOB_TYPE_UNASSIGNED = -1;
	public static final int MOB_TYPE_PLAYER = 0;

	// --------------------------------------
	// Variables
	// --------------------------------------

	public float inputCooldownTimer;
	private int mMobTypeIndex;
	public String mCurrentAnimationName;
	public transient SpriteInstance currentSprite;
	public boolean isPlayerControlled;
	public boolean groundFlag;
	public int lastGroundHeight;
	public boolean isLeftFacing;
	public int health;
	public int maxHealth;
	public float damageCooldownTimer;
	public float jumpVelocity;
	public boolean damagesOnCollide;
	public boolean swingAttackEnabled;
	public float swingRange;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public boolean isAssigned() {
		return mMobTypeIndex != MOB_TYPE_UNASSIGNED;
	}

	public int mobTypeNameIndex() {
		return mMobTypeIndex;
	}

	public boolean isInputCooldownElapsed() {
		return inputCooldownTimer <= 0.f;
	}

	public boolean isDamageCooldownElapsed() {
		return damageCooldownTimer <= 0;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobInstance(int uid) {
		super(uid);

		mMobTypeIndex = MOB_TYPE_UNASSIGNED;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void update(float dt) {
		super.update(dt);

		// physics etc is handled in MobController. Here we just update individual mob timers
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void initialise(int mobTypeName, int pHealth) {
		mMobTypeIndex = mobTypeName;
		health = pHealth;
		maxHealth = pHealth;

	}

	public void update(LintfordCore pCore) {
		if (damageCooldownTimer > 0.0f) {
			damageCooldownTimer -= pCore.gameTime().elapsedTimeMilli();

		}

		if (inputCooldownTimer > 0.0f)
			inputCooldownTimer -= pCore.gameTime().elapsedTimeMilli();

	}

	@SuppressWarnings("unused")
	public void dealDamage(int pAmt, boolean respectCooldown) {
		if (respectCooldown && !isDamageCooldownElapsed())
			return;

		if (!(ConstantsGame.DEBUG_GOD_MODE && isPlayerControlled))
			health -= pAmt;

		if (isPlayerControlled)
			damageCooldownTimer = 1000.f;
		else
			damageCooldownTimer = 200.f;

	}

	public void tryAddHealth() {
		if (health < maxHealth)
			health++;

	}
}
