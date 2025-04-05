package net.lintfordlib.samples.data.level;

import java.util.Arrays;

import net.lintfordlib.samples.ConstantsGame;

public class CellLevel {

	public static final int LEVEL_TILE_COORD_INVALID = -1;

	public static final int LEVEL_TILE_INDEX_NOTHING = 0;
	public static final int LEVEL_TILE_INDEX_DIRT = 1;
	public static final int LEVEL_TILE_INDEX_ENTRY = 3;
	public static final int LEVEL_TILE_INDEX_EXIT = 4;

	public static final int LEVEL_TILE_INDEX_GOLD = 6;
	public static final int LEVEL_TILE_INDEX_GEMS = 6;

	public static final byte LEVEL_BLOCK_HEALTH_GOLD = (byte) 6;
	public static final byte LEVEL_BLOCK_HEALTH_GEMS = (byte) 10;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final int[] mLevelBlockIndices = new int[ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH];
	private final byte[] mLevelBlockHealth = new byte[ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH];

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public int[] levelBlocks() {
		return mLevelBlockIndices;
	}

	public int getLevelBlockType(int pLevelTileCoord) {
		if (pLevelTileCoord < 0 || pLevelTileCoord > (ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH) - 1)
			return LEVEL_TILE_COORD_INVALID;

		return mLevelBlockIndices[pLevelTileCoord];
	}

	public int getLevelBlockType(int pTileX, int pTileY) {
		final int lTileCoord = getLevelTileCoord(pTileX, pTileY);
		if (lTileCoord == LEVEL_TILE_COORD_INVALID)
			return LEVEL_TILE_COORD_INVALID;

		return mLevelBlockIndices[lTileCoord];
	}

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

	public void loadLevel() {
		createTestLevel();
	}

	private void createTestLevel() {
		clearLevel();

		for (int x = 1; x < ConstantsGame.LEVEL_TILES_WIDE; x++) {
			{
				final int lTileCoord = getLevelTileCoord(x, 0);
				if (lTileCoord == LEVEL_TILE_COORD_INVALID)
					continue;

				mLevelBlockIndices[lTileCoord] = LEVEL_TILE_INDEX_DIRT;
				mLevelBlockHealth[lTileCoord] = 0;
			}

			{
				final int lTileCoord = getLevelTileCoord(x, ConstantsGame.LEVEL_TILES_HIGH - 1);
				if (lTileCoord == LEVEL_TILE_COORD_INVALID)
					continue;
				mLevelBlockIndices[lTileCoord] = LEVEL_TILE_INDEX_DIRT;
				mLevelBlockHealth[lTileCoord] = 0;

			}

		}

		for (int y = 0; y < ConstantsGame.LEVEL_TILES_HIGH; y++) {
			{
				final int lTileCoord = getLevelTileCoord(0, y);
				if (lTileCoord == LEVEL_TILE_COORD_INVALID)
					continue;
				mLevelBlockIndices[lTileCoord] = LEVEL_TILE_INDEX_DIRT;
			}

			{
				final int lTileCoord = getLevelTileCoord(ConstantsGame.LEVEL_TILES_WIDE - 1, y);
				if (lTileCoord == LEVEL_TILE_COORD_INVALID)
					continue;
				mLevelBlockIndices[lTileCoord] = LEVEL_TILE_INDEX_DIRT;
			}
		}

		// Random blocks
//		final int lNumRandomBlocks = (int) (ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH * 0.4);
//		for (int i = 0; i < lNumRandomBlocks; i++) {
//			final int lTileCoord = RandomNumbers.random(0, (ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH) - 1);
//
//			mLevelBlockIndices[lTileCoord] = LEVEL_TILE_INDEX_DIRT;
//
//		}
//
//		final int lNumRandomStoneBlocks = (int) (ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH * 0.2);
//		for (int i = 0; i < lNumRandomStoneBlocks; i++) {
//			final int lTileCoord = RandomNumbers.random(0, (ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH) - 1);
//
//			mLevelBlockIndices[lTileCoord] = LEVEL_TILE_INDEX_DIRT;
//
//		}
//
//		// gold blocks
//		final int lNumGoldBlocks = 600 / 10;
//		for (int i = 0; i < lNumGoldBlocks; i++) {
//			final int lTileCoord = RandomNumbers.random(0, (ConstantsGame.LEVEL_TILES_WIDE * ConstantsGame.LEVEL_TILES_HIGH) - 1);
//
//			mLevelBlockIndices[lTileCoord] = LEVEL_TILE_INDEX_GOLD;
//			mLevelBlockHealth[lTileCoord] = LEVEL_BLOCK_HEALTH_GOLD;
//		}
	}

	private void clearLevel() {
		Arrays.fill(mLevelBlockHealth, (byte) 0);
		Arrays.fill(mLevelBlockIndices, LEVEL_TILE_INDEX_NOTHING);
	}

	public boolean hasCollision(int pTileX, int pTileY) {
		if (pTileX < 0 || pTileY < 0)
			return true;
		if (pTileX >= ConstantsGame.LEVEL_TILES_WIDE || pTileY >= ConstantsGame.LEVEL_TILES_HIGH)
			return true;

		final int lTileIndex = getLevelTileCoord(pTileX, pTileY);
		if (lTileIndex == LEVEL_TILE_COORD_INVALID)
			return true;

		return mLevelBlockIndices[lTileIndex] > LEVEL_TILE_INDEX_NOTHING;
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

		boolean wasBlockRemoved = false;

		// then deduct damage
		byte lBlockHealth = mLevelBlockHealth[pTileCoord];

		lBlockHealth -= pDamageAmount;
		if (lBlockHealth < 0) {
			lBlockHealth = 0;
			mLevelBlockIndices[pTileCoord] = LEVEL_TILE_INDEX_DIRT;
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

		mLevelBlockIndices[lTileCoord] = pBlockTypeIndex;
		mLevelBlockHealth[lTileCoord] = pBlockHealth;

		return true;
	}

}
