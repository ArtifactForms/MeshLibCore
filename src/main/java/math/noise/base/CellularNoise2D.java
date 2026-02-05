package math.noise.base;

import math.Mathf;
import math.noise.Noise2D;

/**
 * 2D Cellular (Worley / Voronoi) noise.
 *
 * <p>Computes distances to randomly distributed feature points in grid cells.
 * Supports F1, F2, and F2-F1 variants.
 */
public final class CellularNoise2D implements Noise2D {

  private final long seed;
  private final float cellSize;
  private final CellularDistance distanceType;
  private final CellularResult resultType;

  public CellularNoise2D(
      long seed,
      float cellSize,
      CellularDistance distanceType,
      CellularResult resultType) {

    this.seed = seed;
    this.cellSize = cellSize;
    this.distanceType = distanceType;
    this.resultType = resultType;
  }

  public CellularNoise2D(long seed, float cellSize) {
    this(seed, cellSize, CellularDistance.EUCLIDEAN, CellularResult.F1);
  }

  @Override
  public float sample(float x, float y) {

    // Convert to cell space
    float cx = x / cellSize;
    float cy = y / cellSize;

    int ix = Mathf.floorToInt(cx);
    int iy = Mathf.floorToInt(cy);

    float f1 = Float.MAX_VALUE;
    float f2 = Float.MAX_VALUE;

    // 3x3 neighborhood is sufficient in 2D
    for (int dy = -1; dy <= 1; dy++) {
      for (int dx = -1; dx <= 1; dx++) {

        int nx = ix + dx;
        int ny = iy + dy;

        // Feature point inside cell
        float fx = nx + random01(nx, ny, seed);
        float fy = ny + random01(nx, ny, seed + 1);

        float dxp = fx - cx;
        float dyp = fy - cy;

        float dist = distance(dxp, dyp);

        if (dist < f1) {
          f2 = f1;
          f1 = dist;
        } else if (dist < f2) {
          f2 = dist;
        }
      }
    }

    return switch (resultType) {
      case F1 -> f1;
      case F2 -> f2;
      case F2_MINUS_F1 -> f2 - f1;
    };
  }

  private float distance(float x, float y) {
    return switch (distanceType) {
      case EUCLIDEAN -> Mathf.sqrt(x * x + y * y);
      case MANHATTAN -> Mathf.abs(x) + Mathf.abs(y);
      case CHEBYSHEV -> Math.max(Mathf.abs(x), Mathf.abs(y));
    };
  }

  /**
   * Deterministic hash → [0, 1)
   */
  private static float random01(int x, int y, long seed) {
    long h = seed;
    h ^= x * 0x632BE59BD9B4E019L;
    h ^= y * 0x9E3779B97F4A7C15L;
    h ^= (h >> 27);
    h *= 0x94D049BB133111EBL;
    h ^= (h >> 31);
    return (h & 0xFFFFFF) / (float) 0x1000000;
  }
}
