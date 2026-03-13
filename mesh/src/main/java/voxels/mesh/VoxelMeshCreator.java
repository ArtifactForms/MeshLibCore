package voxels.mesh;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import voxels.Voxel;

public class VoxelMeshCreator implements IMeshCreator {

  private static final float blockSize = 1.0f;

  private static final float radius = blockSize * 0.5f;

  private int nextIndex;

  private Mesh3D mesh;

  private final Set<Voxel> voxels;

  public VoxelMeshCreator(Collection<Voxel> voxels) {
    this.voxels = new HashSet<>(voxels);
  }

  @Override
  public Mesh3D create() {
    nextIndex = 0;
    mesh = new Mesh3D();

    for (Voxel voxel : voxels) {
      createBaseBlock(voxel);
    }

    return mesh;
  }

  private boolean shouldRender(Voxel neighbor) {
    return !voxels.contains(neighbor);
  }

  private void addFace() {
    mesh.addFace(nextIndex, nextIndex + 1, nextIndex + 2, nextIndex + 3);
    nextIndex += 4;
  }

  private void createBaseBlock(Voxel v) {

    float x = v.x();
    float y = v.y();
    float z = v.z();

    // (+Y)
    if (shouldRender(v.add(0, 1, 0))) {
      mesh.addVertex(-radius + x, +radius + y, -radius + z);
      mesh.addVertex(-radius + x, +radius + y, +radius + z);
      mesh.addVertex(+radius + x, +radius + y, +radius + z);
      mesh.addVertex(+radius + x, +radius + y, -radius + z);
      addFace();
    }

    // (-Y)
    if (shouldRender(v.add(0, -1, 0))) {
      mesh.addVertex(-radius + x, -radius + y, -radius + z);
      mesh.addVertex(+radius + x, -radius + y, -radius + z);
      mesh.addVertex(+radius + x, -radius + y, +radius + z);
      mesh.addVertex(-radius + x, -radius + y, +radius + z);
      addFace();
    }

    // (+Z)
    if (shouldRender(v.add(0, 0, 1))) {
      mesh.addVertex(-radius + x, -radius + y, +radius + z);
      mesh.addVertex(+radius + x, -radius + y, +radius + z);
      mesh.addVertex(+radius + x, +radius + y, +radius + z);
      mesh.addVertex(-radius + x, +radius + y, +radius + z);
      addFace();
    }

    // (-Z)
    if (shouldRender(v.add(0, 0, -1))) {
      mesh.addVertex(-radius + x, -radius + y, -radius + z);
      mesh.addVertex(-radius + x, +radius + y, -radius + z);
      mesh.addVertex(+radius + x, +radius + y, -radius + z);
      mesh.addVertex(+radius + x, -radius + y, -radius + z);
      addFace();
    }

    // (+X)
    if (shouldRender(v.add(1, 0, 0))) {
      mesh.addVertex(+radius + x, -radius + y, -radius + z);
      mesh.addVertex(+radius + x, +radius + y, -radius + z);
      mesh.addVertex(+radius + x, +radius + y, +radius + z);
      mesh.addVertex(+radius + x, -radius + y, +radius + z);
      addFace();
    }

    // (-X)
    if (shouldRender(v.add(-1, 0, 0))) {
      mesh.addVertex(-radius + x, -radius + y, -radius + z);
      mesh.addVertex(-radius + x, -radius + y, +radius + z);
      mesh.addVertex(-radius + x, +radius + y, +radius + z);
      mesh.addVertex(-radius + x, +radius + y, -radius + z);
      addFace();
    }
  }
}
