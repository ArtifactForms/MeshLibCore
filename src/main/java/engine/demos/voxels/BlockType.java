package engine.demos.voxels;

/**
 * Represents different types of blocks in the voxel engine. Each block type is associated with a
 * unique ID.
 */
public enum BlockType {
  AIR(0),
  STONE(1),
  GRASS_BLOCK(2),
  DIRT(3),
  GLASS(4),
  WOOD(5),
  WATER(6),
  LEAF(7),
  GRASS(8),
  SNOW(9),
  SAND(10),
  SAND_STONE(11),
  CACTUS(12);

  /** The unique ID associated with the block type. */
  private final short id;

  /**
   * Constructor for a block type.
   *
   * @param id the unique ID of the block type
   */
  private BlockType(int id) {
    this.id = (short) id;
  }

  /**
   * Gets the unique ID of the block type.
   *
   * @return the ID of the block type
   */
  public short getId() {
    return id;
  }

  /**
   * Returns the {@code BlockType} corresponding to the specified ID. If the ID does not match any
   * block type, {@code AIR} is returned by default.
   *
   * @param id the ID to match
   * @return the corresponding {@code BlockType}, or {@code AIR} if the ID is invalid
   */
  public static BlockType fromId(short id) {
    for (BlockType type : values()) {
      if (type.id == id) {
        return type;
      }
    }
    return AIR; // Default to AIR if id is invalid
  }
}
