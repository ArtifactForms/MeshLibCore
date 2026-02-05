package math.noise.map;

import java.awt.Color;

/**
 * Defines a color stop at a specific normalized height value.
 *
 * <p>A {@code BiomeStop} represents a control point in a height-to-color mapping.
 * It is typically used by {@link HeightColorMapper} implementations to interpolate
 * colors between multiple height regions (biomes).
 *
 * <p>The {@code height} value is expected to be normalized and lie within the range
 * {@code [0, 1]}, where:
 *
 * <ul>
 *   <li>{@code 0.0} usually represents the lowest elevation (e.g. deep water)</li>
 *   <li>{@code 1.0} usually represents the highest elevation (e.g. snow peaks)</li>
 * </ul>
 *
 * <p>{@code BiomeStop} instances are commonly ordered by ascending height and interpreted
 * as interpolation boundaries between neighboring stops.
 *
 * <p>This record is immutable and intended as a simple value object.
 *
 * @param height the normalized height value in the range {@code [0,1]}
 * @param color the color associated with this height
 *
 * @see HeightColorMapper
 * @see BiomeColorMapper
 */
public record BiomeStop(float height, Color color) {

  /**
   * Creates a new biome stop.
   *
   * @throws IllegalArgumentException if {@code height} is outside {@code [0,1]}
   * @throws NullPointerException if {@code color} is {@code null}
   */
  public BiomeStop {
    if (height < 0f || height > 1f) {
      throw new IllegalArgumentException("Height must be in [0,1]");
    }
    if (color == null) {
      throw new NullPointerException("Color must not be null");
    }
  }
}
