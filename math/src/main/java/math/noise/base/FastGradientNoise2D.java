package math.noise.base;

import math.Mathf;
import math.noise.Noise2D;

/**
 * Fast 2D gradient noise implementation.
 *
 * <p>This is a lightweight alternative to Perlin/Simplex noise using lattice-based gradients with
 * smooth interpolation.
 *
 * <p>Compared to Simplex:
 *
 * <ul>
 *   <li>No skewing/unskewing
 *   <li>Fewer arithmetic operations
 *   <li>Very cache-friendly
 * </ul>
 *
 * <p>Returns values roughly in the range {@code [-1, 1]}.
 *
 * <h3>Common Use Cases</h3>
 *
 * <ul>
 *   <li>Base terrain heightmaps
 *   <li>Climate and biome maps
 *   <li>FBM layers
 *   <li>Large-scale procedural worlds
 * </ul>
 */
public final class FastGradientNoise2D implements Noise2D {

  private static final float[][] GRADIENTS = {
    {1, 0}, {-1, 0}, {0, 1}, {0, -1},
    {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
  };

  private final short[] perm = new short[512];

  public FastGradientNoise2D(long seed) {
    short[] p = new short[256];
    for (short i = 0; i < 256; i++) {
      p[i] = i;
    }

    // Fisher–Yates shuffle using LCG
    long state = seed;
    for (int i = 255; i >= 0; i--) {
      state = state * 6364136223846793005L + 1442695040888963407L;
      int r = (int) ((state >>> 24) % (i + 1));
      if (r < 0) r += (i + 1);

      short tmp = p[i];
      p[i] = p[r];
      p[r] = tmp;
    }

    for (int i = 0; i < 512; i++) {
      perm[i] = p[i & 255];
    }
  }

  @Override
  public float sample(float x, float y) {

    int x0 = Mathf.floorToInt(x);
    int y0 = Mathf.floorToInt(y);

    int x1 = x0 + 1;
    int y1 = y0 + 1;

    float dx = x - x0;
    float dy = y - y0;

    float sx = fade(dx);
    float sy = fade(dy);

    float n00 = dotGradient(x0, y0, dx, dy);
    float n10 = dotGradient(x1, y0, dx - 1f, dy);
    float n01 = dotGradient(x0, y1, dx, dy - 1f);
    float n11 = dotGradient(x1, y1, dx - 1f, dy - 1f);

    float ix0 = Mathf.lerp(n00, n10, sx);
    float ix1 = Mathf.lerp(n01, n11, sx);

    return Mathf.lerp(ix0, ix1, sy);
  }

  private float dotGradient(int x, int y, float dx, float dy) {
    int h = perm[(x + perm[y & 255]) & 255] & 7;
    float[] g = GRADIENTS[h];
    return g[0] * dx + g[1] * dy;
  }

  /** Quintic fade curve (same as improved Perlin). */
  private static float fade(float t) {
    return t * t * t * (t * (t * 6f - 15f) + 10f);
  }
}
