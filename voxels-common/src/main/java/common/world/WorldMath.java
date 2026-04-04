package common.world;

import math.Vector3f;

public class WorldMath {

  private static final float BLOCK_CENTER_OFFSET = 0.5f;

  private WorldMath() {}

  public static int worldToChunk(float pos, int chunkSize) {
    return (int) Math.floor((pos + BLOCK_CENTER_OFFSET) / chunkSize);
  }

  public static int worldToChunk(double pos, int chunkSize) {
    return (int) Math.floor((pos + BLOCK_CENTER_OFFSET) / chunkSize);
  }

  public static int worldToChunkX(Vector3f position) {
    return worldToChunk(position.x, ChunkData.WIDTH);
  }

  public static int worldToChunkZ(Vector3f position) {
    return worldToChunk(position.z, ChunkData.DEPTH);
  }

  public static int worldToChunkX(Location location) {
    return worldToChunk(location.getX(), ChunkData.WIDTH);
  }

  public static int worldToChunkZ(Location location) {
    return worldToChunk(location.getZ(), ChunkData.DEPTH);
  }
}
