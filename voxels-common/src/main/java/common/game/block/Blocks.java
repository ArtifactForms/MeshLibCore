package common.game.block;

/**
 * Static registry of blocks. Note: These IDs are hardcoded for now to ensure world compatibility.
 */
public final class Blocks {

  public static final BlockType AIR =
      BlockRegistry.register((short) 0, "core:air")
          .setSolid(false)
          .setOpaque(false)
          .setSelectable(false);

  public static final BlockType STONE = BlockRegistry.register((short) 1, "core:stone");

  public static final BlockType GRASS_BLOCK = BlockRegistry.register((short) 2, "core:grass_block");

  public static final BlockType DIRT = BlockRegistry.register((short) 3, "core:dirt");

  public static final BlockType GLASS =
      BlockRegistry.register((short) 4, "core:glass").setOpaque(false);

  public static final BlockType COBBLE_STONE =
      BlockRegistry.register((short) 5, "core:cobblestone");

  public static final BlockType BIRCH_WOOD = BlockRegistry.register((short) 6, "core:birch_wood");

  public static final BlockType OAK_WOOD = BlockRegistry.register((short) 7, "core:oak_wood");

  public static final BlockType WATER =
      BlockRegistry.register((short) 8, "core:water")
          .setSolid(false)
          .setOpaque(false)
          .setSelectable(false);

  public static final BlockType LEAF =
      BlockRegistry.register((short) 9, "core:leaf").setOpaque(false);

  public static final BlockType GRASS =
      BlockRegistry.register((short) 10, "core:grass")
          .setSolid(false)
          .setOpaque(false)
          .setShape(BlockShape.CROSS);

  public static final BlockType SNOW = BlockRegistry.register((short) 11, "core:snow");

  public static final BlockType SAND = BlockRegistry.register((short) 12, "core:sand");

  public static final BlockType SAND_STONE = BlockRegistry.register((short) 13, "core:sandstone");

  public static final BlockType CACTUS =
      BlockRegistry.register((short) 14, "core:cactus").setOpaque(false);

  public static final BlockType GRAVEL = BlockRegistry.register((short) 15, "core:gravel");

  public static final BlockType SPRUCE_WOOD =
      BlockRegistry.register((short) 16, "core:spruce_wood");

  public static final BlockType SPRUCE_LEAF =
      BlockRegistry.register((short) 17, "core:spruce_leaf").setOpaque(false);

  public static final BlockType BEDROCK = BlockRegistry.register((short) 18, "core:bedrock");

  public static final BlockType BLACK_TERRACOTTA =
      BlockRegistry.register((short) 19, "core:black_terracotta");

  public static final BlockType RED_TERRACOTTA =
      BlockRegistry.register((short) 20, "core:red_terracotta");

  public static final BlockType GREEN_TERRACOTTA =
      BlockRegistry.register((short) 21, "core:green_terracotta");

  public static final BlockType BROWN_TERRACOTTA =
      BlockRegistry.register((short) 22, "core:brown_terracotta");
  
  
  
  
  
  
  
//  public static final BlockType WHITE_CLOTH = BlockRegistry.register((short) 23, "core:white_cloth");
//  public static final BlockType ORANGE_CLOTH = BlockRegistry.register((short) 23, "core:orange_cloth");
//  public static final BlockType MAGENTA_CLOTH = BlockRegistry.register((short) 23, "core:magenta_cloth");
//  public static final BlockType LIGHT_BLUE_CLOTH = BlockRegistry.register((short) 23, "core:light_blue_cloth");
//  public static final BlockType LIGHT_BLUE_CLOTH = BlockRegistry.register((short) 23, "core:light_blue_cloth");
  
  
  
  
  

  /** Static initializer to ensure all blocks are registered when the class is loaded. */
  public static void initialize() {
    // Logic for triggering the static fields above
  }
}
