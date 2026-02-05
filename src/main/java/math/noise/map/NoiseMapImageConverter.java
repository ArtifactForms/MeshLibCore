package math.noise.map;

import java.awt.Color;
import java.awt.image.BufferedImage;

import math.Mathf;

/**
 * Presentation-layer utility for converting {@link NoiseMap} data into image or pixel
 * representations.
 *
 * <p>This class performs <b>read-only</b> mapping from normalized scalar fields (typically
 * heightmaps in the range {@code [0,1]}) into:
 *
 * <ul>
 *   <li>{@link BufferedImage} instances for tooling, debugging, and export
 *   <li>{@link PixelBuffer} instances for real-time, allocation-free rendering backends
 * </ul>
 *
 * <p>No methods in this class modify the underlying {@link NoiseMap}.
 */
public final class NoiseMapImageConverter {

  private NoiseMapImageConverter() {
    // Utility class – no instances
  }

  // ---------------------------------------------------------------------------
  // BufferedImage output (tooling / debug / offline usage)
  // ---------------------------------------------------------------------------

  /**
   * Converts a normalized {@link NoiseMap} into a grayscale {@link BufferedImage}.
   *
   * <p>Noise values are expected to be in {@code [0,1]} and are clamped defensively. Each value is
   * mapped linearly to a grayscale intensity in {@code [0,255]}.
   *
   * @param noiseMap the source noise map
   * @return a newly allocated grayscale image
   */
  public static BufferedImage toGrayscale(NoiseMap noiseMap) {
    BufferedImage image =
        new BufferedImage(noiseMap.width, noiseMap.height, BufferedImage.TYPE_INT_RGB);

    for (int y = 0; y < noiseMap.height; y++) {
      for (int x = 0; x < noiseMap.width; x++) {

        float v = Mathf.clamp01(noiseMap.get(x, y));
        int gray = (int) (v * 255f);

        // Using Color here is acceptable for tooling paths
        image.setRGB(x, y, new Color(gray, gray, gray).getRGB());
      }
    }

    return image;
  }

  /**
   * Converts a normalized {@link NoiseMap} into a colored {@link BufferedImage} using a {@link
   * HeightColorMapper}.
   *
   * <p>This method is intended for visualization of heightmaps, biome maps, masks, and other scalar
   * fields.
   *
   * @param noiseMap the source noise map
   * @param mapper mapping function from height {@code [0,1]} to color
   * @return a newly allocated color image
   */
  public static BufferedImage toColorMap(NoiseMap noiseMap, HeightColorMapper mapper) {
    BufferedImage image =
        new BufferedImage(noiseMap.width, noiseMap.height, BufferedImage.TYPE_INT_RGB);

    for (int y = 0; y < noiseMap.height; y++) {
      for (int x = 0; x < noiseMap.width; x++) {

        float v = Mathf.clamp01(noiseMap.get(x, y));
        image.setRGB(x, y, mapper.map(v).getRGB());
      }
    }

    return image;
  }

  // ---------------------------------------------------------------------------
  // PixelBuffer output (runtime / zero-allocation path)
  // ---------------------------------------------------------------------------

  /**
   * Writes a colored representation of a {@link NoiseMap} into an existing {@link PixelBuffer}.
   *
   * <p>This method performs no allocations and is intended for real-time use.
   *
   * @param noiseMap the source noise map
   * @param mapper height-to-color mapping function
   * @param out destination pixel buffer (must match size)
   * @throws IllegalArgumentException if buffer size does not match the noise map
   */
  public static void toColorPixels(NoiseMap noiseMap, HeightColorMapper mapper, PixelBuffer out) {

    if (out.width != noiseMap.width || out.height != noiseMap.height) {
      throw new IllegalArgumentException("PixelBuffer size mismatch");
    }

    float[] h = noiseMap.data;
    int[] p = out.pixels;

    for (int i = 0; i < p.length; i++) {
      float v = Mathf.clamp01(h[i]);
      p[i] = mapper.map(v).getRGB();
    }
  }

  /**
   * Writes a grayscale representation of a {@link NoiseMap} into an existing {@link PixelBuffer}.
   *
   * <p>This method performs no allocations and is suitable for real-time heightmap previews, debug
   * views, or GPU uploads.
   *
   * @param noiseMap the source noise map
   * @param out destination pixel buffer (must match size)
   * @throws IllegalArgumentException if buffer size does not match the noise map
   */
  public static void toGrayscalePixels(NoiseMap noiseMap, PixelBuffer out) {
    if (out.width != noiseMap.width || out.height != noiseMap.height) {
      throw new IllegalArgumentException("PixelBuffer size mismatch");
    }

    float[] h = noiseMap.data;
    int[] p = out.pixels;

    for (int i = 0; i < p.length; i++) {
      float v = Mathf.clamp01(h[i]);
      int gray = (int) (v * 255f);
      p[i] = new Color(gray, gray, gray).getRGB();
    }
  }
}
