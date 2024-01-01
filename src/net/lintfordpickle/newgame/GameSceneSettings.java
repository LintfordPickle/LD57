package net.lintfordpickle.newgame;

import net.lintfordlib.core.AppResources;
import net.lintfordlib.data.BaseSceneSettings;

public class GameSceneSettings extends BaseSceneSettings {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String DataFileExentsion = ".scene";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameSceneSettings(AppResources appResources) {
		super(appResources);

		sceneDataExtension(DataFileExentsion);
	}
}
