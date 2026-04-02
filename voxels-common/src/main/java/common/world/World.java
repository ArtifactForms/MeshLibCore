package common.world;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import common.game.block.BlockType;
import common.game.block.Blocks;

/**
 * Common World logic used by both Server and Client. Manages ChunkData and provides
 * coordinate-to-block translation.
 */
public class World {

  // Chunk dimension constants for internal calculations
  private static final int CHUNK_WIDTH = ChunkData.WIDTH;
  private static final int CHUNK_DEPTH = ChunkData.DEPTH;
  private static final int CHUNK_HEIGHT = ChunkData.HEIGHT;

  // Vertical world limits to prevent out-of-bounds access
  private static final int MIN_Y = 0;
  private static final int MAX_Y = CHUNK_HEIGHT - 1;

  protected final WorldTimeState worldTime = new WorldTimeState();

  protected String name = "";

  // Thread-safe map to store chunks by a single long key (packed X/Z)
  protected final Map<Long, ChunkData> chunks = new ConcurrentHashMap<>();

  // --- World update ---

  public void tick() {
    worldTime.tick();
    for (ChunkData chunk : chunks.values()) {
      tickChunk(chunk);
    }
  }

  protected void tickChunk(ChunkData chunk) {
    // placeholder for future logic
  }

  // --- Chunk Management ---

  public void addChunk(ChunkData chunk) {
    chunks.put(getChunkKey(chunk.getChunkX(), chunk.getChunkZ()), chunk);
  }

  public void removeChunk(int cx, int cz) {
    chunks.remove(getChunkKey(cx, cz));
  }

  public ChunkData getChunk(int cx, int cz) {
    return chunks.get(getChunkKey(cx, cz));
  }

  // --- Block Access (The Bridge) ---

  /** Returns the BlockType at the given world coordinates. */
  public BlockType getBlock(int x, int y, int z) {
    if (y < MIN_Y || y > MAX_Y) {
      return Blocks.AIR;
    }

    ChunkData chunk = getChunkAt(x, y, z);
    if (chunk == null) {
      return Blocks.AIR;
    }

    int localX = Math.floorMod(x, CHUNK_WIDTH);
    int localZ = Math.floorMod(z, CHUNK_DEPTH);

    return chunk.getBlock(localX, y, localZ);
  }

  /** Finds the chunk corresponding to the given world coordinates. */
  public ChunkData getChunkAt(int x, int y, int z) {
    int chunkXIndex = Math.floorDiv(x, CHUNK_WIDTH);
    int chunkZIndex = Math.floorDiv(z, CHUNK_DEPTH);

    return getChunk(chunkXIndex, chunkZIndex);
  }

  /** Checks if a solid block exists at the specified world coordinates. */
  public boolean isSolid(int x, int y, int z) {
    BlockType blockType = getBlock(x, y, z);
    return blockType != null && blockType.isSolid();
  }

  public void setBlock(int x, int y, int z, short blockId) {
    if (y < 0 || y >= ChunkData.HEIGHT) return;

    int cx = Math.floorDiv(x, ChunkData.WIDTH);
    int cz = Math.floorDiv(z, ChunkData.DEPTH);

    ChunkData chunk = getChunk(cx, cz);
    if (chunk != null) {

      int lx = Math.floorMod(x, ChunkData.WIDTH);
      int lz = Math.floorMod(z, ChunkData.DEPTH);

      chunk.setBlockId(blockId, lx, y, lz);
    }
  }

  /** Returns the highest non-air block Y-coordinate at the given world coordinates. */
  public int getHeightAt(int x, int z) {
    ChunkData chunk = getChunkAt(x, 0, z); // Y is irrelevant for chunk finding here
    if (chunk == null) {
      return 0;
    }
    int localX = Math.floorMod(x, CHUNK_WIDTH);
    int localZ = Math.floorMod(z, CHUNK_DEPTH);
    return chunk.getHeightValue(localX, localZ);
  }

  public static long getChunkKey(int x, int z) {
    return ((long) x << 32) | (z & 0xFFFFFFFFL);
  }

  public static int unpackChunkX(long key) {
    return (int) (key >> 32);
  }

  public static int unpackChunkZ(long key) {
    return (int) (key & 0xFFFFFFFFL);
  }

  public int getLoadedChunksCount() {
    return chunks.size();
  }

  public String getName() {
    return name;
  }

  // --- World time ---
  
  public long getDay() {
	  return worldTime.getDay();
  }

  public long getWorldTime() {
    return worldTime.getWorldTime();
  }

  public void setWorldTime(long worldTime) {
    this.worldTime.setWorldTime(worldTime);
  }

  public float getTimeOfDayNormalized() {
    return worldTime.getTimeOfDayNormalized();
  }

  public long getTimeOfDay() {
    return worldTime.getTimeOfDay();
  }
  
  public void setTimeOfDay(long timeOfDay) {
    this.worldTime.setTimeOfDay(timeOfDay);
  }
}
