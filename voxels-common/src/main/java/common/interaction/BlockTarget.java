package common.interaction;

import common.world.BlockFace;

public class BlockTarget implements InteractionTarget {

  public final int x;
  public final int y;
  public final int z;

  public final int placeX;
  public final int placeY;
  public final int placeZ;

  public final BlockFace face;

  public BlockTarget(int x, int y, int z, int px, int py, int pz, BlockFace face) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.placeX = px;
    this.placeY = py;
    this.placeZ = pz;
    this.face = face;
  }
}
