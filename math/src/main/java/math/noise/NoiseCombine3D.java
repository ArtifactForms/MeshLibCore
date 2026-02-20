package math.noise;

import java.util.Objects;

/**
 * Combines two {@link Noise3D} sources into a single 3D noise function
 * using a specified {@link CombineMode}.
 *
 * <p>This class performs a deterministic, per-sample combination of two
 * noise signals. No normalization, clamping, or remapping is applied
 * automatically; such post-processing must be performed explicitly
 * using dedicated noise utilities.</p>
 *
 * <p>{@code NoiseCombine3D} is intended as a low-level composition building
 * block and can be freely chained with other noise functions.</p>
 *
 * <h3>Typical Use Cases</h3>
 * <ul>
 *   <li>Combining large-scale volumetric noise with local detail noise</li>
 *   <li>Masking one 3D noise field by another</li>
 *   <li>Prioritizing or excluding volumetric features via min/max composition</li>
 * </ul>
 *
 * @see CombineMode
 * @see Noise3D
 */
public final class NoiseCombine3D implements Noise3D {

  private final Noise3D a;
  private final Noise3D b;
  private final CombineMode mode;

  /**
   * Creates a new combined 3D noise function.
   *
   * @param a the first noise source
   * @param b the second noise source
   * @param mode the combination mode to apply
   * @throws NullPointerException if any argument is {@code null}
   */
  public NoiseCombine3D(Noise3D a, Noise3D b, CombineMode mode) {
    this.a = Objects.requireNonNull(a);
    this.b = Objects.requireNonNull(b);
    this.mode = Objects.requireNonNull(mode);
  }

  /**
   * Samples the combined noise at the given 3D position.
   *
   * <p>The two source noises are sampled at the same position and
   * combined according to the selected {@link CombineMode}.</p>
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @param z z-coordinate
   * @return the combined noise value (not automatically normalized)
   */
  @Override
  public float sample(float x, float y, float z) {
    float va = a.sample(x, y, z);
    float vb = b.sample(x, y, z);

    return switch (mode) {
      case ADD -> va + vb;
      case MULTIPLY -> va * vb;
      case MIN -> Math.min(va, vb);
      case MAX -> Math.max(va, vb);
    };
  }
}
