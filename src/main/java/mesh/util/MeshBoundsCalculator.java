package mesh.util;

import java.util.List;

import math.Bounds;
import math.Vector3f;
import mesh.Mesh3D;

public class MeshBoundsCalculator {

  /**
   * Calculates the axis-aligned bounding box (AABB) for the given 3D mesh.
   *
   * @param mesh The mesh object whose bounding box needs to be calculated.
   * @return A {@link Bounds} object representing the calculated bounding box.
   */
  public static Bounds calculateBounds(Mesh3D mesh) {
    List<Vector3f> vertices = mesh.getVertices();
    if (vertices.isEmpty()) {
      return new Bounds(Vector3f.ZERO, Vector3f.ZERO);
    }

    Vector3f min = new Vector3f(vertices.get(0));
    Vector3f max = new Vector3f(vertices.get(0));

    for (Vector3f vertex : vertices) {
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
