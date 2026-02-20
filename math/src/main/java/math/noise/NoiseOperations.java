package math.noise;

/**
 * Static factory and utility methods for performing operations on {@link Noise2D} and {@link
 * Noise3D} functions.
 *
 * <p>This class provides convenience methods for combining multiple noise sources using common
 * {@link CombineMode} operations without explicitly instantiating combination classes.
 *
 * <p>The returned noise functions are deterministic, stateless, and do not apply any normalization,
 * clamping, or remapping automatically. Any post-processing must be applied explicitly using
 * dedicated noise utilities.
 *
 * <h3>Design Intent</h3>
 *
 * <ul>
 *   <li>Provide a concise and readable API for noise signal operations
 *   <li>Encourage explicit, intention-revealing noise graphs
 *   <li>Avoid hidden behavior or implicit value assumptions
 * </ul>
 *
 * <p>This class is not intended to be instantiated.
 *
 * @see Noise2D
 * @see Noise3D
 * @see NoiseCombine2D
 * @see NoiseCombine3D
 * @see CombineMode
 */
public final class NoiseOperations {

  private NoiseOperations() {}

  /* ------------------------------------------------------------------ */
  /* 2D Noise                                                            */
  /* ------------------------------------------------------------------ */

  /**
   * Creates a noise function that adds two {@link Noise2D} sources.
   *
   * @param a the first noise source
   * @param b the second noise source
   * @return a combined noise function using {@link CombineMode#ADD}
   * @throws NullPointerException if any argument is {@code null}
   */
  public static Noise2D add(Noise2D a, Noise2D b) {
    return new NoiseCombine2D(a, b, CombineMode.ADD);
  }

  /**
   * Creates a noise function that multiplies two {@link Noise2D} sources.
   *
   * <p>Commonly used for masking and density modulation.
   *
   * @param a the first noise source
   * @param b the second noise source
   * @return a combined noise function using {@link CombineMode#MULTIPLY}
   * @throws NullPointerException if any argument is {@code null}
   */
  public static Noise2D multiply(Noise2D a, Noise2D b) {
    return new NoiseCombine2D(a, b, CombineMode.MULTIPLY);
  }

  /**
   * Creates a noise function that selects the minimum of two {@link Noise2D} sources.
   *
   * @param a the first noise source
   * @param b the second noise source
   * @return a combined noise function using {@link CombineMode#MIN}
   * @throws NullPointerException if any argument is {@code null}
   */
  public static Noise2D min(Noise2D a, Noise2D b) {
    return new NoiseCombine2D(a, b, CombineMode.MIN);
  }

  /**
   * Creates a noise function that selects the maximum of two {@link Noise2D} sources.
   *
   * @param a the first noise source
   * @param b the second noise source
   * @return a combined noise function using {@link CombineMode#MAX}
   * @throws NullPointerException if any argument is {@code null}
   */
  public static Noise2D max(Noise2D a, Noise2D b) {
    return new NoiseCombine2D(a, b, CombineMode.MAX);
  }

  /* ------------------------------------------------------------------ */
  /* 3D Noise                                                            */
  /* ------------------------------------------------------------------ */

  /**
   * Creates a noise function that adds two {@link Noise3D} sources.
   *
   * @param a the first noise source
   * @param b the second noise source
   * @return a combined noise function using {@link CombineMode#ADD}
   * @throws NullPointerException if any argument is {@code null}
   */
  public static Noise3D add(Noise3D a, Noise3D b) {
    return new NoiseCombine3D(a, b, CombineMode.ADD);
  }

  /**
   * Creates a noise function that multiplies two {@link Noise3D} sources.
   *
   * <p>Commonly used for volumetric masking and density modulation.
   *
   * @param a the first noise source
   * @param b the second noise source
   * @return a combined noise function using {@link CombineMode#MULTIPLY}
   * @throws NullPointerException if any argument is {@code null}
   */
  public static Noise3D multiply(Noise3D a, Noise3D b) {
    return new NoiseCombine3D(a, b, CombineMode.MULTIPLY);
  }

  /**
   * Creates a noise function that selects the minimum of two {@link Noise3D} sources.
   *
   * @param a the first noise source
   * @param b the second noise source
   * @return a combined noise function using {@link CombineMode#MIN}
   * @throws NullPointerException if any argument is {@code null}
   */
  public static Noise3D min(Noise3D a, Noise3D b) {
    return new NoiseCombine3D(a, b, CombineMode.MIN);
  }

  /**
   * Creates a noise function that selects the maximum of two {@link Noise3D} sources.
   *
   * @param a the first noise source
   * @param b the second noise source
   * @return a combined noise function using {@link CombineMode#MAX}
   * @throws NullPointerException if any argument is {@code null}
   */
  public static Noise3D max(Noise3D a, Noise3D b) {
    return new NoiseCombine3D(a, b, CombineMode.MAX);
  }
}
