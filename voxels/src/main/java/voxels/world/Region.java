package voxels.world;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Region {

  public static final int REGION_SIZE = 2; // 2x2 chunks

  private final int regionX;
  private final int regionZ;

  private final Map<Long, Chunk> chunks = new LinkedHashMap<>();

  public Region(int regionX, int regionZ) {
    this.regionX = regionX;
    this.regionZ = regionZ;
  }

  public void addChunk(Chunk chunk) {
    chunks.put(key(chunk.getChunkX(), chunk.getChunkZ()), chunk);
  }

  public void removeChunk(int chunkX, int chunkZ) {
    chunks.remove(key(chunkX, chunkZ));
  }

  public Collection<Chunk> getChunks() {
    return chunks.values();
  }

  public boolean isEmpty() {
    return chunks.isEmpty();
  }

  public int getRegionX() {
    return regionX;
  }

  public int getRegionZ() {
    return regionZ;
  }

  private long key(int chunkX, int chunkZ) {
    return (((long) chunkX) << 32) | (chunkZ & 0xffffffffL);
  }
}