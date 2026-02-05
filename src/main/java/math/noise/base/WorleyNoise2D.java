package math.noise.base;

import math.Mathf;
import math.noise.Noise2D;

/**
 * 2D Worley (Cellular) noise implementation.
 *
 * <p>Computes the distance from the sample position to the nearest randomly
 * distributed feature point inside a grid of cells.
 *
 * <p>This implementation returns the raw F1 distance and does not apply
 * normalization, inversion, or remapping.
 *
 * <h3>Common Use Cases</h3>
 * <ul>
 *   <li>Biome masks</li>
 *   <li>Cracked surfaces and ridges</li>
 *   <li>Rock and cellular patterns</li>
 *   <li>Stylized terrain features</li>
 * </ul>
 */
public final class WorleyNoise2D implements Noise2D {

  private final long seed;
  private final float cellSize;
  private final CellularDistance metric;

  public WorleyNoise2D(long seed, float cellSize, CellularDistance metric) {
    this.seed = seed;
    this.cellSize = cellSize;
    this.metric = metric;
  }

  public WorleyNoise2D(long seed, float cellSize) {
    this(seed, cellSize, CellularDistance.EUCLIDEAN);
  }

  @Override
  public float sample(float x, float y) {
    // Convert world space to cell space
    float cx = x / cellSize;
    float cy = y / cellSize;

    int cellX = Mathf.floorToInt(cx);
    int cellY = Mathf.floorToInt(cy);

    float minDist = Float.MAX_VALUE;

    // Check neighboring cells (3x3 neighborhood)
    for (int dy = -1; dy <= 1; dy++) {
      for (int dx = -1; dx <= 1; dx++) {

        int nx = cellX + dx;
        int ny = cellY + dy;

        // Feature point inside the cell
        float fx = nx + random01(nx, ny, seed);
        float fy = ny + random01(nx, ny, seed ^ 0x9E3779B97F4A7C15L);

        float dxp = fx - cx;
        float dyp = fy - cy;

        float dist = distance(dxp, dyp);
        minDist = Math.min(minDist, dist);
      }
    }

    return minDist;
  }

  private float distance(float x, float y) {
    return switch (metric) {
      case EUCLIDEAN -> Mathf.sqrt(x * x + y * y);
      case MANHATTAN -> Mathf.abs(x) + Mathf.abs(y);
      case CHEBYSHEV -> Math.max(Mathf.abs(x), Mathf.abs(y));
    };
  }

  /**
   * Deterministic hash-based pseudo-random value in [0, 1).
   */
  private static float random01(int x, int y, long seed) {
    long h = seed;
    h ^= x * 0x632BE59BD9B4E019L;
    h ^= y * 0x9E3779B97F4A7C15L;

    h ^= (h >>> 27);
    h *= 0x94D049BB133111EBL;
    h ^= (h >>> 31);

    // take top 24 bits → float in [0,1)
    return (h >>> 40) / (float) (1L << 24);
  }
}
