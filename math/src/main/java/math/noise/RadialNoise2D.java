package math.noise;

import math.Mathf;

/**
 * A deterministic 2D noise function producing radial (concentric) wave patterns.
 *
 * <p>The noise value is computed as a sine function of the distance from the origin, resulting in
 * circular bands centered at (0, 0).
 *
 * <p>This noise is purely mathematical and highly periodic. It is best suited for:
 *
 * <ul>
 *   <li>Masks and falloff functions
 *   <li>Stylized or non-natural terrain features
 *   <li>Energy fields, ripples, or debug visualizations
 * </ul>
 *
 * <p>No normalization, clamping, or remapping is performed. The raw output range is approximately
 * {@code [-1, 1]} and should be processed explicitly if required.
 *
 * <h3>Parameters</h3>
 *
 * <ul>
 *   <li><b>frequency</b> — Controls the spacing between concentric rings
 *   <li><b>phase</b> — Phase offset applied to the radial wave
 * </ul>
 *
 * <p>This class is immutable and thread-safe.
 *
 * @see Noise2D
 */
public final class RadialNoise2D implements Noise2D {

  private final float frequency;
  private final float phase;

  /**
   * Creates a radial noise function with the given frequency and phase.
   *
   * @param frequency controls the spacing between rings
   * @param phase phase offset applied to the radial wave
   */
  public RadialNoise2D(float frequency, float phase) {
    this.frequency = frequency;
    this.phase = phase;
  }

  /**
   * Creates a radial noise function with the given frequency and zero phase.
   *
   * @param frequency controls the spacing between rings
   */
  public RadialNoise2D(float frequency) {
    this(frequency, 0f);
  }

  /**
   * Samples the noise function at the given 2D position.
   *
   * <p>The input coordinates are interpreted in continuous space and are not internally scaled or
   * wrapped.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @return radial noise value (approximately in {@code [-1, 1]})
   */
  @Override
  public float sample(float x, float y) {
    float r = Mathf.sqrt(x * x + y * y);
    return Mathf.sin(r * frequency + phase);
  }
}
