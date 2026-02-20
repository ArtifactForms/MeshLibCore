package math.noise;

/**
 * Represents a deterministic 2D noise function.
 *
 * <p>A {@code Noise2D} maps a 2D position ({@code x, y}) to a continuous scalar value.
 * Implementations are expected to be deterministic for a given input and seed, making them suitable
 * for procedural generation.
 *
 * <p>Noise functions are commonly used to control density, variation, or masking in 2D domains such
 * as terrain surfaces, foliage placement, texture synthesis, or biome maps.
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
public interface Noise2D {

  /**
   * Samples the noise function at the given 2D position.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @return noise value in the range {@code [0, 1]}
   */
  float sample(float x, float y);
}
