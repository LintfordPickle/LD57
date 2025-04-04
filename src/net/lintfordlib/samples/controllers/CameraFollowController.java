package net.lintfordlib.samples.controllers;

import org.lwjgl.glfw.GLFW;

import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.camera.ICamera;
import net.lintfordlib.core.geometry.Rectangle;
import net.lintfordlib.core.maths.Vector2f;
import net.lintfordlib.samples.ConstantsGame;
import net.lintfordlib.samples.data.entities.CellEntity;

public class CameraFollowController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "CameraFollowController";

	private static final float CAMERA_MAN_MOVE_SPEED = 0.05f;
	private static final float CAMERA_MAN_MOVE_SPEED_MAX = 1f;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private ICamera mGameCamera;
	private CellEntity mTrackedEntity;
	private boolean mIsTrackingPlayer;
	private Vector2f mVelocity;

	private final Rectangle mLevelAreaBounds = new Rectangle();

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public ICamera gameCamera() {
		return mGameCamera;
	}

	public boolean trackPlayer() {
		return mIsTrackingPlayer;
	}

	public void trackPlayer(boolean pNewValue) {
		mIsTrackingPlayer = pNewValue;
	}

	@Override
	public boolean isInitialized() {
		return mGameCamera != null;

	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public CameraFollowController(ControllerManager pControllerManager, ICamera pCamera, int pControllerGroup) {
		super(pControllerManager, CONTROLLER_NAME, pControllerGroup);

		mVelocity = new Vector2f();

		//
		mGameCamera = pCamera;
		mIsTrackingPlayer = true;

		final float lWidth = ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.BLOCK_SIZE;
		final float lHeight = ConstantsGame.LEVEL_TILES_HIGH * ConstantsGame.BLOCK_SIZE;

		mLevelAreaBounds.set(0, 0, lWidth, lHeight);

	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {

	}

	public void initialize(ICamera pGameCamera, CellEntity pTrackedEntity) {
		mGameCamera = pGameCamera;
		mTrackedEntity = pTrackedEntity;

	}

	@Override
	public boolean handleInput(LintfordCore pCore) {
		if (mGameCamera == null)
			return false;

		final float speed = CAMERA_MAN_MOVE_SPEED / mGameCamera.getZoomFactor();

		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_LEFT)) {
			mVelocity.x -= speed;
			mIsTrackingPlayer = false;

		}

		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
			mVelocity.x += speed;
			mIsTrackingPlayer = false;

		}

		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_DOWN)) {
			mVelocity.y += speed;
			mIsTrackingPlayer = false;

		}

		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_UP)) {
			mVelocity.y -= speed;
			mIsTrackingPlayer = false;

		}

		return false;

	}

	@Override
	public void update(LintfordCore pCore) {
		if (mGameCamera == null)
			return;

		mIsTrackingPlayer = mTrackedEntity != null;
		if (mIsTrackingPlayer) {

			final float lCameraHalfWidth = mGameCamera.getWidth() * 0.5f;
			final float lCameraHalfHeight = mGameCamera.getHeight() * 0.5f;

			float lCurPositionX = mTrackedEntity.xx;
			float lCurPositionY = mTrackedEntity.yy;

			if (lCurPositionX - lCameraHalfWidth < mLevelAreaBounds.left())
				lCurPositionX = mLevelAreaBounds.left() + lCameraHalfWidth;

			if (lCurPositionX + lCameraHalfWidth > mLevelAreaBounds.right())
				lCurPositionX = mLevelAreaBounds.right() - lCameraHalfWidth;

			if (lCurPositionY + lCameraHalfHeight > mLevelAreaBounds.bottom())
				lCurPositionY = mLevelAreaBounds.bottom() - lCameraHalfHeight;

			mGameCamera.setPosition(lCurPositionX, lCurPositionY);

		} else {
			// Cap
			if (mVelocity.x < -CAMERA_MAN_MOVE_SPEED_MAX)
				mVelocity.x = -CAMERA_MAN_MOVE_SPEED_MAX;
			if (mVelocity.x > CAMERA_MAN_MOVE_SPEED_MAX)
				mVelocity.x = CAMERA_MAN_MOVE_SPEED_MAX;
			if (mVelocity.y < -CAMERA_MAN_MOVE_SPEED_MAX)
				mVelocity.y = -CAMERA_MAN_MOVE_SPEED_MAX;
			if (mVelocity.y > CAMERA_MAN_MOVE_SPEED_MAX)
				mVelocity.y = CAMERA_MAN_MOVE_SPEED_MAX;

			float elapsed = (float) pCore.appTime().elapsedTimeMilli();

			// Apply
			float lCurX = mGameCamera.getPosition().x;
			float lCurY = mGameCamera.getPosition().y;

			mGameCamera.setPosition(lCurX + mVelocity.x * elapsed, lCurY + mVelocity.y * elapsed);

		}

		// DRAG
		mVelocity.x *= 0.917f;
		mVelocity.y *= 0.917f;
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void setFollowEntity(CellEntity pFollowEntity) {
		mTrackedEntity = pFollowEntity;
	}

}