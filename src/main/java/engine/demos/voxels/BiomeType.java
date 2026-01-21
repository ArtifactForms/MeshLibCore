package engine.demos.voxels;

/**
 * Represents different types of biomes that can be used in the voxel engine. Each biome type can
 * have distinct characteristics such as terrain features, weather patterns, and vegetation.
 */
public enum BiomeType {

  /**
   * Represents flat and grassy terrain, typically with minimal elevation changes. Common features:
   * grass, flowers, and small hills.
   */
  PLAINS,

  /**
   * Represents arid terrain with minimal vegetation and sandy surfaces. Common features: dunes,
   * cacti, and occasional dry vegetation.
   */
  DESERT,

  /**
   * Represents densely wooded terrain with a variety of tree types and plants. Common features:
   * trees, shrubs, and diverse wildlife.
   */
  FOREST,

  /**
   * Represents rocky and elevated terrain, often with steep cliffs and peaks. Common features:
   * rugged landscapes, cliffs, and sparse vegetation.
   */
  MOUNTAIN,

  /**
   * Represents cold, snowy terrain with frozen ground and icy features. Common features:
   * snow-covered landscapes, ice, and tundra-like vegetation.
   */
  SNOW,
}
