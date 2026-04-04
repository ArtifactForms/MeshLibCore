package server.world.generation;

import common.game.block.BlockType;
import common.game.block.Blocks;

/**
 * Defines the different biomes in the game world. Each biome has specific properties like top
 * blocks, filler blocks, tree spawn chances, and a base height for terrain generation.
 */
public enum BiomeType {
  // --- COLD BIOMES ---
  /** Snowy plains with low tree density. */
  TUNDRA(Blocks.SNOW, Blocks.DIRT, 0.05f, 62),

  /** Dense snowy forests. */
  SNOWY_TAIGA(Blocks.SNOW, Blocks.DIRT, 0.4f, 85),

  /** Cold, high-altitude regions. */
  SNOW(Blocks.SNOW, Blocks.DIRT, 0.01f, 100),

  // --- TEMPERATE BIOMES ---
  /** Flat, grassy areas with few trees. */
  PLAINS(Blocks.GRASS_BLOCK, Blocks.DIRT, 0.1f, 68),

  /** Standard forest with high tree density. */
  FOREST(Blocks.GRASS_BLOCK, Blocks.DIRT, 0.6f, 80),

  /** Low-lying, wet areas. */
  SWAMP(Blocks.GRASS_BLOCK, Blocks.DIRT, 0.15f, 61),

  // --- HOT BIOMES ---
  /** Arid regions dominated by sand. */
  DESERT(Blocks.SAND, Blocks.SAND, 0.02f, 65),

  /** Tropical, flat grasslands with scattered trees. */
  SAVANNA(Blocks.GRASS_BLOCK, Blocks.DIRT, 0.2f, 72),

  /** Extremely dense tropical vegetation and high terrain. */
  JUNGLE(Blocks.GRASS_BLOCK, Blocks.DIRT, 0.85f, 95);

  public final BlockType topBlock;

  public final BlockType fillerBlock;

  public final float treeChance;

  public final int baseHeight;

  /**
   * @param top The block used for the very top layer (e.g., Grass).
   * @param filler The block used directly beneath the top layer (e.g., Dirt).
   * @param treeChance Probability (0.0 - 1.0) of a tree spawning in this biome.
   * @param baseHeight The target median height for this biome's terrain.
   */
  BiomeType(BlockType top, BlockType filler, float treeChance, int baseHeight) {
    this.topBlock = top;
    this.fillerBlock = filler;
    this.treeChance = treeChance;
    this.baseHeight = baseHeight;
  }
}
