package math.noise;

import java.util.Objects;

/**
 * Combines two {@link Noise2D} sources into a single 2D noise function
 * using a specified {@link CombineMode}.
 *
 * <p>This class performs a deterministic, per-sample combination of two
 * noise signals. No normalization, clamping, or remapping is applied
 * automatically; such post-processing must be performed explicitly
 * using dedicated noise utilities.</p>
 *
 * <p>{@code NoiseCombine2D} is intended as a low-level composition building
 * block and can be freely chained with other noise functions.</p>
 *
 * <h3>Typical Use Cases</h3>
 * <ul>
 *   <li>Combining large-scale biome noise with local detail noise</li>
 *   <li>Masking one noise field by another</li>
 *   <li>Prioritizing or excluding features via min/max composition</li>
 * </ul>
 *
 * @see CombineMode
 * @see Noise2D
 */
public final class NoiseCombine2D implements Noise2D {

  private final Noise2D a;
  private final Noise2D b;
  private final CombineMode mode;

  /**
   * Creates a new combined 2D noise function.
   *
   * @param a the first noise source
   * @param b the second noise source
   * @param mode the combination mode to apply
   * @throws NullPointerException if any argument is {@code null}
   */
  public NoiseCombine2D(Noise2D a, Noise2D b, CombineMode mode) {
    this.a = Objects.requireNonNull(a);
    this.b = Objects.requireNonNull(b);
    this.mode = Objects.requireNonNull(mode);
  }

  /**
   * Samples the combined noise at the given 2D position.
   *
   * <p>The two source noises are sampled at the same position and
   * combined according to the selected {@link CombineMode}.</p>
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @return the combined noise value (not automatically normalized)
   */
  @Override
  public float sample(float x, float y) {
    float va = a.sample(x, y);
    float vb = b.sample(x, y);

    return switch (mode) {
      case ADD -> va + vb;
      case MULTIPLY -> va * vb;
      case MIN -> Math.min(va, vb);
      case MAX -> Math.max(va, vb);
    };
  }
}
