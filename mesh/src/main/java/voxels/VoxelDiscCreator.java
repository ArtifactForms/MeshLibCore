package voxels;

import java.util.Collection;
import java.util.HashSet;

public class VoxelDiscCreator implements VoxelCreator {

  private final int radius;
  private final Voxel center;
  private final SamplingMode samplingMode;

  public VoxelDiscCreator(int radius) {
    this(new Voxel(0, 0, 0), radius);
  }

  public VoxelDiscCreator(Voxel center, int radius) {
    this(center, radius, SamplingMode.CENTER);
  }

  public VoxelDiscCreator(Voxel center, int radius, SamplingMode samplingMode) {
    this.center = center;
    this.radius = radius;
    this.samplingMode = samplingMode;
  }

  @Override
  public Collection<Voxel> create() {

    HashSet<Voxel> voxels = new HashSet<>();

    int rSquared = radius * radius;

    double shift = (samplingMode == SamplingMode.CENTER) ? 0.5 : 0.0;

    for (int x = -radius; x <= radius; x++) {
      for (int z = -radius; z <= radius; z++) {

        double dx = x + shift;
        double dz = z + shift;

        if (dx * dx + dz * dz <= rSquared) {
          voxels.add(new Voxel(center.x() + x, center.y(), center.z() + z));
        }
      }
    }

    return voxels;
  }
}
