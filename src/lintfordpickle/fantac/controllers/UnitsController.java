package lintfordpickle.fantac.controllers;

import lintfordpickle.fantac.data.units.UnitDefinitions;
import lintfordpickle.fantac.data.units.UnitsManager;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.maths.CollisionExtensions;

public class UnitsController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Units Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private UnitsManager mUnitsManager;

	private AnimationController mAnimationController;
	private SettlementsController mSettlementsController;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public UnitsManager unitsManager() {
		return mUnitsManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public UnitsController(ControllerManager controllerManager, UnitsManager unitManager, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mUnitsManager = unitManager;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();
		mAnimationController = (AnimationController) lControllerManager.getControllerByNameRequired(AnimationController.CONTROLLER_NAME, entityGroupUid());
		mSettlementsController = (SettlementsController) lControllerManager.getControllerByNameRequired(SettlementsController.CONTROLLER_NAME, entityGroupUid());
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final float dt = (float) core.gameTime().elapsedTimeMilli();

		final var lUnitInstances = mUnitsManager.unitsInField;
		final var lNumUnits = lUnitInstances.size();
		for (int i = lNumUnits - 1; i >= 0; i--) {
			final var u = lUnitInstances.get(i);
			final var lMovementSpeed = 2.f;

			// Get vector towards target
			final var xx = u.to.x - u.x;
			final var yy = u.to.y - u.y;

			// move unit towards target
			final var len = (float) Math.sqrt(xx * xx + yy * yy);
			u.vx += (xx / len) * lMovementSpeed * dt * .0001f;
			u.vy += (yy / len) * lMovementSpeed * dt * .0001f;

			u.x += u.vx * dt;
			u.y += u.vy * dt;

			u.vx *= 0.97f;
			u.vy *= 0.97f;

			// process on destination reached
			if (CollisionExtensions.intersectsCircleCircle(u.to.x, u.to.y, u.to.radius, u.x, u.y, u.radius)) {

				// If this settlement is on own team, then add to it
				if (u.teamUid == u.to.teamUid) {
					if (u.unitTypeUid == UnitDefinitions.UNIT_WORKER_UID)
						u.to.numWorkers++;
					else if (u.unitTypeUid == UnitDefinitions.UNIT_SOLDIER_UID)
						u.to.numSoldiers++;

					mUnitsManager.returnPooledItem(lUnitInstances.remove(i));

				} else {
					// otherwise, attack
					mSettlementsController.attackSettlement(u.to, u);

					// Attack animation
					mAnimationController.playAttackAnimation(u.x, u.y);

					mUnitsManager.returnPooledItem(lUnitInstances.remove(i));
				}
			}
		}
	}

}
