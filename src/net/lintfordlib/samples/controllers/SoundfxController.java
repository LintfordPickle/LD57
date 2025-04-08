package net.lintfordlib.samples.controllers;

import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.audio.AudioFireAndForgetManager;
import net.lintfordlib.core.audio.AudioManager;

public class SoundfxController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Sound Fx Controller";

	public static final String SOUND_CASHOUT = "SOUND_CASHOUT";
	
	public static final String SOUND_COLLAPSE = "SOUND_COLLAPSE";
	
	public static final String SOUND_DIG_ARROW0 = "SOUND_ARROW0";
	public static final String SOUND_DIG_ARROW1 = "SOUND_ARROW1";
	public static final String SOUND_DIG_ARROW2 = "SOUND_ARROW2";

	public static final String SOUND_DIG_ATTACK0 = "SOUND_ATTACK0";

	public static final String SOUND_DIG_BLOCK0 = "SOUND_BLOCK0";
	public static final String SOUND_FOOTSTEP = "SOUND_STEP";

	public static final String SOUND_DIG_HURT0 = "SOUND_HURT0";
	public static final String SOUND_DIG_HURT1 = "SOUND_HURT1";
	public static final String SOUND_DIG_HURT2 = "SOUND_HURT2";
	public static final String SOUND_DIG_HURT3 = "SOUND_HURT3";
	public static final String SOUND_DIG_HURT4 = "SOUND_HURT4";
	public static final String SOUND_DIG_HURT5 = "SOUND_HURT5";
	public static final String SOUND_DIG_HURT6 = "SOUND_HURT6";
	public static final String SOUND_DIG_HURT7 = "SOUND_HURT7";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private AudioFireAndForgetManager mAudioFireAndForgetManager;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	@Override
	public boolean isInitialized() {
		return mAudioFireAndForgetManager != null;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public SoundfxController(ControllerManager pControllerManager, AudioManager pAudioManager, int pEntityGroupID) {
		super(pControllerManager, CONTROLLER_NAME, pEntityGroupID);

		mAudioFireAndForgetManager = new AudioFireAndForgetManager(pAudioManager);

	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		mAudioFireAndForgetManager.acquireAudioSources(6);

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void playSound(String pSoundFxName) {
		mAudioFireAndForgetManager.play(pSoundFxName);

	}
}
