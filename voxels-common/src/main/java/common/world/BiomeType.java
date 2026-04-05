package common.world;

/**
 * Represents different types of biomes that can be used in the voxel engine. Each biome type can
 * have distinct characteristics such as terrain features, weather patterns, and vegetation.
 */
public enum BiomeType {

  /**
   * Represents flat and grassy terrain, typically with minimal elevation changes. Common features:
   * grass, flowers, and small hills.
   */
  PLAINS(1.0f, 1.0f),
  /**
   * Represents arid terrain with minimal vegetation and sandy surfaces. Common features: dunes,
   * cacti, and occasional dry vegetation.
   */
  DESERT(0.8f, 0.5f), // Flacher, weichere Dünen
  /**
   * Represents densely wooded terrain with a variety of tree types and plants. Common features:
   * trees, shrubs, and diverse wildlife.
   */
  FOREST(1.2f, 1.1f), // Etwas hügeliger
  /**
   * Represents rocky and elevated terrain, often with steep cliffs and peaks. Common features:
   * rugged landscapes, cliffs, and sparse vegetation.
   */
  MOUNTAIN(2.8f, 1.8f), // Steile Gipfel, massive Höhen
  /**
   * Represents cold, snowy terrain with frozen ground and icy features. Common features:
   * snow-covered landscapes, ice, and tundra-like vegetation.
   */
  SNOW(1.1f, 0.9f);

  private final float heightInfluence;

  private final float scaleModifier;

  BiomeType(float heightInfluence, float scaleModifier) {
    this.heightInfluence = heightInfluence;
    this.scaleModifier = scaleModifier;
  }

  public float getHeightInfluence() {
    return heightInfluence;
  }

  public float getScaleModifier() {
    return scaleModifier;
  }
}
