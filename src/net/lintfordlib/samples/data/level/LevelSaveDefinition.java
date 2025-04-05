package net.lintfordlib.samples.data.level;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import net.lintfordlib.samples.ConstantsGame;

public class LevelSaveDefinition implements Serializable {

	private static final long serialVersionUID = -7071257978085311130L;

	@SerializedName(value = "Level")
	public int level;

	@SerializedName(value = "Name")
	public String name;

	@SerializedName(value = "Width")
	public int levelWidth;

	@SerializedName(value = "Height")
	public int levelHeight;

	@SerializedName(value = "Entrance")
	public int entranceTilecoord;

	// if this save definition was loaded from a file, then the name of the file is here (this way we can save levels fast)
	public transient String fileName;

	@SerializedName(value = "Blocks")
	public int[] blockTypeIndices = new int[ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH];

	@SerializedName(value = "BlockHealth")
	public byte[] levelBlockHealth = new byte[ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH];

	@SerializedName(value = "Item Indices")
	public int[] itemIndices = new int[ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH];

	public LevelSaveDefinition() {
	}

}
