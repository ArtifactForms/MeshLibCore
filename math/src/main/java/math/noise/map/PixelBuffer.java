package math.noise.map;

/**
 * A lightweight, mutable pixel buffer backed by a linear {@code int[]} array.
 *
 * <p>{@code PixelBuffer} represents raw image data in a format suitable for direct upload to
 * rendering backends (e.g. textures, sprites, or images). It is commonly used as an output target
 * for noise-to-image conversions.
 *
 * <h3>Memory Layout</h3>
 *
 * <p>Pixels are stored in row-major order:
 *
 * <pre>
 * index = y * width + x
 * </pre>
 *
 * <p>Each entry in {@link #pixels} is expected to be a packed ARGB or RGB integer (depending on the
 * rendering backend).
 *
 * <h3>Design Intent</h3>
 *
 * <ul>
 *   <li>Minimal abstraction and zero per-pixel overhead
 *   <li>Explicit ownership of pixel memory
 *   <li>Reusable across frames to avoid allocations
 * </ul>
 *
 * <p>This class performs no validation, color conversion, or bounds checking. Higher-level logic is
 * intentionally handled by converter or renderer classes.
 *
 * @see NoiseMap
 * @see NoiseMapImageConverter
 */
public final class PixelBuffer {

  /** Width of the buffer in pixels (x-axis). */
  public final int width;

  /** Height of the buffer in pixels (y-axis). */
  public final int height;

  /**
   * Linear backing array storing pixel values.
   *
   * <p>Layout is {@code pixels[y * width + x]}.
   */
  public final int[] pixels;

  /**
   * Creates a new {@code PixelBuffer} with the given dimensions.
   *
   * <p>The backing array is allocated immediately and initialized to zero.
   *
   * @param width buffer width in pixels (must be > 0)
   * @param height buffer height in pixels (must be > 0)
   */
  public PixelBuffer(int width, int height) {
    this.width = width;
    this.height = height;
    this.pixels = new int[width * height];
  }
}
