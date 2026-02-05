package math.noise.base;

import math.Mathf;
import math.noise.Noise3D;

/**
 * 3D Cellular (Worley / Voronoi) noise.
 *
 * <p>Computes distances to randomly distributed feature points in 3D grid cells.
 * Supports F1, F2, and F2-F1 variants.
 *
 * <p>This implementation returns raw distance values and does not normalize
 * or remap the output.
 */
public final class CellularNoise3D implements Noise3D {

  private final long seed;
  private final float cellSize;
  private final CellularDistance distanceType;
  private final CellularResult resultType;

  public CellularNoise3D(
      long seed,
      float cellSize,
      CellularDistance distanceType,
      CellularResult resultType) {

    this.seed = seed;
    this.cellSize = cellSize;
    this.distanceType = distanceType;
    this.resultType = resultType;
  }

  public CellularNoise3D(long seed, float cellSize) {
    this(seed, cellSize, CellularDistance.EUCLIDEAN, CellularResult.F1);
  }

  @Override
  public float sample(float x, float y, float z) {

    // Convert to cell space
    float cx = x / cellSize;
    float cy = y / cellSize;
    float cz = z / cellSize;

    int ix = Mathf.floorToInt(cx);
    int iy = Mathf.floorToInt(cy);
    int iz = Mathf.floorToInt(cz);

    float f1 = Float.MAX_VALUE;
    float f2 = Float.MAX_VALUE;

    // 3x3x3 neighborhood
    for (int dz = -1; dz <= 1; dz++) {
      for (int dy = -1; dy <= 1; dy++) {
        for (int dx = -1; dx <= 1; dx++) {

          int nx = ix + dx;
          int ny = iy + dy;
          int nz = iz + dz;

          // Feature point inside cell
          float fx = nx + random01(nx, ny, nz, seed);
          float fy = ny + random01(nx, ny, nz, seed + 1);
          float fz = nz + random01(nx, ny, nz, seed + 2);

          float dxp = fx - cx;
          float dyp = fy - cy;
          float dzp = fz - cz;

          float dist = distance(dxp, dyp, dzp);

          if (dist < f1) {
            f2 = f1;
            f1 = dist;
          } else if (dist < f2) {
            f2 = dist;
          }
        }
      }
    }

    return switch (resultType) {
      case F1 -> f1;
      case F2 -> f2;
      case F2_MINUS_F1 -> f2 - f1;
    };
  }

  private float distance(float x, float y, float z) {
    return switch (distanceType) {
      case EUCLIDEAN -> Mathf.sqrt(x * x + y * y + z * z);
      case MANHATTAN -> Mathf.abs(x) + Mathf.abs(y) + Mathf.abs(z);
      case CHEBYSHEV ->
          Math.max(Mathf.abs(x), Math.max(Mathf.abs(y), Mathf.abs(z)));
    };
  }

  /**
   * Deterministic hash → [0, 1)
   */
  private static float random01(int x, int y, int z, long seed) {
    long h = seed;
    h ^= x * 0x632BE59BD9B4E019L;
    h ^= y * 0x9E3779B97F4A7C15L;
    h ^= z * 0x85157AF5L;

    h ^= (h >> 27);
    h *= 0x94D049BB133111EBL;
    h ^= (h >> 31);

    return (h & 0xFFFFFF) / (float) 0x1000000;
  }
}
