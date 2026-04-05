package voxels;

import java.util.Collection;
import java.util.HashSet;

public class VoxelCircleCreator implements VoxelCreator {

  private final int radius;

  private final Voxel center;

  public VoxelCircleCreator(int radius) {
    this(new Voxel(0, 0, 0), radius);
  }

  public VoxelCircleCreator(Voxel center, int radius) {
    this.radius = radius;
    this.center = center;
  }

  @Override
  public Collection<Voxel> create() {

    HashSet<Voxel> voxels = new HashSet<>();

    int x = radius;
    int z = 0;
    int decision = 1 - radius;

    while (x >= z) {

      addSymmetricPoints(voxels, x, z);

      z++;

      if (decision <= 0) {
        decision += 2 * z + 1;
      } else {
        x--;
        decision += 2 * (z - x) + 1;
      }
    }

    return voxels;
  }

  private void addSymmetricPoints(HashSet<Voxel> voxels, int x, int z) {

    int cx = center.x();
    int cy = center.y();
    int cz = center.z();

    voxels.add(new Voxel(cx + x, cy, cz + z));
    voxels.add(new Voxel(cx - x, cy, cz + z));
    voxels.add(new Voxel(cx + x, cy, cz - z));
    voxels.add(new Voxel(cx - x, cy, cz - z));

    voxels.add(new Voxel(cx + z, cy, cz + x));
    voxels.add(new Voxel(cx - z, cy, cz + x));
    voxels.add(new Voxel(cx + z, cy, cz - x));
    voxels.add(new Voxel(cx - z, cy, cz - x));
  }
}
