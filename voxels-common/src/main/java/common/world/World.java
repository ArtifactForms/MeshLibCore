package common.world;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Common World logic used by both Server and Client. Manages ChunkData and provides
 * coordinate-to-block translation.
 */
public class World {

  // Thread-safe map to store chunks by a single long key (packed X/Z)
  protected final Map<Long, ChunkData> chunks = new ConcurrentHashMap<>();

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

  public short getBlockId(int x, int y, int z) {
    if (y < 0 || y >= ChunkData.HEIGHT) return BlockType.AIR.getId();

    int cx = Math.floorDiv(x, ChunkData.WIDTH);
    int cz = Math.floorDiv(z, ChunkData.DEPTH);

    ChunkData chunk = getChunk(cx, cz);
    if (chunk == null) return BlockType.AIR.getId();

    int lx = Math.floorMod(x, ChunkData.WIDTH);
    int lz = Math.floorMod(z, ChunkData.DEPTH);

    return chunk.getBlockId(lx, y, lz);
  }

  public boolean isSolid(int x, int y, int z) {
    short id = getBlockId(x, y, z);
    return BlockType.fromId(id).isSolid();
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

  public static long getChunkKey(int x, int z) {
    return ((long) x << 32) | (z & 0xFFFFFFFFL);
  }

  public static int unpackChunkX(long key) {
    return (int) (key >> 32);
  }

  public static int unpackChunkZ(long key) {
    return (int) (key & 0xFFFFFFFFL);
  }
}
