package math.noise.base;

import math.Mathf;
import math.noise.Noise3D;

/**
 * 3D Worley (Cellular) noise implementation.
 *
 * <p>Computes the distance from the sample position to the nearest randomly
 * distributed feature point inside a grid of 3D cells.
 *
 * <p>This implementation returns the raw F1 distance and does not apply
 * normalization, inversion, or remapping.
 *
 * <h3>Common Use Cases</h3>
 * <ul>
 *   <li>Volumetric cave and tunnel masks</li>
 *   <li>Rock and crystal formations</li>
 *   <li>Cloud density fields</li>
 *   <li>Voxel terrain materials</li>
 * </ul>
 */
public final class WorleyNoise3D implements Noise3D {

  private final long seed;
  private final float cellSize;
  private final CellularDistance metric;

  /**
   * Creates a new 3D Worley noise source.
   *
   * @param seed world seed used for deterministic feature point placement
   * @param cellSize size of a single Worley cell in world units
   * @param metric distance metric used for feature distance calculation
   */
  public WorleyNoise3D(long seed, float cellSize, CellularDistance metric) {
    this.seed = seed;
    this.cellSize = cellSize;
    this.metric = metric;
  }

  /**
   * Creates a 3D Worley noise source using Euclidean distance.
   *
   * @param seed world seed used for deterministic feature point placement
   * @param cellSize size of a single Worley cell in world units
   */
  public WorleyNoise3D(long seed, float cellSize) {
    this(seed, cellSize, CellularDistance.EUCLIDEAN);
  }

  @Override
  public float sample(float x, float y, float z) {
    // Convert world space to cell space
    float cx = x / cellSize;
    float cy = y / cellSize;
    float cz = z / cellSize;

    int cellX = Mathf.floorToInt(cx);
    int cellY = Mathf.floorToInt(cy);
    int cellZ = Mathf.floorToInt(cz);

    float minDist = Float.MAX_VALUE;

    // Check neighboring cells (3x3x3 neighborhood)
    for (int dz = -1; dz <= 1; dz++) {
      for (int dy = -1; dy <= 1; dy++) {
        for (int dx = -1; dx <= 1; dx++) {

          int nx = cellX + dx;
          int ny = cellY + dy;
          int nz = cellZ + dz;

          // Feature point inside the cell
          float fx = nx + random01(nx, ny, nz, seed);
          float fy = ny + random01(nx, ny, nz, seed ^ 0x9E3779B97F4A7C15L);
          float fz = nz + random01(nx, ny, nz, seed ^ 0xBF58476D1CE4E5B9L);

          float dxp = fx - cx;
          float dyp = fy - cy;
          float dzp = fz - cz;

          float dist = distance(dxp, dyp, dzp);
          minDist = Math.min(minDist, dist);
        }
      }
    }

    return minDist;
  }

  private float distance(float x, float y, float z) {
    return switch (metric) {
      case EUCLIDEAN -> Mathf.sqrt(x * x + y * y + z * z);
      case MANHATTAN -> Mathf.abs(x) + Mathf.abs(y) + Mathf.abs(z);
      case CHEBYSHEV -> Math.max(Math.max(Mathf.abs(x), Mathf.abs(y)), Mathf.abs(z));
    };
  }

  /**
   * Deterministic hash-based pseudo-random value in [0, 1).
   */
  private static float random01(int x, int y, int z, long seed) {
    long h = seed;
    h ^= x * 0x632BE59BD9B4E019L;
    h ^= y * 0x9E3779B97F4A7C15L;
    h ^= z * 0xBF58476D1CE4E5B9L;

    h ^= (h >>> 27);
    h *= 0x94D049BB133111EBL;
    h ^= (h >>> 31);

    // take top 24 bits → float in [0,1)
    return (h >>> 40) / (float) (1L << 24);
  }
}
