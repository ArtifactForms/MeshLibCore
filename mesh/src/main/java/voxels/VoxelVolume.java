package voxels;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class VoxelVolume {

  private final Set<Voxel> voxels = new HashSet<>();

  public void add(Voxel v) {
    voxels.add(v);
  }

  public void add(int x, int y, int z) {
    voxels.add(new Voxel(x, y, z));
  }

  public boolean contains(Voxel v) {
    return voxels.contains(v);
  }

  public Set<Voxel> getVoxels() {
    return Collections.unmodifiableSet(voxels);
  }
}
