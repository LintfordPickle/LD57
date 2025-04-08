package net.lintfordlib.samples;

import net.lintfordlib.assets.ResourceGroupProvider;

public class ConstantsGame {

	// ---------------------------------------------
	// Setup
	// ---------------------------------------------

	public static final String FOOTER_TEXT = "(c) 2025 LintfordPickle";

	public static final String APPLICATION_NAME = "LD57 - Depth";
	public static final String WINDOW_TITLE = "Ludum Dare 57 - Fill the Coffers";

	public static final float ASPECT_RATIO = 16.f / 9.f;

	public static final int GAME_CANVAS_WIDTH = 960;
	public static final int GAME_CANVAS_HEIGHT = 540;

	public static final int GAME_RESOURCE_GROUP_ID = ResourceGroupProvider.getRollingEntityNumber();

	// ---------------------------------------------
	// Game
	// ---------------------------------------------

	public static final int NUM_LEVELS = 1;

	public static final String LEVEL_EXTENSION = ".lvl";
	public static final String LEVEL_DIRECTORY = "res//def//levels";

	public static final int LEVEL_TILES_WIDE = 25;
	public static final int LEVEL_TILES_HIGH = 25;

	// I think this is true because we use manhatten distance for caluclating depth
	public static final int MAX_TILE_DEPTH = LEVEL_TILES_WIDE + LEVEL_TILES_HIGH;

	public static final int BLOCK_SIZE = 16;

	public static final boolean LOCK_ZOOM_TO_ONE = true;

	public static final int STARTING_PLAYER_HEALTH = 4;
	public static final int MIN_HEIGHT_FALL_DAMAGE = 3;

	// ---------------------------------------------
	// Debug
	// ---------------------------------------------

	public static final boolean IS_LEVEL_EDIT_MODE = false;
	public static final boolean IS_DEBUG_MODE = false;
	public static final boolean DEBUG_GOD_MODE = false;
	public static final boolean IS_DEBUG_RENDERING_MODE = false;
	public static final boolean DEBUG_ENABLE_ENEMY_MOBS = false;

	public static final boolean DEBUG_ENABLE_DRAW_DEPTHVALUES = false;
	public static final boolean DEBUG_ENABLE_DRAW_TILECOORDS = false;

	public static boolean DEBUG_DRAW_MOB_COLLIDERS = false;
	public static boolean DEBUG_DRAW_MOB_TARGET_LINES = false;

	public static final boolean IS_AI_ENABLED = true;
	public static final boolean CAMERA_DEBUG_MODE = false;

}
