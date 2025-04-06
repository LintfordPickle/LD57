package net.lintfordlib.samples.data.projectiles;

import net.lintfordlib.core.graphics.sprites.SpriteInstance;
import net.lintfordlib.samples.data.entities.CellEntity;

// all projectiles are arrows - no time for anything else
public class ProjectileInstance extends CellEntity {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = -5778125755953549324L;

	public static final int TEAM_ID_PLAYER = 0;
	public static final int TEAM_ID_ENEMY = 1;

	// --------------------------------------
	// Variables
	// --------------------------------------

	public float heading;
	public int damageAmt;
	public boolean isInUse;
	public float lifeRemaining;
	public int ownerTeamUid;
	public float damageCooldownTimerMs;
	public boolean isSpent;

	public transient SpriteInstance spriteInstance;

	// --------------------------------------
	// Variables
	// --------------------------------------

	public boolean isDamageCooldownElapsed() {
		return damageCooldownTimerMs <= 0;
	}

	public void resetDamageCooldown() {
		damageCooldownTimerMs = 1000.f;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ProjectileInstance(int uid) {
		super(uid);

		reset();
		radiusRatio = .3f;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void update(float dt) {

		// update timers only

		if (lifeRemaining > 0)
			lifeRemaining -= dt;
		if (damageCooldownTimerMs > 0)
			damageCooldownTimerMs -= dt;

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void reset() {
		heading = 0;
		damageAmt = 0;
		isInUse = false;
		lifeRemaining = 0.f;
		ownerTeamUid = -1;
	}

	public void initialise(float worldX, float worldY, float heading, int ownerTeamUid, float lifeTime, int damage) {
		setPosition(worldX, worldY);

		isSpent = false;
		this.heading = heading;
		damageAmt = damage;
		isInUse = true;
		lifeRemaining = lifeTime;
		this.ownerTeamUid = ownerTeamUid;
	}

	public void showDamageFlash(boolean respectCooldown) {
		if (respectCooldown && !isDamageCooldownElapsed())
			return;

		resetDamageCooldown();
	}

}
