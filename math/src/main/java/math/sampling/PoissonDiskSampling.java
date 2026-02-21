package math.sampling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import math.Vector2f;

/**
 * Poisson-disk point sampler.
 *
 * <p>Poisson-disk sampling produces points that are randomly distributed while maintaining a
 * minimum distance between any two points. This results in natural-looking, evenly spaced
 * distributions without grid artifacts or clustering.
 *
 * <p>This implementation is based on Bridson's algorithm and is deterministic with respect to the
 * provided random seed.
 *
 * <p>Typical use cases include:
 *
 * <ul>
 *   <li>Procedural foliage placement (trees, bushes, rocks)
 *   <li>Object scattering on terrain
 *   <li>Blue-noise style spatial sampling
 * </ul>
 *
 * <p>The sampler operates in local 2D space (e.g. XZ plane) and is intended to be used per chunk or
 * region rather than globally.
 */
public final class PoissonDiskSampling implements PointSampler2D {

  private final float minDistance;
  private final int maxAttempts;

  /**
   * Creates a Poisson-disk sampler with the given configuration.
   *
   * @param minDistance minimum allowed distance between points
   * @param maxAttempts number of candidate samples per active point (typically 20–30)
   */
  public PoissonDiskSampling(float minDistance, int maxAttempts) {
    this.minDistance = minDistance;
    this.maxAttempts = maxAttempts;
  }

  /**
   * Generates a Poisson-disk distributed set of points for the given domain.
   *
   * @param width the width of the sampling domain
   * @param height the height of the sampling domain
   * @param seed random seed used to ensure deterministic output
   * @return list of sampled points in local 2D coordinates
   */
  @Override
  public List<Vector2f> sample(float width, float height, long seed) {
    return sample2D(width, height, minDistance, maxAttempts, seed);
  }

  /**
   * Static utility method for Poisson-disk sampling.
   *
   * <p>This method does not require creating an instance and is suitable for direct, one-off
   * sampling operations.
   */
  public static List<Vector2f> sample2D(
      float width, float height, float minDistance, int maxAttempts, long seed) {

    Random random = new Random(seed);

    float cellSize = (float) (minDistance / Math.sqrt(2));
    int gridWidth = (int) Math.ceil(width / cellSize);
    int gridHeight = (int) Math.ceil(height / cellSize);

    Vector2f[][] grid = new Vector2f[gridWidth][gridHeight];
    List<Vector2f> points = new ArrayList<>();
    List<Vector2f> active = new ArrayList<>();

    Vector2f initial = new Vector2f(random.nextFloat() * width, random.nextFloat() * height);

    points.add(initial);
    active.add(initial);
    grid[(int) (initial.x / cellSize)][(int) (initial.y / cellSize)] = initial;

    while (!active.isEmpty()) {
      int index = random.nextInt(active.size());
      Vector2f point = active.get(index);
      boolean found = false;

      for (int i = 0; i < maxAttempts; i++) {
        Vector2f candidate = generateCandidate(point, minDistance, random);

        if (!isInside(candidate, width, height)) {
          continue;
        }

        if (isFarEnough(candidate, grid, cellSize, minDistance)) {
          points.add(candidate);
          active.add(candidate);
          grid[(int) (candidate.x / cellSize)][(int) (candidate.y / cellSize)] = candidate;
          found = true;
          break;
        }
      }

      if (!found) {
        active.remove(index);
      }
    }

    return points;
  }

  /**
   * Generates a random candidate point around a given center point.
   *
   * <p>The candidate is placed at a random angle and at a distance between {@code minDistance} and
   * {@code 2 * minDistance} from the center.
   *
   * @param center the reference point
   * @param minDistance the minimum sampling distance
   * @param random random number generator
   * @return a new candidate point
   */
  private static Vector2f generateCandidate(Vector2f center, float minDistance, Random random) {

    float angle = random.nextFloat() * (float) (Math.PI * 2);
    float radius = minDistance * (1 + random.nextFloat());

    return new Vector2f(
        center.x + (float) Math.cos(angle) * radius, center.y + (float) Math.sin(angle) * radius);
  }

  /**
   * Checks whether a point lies inside the sampling domain.
   *
   * @param p the point to test
   * @param width domain width
   * @param height domain height
   * @return {@code true} if the point is inside the domain
   */
  private static boolean isInside(Vector2f p, float width, float height) {
    return p.x >= 0 && p.y >= 0 && p.x < width && p.y < height;
  }
  /**
   * Tests whether a candidate point maintains the minimum required distance to all nearby points
   * using a grid-based acceleration structure.
   *
   * <p>Only neighboring grid cells are checked, resulting in constant-time distance validation.
   *
   * @param candidate the candidate point
   * @param grid spatial acceleration grid
   * @param cellSize size of a single grid cell
   * @param minDistance minimum allowed distance between points
   * @return {@code true} if the candidate is sufficiently far from neighbors
   */
  private static boolean isFarEnough(
      Vector2f candidate, Vector2f[][] grid, float cellSize, float minDistance) {

    int gx = (int) (candidate.x / cellSize);
    int gy = (int) (candidate.y / cellSize);

    int startX = Math.max(0, gx - 2);
    int endX = Math.min(grid.length - 1, gx + 2);
    int startY = Math.max(0, gy - 2);
    int endY = Math.min(grid[0].length - 1, gy + 2);

    float minDistSq = minDistance * minDistance;

    for (int x = startX; x <= endX; x++) {
      for (int y = startY; y <= endY; y++) {
        Vector2f p = grid[x][y];
        if (p != null && p.distanceSquared(candidate) < minDistSq) {
          return false;
        }
      }
    }
    return true;
  }
}
