package demos.voxels.world;

import java.util.UUID;

import demos.voxels.chunk.Chunk;
import demos.voxels.chunk.ChunkGenerator;
import demos.voxels.chunk.ChunkGenerator3;
import demos.voxels.chunk.ChunkManager;

public class World {

  // Chunk dimension constants
  private static final int CHUNK_WIDTH = Chunk.WIDTH;
  private static final int CHUNK_DEPTH = Chunk.DEPTH;
  private static final int CHUNK_HEIGHT = Chunk.HEIGHT;

  // Vertical world limits
  private static final int MIN_Y = 0;
  private static final int MAX_Y = CHUNK_HEIGHT - 1;

  private long seed;
  private String name;
  private UUID uniqueId;
  private ChunkManager chunkManager;
  private ChunkGenerator chunkGenerator;

  public World(ChunkManager chunkManager, long seed) {
    this.uniqueId = UUID.randomUUID();
    this.chunkManager = chunkManager;
    this.chunkManager.setWorld(this);

    // Default terrain generator
    this.chunkGenerator = new ChunkGenerator3(seed);
    // this.chunkGenerator = new ChunkGenerator2();
  }

  public BlockType getBlock(int x, int y, int z) {
    // Outside vertical bounds → treat as air
    if (y < MIN_Y || y > MAX_Y) return BlockType.AIR;

    Chunk chunk = getChunkAt(x, y, z);

    // If chunk is not loaded, return air
    if (chunk == null) return BlockType.AIR;

    // Convert world coordinates to local chunk coordinates
    int localX = Math.floorMod(x, CHUNK_WIDTH);
    int localZ = Math.floorMod(z, CHUNK_DEPTH);

    return chunk.getBlock(localX, y, localZ);
  }

  public void setBlock(int x, int y, int z, BlockType type) {
    // Ignore if outside vertical bounds
    if (y < MIN_Y || y > MAX_Y) return;

    Chunk targetChunk = getChunkAt(x, y, z);

    // Ignore if the chunk is not available
    if (targetChunk == null) return;

    // Convert world coordinates to local chunk coordinates
    int localX = Math.floorMod(x, Chunk.WIDTH);
    int localZ = Math.floorMod(z, Chunk.DEPTH);

    // Set block inside the chunk
    targetChunk.setBlockAt(type, localX, y, localZ);

    // Force immediate mesh rebuild for the modified chunk
    chunkManager.forceImmediateMesh(targetChunk);

    // Handle special case: block placed on a chunk border
    // Neighbor chunks may need a mesh rebuild (face culling / AO)
    triggerNeighborUpdate(x, y, z);
  }

  private void triggerNeighborUpdate(int x, int y, int z) {

    // Compute the local position inside the chunk
    int lx = Math.floorMod(x, Chunk.WIDTH);
    int lz = Math.floorMod(z, Chunk.DEPTH);

    // If the block is on a chunk border, the neighboring chunk
    // must also update its mesh
    if (lx == 0) forceUpdateAt(x - 1, z);
    if (lx == Chunk.WIDTH - 1) forceUpdateAt(x + 1, z);
    if (lz == 0) forceUpdateAt(x, z - 1);
    if (lz == Chunk.DEPTH - 1) forceUpdateAt(x, z + 1);
  }

  private void forceUpdateAt(int worldX, int worldZ) {
    // Retrieve neighbor chunk and force a mesh rebuild
    Chunk neighbor = getChunkAt(worldX, 0, worldZ);
    if (neighbor != null) {
      chunkManager.forceImmediateMesh(neighbor);
    }
  }

  public void generate(Chunk chunk) {
    // Generate voxel data for the chunk using the active generator
    chunk.generateData(chunkGenerator);
  }

  public void setChunkGenerator(ChunkGenerator chunkGenerator) {
    this.chunkGenerator = chunkGenerator;
  }

  public Chunk getChunkAt(int x, int y, int z) {

    // Convert world coordinates to chunk indices
    int chunkXIndex = Math.floorDiv(x, Chunk.WIDTH);
    int chunkZIndex = Math.floorDiv(z, Chunk.DEPTH);

    return chunkManager.getChunk(chunkXIndex, chunkZIndex);
  }

  public boolean isBlockAt(int x, int y, int z) {
    BlockType blockType = getBlock(x, y, z);

    // No block if null or air
    if (blockType == null) return false;
    if (blockType == BlockType.AIR) return false;

    return true;
  }

  public boolean isSolid(int x, int y, int z) {
    return isBlockAt(x, y, z);
  }

  public long getSeed() {
    return seed;
  }

  public void setSeed(long seed) {
    this.seed = seed;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(UUID uniqueId) {
    this.uniqueId = uniqueId;
  }
}
