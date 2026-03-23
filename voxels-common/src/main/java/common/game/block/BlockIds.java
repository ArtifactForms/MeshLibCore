package common.game.block;

/**
 * Static registry of block identifiers used throughout the engine. These strings must match the
 * "name" field in the corresponding JSON asset files. Using constants prevents typos and provides
 * compile-time safety.
 */
public final class BlockIds {

  // Core blocks
  public static final String AIR = "core:air";
  public static final String STONE = "core:stone";
  public static final String GRASS_BLOCK = "core:grass_block";
  public static final String DIRT = "core:dirt";
  public static final String GLASS = "core:glass";
  public static final String COBBLESTONE = "core:cobblestone";

  // Flora and Wood
  public static final String BIRCH_WOOD = "core:birch_wood";
  public static final String OAK_WOOD = "core:oak_wood";
  public static final String SPRUCE_WOOD = "core:spruce_wood";
  public static final String LEAF = "core:leaf";
  public static final String SPRUCE_LEAF = "core:spruce_leaf";
  public static final String GRASS = "core:grass";

  // Environment and Terrain
  public static final String WATER = "core:water";
  public static final String SNOW = "core:snow";
  public static final String SAND = "core:sand";
  public static final String SANDSTONE = "core:sandstone";
  public static final String GRAVEL = "core:gravel";
  public static final String CACTUS = "core:cactus";
  public static final String BEDROCK = "core:bedrock";

  // Colored variants (Terracotta)
  public static final String BLACK_TERRACOTTA = "core:black_terracotta";
  public static final String RED_TERRACOTTA = "core:red_terracotta";
  public static final String GREEN_TERRACOTTA = "core:green_terracotta";
  public static final String BROWN_TERRACOTTA = "core:brown_terracotta";

  /** Private constructor to prevent instantiation of this utility class. */
  private BlockIds() {}
}
