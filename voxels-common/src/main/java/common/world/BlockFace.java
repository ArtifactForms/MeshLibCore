package common.world;

public enum BlockFace {
  UP(0, 1, 0),

  DOWN(0, -1, 0),

  NORTH(0, 0, -1),

  SOUTH(0, 0, 1),

  WEST(-1, 0, 0),

  EAST(1, 0, 0),

  NONE(0, 0, 0);

  public final int x;

  public final int y;

  public final int z;

  BlockFace(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public BlockFace opposite() {
    switch (this) {
      case UP:
        return DOWN;
      case DOWN:
        return UP;

      case NORTH:
        return SOUTH;
      case SOUTH:
        return NORTH;

      case WEST:
        return EAST;
      case EAST:
        return WEST;

      default:
        return NONE;
    }
  }
}
