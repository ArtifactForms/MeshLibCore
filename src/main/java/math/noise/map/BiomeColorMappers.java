package math.noise.map;

import java.awt.Color;

/** Common biome-based color mappers for terrain visualization. */
public final class BiomeColorMappers {

  private BiomeColorMappers() {}

  /** Earth-like biome coloring with water, vegetation, rock and snow. */
  public static final HeightColorMapper EARTH =
      new BiomeColorMapper(
          new BiomeStop(0.00f, new Color(0, 10, 60)), // deep water
          new BiomeStop(0.40f, new Color(30, 120, 200)), // shallow water
          new BiomeStop(0.45f, new Color(210, 200, 140)), // beach
          new BiomeStop(0.65f, new Color(60, 170, 80)), // grass
          new BiomeStop(0.80f, new Color(30, 110, 50)), // forest
          new BiomeStop(0.92f, new Color(120, 120, 120)), // rock
          new BiomeStop(1.00f, Color.WHITE) // snow
          );

  /** Debug / analysis heatmap coloring (low values = cold, high values = hot). */
  public static final HeightColorMapper HEATMAP =
      new BiomeColorMapper(
          new BiomeStop(0.00f, new Color(0, 0, 128)), // deep blue
          new BiomeStop(0.25f, new Color(0, 128, 255)), // cyan
          new BiomeStop(0.50f, new Color(0, 255, 0)), // green
          new BiomeStop(0.75f, new Color(255, 255, 0)), // yellow
          new BiomeStop(0.90f, new Color(255, 128, 0)), // orange
          new BiomeStop(1.00f, new Color(255, 0, 0)) // red
          );
}
