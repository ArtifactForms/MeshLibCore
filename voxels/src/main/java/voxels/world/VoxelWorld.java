package voxels.world;

import java.util.*;

public class VoxelWorld implements BlockAccess {

  private final Map<Long, Chunk> chunks = new HashMap<>();
  private final Map<Long, Region> regions = new HashMap<>();

  public void addChunk(Chunk chunk) {

    int chunkX = chunk.getChunkX();
    int chunkZ = chunk.getChunkZ();

    long chunkKey = key(chunkX, chunkZ);
    if (chunks.containsKey(chunkKey)) {
      return;
    }

    chunks.put(chunkKey, chunk);

    int regionX = Math.floorDiv(chunkX, Region.REGION_SIZE);
    int regionZ = Math.floorDiv(chunkZ, Region.REGION_SIZE);

    long regionKey = key(regionX, regionZ);

    Region region = regions.computeIfAbsent(regionKey, k -> new Region(regionX, regionZ));
    region.addChunk(chunk);
  }

  public Chunk removeChunk(int chunkX, int chunkZ) {
    long chunkKey = key(chunkX, chunkZ);
    Chunk removedChunk = chunks.remove(chunkKey);

    if (removedChunk == null) {
      return null;
    }

    int regionX = Math.floorDiv(chunkX, Region.REGION_SIZE);
    int regionZ = Math.floorDiv(chunkZ, Region.REGION_SIZE);

    long regionKey = key(regionX, regionZ);
    Region region = regions.get(regionKey);
    if (region != null) {
      region.removeChunk(chunkX, chunkZ);
      if (region.isEmpty()) {
        regions.remove(regionKey);
      }
    }

    return removedChunk;
  }

  public boolean hasChunk(int chunkX, int chunkZ) {
    return chunks.containsKey(key(chunkX, chunkZ));
  }

  public Chunk getChunk(int chunkX, int chunkZ) {
    return chunks.get(key(chunkX, chunkZ));
  }

  public Collection<Region> getRegions() {
    return regions.values();
  }

  public List<Chunk> getChunks() {
    return new ArrayList<>(chunks.values());
  }

  public Region getRegion(int regionX, int regionZ) {
    return regions.get(key(regionX, regionZ));
  }

  public short getBlock(int worldX, int worldY, int worldZ) {

    if (worldY < 0 || worldY >= Chunk.SIZE_Y) return Blocks.AIR;

    int chunkX = Math.floorDiv(worldX, Chunk.SIZE_X);
    int chunkZ = Math.floorDiv(worldZ, Chunk.SIZE_Z);

    Chunk chunk = getChunk(chunkX, chunkZ);
    if (chunk == null) return Blocks.AIR;

    int localX = Math.floorMod(worldX, Chunk.SIZE_X);
    int localZ = Math.floorMod(worldZ, Chunk.SIZE_Z);

    return chunk.getBlock(localX, worldY, localZ);
  }

  public long key(int x, int z) {
    return (((long) x) << 32) | (z & 0xffffffffL);
  }
}