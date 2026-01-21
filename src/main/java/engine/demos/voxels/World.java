package engine.demos.voxels;

import java.util.UUID;

public class World {

  private static final int CHUNK_WIDTH = Chunk.WIDTH;
  private static final int CHUNK_DEPTH = Chunk.DEPTH;
  private static final int CHUNK_HEIGHT = Chunk.HEIGHT;
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
    this.chunkGenerator = new ChunkGenerator3(seed);
  }

  public BlockType getBlock(int x, int y, int z) {
    if (y < MIN_Y || y > MAX_Y) return BlockType.AIR;

    Chunk chunk = getChunkAt(x, y, z);
    if (chunk == null) {
      System.err.println("Null chunk requested: " + x + "," + y + "," + z);
      return null;
    }

    int localX = (x % CHUNK_WIDTH + CHUNK_WIDTH) % CHUNK_WIDTH;
    int localZ = (z % CHUNK_DEPTH + CHUNK_DEPTH) % CHUNK_DEPTH;

    return chunk.getBlock(localX, y % CHUNK_HEIGHT, localZ);
  }

  public Chunk getChunkAt(int x, int y, int z) {
    int chunkXIndex = (int) Math.floor(x / Chunk.WIDTH);
    int chunkZIndex = (int) Math.floor(z / Chunk.DEPTH);
    Chunk chunk = chunkManager.getChunk(chunkXIndex, chunkZIndex);
    return chunk;
  }

  public void generate(Chunk chunk) {
    chunk.generateData(chunkGenerator);
  }

  public boolean isBlockAt(int x, int y, int z) {
    BlockType blockType = getBlock(x, y, z);
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
