package math.noise;

/**
 * Defines how two {@link Noise2D} or {@link Noise3D} sources are combined into a single noise
 * signal.
 *
 * <p>This enum is used by noise composition utilities (e.g. {@code NoiseCombine2D} or {@code
 * NoiseCombine3D}) to build more expressive noise functions from simple building blocks.
 *
 * <p>Each mode represents a deterministic, stateless operation applied per sample position. No
 * normalization or clamping is performed automatically; such post-processing should be applied
 * explicitly if required.
 *
 * <h3>Design Intent</h3>
 *
 * <ul>
 *   <li>Enable modular composition of noise fields
 *   <li>Keep noise operations explicit and predictable
 *   <li>Avoid hidden normalization or value assumptions
 * </ul>
 *
 * <h3>Typical Use Cases</h3>
 *
 * <ul>
 *   <li>Biome masks combined with local detail noise
 *   <li>Density modulation for foliage or props
 *   <li>Feature prioritization and exclusion zones
 * </ul>
 */
public enum CombineMode {

  /**
   * Adds both noise values together.
   *
   * <p>Useful for layering multiple noise sources to increase variation or to introduce secondary
   * detail. Often followed by remapping or clamping.
   */
  ADD,

  /**
   * Multiplies both noise values.
   *
   * <p>Commonly used for masking and density control, where one noise signal modulates the
   * influence of another.
   */
  MULTIPLY,

  /**
   * Selects the minimum of both noise values.
   *
   * <p>Useful for enforcing upper bounds, carving paths, or creating exclusion zones where the
   * weaker signal should dominate.
   */
  MIN,

  /**
   * Selects the maximum of both noise values.
   *
   * <p>Useful for feature dominance and prioritization, where the stronger signal should override
   * others (e.g. rivers over terrain detail).
   */
  MAX
}
