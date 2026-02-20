package mesh.util;

import math.Bounds;
import math.Vector3f;
import mesh.Mesh;

public class MeshBoundsCalculator {

  /**
   * Calculates the axis-aligned bounding box (AABB) for the given 3D mesh.
   *
   * @param mesh The mesh object whose bounding box needs to be calculated.
   * @return A {@link Bounds} object representing the calculated bounding box.
   */
  public static Bounds calculateBounds(Mesh mesh) {
    if (mesh.getVertexCount() == 0) return new Bounds(Vector3f.ZERO, Vector3f.ZERO);

    Vector3f min = new Vector3f(mesh.getVertexAt(0));
    Vector3f max = new Vector3f(mesh.getVertexAt(0));

    for (int i = 0; i < mesh.getVertexCount(); i++) {
      Vector3f vertex = mesh.getVertexAt(i);
      min.set(
          Math.min(min.getX(), vertex.getX()),
          Math.min(min.getY(), vertex.getY()),
          Math.min(min.getZ(), vertex.getZ()));
      max.set(
          Math.max(max.getX(), vertex.getX()),
          Math.max(max.getY(), vertex.getY()),
          Math.max(max.getZ(), vertex.getZ()));
    }

    return new Bounds(min, max);
  }
}
