package common.game.block;

public final class Blocks {

  public static void initialize() {
    // forces class loading
  }
  
  public static final BlockType AIR =
      BlockRegistry.register("core:air").setSolid(false).setOpaque(false);

  public static final BlockType STONE = BlockRegistry.register("core:stone");

  public static final BlockType GRASS_BLOCK = BlockRegistry.register("core:grass_block");

  public static final BlockType DIRT = BlockRegistry.register("core:dirt");

  public static final BlockType GLASS = BlockRegistry.register("core:glass").setOpaque(false);

  public static final BlockType COBBLE_STONE = BlockRegistry.register("core:cobblestone");

  public static final BlockType BIRCH_WOOD = BlockRegistry.register("core:birch_wood");

  public static final BlockType OAK_WOOD = BlockRegistry.register("core:oak_wood");

  public static final BlockType WATER =
      BlockRegistry.register("core:water").setSolid(false).setOpaque(false);

  public static final BlockType LEAF = BlockRegistry.register("core:leaf").setOpaque(false);

  public static final BlockType GRASS =
      BlockRegistry.register("core:grass").setSolid(false).setOpaque(false);

  public static final BlockType SNOW = BlockRegistry.register("core:snow");

  public static final BlockType SAND = BlockRegistry.register("core:sand");

  public static final BlockType SAND_STONE = BlockRegistry.register("core:sandstone");

  public static final BlockType CACTUS = BlockRegistry.register("core:cactus").setOpaque(false);

  public static final BlockType GRAVEL = BlockRegistry.register("core:gravel");

  public static final BlockType SPRUCE_WOOD = BlockRegistry.register("core:spruce_wood");

  public static final BlockType SPRUCE_LEAF =
      BlockRegistry.register("core:spruce_leaf").setOpaque(false);

  public static final BlockType BEDROCK = BlockRegistry.register("core:bedrock");
  
  public static final BlockType BLACK_TERRACOTTA = BlockRegistry.register("core:black_terracotta");
  public static final BlockType RED_TERRACOTTA = BlockRegistry.register("core:black_terracotta");
  public static final BlockType GREEN_TERRACOTTA = BlockRegistry.register("core:green_terracotta");
  public static final BlockType BROWN_TERRACOTTA = BlockRegistry.register("core:brown_terracotta");
}
