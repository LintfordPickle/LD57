package net.lintfordlib.samples.controllers;

import org.lwjgl.glfw.GLFW;

import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
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

		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
			mCommanderInstance.vx -= 1.f;
		}

		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
			mCommanderInstance.vx += 1.f;
		}

		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
			mCommanderInstance.vy -= 1.f;
		}

		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
			mCommanderInstance.vy += 1.f;
		}

		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			// change digger command RETURN/GO FORTH!
		}

		return super.handleInput(core);
	}

}
