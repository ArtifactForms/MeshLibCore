package voxels;

import java.util.Collection;
import java.util.HashSet;

public class FilledVoxelSphereCreator implements VoxelCreator {

  private final int radius;

  private final Voxel center;

  private final SamplingMode samplingMode;

  public FilledVoxelSphereCreator(int radius) {
    this(new Voxel(0, 0, 0), radius);
  }

  public FilledVoxelSphereCreator(Voxel center, int radius) {
    this(center, radius, SamplingMode.CENTER);
  }

  public FilledVoxelSphereCreator(Voxel center, int radius, SamplingMode samplingMode) {
    this.center = center;
    this.radius = radius;
    this.samplingMode = samplingMode;
  }

  @Override
  public Collection<Voxel> create() {
    HashSet<Voxel> voxels = new HashSet<>();

    double rSquared = (double) radius * radius;
    double shift = (samplingMode == SamplingMode.CENTER) ? 0.5 : 0.0;

    // Iterate within the bounding box of the sphere
    for (int x = -radius; x <= radius; x++) {
      double dx2 = Math.pow(x + shift, 2);

      for (int y = -radius; y <= radius; y++) {
        double dy2 = Math.pow(y + shift, 2);

        // Optimization: Skip the Z-loop if the XY-plane distance already exceeds the radius
        if (dx2 + dy2 <= rSquared) {
          for (int z = -radius; z <= radius; z++) {
            double dz2 = Math.pow(z + shift, 2);

            if (dx2 + dy2 + dz2 <= rSquared) {
              voxels.add(new Voxel(center.x() + x, center.y() + y, center.z() + z));
            }
          }
        }
      }
    }

    return voxels;
  }
}
