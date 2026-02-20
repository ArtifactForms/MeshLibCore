package math.noise.map;

/**
 * A dense, mutable 2D scalar field backed by a linear array.
 *
 * <p>{@code NoiseMap} represents sampled noise data such as heightmaps, density fields, masks, or
 * biome maps. It is designed as a low-level, cache-friendly data container with minimal
 * abstraction.
 *
 * <h3>Memory Layout</h3>
 *
 * <p>Data is stored in a single {@code float[]} in row-major order:
 *
 * <pre>
 * index = y * width + x
 * </pre>
 *
 * <p>This layout enables:
 *
 * <ul>
 *   <li>Fast sequential iteration
 *   <li>Efficient bulk processing
 *   <li>Zero-overhead interop with image buffers and GPU uploads
 * </ul>
 *
 * <h3>Design Intent</h3>
 *
 * <ul>
 *   <li>Minimal API: no bounds checks, no hidden normalization
 *   <li>Explicit ownership of dimensions and memory
 *   <li>Suitable for reuse across multiple processing stages
 * </ul>
 *
 * <p>Higher-level operations such as normalization, remapping, visualization, and analysis are
 * intentionally implemented in separate utility classes.
 *
 * @see NoiseMapBuilder
 * @see NoiseMapImageConverter
 */
public final class NoiseMap {

  /** Width of the map in samples (x-axis). */
  public final int width;

  /** Height of the map in samples (y-axis). */
  public final int height;

  /**
   * Linear backing array storing noise values.
   *
   * <p>Layout is {@code data[y * width + x]}. Values are typically in the range {@code [0, 1]}, but
   * this is not enforced by the class.
   */
  public final float[] data; // linear, cache-friendly

  /**
   * Creates a new {@code NoiseMap} with the given dimensions.
   *
   * <p>The backing array is allocated immediately and initialized to zero.
   *
   * @param width map width in samples (must be > 0)
   * @param height map height in samples (must be > 0)
   */
  public NoiseMap(int width, int height) {
    this.width = width;
    this.height = height;
    this.data = new float[width * height];
  }

  /**
   * Returns the value at the given coordinates.
   *
   * <p>No bounds checking is performed for performance reasons.
   *
   * @param x x-coordinate in {@code [0, width)}
   * @param y y-coordinate in {@code [0, height)}
   * @return the stored scalar value
   */
  public float get(int x, int y) {
    return data[y * width + x];
  }

  /**
   * Sets the value at the given coordinates.
   *
   * <p>No bounds checking is performed for performance reasons.
   *
   * @param x x-coordinate in {@code [0, width)}
   * @param y y-coordinate in {@code [0, height)}
   * @param v value to store
   */
  public void set(int x, int y, float v) {
    data[y * width + x] = v;
  }
}
