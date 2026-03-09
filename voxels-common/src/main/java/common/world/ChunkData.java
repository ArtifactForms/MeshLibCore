package common.world;

import java.util.Arrays;

import math.Bounds;
import math.Vector3f;

/**
 * Pure data representation of a voxel chunk. This class is "Headless" and can run on a server
 * without any graphics API.
 */
public class ChunkData {

  public static final int WIDTH = 16;
  public static final int DEPTH = 16;
  public static final int HEIGHT = 384;

  //  protected final short[] blockData;
  protected short[] blockData;
  protected final int[] heightMap;

  protected int chunkX;
  protected int chunkZ;

  public ChunkData(int chunkX, int chunkZ) {
    this.chunkX = chunkX;
    this.chunkZ = chunkZ;
    this.blockData = new short[WIDTH * DEPTH * HEIGHT];
    this.heightMap = new int[WIDTH * DEPTH];
  }

  // --- Block Access ---

  public short getBlockId(int x, int y, int z) {
    if (!isInside(x, y, z)) return BlockType.AIR.getId();
    return blockData[getIndex(x, y, z)];
  }

  public void setBlockId(short id, int x, int y, int z) {
    if (!isInside(x, y, z)) return;

    int index = getIndex(x, y, z);
    blockData[index] = id;
    updateHeightMap(x, y, z, id);
  }

  public BlockType getBlock(int x, int y, int z) {
    short id = getBlockId(x, y, z);
    return BlockType.fromId(id);
  }

  public void setBlockAt(BlockType type, int x, int y, int z) {
    setBlockId(type.getId(), x, y, z);
  }

  // --- Logic ---

  /** Internal helper to keep the heightmap in sync with block changes. */
  private void updateHeightMap(int x, int y, int z, short id) { // Added 'int z' here
    int currentMaxY = getHeightValue(x, z);

    // If a non-air block is placed
    if (id != BlockType.AIR.getId()) {
      if (y >= currentMaxY) {
        setHeightValue(y, x, z);
      }
    }
    // If the currently highest block is removed
    else if (y == currentMaxY) {
      int newHeight = 0;
      for (int ny = y - 1; ny >= 0; ny--) {
        if (getBlockId(x, ny, z) != BlockType.AIR.getId()) {
          newHeight = ny;
          break;
        }
      }
      setHeightValue(newHeight, x, z);
    }
  }

  public boolean isSolid(int x, int y, int z) {
    if (!isInside(x, y, z)) return false;
    return blockData[getIndex(x, y, z)] != BlockType.AIR.getId();
  }

  public boolean isBlockSolid(int x, int y, int z) {
    return isSolid(x, y, z);
  }

  // --- Helpers ---

  public Bounds getChunkBounds() {
    Vector3f min = new Vector3f(getChunkX() * WIDTH, 0, getChunkZ() * DEPTH);
    Vector3f max = new Vector3f(min.x + WIDTH, HEIGHT, min.z + DEPTH);
    return new Bounds(min, max);
  }

  protected int getIndex(int x, int y, int z) {
    return x + WIDTH * (y + HEIGHT * z);
  }

  public boolean isInside(int x, int y, int z) {
    return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && z >= 0 && z < DEPTH;
  }

  public void clear() {
    Arrays.fill(blockData, BlockType.AIR.getId());
    Arrays.fill(heightMap, 0);
  }

  // --- Getters / Setters ---

  public int getHeightValue(int x, int z) {
    return heightMap[x + z * WIDTH];
  }

  public void setHeightValue(int value, int x, int z) {
    heightMap[x + z * WIDTH] = value;
  }

  public short[] getRawBlockData() {
    return blockData;
  }

  public int[] getRawHeightMap() {
    return heightMap;
  }

  public int getChunkX() {
    return chunkX;
  }

  public int getChunkZ() {
    return chunkZ;
  }

  public void setBlockData(short[] blockData) {
    this.blockData = blockData;
  }
}
