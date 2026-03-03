package voxels.editor;

public class WorldAnchor {

  private float x;
  private float z;

  public float getX() {
    return x;
  }

  public float getZ() {
    return z;
  }

  public void move(float dx, float dz) {
    x += dx;
    z += dz;
  }
}
