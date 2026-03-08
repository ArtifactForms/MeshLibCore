package common.world;

public enum BlockType {

  AIR(0, false, false),
  STONE(1, true, true),
  GRASS_BLOCK(2, true, true),
  DIRT(3, true, true),
  GLASS(4, true, false),
  COBBLE_STONE(5, true, true),

  BIRCH_WOOD(6, true, true),
  OAK_WOOD(7, true, true),

  WATER(8, false, false),

  LEAF(9, true, false),
  GRASS(10, false, false),

  SNOW(11, true, true),
  SAND(12, true, true),
  SAND_STONE(13, true, true),

  CACTUS(14, true, false),
  GRAVEL(15, true, true),

  SPRUCE_WOOD(16, true, true),
  SPRUCE_LEAF(17, true, false),

  BEDROCK(18, true, true);

  private final short id;
  private final boolean solid;
  private final boolean opaque;

  private static final BlockType[] BY_ID = new BlockType[256];

  static {
    for (BlockType type : values()) {
      BY_ID[type.id] = type;
    }
  }

  BlockType(int id, boolean solid, boolean opaque) {
    this.id = (short) id;
    this.solid = solid;
    this.opaque = opaque;
  }

  public short getId() {
    return id;
  }

  public boolean isSolid() {
    return solid;
  }

  public boolean isOpaque() {
    return opaque;
  }

  public static BlockType fromId(short id) {
    if (id < 0 || id >= BY_ID.length) return AIR;
    BlockType type = BY_ID[id];
    return type == null ? AIR : type;
  }
}