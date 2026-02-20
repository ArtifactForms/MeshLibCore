package math.sampling;

import java.util.List;
import math.Vector2f;

/**
 * Defines a strategy for generating 2D point distributions within a bounded domain.
 *
 * <p>Implementations of this interface generate sets of points according to a specific spatial
 * sampling algorithm (e.g. Poisson-disk, jittered grid, random).
 *
 * <p>The sampler operates in local 2D space and is intended to be deterministic with respect to the
 * provided seed. Typical usage includes procedural placement of objects such as foliage, rocks, or
 * gameplay entities.
 *
 * <p>This interface represents a pure sampling operation and must not maintain internal state
 * between invocations.
 */
@FunctionalInterface
public interface PointSampler2D {

  /**
   * Generates a set of points within the specified 2D domain.
   *
   * <p>The coordinate system is local to the sampling domain. The origin is assumed to be at {@code
   * (0, 0)}, and the domain spans {@code width} units along the X axis and {@code height} units
   * along the Y axis.
   *
   * @param width the width of the sampling domain
   * @param height the height of the sampling domain
   * @param seed random seed used to ensure deterministic output
   * @return a list of sampled points in local 2D coordinates
   */
  List<Vector2f> sample(float width, float height, long seed);
}
