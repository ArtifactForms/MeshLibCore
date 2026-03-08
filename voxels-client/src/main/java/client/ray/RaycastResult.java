package client.ray;

import common.world.BlockFace;

public class RaycastResult {
  public final boolean hit;
  public final int blockX, blockY, blockZ;
  public final int placeX, placeY, placeZ;
  public final BlockFace face;

  public RaycastResult(
      boolean hit, int bx, int by, int bz, int px, int py, int pz, BlockFace face) {
    this.hit = hit;
    this.blockX = bx;
    this.blockY = by;
    this.blockZ = bz;
    this.placeX = px;
    this.placeY = py;
    this.placeZ = pz;
    this.face = face;
  }

  public static RaycastResult miss() {
    return new RaycastResult(false, 0, 0, 0, 0, 0, 0, BlockFace.NONE);
  }

  public boolean isMiss() {
    return !hit;
  }

  @Override
  public String toString() {
    return String.format(
        "Hit: %b | Block: [%d, %d, %d] | Face: %s", hit, blockX, blockY, blockZ, face);
  }
}
