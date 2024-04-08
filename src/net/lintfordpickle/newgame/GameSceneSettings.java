package net.lintfordpickle.newgame;

import net.lintfordlib.data.scene.BaseSceneSettings;
import net.lintfordlib.options.ResourcePathsConfig;

public class GameSceneSettings extends BaseSceneSettings {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String ScenesDirectory = "scenes";
	public static final String DataFileExentsion = ".scn";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameSceneSettings(ResourcePathsConfig appResources) {
		super(appResources);

		sceneDataExtension(DataFileExentsion);
		scenesDirectory(ScenesDirectory);
	}
}
