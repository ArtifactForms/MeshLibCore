package voxels.world;

public final class Chunk {

  public static final int SIZE_X = 16;
  public static final int SIZE_Y = 128;
  public static final int SIZE_Z = 16;

  private final int chunkX;
  private final int chunkZ;

  private final short[] blocks;

  private final int[] heightMap;
  private int minHeight = Integer.MAX_VALUE;
  private int maxHeight = 0;

  public Chunk(int chunkX, int chunkZ) {
    this.chunkX = chunkX;
    this.chunkZ = chunkZ;
    this.blocks = new short[SIZE_X * SIZE_Y * SIZE_Z];
    this.heightMap = new int[SIZE_X * SIZE_Z];
  }

  public short getBlock(int x, int y, int z) {
    return blocks[index(x, y, z)];
  }

  public void setBlock(int x, int y, int z, short id) {
    blocks[index(x, y, z)] = id;
  }

  public void setHeight(int x, int z, int height) {
    heightMap[x + SIZE_X * z] = height;

    if (height < minHeight) minHeight = height;
    if (height > maxHeight) maxHeight = height;
  }

  public int getHeight(int x, int z) {
    return heightMap[x + SIZE_X * z];
  }

  public int getMinHeight() {
    return minHeight;
  }

  public int getMaxHeight() {
    return maxHeight;
  }

  private static int index(int x, int y, int z) {
    return x + SIZE_X * (z + SIZE_Z * y);
  }

  public int getChunkX() {
    return chunkX;
  }

  public int getChunkZ() {
    return chunkZ;
  }
}