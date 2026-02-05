package math.noise.base;

import math.Mathf;
import math.noise.Noise3D;

/**
 * Fast 3D gradient noise implementation.
 *
 * <p>This is a lightweight, grid-based gradient noise suitable for volumetric data. It trades some
 * isotropy for speed and simplicity compared to Simplex noise.
 *
 * <p>Characteristics:
 *
 * <ul>
 *   <li>Deterministic and stateless
 *   <li>No skewing or simplex lookup
 *   <li>Very fast and cache-friendly
 *   <li>Ideal for FBM, terrain volumes, and deformation
 * </ul>
 *
 * <p>Returns values roughly in the range {@code [-1, 1]}.
 *
 * <h3>Common Use Cases</h3>
 *
 * <ul>
 *   <li>Terrain displacement
 *   <li>Cave and density fields
 *   <li>Mesh deformation via normals
 *   <li>FBM and domain warping
 * </ul>
 */
public final class FastGradientNoise3D implements Noise3D {

  // 12 classic gradient directions (Perlin-style)
  private static final float[][] GRADIENTS = {
    {1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0},
    {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1},
    {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}
  };

  private final short[] perm = new short[512];

  /**
   * Creates a new fast gradient noise generator.
   *
   * @param seed deterministic seed value
   */
  public FastGradientNoise3D(long seed) {
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
  public float sample(float x, float y, float z) {

    int x0 = Mathf.floorToInt(x);
    int y0 = Mathf.floorToInt(y);
    int z0 = Mathf.floorToInt(z);

    int x1 = x0 + 1;
    int y1 = y0 + 1;
    int z1 = z0 + 1;

    float dx = x - x0;
    float dy = y - y0;
    float dz = z - z0;

    float sx = fade(dx);
    float sy = fade(dy);
    float sz = fade(dz);

    float n000 = dotGradient(x0, y0, z0, dx, dy, dz);
    float n100 = dotGradient(x1, y0, z0, dx - 1, dy, dz);
    float n010 = dotGradient(x0, y1, z0, dx, dy - 1, dz);
    float n110 = dotGradient(x1, y1, z0, dx - 1, dy - 1, dz);

    float n001 = dotGradient(x0, y0, z1, dx, dy, dz - 1);
    float n101 = dotGradient(x1, y0, z1, dx - 1, dy, dz - 1);
    float n011 = dotGradient(x0, y1, z1, dx, dy - 1, dz - 1);
    float n111 = dotGradient(x1, y1, z1, dx - 1, dy - 1, dz - 1);

    float ix00 = Mathf.lerp(n000, n100, sx);
    float ix10 = Mathf.lerp(n010, n110, sx);
    float ix01 = Mathf.lerp(n001, n101, sx);
    float ix11 = Mathf.lerp(n011, n111, sx);

    float iy0 = Mathf.lerp(ix00, ix10, sy);
    float iy1 = Mathf.lerp(ix01, ix11, sy);

    return Mathf.lerp(iy0, iy1, sz);
  }

  private float dotGradient(int x, int y, int z, float dx, float dy, float dz) {
    int h = perm[(x + perm[(y + perm[z & 255]) & 255]) & 255] % GRADIENTS.length;

    float[] g = GRADIENTS[h];
    return g[0] * dx + g[1] * dy + g[2] * dz;
  }

  /** Quintic fade curve (smoothstep^2). */
  private static float fade(float t) {
    return t * t * t * (t * (t * 6f - 15f) + 10f);
  }
}
