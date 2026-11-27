package client.world;

import common.world.ChunkData;

/**
 * Maintains the client-side floating-origin anchor.
 *
 * <p>The server and world simulation continue to use absolute world/chunk coordinates. The client
 * can rebase rendering-related systems around the current origin chunk to keep local coordinates
 * numerically stable.
 */
public class ClientWorldOrigin {

  private int originChunkX;
  private int originChunkZ;

  public int getOriginChunkX() {
    return originChunkX;
  }

  public int getOriginChunkZ() {
    return originChunkZ;
  }

  public void setOriginChunk(int chunkX, int chunkZ) {
    this.originChunkX = chunkX;
    this.originChunkZ = chunkZ;
  }

  public float getOriginWorldX() {
    return originChunkX * (float) ChunkData.WIDTH;
  }

  public float getOriginWorldZ() {
    return originChunkZ * (float) ChunkData.DEPTH;
  }

  public float toLocalX(float absoluteX) {
    return absoluteX - getOriginWorldX();
  }

  public float toLocalY(float absoluteY) {
    return absoluteY;
  }

  public float toLocalZ(float absoluteZ) {
    return absoluteZ - getOriginWorldZ();
  }

  public float toAbsoluteX(float localX) {
    return getOriginWorldX() + localX;
  }

  public float toAbsoluteY(float localY) {
    return localY;
  }

  public float toAbsoluteZ(float localZ) {
    return getOriginWorldZ() + localZ;
  }

  public boolean isOutsideShiftThreshold(int chunkX, int chunkZ, int thresholdInChunks) {
    return Math.abs(chunkX - originChunkX) > thresholdInChunks
        || Math.abs(chunkZ - originChunkZ) > thresholdInChunks;
  }
}
