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
	private MobInstance mPlayerMobInstance;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mPlayerMobInstance != null;

	}

	public MobInstance playerMobInstance() {
		return mPlayerMobInstance;
	}

	public void playerMobInstance(MobInstance playerMobInstance) {
		mPlayerMobInstance = playerMobInstance;
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
			mPlayerMobInstance.vx -= 1.f;
		}

		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
			mPlayerMobInstance.vx += 1.f;
		}

		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
			mPlayerMobInstance.vy -= 1.f;
		}

		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
			mPlayerMobInstance.vy += 1.f;
		}

		if (lKeyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			// change digger command RETURN/GO FORTH!
		}

		return super.handleInput(core);
	}

}
