package net.lintfordlib.samples.data.level;

import java.util.Arrays;

import net.lintfordlib.samples.ConstantsGame;

public class CellLevel {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final int LEVEL_TILE_COORD_INVALID = -1;

	public static final int LEVEL_TILE_INDEX_NOTHING = 0;
	public static final int LEVEL_TILE_INDEX_DIRT = 1;
	public static final int LEVEL_TILE_INDEX_ENTRY = 3;
	public static final int LEVEL_TILE_INDEX_EXIT = 4;

	public static final int LEVEL_TILE_INDEX_GOLD = 6;
	public static final int LEVEL_TILE_INDEX_GEMS = 6;

	public static final byte LEVEL_BLOCK_HEALTH_GOLD = (byte) 6;
	public static final byte LEVEL_BLOCK_HEALTH_GEMS = (byte) 10;

	public static final int LEVEL_ITEMS_NOTHING = 0;
	public static final int LEVEL_ITEMS_GOLD = 1;
	public static final int LEVEL_ITEMS_GEMS = 2;
	public static final int LEVEL_ITEMS_SPAWNER = 3;
	public static final int LEVEL_ITEMS_ENTERANCE = 4;
	public static final int LEVEL_ITEMS_TREASURE = 5;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private String mLevelName;
	private String mLevelFileName;
	private int mTilesWide;
	private int mTilesHigh;

	private int[] mBlockTypeIndices = new int[ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH];
	private byte[] mLevelBlockHealth = new byte[ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH];
	private int[] mItemIndices = new int[ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH];

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public String name() {
		return mLevelName;
	}

	public String filename() {
		return mLevelFileName;
	}

	public int tilesWide() {
		return mTilesWide;
	}

	public int tilesHigh() {
		return mTilesHigh;
	}

	public int[] levelBlocks() {
		return mBlockTypeIndices;
	}

	public int getLevelBlockType(int pLevelTileCoord) {
		if (pLevelTileCoord < 0 || pLevelTileCoord > (ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH) - 1)
			return LEVEL_TILE_COORD_INVALID;

		return mBlockTypeIndices[pLevelTileCoord];
	}

	public int getLevelBlockType(int pTileX, int pTileY) {
		final int lTileCoord = getLevelTileCoord(pTileX, pTileY);
		if (lTileCoord == LEVEL_TILE_COORD_INVALID)
			return LEVEL_TILE_COORD_INVALID;

		return mBlockTypeIndices[lTileCoord];
	}

	// ---------------------------------------------

	public int getLevelTileCoord(int pTileX, int pTileY) {
		final int lTileCoord = pTileY * ConstantsGame.LEVEL_TILES_WIDE + pTileX;
		if (lTileCoord < 0 || lTileCoord >= ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH)
			return LEVEL_TILE_COORD_INVALID;

		return lTileCoord;

	}

	public int getTopBlockIndex(int tileIndex) {
		if (tileIndex < ConstantsGame.LEVEL_TILES_WIDE)
			return LEVEL_TILE_COORD_INVALID;

		return tileIndex - ConstantsGame.LEVEL_TILES_WIDE;
	}

	public int getBottomBlockIndex(int tileIndex) {
		if (tileIndex > ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH - ConstantsGame.LEVEL_TILES_WIDE)
			return LEVEL_TILE_COORD_INVALID;

		return tileIndex + ConstantsGame.LEVEL_TILES_WIDE;
	}

	public int getLeftBlockIndex(int tileIndex) {
		if (tileIndex % ConstantsGame.LEVEL_TILES_WIDE == 0)
			return LEVEL_TILE_COORD_INVALID;

		return tileIndex - 1;
	}

	public int getRightBlockIndex(int tileIndex) {
		if (tileIndex % ConstantsGame.LEVEL_TILES_WIDE == ConstantsGame.LEVEL_TILES_WIDE - 1)
			return LEVEL_TILE_COORD_INVALID;

		return tileIndex + 1;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public CellLevel() {
		clearLevel();
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	public LevelSaveDefinition saveLevel() {
		final var lSaveDef = new LevelSaveDefinition();

		// TODO:
		lSaveDef.name = "unnamed";
		lSaveDef.levelWidth = ConstantsGame.LEVEL_TILES_WIDE;
		lSaveDef.levelHeight = ConstantsGame.LEVEL_TILES_HIGH;

		final var lNumBlocks = mBlockTypeIndices.length;
		lSaveDef.blockTypeIndices = Arrays.copyOf(mBlockTypeIndices, lNumBlocks);
		lSaveDef.levelBlockHealth = Arrays.copyOf(mLevelBlockHealth, lNumBlocks);
		lSaveDef.itemIndices = Arrays.copyOf(mItemIndices, lNumBlocks);

		return lSaveDef;
	}

	public void loadLevel(LevelSaveDefinition levelDefinition) {
		mLevelName = levelDefinition.name;
		mTilesWide = levelDefinition.levelWidth;
		mTilesHigh = levelDefinition.levelHeight;

		mLevelName = levelDefinition.name;
		mLevelFileName = levelDefinition.fileName;
		
		final var lNumTiles = mTilesWide * mTilesHigh;

		mBlockTypeIndices = Arrays.copyOf(levelDefinition.blockTypeIndices, lNumTiles);
		mLevelBlockHealth = Arrays.copyOf(levelDefinition.levelBlockHealth, lNumTiles);
		mItemIndices = Arrays.copyOf(levelDefinition.itemIndices, lNumTiles);
	}

	public void createTestLevel() {
		clearLevel();

		mTilesWide = ConstantsGame.LEVEL_TILES_WIDE;
		mTilesHigh = ConstantsGame.LEVEL_TILES_HIGH;

		for (int x = 1; x < mTilesWide; x++) {
			{
				final int lTileCoord = getLevelTileCoord(x, 0);
				if (lTileCoord == LEVEL_TILE_COORD_INVALID)
					continue;

				mBlockTypeIndices[lTileCoord] = LEVEL_TILE_INDEX_DIRT;
				mLevelBlockHealth[lTileCoord] = 0;
			}

			{
				final int lTileCoord = getLevelTileCoord(x, mTilesHigh - 1);
				if (lTileCoord == LEVEL_TILE_COORD_INVALID)
					continue;
				mBlockTypeIndices[lTileCoord] = LEVEL_TILE_INDEX_DIRT;
				mLevelBlockHealth[lTileCoord] = 0;

			}

		}

		for (int y = 0; y < mTilesHigh; y++) {
			{
				final int lTileCoord = getLevelTileCoord(0, y);
				if (lTileCoord == LEVEL_TILE_COORD_INVALID)
					continue;
				mBlockTypeIndices[lTileCoord] = LEVEL_TILE_INDEX_DIRT;
			}

			{
				final int lTileCoord = getLevelTileCoord(mTilesWide - 1, y);
				if (lTileCoord == LEVEL_TILE_COORD_INVALID)
					continue;
				mBlockTypeIndices[lTileCoord] = LEVEL_TILE_INDEX_DIRT;
			}
		}

		// Random blocks

	}

	private void clearLevel() {
		Arrays.fill(mLevelBlockHealth, (byte) 0);
		Arrays.fill(mBlockTypeIndices, LEVEL_TILE_INDEX_NOTHING);
		Arrays.fill(mItemIndices, LEVEL_ITEMS_NOTHING);
	}

	public boolean hasCollision(int pTileX, int pTileY) {
		if (pTileX < 0 || pTileY < 0)
			return true;
		if (pTileX >= ConstantsGame.LEVEL_TILES_WIDE || pTileY >= ConstantsGame.LEVEL_TILES_HIGH)
			return true;

		final int lTileIndex = getLevelTileCoord(pTileX, pTileY);
		if (lTileIndex == LEVEL_TILE_COORD_INVALID)
			return true;

		return mBlockTypeIndices[lTileIndex] > LEVEL_TILE_INDEX_NOTHING;
	}

	public boolean digBlock(int pTileX, int pTileY, byte pDamageAmount) {
		final int lTileIndex = getLevelTileCoord(pTileX, pTileY);
		if (lTileIndex == LEVEL_TILE_COORD_INVALID)
			return false;

		return digBlock(lTileIndex, pDamageAmount);
	}

	public boolean digBlock(int pTileCoord, byte pDamageAmount) {
		if (pTileCoord < 0 || pTileCoord >= ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH)
			return false;

		// TODO: make sure not protected (edges)?

		boolean wasBlockRemoved = false;

		// then deduct damage
		byte lBlockHealth = mLevelBlockHealth[pTileCoord];

		lBlockHealth -= pDamageAmount;
		if (lBlockHealth < 0) {
			lBlockHealth = 0;
			mBlockTypeIndices[pTileCoord] = LEVEL_TILE_INDEX_NOTHING;
			wasBlockRemoved = true;
		}

		mLevelBlockHealth[pTileCoord] = lBlockHealth;

		return wasBlockRemoved;
	}

	public byte getBlockHealth(int pTileX, int pTileY) {
		final int lTileCoord = getLevelTileCoord(pTileX, pTileY);
		if (lTileCoord == LEVEL_TILE_COORD_INVALID)
			return (byte) 255;

		return mLevelBlockHealth[lTileCoord];
	}

	public boolean placeBlock(int pTileX, int pTileY, int pBlockTypeIndex, byte pBlockHealth) {
		final int lTileCoord = getLevelTileCoord(pTileX, pTileY);
		if (lTileCoord == LEVEL_TILE_COORD_INVALID)
			return false;

		final int lCurrentBlockTypeIndex = getLevelBlockType(lTileCoord);
		if (lCurrentBlockTypeIndex != LEVEL_TILE_INDEX_NOTHING)
			return false;

		mBlockTypeIndices[lTileCoord] = pBlockTypeIndex;
		mLevelBlockHealth[lTileCoord] = pBlockHealth;

		return true;
	}

}
