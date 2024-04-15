package lintfordpickle.fantac.data;

public class GameOptions {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final int RESOURCES_LOW = 0;
	public static final int RESOURCES_MEDIUM = 1;
	public static final int RESOURCES_HIGH = 2;

	public static final int DIFFICULT_EASY = 0;
	public static final int DIFFICULT_HARD = 1;

	// --------------------------------------
	// Variables
	// --------------------------------------

	public int numberOfPlayers;
	public int resources;
	public int difficulty;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameOptions() {
	}

}
