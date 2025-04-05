package net.lintfordlib.samples.data.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public static final int LEVEL_ITEMS_ENTRANCE = 1;
	public static final int LEVEL_ITEMS_GOLD = 2;
	public static final int LEVEL_ITEMS_GEMS = 3;
	public static final int LEVEL_ITEMS_SPAWNER = 4;
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
	private int[] mItemTypeUids = new int[ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH];

	// distance away from opening
	private transient int[] mTileDepth = new int[ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH];

	private transient float[] mItemTimers = new float[ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH];
	private transient float[] mItemHealth = new float[ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH];

	private int mEntranceTileCoord;
	private transient List<Integer> spawnerIndices = new ArrayList<>();

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public List<Integer> spawnerIndices() {
		return spawnerIndices;
	}

	public int[] tileDepth() {
		return mTileDepth;
	}

	public float[] itemTimers() {
		return mItemTimers;
	}

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

	public int getLevelBlockType(int tileX, int tileY) {
		final int lTileCoord = getLevelTileCoord(tileX, tileY);
		if (lTileCoord == LEVEL_TILE_COORD_INVALID)
			return LEVEL_TILE_COORD_INVALID;

		return mBlockTypeIndices[lTileCoord];
	}

	public int getItemTypeUid(int pLevelTileCoord) {
		if (pLevelTileCoord < 0 || pLevelTileCoord > (ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH) - 1)
			return LEVEL_TILE_COORD_INVALID;

		return mItemTypeUids[pLevelTileCoord];
	}

	public int getItemTypeUid(int tileX, int tileY) {
		final int lTileCoord = getLevelTileCoord(tileX, tileY);
		if (lTileCoord == LEVEL_TILE_COORD_INVALID)
			return LEVEL_TILE_COORD_INVALID;

		return mItemTypeUids[lTileCoord];
	}

	// ---------------------------------------------

	public int getLevelTileCoord(int tileX, int tileY) {
		final int lTileCoord = tileY * ConstantsGame.LEVEL_TILES_WIDE + tileX;
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

		// TODO: save def meta data
		lSaveDef.name = "unnamed";
		lSaveDef.levelWidth = ConstantsGame.LEVEL_TILES_WIDE;
		lSaveDef.levelHeight = ConstantsGame.LEVEL_TILES_HIGH;
		lSaveDef.entranceTilecoord = mEntranceTileCoord;

		final var lNumBlocks = mBlockTypeIndices.length;
		lSaveDef.blockTypeIndices = Arrays.copyOf(mBlockTypeIndices, lNumBlocks);
		lSaveDef.levelBlockHealth = Arrays.copyOf(mLevelBlockHealth, lNumBlocks);
		lSaveDef.itemIndices = Arrays.copyOf(mItemTypeUids, lNumBlocks);

		return lSaveDef;
	}

	public void loadLevel(LevelSaveDefinition levelDefinition) {
		mLevelName = levelDefinition.name;
		mTilesWide = levelDefinition.levelWidth;
		mTilesHigh = levelDefinition.levelHeight;
		mEntranceTileCoord = levelDefinition.entranceTilecoord;

		mLevelName = levelDefinition.name;
		mLevelFileName = levelDefinition.fileName;

		final var lNumTiles = mTilesWide * mTilesHigh;

		mBlockTypeIndices = Arrays.copyOf(levelDefinition.blockTypeIndices, lNumTiles);
		mLevelBlockHealth = Arrays.copyOf(levelDefinition.levelBlockHealth, lNumTiles);
		mItemTypeUids = Arrays.copyOf(levelDefinition.itemIndices, lNumTiles);

		for (int i = 0; i < lNumTiles; i++) {
			if (mItemTypeUids[i] == LEVEL_ITEMS_SPAWNER) {

				mItemTimers[i] = 10.f;
				mItemHealth[i] = 10;

				spawnerIndices.add(i);
			}
		}
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
	}

	private void clearLevel() {
		Arrays.fill(mLevelBlockHealth, (byte) 0);
		Arrays.fill(mBlockTypeIndices, LEVEL_TILE_INDEX_NOTHING);
		Arrays.fill(mItemTypeUids, LEVEL_ITEMS_NOTHING);
	}

	public boolean hasCollision(int tileX, int tileY) {
		if (tileX < 0 || tileY < 0)
			return true;
		if (tileX >= ConstantsGame.LEVEL_TILES_WIDE || tileY >= ConstantsGame.LEVEL_TILES_HIGH)
			return true;

		final int lTileIndex = getLevelTileCoord(tileX, tileY);
		if (lTileIndex == LEVEL_TILE_COORD_INVALID)
			return true;

		return mBlockTypeIndices[lTileIndex] > LEVEL_TILE_INDEX_NOTHING;
	}

	public boolean digBlock(int tileX, int tileY, byte pDamageAmount) {
		final int lTileIndex = getLevelTileCoord(tileX, tileY);
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

	public byte getBlockHealth(int tileX, int tileY) {
		final int lTileCoord = getLevelTileCoord(tileX, tileY);
		if (lTileCoord == LEVEL_TILE_COORD_INVALID)
			return (byte) 255;

		return mLevelBlockHealth[lTileCoord];
	}

	public boolean placeBlock(int tileX, int tileY, int pBlockTypeIndex, byte pBlockHealth) {
		final int lTileCoord = getLevelTileCoord(tileX, tileY);
		if (lTileCoord == LEVEL_TILE_COORD_INVALID)
			return false;

		final int lCurrentBlockTypeIndex = getLevelBlockType(lTileCoord);
		if (lCurrentBlockTypeIndex != LEVEL_TILE_INDEX_NOTHING)
			return false;

		mBlockTypeIndices[lTileCoord] = pBlockTypeIndex;
		mLevelBlockHealth[lTileCoord] = pBlockHealth;

		return true;
	}

	public boolean placeItem(int tileX, int tileY, int itemIndex) {
		final int lTileIndex = getLevelTileCoord(tileX, tileY);
		if (lTileIndex == LEVEL_TILE_COORD_INVALID)
			return false;

		return placeItem(lTileIndex, itemIndex);
	}

	public boolean placeItem(int tileCoord, int itemTypeUidToPlace) {
		final int lCurItemTypeUid = getItemTypeUid(tileCoord);
		if (lCurItemTypeUid != LEVEL_TILE_INDEX_NOTHING)
			return false;

		switch (itemTypeUidToPlace) {
		case LEVEL_ITEMS_SPAWNER: {
			if (spawnerIndices.contains((Integer) tileCoord))
				return false;

			mItemTypeUids[tileCoord] = itemTypeUidToPlace;

			mItemTimers[tileCoord] = 0.f;
			mItemHealth[tileCoord] = 0;
			spawnerIndices.add((Integer) tileCoord);
			break;
		}

		case LEVEL_ITEMS_GOLD: {
			final var lBlockTypeUid = getLevelBlockType(tileCoord);
			if (lBlockTypeUid != LEVEL_TILE_INDEX_DIRT)
				return false; // gold need dirt

			mItemTypeUids[tileCoord] = itemTypeUidToPlace;

			break;
		}

		case LEVEL_ITEMS_GEMS: {
			final var lBlockTypeUid = getLevelBlockType(tileCoord);
			if (lBlockTypeUid != LEVEL_TILE_INDEX_DIRT)
				return false; // gold need dirt

			mItemTypeUids[tileCoord] = itemTypeUidToPlace;

			break;
		}

		case LEVEL_ITEMS_TREASURE: {
			final var lBlockTypeUid = getLevelBlockType(tileCoord);
			if (lBlockTypeUid != LEVEL_TILE_INDEX_NOTHING)
				return false; // chests only on floor

			mItemTypeUids[tileCoord] = itemTypeUidToPlace;

			break;
		}
		}

		return true;
	}

	public boolean removeItem(int tileX, int tileY) {
		final int lTileIndex = getLevelTileCoord(tileX, tileY);
		if (lTileIndex == LEVEL_TILE_COORD_INVALID)
			return false;

		return removeItem(lTileIndex);
	}

	public boolean removeItem(int tileCoord) {
		final int lCurrentItemType = getItemTypeUid(tileCoord);
		if (lCurrentItemType == LEVEL_TILE_INDEX_NOTHING)
			return false;

		final var lCurItemType = mItemTypeUids[tileCoord];
		switch (lCurItemType) {
		case LEVEL_ITEMS_SPAWNER: {
			mItemTimers[tileCoord] = 0.f;
			mItemHealth[tileCoord] = 0;
			spawnerIndices.remove((Integer) tileCoord);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + lCurItemType);
		}

		mItemTypeUids[tileCoord] = LEVEL_ITEMS_NOTHING;
		return true;
	}

	public boolean setCaveEntrance(int tileX, int tileY) {
		final int lTileIndex = getLevelTileCoord(tileX, tileY);
		if (lTileIndex == LEVEL_TILE_COORD_INVALID)
			return false;

		final int lCurrentItemIndex = getItemTypeUid(lTileIndex);
		if (lCurrentItemIndex != LEVEL_ITEMS_NOTHING)
			return false;

		System.out.println("Setting new cave entrance to " + lTileIndex);
		mEntranceTileCoord = lTileIndex;
		placeItem(lTileIndex, LEVEL_ITEMS_ENTRANCE);

		// update depths
		final var lNumTiles = mTilesWide * mTilesHigh;
		for (int i = 0; i < lNumTiles; i++) {
			if (i != lTileIndex) {
				final int itemIndex = getItemTypeUid(i);
				if (itemIndex == LEVEL_ITEMS_ENTRANCE)
					mItemTypeUids[i] = LEVEL_ITEMS_NOTHING;
			}

			final var xx = i % mTilesWide;
			final var yy = i / mTilesHigh;

			final var manhattenDist = Math.abs(xx - tileX) + Math.abs(yy - tileY);

			mTileDepth[i] = manhattenDist;
		}

		return true;
	}
}
