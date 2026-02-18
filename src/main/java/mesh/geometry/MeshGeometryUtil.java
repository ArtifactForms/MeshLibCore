package mesh.geometry;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh;

/**
 * Utility class for read-only geometric computations on meshes.
 *
 * <p>This class contains pure geometric analysis methods. It does NOT modify topology and does NOT
 * store derived data.
 */
public final class MeshGeometryUtil {

  private MeshGeometryUtil() {
    throw new AssertionError("No instances allowed.");
  }

  /* ===================================================================== */
  /*  Face Normal                                                          */
  /* ===================================================================== */

  /** Computes the face normal using Newell's method. Allocates a new Vector3f. */
  public static Vector3f calculateFaceNormal(Mesh mesh, Face3D face) {
    return calculateFaceNormal(mesh, face, new Vector3f());
  }

  /**
   * Computes the face normal using Newell's method. Writes the result into the provided store
   * vector.
   */
  public static Vector3f calculateFaceNormal(Mesh mesh, Face3D face, Vector3f store) {
    if (mesh == null || face == null) {
      throw new IllegalArgumentException("Mesh and face must not be null.");
    }

    store.set(0f, 0f, 0f);

    final int count = face.getVertexCount();
    if (count < 3) {
      return store;
    }

    for (int i = 0; i < count; i++) {
      final int currentIndex = face.getIndexAt(i);
      final int nextIndex = face.getIndexAt((i + 1) % count);

      final Vector3f current = mesh.getVertexAt(currentIndex);
      final Vector3f next = mesh.getVertexAt(nextIndex);

      final float x = (current.y - next.y) * (current.z + next.z);
      final float y = (current.z - next.z) * (current.x + next.x);
      final float z = (current.x - next.x) * (current.y + next.y);

      store.addLocal(x, y, z);
    }

    return store.normalizeLocal();
  }

  /* ===================================================================== */
  /*  Face Center                                                          */
  /* ===================================================================== */

  /** Computes the geometric center (centroid) of a face. Allocates a new Vector3f. */
  public static Vector3f calculateFaceCenter(Mesh mesh, Face3D face) {
    return calculateFaceCenter(mesh, face, new Vector3f());
  }

  /**
   * Computes the geometric center (centroid) of a face. Writes the result into the provided store
   * vector.
   */
  public static Vector3f calculateFaceCenter(Mesh mesh, Face3D face, Vector3f store) {
    if (mesh == null || face == null) {
      throw new IllegalArgumentException("Mesh and face must not be null.");
    }

    store.set(0f, 0f, 0f);

    final int count = face.getVertexCount();
    if (count == 0) {
      return store;
    }

    for (int i = 0; i < count; i++) {
      store.addLocal(mesh.getVertexAt(face.getIndexAt(i)));
    }

    return store.divideLocal(count);
  }
}
