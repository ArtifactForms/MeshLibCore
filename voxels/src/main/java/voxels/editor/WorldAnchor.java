package voxels.editor;

import voxels.world.Chunk;

public class WorldAnchor {

  private float x;
  private float z;

  public void move(float dx, float dz) {
    this.x += dx;
    this.z += dz;
  }

  public float getX() {
    return x;
  }

  public float getZ() {
    return z;
  }

  public int getRegionX() {
    return (int) Math.floor(x / (Chunk.SIZE_X * 4f));
  }

  public int getRegionZ() {
    return (int) Math.floor(z / (Chunk.SIZE_Z * 4f));
  }
}