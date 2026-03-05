package demos.voxels;

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
    //    this.chunkGenerator = new ChunkGenerator2();
  }

  public BlockType getBlock(int x, int y, int z) {
    if (y < MIN_Y || y > MAX_Y) return BlockType.AIR;

    Chunk chunk = getChunkAt(x, y, z);
    if (chunk == null) return BlockType.AIR;

    int localX = Math.floorMod(x, CHUNK_WIDTH);
    int localZ = Math.floorMod(z, CHUNK_DEPTH);

    return chunk.getBlock(localX, y, localZ);
  }

  public void generate(Chunk chunk) {
    chunk.generateData(chunkGenerator);
  }

  public void setChunkGenerator(ChunkGenerator chunkGenerator) {
    this.chunkGenerator = chunkGenerator;
  }

  public Chunk getChunkAt(int x, int y, int z) {
    int chunkXIndex = Math.floorDiv(x, Chunk.WIDTH);
    int chunkZIndex = Math.floorDiv(z, Chunk.DEPTH);
    return chunkManager.getChunk(chunkXIndex, chunkZIndex);
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
