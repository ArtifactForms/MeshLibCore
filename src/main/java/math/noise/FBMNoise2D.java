package math.noise;

/**
 * Fractal Brownian Motion (fBm) noise in 2D.
 *
 * <p>This noise implementation layers multiple octaves of a base {@link Noise2D} source to produce
 * fractal detail across multiple scales.
 *
 * <p>Each octave samples the base noise at an increased frequency and reduced amplitude:
 *
 * <ul>
 *   <li><b>frequency</b> is multiplied by {@code lacunarity} per octave
 *   <li><b>amplitude</b> is multiplied by {@code persistence} per octave
 * </ul>
 *
 * <p>The resulting value is the weighted sum of all octave samples. No normalization or clamping is
 * performed by this class.
 *
 * <p>This class is stateless and deterministic as long as the base noise is deterministic.
 *
 * @see Noise2D
 */
public final class FBMNoise2D implements Noise2D {

  /** Base noise source sampled at increasing frequencies. */
  private final Noise2D base;

  /** Number of noise layers (octaves). */
  private final int octaves;

  /** Amplitude multiplier per octave (commonly in range (0, 1]). */
  private final float persistence;

  /** Frequency multiplier per octave (commonly >= 2). */
  private final float lacunarity;

  /**
   * Creates a new 2D fBm noise generator.
   *
   * @param base base noise source
   * @param octaves number of octaves to accumulate
   * @param persistence amplitude multiplier per octave
   * @param lacunarity frequency multiplier per octave
   */
  public FBMNoise2D(Noise2D base, int octaves, float persistence, float lacunarity) {
    this.base = base;
    this.octaves = octaves;
    this.persistence = persistence;
    this.lacunarity = lacunarity;
  }

  /**
   * Samples the fBm noise at the given 2D position.
   *
   * <p>The returned value is the unnormalized sum of all octave contributions. Depending on the
   * base noise and parameters, the value may exceed {@code [0, 1]}.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @return accumulated fBm noise value
   */
  @Override
  public float sample(float x, float y) {
    float value = 0f;
    float amplitude = 1f;
    float frequency = 1f;

    for (int i = 0; i < octaves; i++) {
      value += base.sample(x * frequency, y * frequency) * amplitude;
      amplitude *= persistence;
      frequency *= lacunarity;
    }

    return value;
  }
}
