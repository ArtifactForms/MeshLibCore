package math.sampling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import math.Vector2f;

/**
 * Generates a jittered grid distribution of points in 2D space.
 *
 * <p>The domain is divided into a regular grid, and one point is placed per cell with a small
 * random offset (jitter). This produces uniform coverage while avoiding the artificial look of a
 * perfect grid.
 *
 * <h3>Design intent</h3>
 *
 * <ul>
 *   <li>Fast and lightweight
 *   <li>Uniform spatial coverage
 *   <li>Slight randomness for natural variation
 * </ul>
 *
 * <p>This sampler is much faster than Poisson-disk sampling and does not require a spatial
 * acceleration grid or neighbor distance checks. It is intended for dense distributions where
 * strict minimum distances are unnecessary.
 *
 * <h3>Typical use cases</h3>
 *
 * <ul>
 *   <li>Grass
 *   <li>Flowers
 *   <li>Pebbles
 *   <li>Detail decals
 *   <li>Small environmental props
 * </ul>
 *
 * <p>The sampler is deterministic with respect to the provided seed and operates in local 2D space.
 * No acceleration structures or overengineering are involved.
 */
public final class JitteredGridSampling implements PointSampler2D {

  private final float cellSize;
  private final float jitter;

  /**
   * Creates a jittered grid sampler.
   *
   * @param cellSize size of each grid cell
   * @param jitter maximum random offset applied to each point, expressed as a fraction of {@code
   *     cellSize} (range {@code [0, 1]})
   */
  public JitteredGridSampling(float cellSize, float jitter) {
    this.cellSize = cellSize;
    this.jitter = Math.max(0.0f, Math.min(1.0f, jitter));
  }

  /**
   * Generates a jittered grid distribution of points.
   *
   * @param width the width of the sampling domain
   * @param height the height of the sampling domain
   * @param seed random seed used to ensure deterministic output
   * @return list of sampled points in local 2D coordinates
   */
  @Override
  public List<Vector2f> sample(float width, float height, long seed) {
    Random random = new Random(seed);
    List<Vector2f> points = new ArrayList<>();

    int cellsX = (int) Math.ceil(width / cellSize);
    int cellsY = (int) Math.ceil(height / cellSize);

    float maxOffset = cellSize * jitter * 0.5f;

    for (int y = 0; y < cellsY; y++) {
      for (int x = 0; x < cellsX; x++) {

        float baseX = x * cellSize + cellSize * 0.5f;
        float baseY = y * cellSize + cellSize * 0.5f;

        float offsetX = (random.nextFloat() * 2f - 1f) * maxOffset;
        float offsetY = (random.nextFloat() * 2f - 1f) * maxOffset;

        float px = baseX + offsetX;
        float py = baseY + offsetY;

        if (px >= 0 && py >= 0 && px < width && py < height) {
          points.add(new Vector2f(px, py));
        }
      }
    }

    return points;
  }
}
