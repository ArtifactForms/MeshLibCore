package common.interaction;

import java.util.Objects;

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

  @Override
  public int hashCode() {
    return Objects.hash(face, placeX, placeY, placeZ, x, y, z);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BlockTarget other = (BlockTarget) obj;
    return face == other.face
        && placeX == other.placeX
        && placeY == other.placeY
        && placeZ == other.placeZ
        && x == other.x
        && y == other.y
        && z == other.z;
  }
}
