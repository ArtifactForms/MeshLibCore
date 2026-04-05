package demos.voxels.ray;

public class RaycastResult {

  public final boolean hit;

  public final int blockX;

  public final int blockY;

  public final int blockZ;

  public final int placeX;

  public final int placeY;

  public final int placeZ;

  public RaycastResult(boolean hit, int bx, int by, int bz, int px, int py, int pz) {
    this.hit = hit;
    this.blockX = bx;
    this.blockY = by;
    this.blockZ = bz;
    this.placeX = px;
    this.placeY = py;
    this.placeZ = pz;
  }

  public static RaycastResult miss() {
    return new RaycastResult(false, 0, 0, 0, 0, 0, 0);
  }

  @Override
  public String toString() {
    return String.format("Hit: %b | Block: [%d, %d, %d]", hit, blockX, blockY, blockZ);
  }
}
