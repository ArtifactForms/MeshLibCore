package voxels;

import java.util.Collection;

/**
 * Represents a generator that creates a discrete set of {@link Voxel} coordinates.
 *
 * <p>Implementations generate voxel-based geometric primitives such as circles, spheres, cylinders,
 * or custom shapes.
 *
 * <p>The returned collection represents a discrete point cloud in integer voxel space. No
 * guarantees are made about ordering.
 */
public interface VoxelCreator {

  /**
   * Generates a collection of voxels.
   *
   * @return a collection of unique voxel coordinates
   */
  Collection<Voxel> create();
}
