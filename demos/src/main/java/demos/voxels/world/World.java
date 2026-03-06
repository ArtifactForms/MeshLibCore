package demos.voxels.world;

import demos.voxels.chunk.Chunk;
import demos.voxels.chunk.ChunkGenerator;
import demos.voxels.chunk.ChunkGenerator3;
import demos.voxels.chunk.ChunkManager;
import java.util.UUID;

/**
 * Manages the high-level world state, coordinates between chunks, and handles block-level
 * modifications.
 */
public class World {

  // Chunk dimension constants for internal calculations
  private static final int CHUNK_WIDTH = Chunk.WIDTH;
  private static final int CHUNK_DEPTH = Chunk.DEPTH;
  private static final int CHUNK_HEIGHT = Chunk.HEIGHT;

  // Vertical world limits to prevent out-of-bounds access
  private static final int MIN_Y = 0;
  private static final int MAX_Y = CHUNK_HEIGHT - 1;

  private long seed;
  private String name;
  private UUID uniqueId;

  private final ChunkManager chunkManager;
  private ChunkGenerator chunkGenerator;

  public World(ChunkManager chunkManager, long seed) {
    this.uniqueId = UUID.randomUUID();
    this.seed = seed;
    this.chunkManager = chunkManager;
    this.chunkManager.setWorld(this);

    // Initialize with default terrain generator
    this.chunkGenerator = new ChunkGenerator3(seed);
  }

  // --- Block Accessors ---

  /** Returns the BlockType at the given world coordinates. */
  public BlockType getBlock(int x, int y, int z) {
    if (y < MIN_Y || y > MAX_Y) {
      return BlockType.AIR;
    }

    Chunk chunk = getChunkAt(x, y, z);
    if (chunk == null) {
      return BlockType.AIR;
    }

    int localX = Math.floorMod(x, CHUNK_WIDTH);
    int localZ = Math.floorMod(z, CHUNK_DEPTH);

    return chunk.getBlock(localX, y, localZ);
  }

  /** Updates a block at the given world coordinates and triggers necessary mesh rebuilds. */
  public void setBlock(int x, int y, int z, BlockType type) {
    if (y < MIN_Y || y > MAX_Y) {
      return;
    }

    Chunk targetChunk = getChunkAt(x, y, z);
    if (targetChunk == null) {
      return;
    }

    int localX = Math.floorMod(x, CHUNK_WIDTH);
    int localZ = Math.floorMod(z, CHUNK_DEPTH);

    // Update data within the chunk
    targetChunk.setBlockAt(type, localX, y, localZ);

    // Notify ChunkManager to rebuild the mesh immediately
    chunkManager.forceImmediateMesh(targetChunk);

    // Check if the update affects neighboring chunk meshes (borders)
    triggerNeighborUpdate(x, y, z);
  }

  /** Checks if a solid block exists at the specified world coordinates. */
  public boolean isSolid(int x, int y, int z) {
    BlockType blockType = getBlock(x, y, z);
    return blockType != null && blockType != BlockType.AIR;
  }

  // --- Chunk Management ---

  /** Finds the chunk corresponding to the given world coordinates. */
  public Chunk getChunkAt(int x, int y, int z) {
    int chunkXIndex = Math.floorDiv(x, CHUNK_WIDTH);
    int chunkZIndex = Math.floorDiv(z, CHUNK_DEPTH);

    return chunkManager.getChunk(chunkXIndex, chunkZIndex);
  }

  /** Triggers the generation of voxel data for a specific chunk. */
  public void generate(Chunk chunk) {
    chunk.generateData(chunkGenerator);
  }

  // --- Internal Helpers ---

  /** Forces mesh updates for neighbors if a block on the chunk boundary changed. */
  private void triggerNeighborUpdate(int x, int y, int z) {
    int lx = Math.floorMod(x, CHUNK_WIDTH);
    int lz = Math.floorMod(z, CHUNK_DEPTH);

    if (lx == 0) forceUpdateAt(x - 1, z);
    if (lx == CHUNK_WIDTH - 1) forceUpdateAt(x + 1, z);
    if (lz == 0) forceUpdateAt(x, z - 1);
    if (lz == CHUNK_DEPTH - 1) forceUpdateAt(x, z + 1);
  }

  /** Helper to force a mesh rebuild at a specific world-column. */
  private void forceUpdateAt(int worldX, int worldZ) {
    Chunk neighbor = getChunkAt(worldX, 0, worldZ);
    if (neighbor != null) {
      chunkManager.forceImmediateMesh(neighbor);
    }
  }

  // --- Getters & Setters ---

  public void setChunkGenerator(ChunkGenerator chunkGenerator) {
    this.chunkGenerator = chunkGenerator;
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
