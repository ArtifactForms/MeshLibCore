package math.noise.map;

import java.awt.Color;
import java.util.Arrays;

/**
 * Height-based biome color mapper with linear interpolation between regions.
 */
public final class BiomeColorMapper implements HeightColorMapper {

  private final BiomeStop[] stops;

  public BiomeColorMapper(BiomeStop... stops) {
    if (stops.length < 2) {
      throw new IllegalArgumentException("At least two biome stops required");
    }
    this.stops = Arrays.copyOf(stops, stops.length);
  }

  @Override
  public Color map(float h) {
    h = Math.max(0f, Math.min(1f, h));

    for (int i = 0; i < stops.length - 1; i++) {
      BiomeStop a = stops[i];
      BiomeStop b = stops[i + 1];

      if (h >= a.height() && h <= b.height()) {
        float t = (h - a.height()) / (b.height() - a.height());
        return lerp(a.color(), b.color(), t);
      }
    }

    return stops[stops.length - 1].color();
  }

  private static Color lerp(Color a, Color b, float t) {
    int r = (int) (a.getRed()   + t * (b.getRed()   - a.getRed()));
    int g = (int) (a.getGreen() + t * (b.getGreen() - a.getGreen()));
    int bl = (int) (a.getBlue() + t * (b.getBlue()  - a.getBlue()));
    return new Color(r, g, bl);
  }
}
