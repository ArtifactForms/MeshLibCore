package math.noise;

/**
 * Domain-warping noise wrapper.
 *
 * <p>Warps the input domain (x,y) using one or two warp noise fields before sampling the source
 * noise.
 */
public final class DomainWarpNoise2D implements Noise2D {

  private final Noise2D source;
  private final Noise2D warp;

  private final float warpFrequency;
  private final float warpStrength;

  private final float offsetX;
  private final float offsetY;

  /** Simple domain warp with one warp noise. */
  public DomainWarpNoise2D(Noise2D source, Noise2D warp, float warpFrequency, float warpStrength) {
    this(source, warp, warpFrequency, warpStrength, 1000f, 2000f);
  }

  /** Domain warp with explicit axis offsets. */
  public DomainWarpNoise2D(
      Noise2D source,
      Noise2D warp,
      float warpFrequency,
      float warpStrength,
      float offsetX,
      float offsetY) {
    this.source = source;
    this.warp = warp;
    this.warpFrequency = warpFrequency;
    this.warpStrength = warpStrength;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
  }

  @Override
  public float sample(float x, float y) {

    float nx = x * warpFrequency;
    float ny = y * warpFrequency;

    float wx = warp.sample(nx, ny) * warpStrength;
    float wy = warp.sample(nx + offsetX, ny + offsetY) * warpStrength;

    return source.sample(x + wx, y + wy);
  }
}
