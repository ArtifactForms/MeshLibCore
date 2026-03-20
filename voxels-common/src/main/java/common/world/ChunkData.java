package common.world;

import java.util.Arrays;

import common.game.block.BlockRegistry;
import common.game.block.BlockType;
import common.game.block.Blocks;
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

  protected short[] blockData;
  protected final int[] heightMap;

  protected int chunkX;
  protected int chunkZ;

  protected boolean dirty = false;

  public ChunkData(int chunkX, int chunkZ) {
    this.chunkX = chunkX;
    this.chunkZ = chunkZ;
    this.blockData = new short[WIDTH * DEPTH * HEIGHT];
    this.heightMap = new int[WIDTH * DEPTH];
  }

  public ChunkData(int chunkX, int chunkZ, short[] blockData, int[] heightMap) {
    this.chunkX = chunkX;
    this.chunkZ = chunkZ;
    this.blockData = blockData;
    this.heightMap = heightMap;
    this.dirty = false;
  }

  public short getBlockId(int x, int y, int z) {
    if (!isInside(x, y, z)) return Blocks.AIR.getId();
    return blockData[getIndex(x, y, z)];
  }

  public void setBlockId(short id, int x, int y, int z) {
    if (!isInside(x, y, z)) return;

    int index = getIndex(x, y, z);
    blockData[index] = id;
    updateHeightMap(x, y, z, id);
    dirty = true;
  }

  public BlockType getBlock(int x, int y, int z) {
    short id = getBlockId(x, y, z);
    return BlockRegistry.get(id);
  }

  public void setBlockAt(BlockType type, int x, int y, int z) {
    setBlockId(type.getId(), x, y, z);
  }

  /** Internal helper to keep the heightmap in sync with block changes. */
  private void updateHeightMap(int x, int y, int z, short id) { // Added 'int z' here
    int currentMaxY = getHeightValue(x, z);

    // If a non-air block is placed
    if (id != Blocks.AIR.getId()) {
      if (y >= currentMaxY) {
        setHeightValue(y, x, z);
      }
    }
    // If the currently highest block is removed
    else if (y == currentMaxY) {
      int newHeight = 0;
      for (int ny = y - 1; ny >= 0; ny--) {
        if (getBlockId(x, ny, z) != Blocks.AIR.getId()) {
          newHeight = ny;
          break;
        }
      }
      setHeightValue(newHeight, x, z);
    }
  }

  /**
   * Fully recalculates the heightmap for the entire chunk. Should be called after setBlockData() or
   * bulk operations.
   */
  public void recalculateHeightMap() {
    for (int x = 0; x < WIDTH; x++) {
      for (int z = 0; z < DEPTH; z++) {
        int newHeight = 0;
        // Scan from top to bottom
        for (int y = HEIGHT - 1; y >= 0; y--) {
          if (getBlockId(x, y, z) != Blocks.AIR.getId()) {
            newHeight = y;
            break;
          }
        }
        setHeightValue(newHeight, x, z);
      }
    }
  }

  public boolean isSolid(int x, int y, int z) {
    if (!isInside(x, y, z)) return false;
    
    short type = blockData[getIndex(x, y, z)];
    
    // TODO return block type is solid
    if (type == Blocks.AIR.getId() || type == Blocks.GRASS.getId()) {
    	return false;
    }
    
    return true;
  }

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
    Arrays.fill(blockData, Blocks.AIR.getId());
    Arrays.fill(heightMap, 0);
  }

  public void setBlockData(short[] blockData) {
    int validLength = WIDTH * HEIGHT * DEPTH;
    if (blockData.length != validLength) {
      throw new IllegalArgumentException("Block data length must be " + validLength + ".");
    }
    this.blockData = blockData;
  }

  public int getHeightValue(int x, int z) {
    if (!isInsideXZ(x, z)) return 0;
    return heightMap[x + z * WIDTH];
  }

  public void setHeightValue(int value, int x, int z) {
    if (!isInsideXZ(x, z)) return;
    heightMap[x + z * WIDTH] = value;
  }

  /** Internal helper for 2D boundary checks */
  private boolean isInsideXZ(int x, int z) {
    return x >= 0 && x < WIDTH && z >= 0 && z < DEPTH;
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

  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }
}
