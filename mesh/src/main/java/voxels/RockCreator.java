package voxels;

import java.util.Collection;

import mesh.Mesh3D;
import mesh.creator.primitives.IcoSphereCreator;
import mesh.modifier.deform.NoiseModifier;
import mesh.modifier.transform.ScaleModifier;
import voxels.mesh.MeshVoxelizer;

public class RockCreator implements VoxelCreator {

  private int width;
  private int height;
  private int depth;
  private int subdivisions = 1;

  public RockCreator(int width, int height, int depth, int subdivisions) {
    this.width = width;
    this.height = height;
    this.depth = depth;
    this.subdivisions = subdivisions;
  }

  @Override
  public Collection<Voxel> create() {
    Mesh3D mesh = new IcoSphereCreator(0.5f, subdivisions).create();
    new NoiseModifier(0, 0.1f).modify(mesh);
    new ScaleModifier(width, height, depth).modify(mesh);

    Collection<Voxel> voxels = new MeshVoxelizer().voxelize(mesh);

    return voxels;
  }
}
