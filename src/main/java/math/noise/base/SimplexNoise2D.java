package math.noise.base;

import math.Mathf;
import math.noise.Noise2D;

/**
 * 2D Simplex noise implementation.
 *
 * <p>Simplex noise is a gradient noise algorithm designed to reduce directional artifacts and
 * computational complexity compared to classic Perlin noise.
 *
 * <p>This implementation:
 *
 * <ul>
 *   <li>Uses proper isotropic 2D gradients
 *   <li>Is deterministic based on a {@code long} seed
 *   <li>Returns values roughly in the range {@code [-1, 1]}
 * </ul>
 *
 * <h3>Common Use Cases</h3>
 *
 * <ul>
 *   <li>Terrain heightmaps
 *   <li>FBM (fractal noise)
 *   <li>Domain warping
 *   <li>Procedural textures
 * </ul>
 */
public final class SimplexNoise2D implements Noise2D {

  // Skewing and unskewing factors for 2D
  private static final float F2 = 0.366025403f; // (sqrt(3) - 1) / 2
  private static final float G2 = 0.211324865f; // (3 - sqrt(3)) / 6

  // 12 gradient directions (isotropic)
  private static final float[][] GRAD2 = {
    {1, 1}, {-1, 1}, {1, -1}, {-1, -1},
    {1, 0}, {-1, 0}, {0, 1}, {0, -1},
    {1, 2}, {-1, 2}, {1, -2}, {-1, -2}
  };

  private final short[] perm = new short[512];

  public SimplexNoise2D(long seed) {
    short[] p = new short[256];
    for (short i = 0; i < 256; i++) {
      p[i] = i;
    }

    // Shuffle using seed
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

    // Skew input space
    float s = (x + y) * F2;
    int i = Mathf.floorToInt(x + s);
    int j = Mathf.floorToInt(y + s);

    float t = (i + j) * G2;
    float X0 = i - t;
    float Y0 = j - t;

    float x0 = x - X0;
    float y0 = y - Y0;

    // Determine simplex triangle
    int i1, j1;
    if (x0 > y0) {
      i1 = 1;
      j1 = 0;
    } else {
      i1 = 0;
      j1 = 1;
    }

    float x1 = x0 - i1 + G2;
    float y1 = y0 - j1 + G2;
    float x2 = x0 - 1f + 2f * G2;
    float y2 = y0 - 1f + 2f * G2;

    float n0 = corner(i, j, x0, y0);
    float n1 = corner(i + i1, j + j1, x1, y1);
    float n2 = corner(i + 1, j + 1, x2, y2);

    // Scale to roughly [-1, 1]
    return 45f * (n0 + n1 + n2);
  }

  private float corner(int i, int j, float x, float y) {
    float t = 0.5f - x * x - y * y;
    if (t <= 0f) return 0f;

    int ii = i & 255;
    int jj = j & 255;

    int h = perm[ii + perm[jj]] % 12;

    float gx = GRAD2[h][0];
    float gy = GRAD2[h][1];

    float dot = gx * x + gy * y;

    t *= t;
    return t * t * dot;
  }
}
