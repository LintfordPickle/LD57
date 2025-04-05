package net.lintfordlib.samples.data.mobs;

import net.lintfordlib.core.graphics.sprites.SpriteInstance;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.data.entities.CellEntity;

public class MobInstance extends CellEntity {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = -5778125755953549324L;

	public static final int TEAM_ID_PLAYER = 0;
	public static final int TEAM_ID_ENEMY = 1;

	public enum MobOrder {
		normal, back, attack
	}

	public enum MobState {
		normal, attacking
	}

	// --------------------------------------
	// Variables
	// --------------------------------------

	// temp
	public MobInstance targetMob;
	public int targetTileCoord;
	public float dist2Px;
	public float distManhatten;
	// end temp

	public MobState state = MobState.normal;
	public MobOrder order = MobOrder.normal;

	private MobDefinition mMobDefinition;
	public String mCurrentAnimationName;
	public transient SpriteInstance currentSprite;
	public boolean isPlayerControlled;
	public boolean isLeftFacing;
	public float health;

	public float heading;
	public float holdingGoldAmt;
	public float holdingGoldAmtMax = 10;

	public int teamUid;
	public float targetX;
	public float targetY;

	// used by commanders - defines points in front and behind where melee and archers can rally to.
	public float targetPosX;
	public float targetPosY;
	public float auxForwardPosX;
	public float auxForwardPosY;
	public float auxRearPosX;
	public float auxRearPosY;

	// swing attack
	public boolean swingingFlag;
	public int minAttackCellClearanceX;
	public int minAttackCellClearanceY;

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

	// TODO: timer resets are mob and player specific (belong in def)

	public void resetAttackTimer() {
		attackCooldownTimerMs = 700.f;
	}

	public void resetDamageCooldownr() {
		attackCooldownTimerMs = 2000.f;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobInstance(int uid) {
		super(uid);

		targetMob = null;
		targetTileCoord = -1;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void update(float dt) {

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
		radiusRatio = definition.radius;

		targetMob = null;
		targetTileCoord = -1;
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
