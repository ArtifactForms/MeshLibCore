package math.noise.base;

/**
 * Defines the distance metric used by cellular (Worley-style) noise algorithms.
 *
 * <p>The chosen distance function determines the geometric appearance of the resulting cell
 * structures (e.g. round cells, diamond shapes, or grid-aligned patterns).
 *
 * <p>This enum is typically used when computing the distance from a sample point to the nearest
 * feature point inside a cell.
 *
 * @see CellularNoise2D
 */
public enum CellularDistance {

  /**
   * Euclidean distance (L2 norm).
   *
   * <p>Produces round, organic-looking cells. This is the most common and visually natural choice
   * for terrain, stone, clouds, and organic patterns.
   *
   * <pre>
   * d = sqrt(dx² + dy²)
   * </pre>
   */
  EUCLIDEAN,

  /**
   * Manhattan distance (L1 norm).
   *
   * <p>Produces diamond-shaped cells aligned to the axes. Useful for stylized maps, city layouts,
   * or grid-based patterns.
   *
   * <pre>
   * d = |dx| + |dy|
   * </pre>
   */
  MANHATTAN,

  /**
   * Chebyshev distance (L∞ norm).
   *
   * <p>Produces square cells with hard edges. Often used for tile-based worlds, voxel-like
   * structures, or artificial-looking patterns.
   *
   * <pre>
   * d = max(|dx|, |dy|)
   * </pre>
   */
  CHEBYSHEV
}
