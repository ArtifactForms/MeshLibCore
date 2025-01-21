package engine.demos.voxels;

/**
 * Represents different types of blocks in the voxel engine. Each block type is associated with a
 * unique ID.
 */
public enum BlockType {

  /** Represents an air block. Typically used for empty spaces. */
  AIR(0),

  /** Represents a stone block. Commonly used for solid, hard surfaces. */
  STONE(1),

  /** Represents a grass block. Typically used for surface terrain. */
  GRASS(2),

  /** Represents a dirt block. Commonly found beneath grass blocks. */
  DIRT(3),

  /** Represents a glass block. Used for transparent surfaces. */
  GLASS(4);

  /** The unique ID associated with the block type. */
  private final int id;

  /**
   * Constructor for a block type.
   *
   * @param id the unique ID of the block type
   */
  BlockType(int id) {
    this.id = id;
  }

  /**
   * Gets the unique ID of the block type.
   *
   * @return the ID of the block type
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the {@code BlockType} corresponding to the specified ID. If the ID does not match any
   * block type, {@code AIR} is returned by default.
   *
   * @param id the ID to match
   * @return the corresponding {@code BlockType}, or {@code AIR} if the ID is invalid
   */
  public static BlockType fromId(int id) {
    for (BlockType type : values()) {
      if (type.id == id) {
        return type;
      }
    }
    return AIR; // Default to AIR if id is invalid
  }
}
