package math.noise;

/**
 * Represents a deterministic 3D noise function.
 *
 * <p>A {@code Noise3D} maps a 3D position ({@code x, y, z}) to a continuous scalar value.
 * Implementations are expected to be deterministic for a given input and seed, making them suitable
 * for procedural generation.
 *
 * <p>Noise functions are typically used to modulate density, structure, or variation in volumetric
 * contexts such as terrain, clouds, caves, or 3D material patterns.
 *
 * <h3>Value Range</h3>
 *
 * <p>The returned value is expected to be in the normalized range {@code [0, 1]}, unless explicitly
 * documented otherwise by the implementation.
 *
 * <h3>Design Intent</h3>
 *
 * <ul>
 *   <li>Stateless and side-effect free
 *   <li>Deterministic for stable procedural worlds
 *   <li>Composable via noise utility and combination functions
 * </ul>
 */
@FunctionalInterface
public interface Noise3D {

  /**
   * Samples the noise function at the given 3D position.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @param z z-coordinate
   * @return noise value in the range {@code [0, 1]}
   */
  float sample(float x, float y, float z);
}
