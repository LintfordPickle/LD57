package net.lintfordlib.samples.controllers;

import org.lwjgl.glfw.GLFW;

import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.maths.MathHelper;
import net.lintfordlib.samples.data.mobs.MobInstance;

public class PlayerController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Player Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private LevelController mLevelController;
	private MobInstance mCommanderInstance;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mCommanderInstance != null;
	}

	public MobInstance commanderInstance() {
		return mCommanderInstance;
	}

	public void commanderInstance(MobInstance commanderInst) {
		mCommanderInstance = commanderInst;
	}

	public float commanderHealth() {
		return mCommanderInstance.health;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public PlayerController(ControllerManager controllerManager, int eEntityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, eEntityGroupUid);

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		mLevelController = (LevelController) core.controllerManager().getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupUid());

	}

	@Override
	public boolean handleInput(LintfordCore core) {
		final var lKeyboard = core.input().keyboard();

		float origHeading = mCommanderInstance.heading;
		float effect = 1.f;
		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
			mCommanderInstance.vx -= 1.f;
			mCommanderInstance.heading += MathHelper.turnToFace((float) Math.toRadians(180.f), origHeading, .5f * effect);
			effect = .5f;
		}

		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
			mCommanderInstance.vx += 1.f;
			mCommanderInstance.heading += MathHelper.turnToFace((float) Math.toRadians(0.f), origHeading, .5f * effect);
			effect = .5f;
		}

		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
			mCommanderInstance.vy -= 1.f;
			mCommanderInstance.heading += MathHelper.turnToFace((float) Math.toRadians(-90.f), origHeading, .5f * effect);
			effect = .5f;
		}

		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
			mCommanderInstance.vy += 1.f;
			mCommanderInstance.heading += MathHelper.turnToFace((float) Math.toRadians(90.f), origHeading, .5f * effect);
		}

		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			// change digger command RETURN/GO FORTH!
		}

		return super.handleInput(core);
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		// update the rally positions

		final var wcx = mCommanderInstance.xx;
		final var wcy = mCommanderInstance.yy;
		
		final var lLeadDist = 40.f;

		mCommanderInstance.auxForwardPosX = wcx + (float) Math.cos(mCommanderInstance.heading) * lLeadDist;
		mCommanderInstance.auxForwardPosY = wcy + (float) Math.sin(mCommanderInstance.heading) * lLeadDist;

		mCommanderInstance.auxRearPosX = wcx - (float) Math.cos(mCommanderInstance.heading) * 20.f;
		mCommanderInstance.auxRearPosY = wcy - (float) Math.sin(mCommanderInstance.heading) * 20.f;
	}

}
