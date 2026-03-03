package mesh.geometry;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh;

/**
 * Utility class for read-only geometric computations on meshes.
 *
 * <p>This class contains pure geometric analysis methods. It does not modify mesh topology and does
 * not cache or store derived data.
 *
 * <p>All methods are stateless and thread-safe as long as the provided {@link Mesh} implementation
 * is not modified concurrently.
 */
public final class MeshGeometryUtil {

  private MeshGeometryUtil() {
    throw new AssertionError("No instances allowed.");
  }

  /* ===================================================================== */
  /*  Face Normal                                                          */
  /* ===================================================================== */

  /**
   * Computes the normalized face normal using Newell's method.
   *
   * <p>This method allocates a new {@link Vector3f} instance.
   *
   * <p>The returned vector is normalized unless the face is degenerate (fewer than 3 vertices or
   * zero area), in which case the zero vector is returned.
   *
   * @param mesh the mesh containing the face vertices
   * @param face the face for which to compute the normal
   * @return the normalized face normal (or zero vector if degenerate)
   * @throws IllegalArgumentException if mesh or face is null
   */
  public static Vector3f calculateFaceNormal(Mesh mesh, Face3D face) {
    return calculateFaceNormal(mesh, face, new Vector3f());
  }

  /**
   * Computes the normalized face normal using Newell's method and writes the result into the
   * provided store vector.
   *
   * <p>The resulting vector is normalized unless the face is degenerate.
   *
   * @param mesh the mesh containing the face vertices
   * @param face the face for which to compute the normal
   * @param store the vector to store the result in (must not be null)
   * @return the normalized face normal (same instance as {@code store})
   * @throws IllegalArgumentException if mesh or face is null
   */
  public static Vector3f calculateFaceNormal(Mesh mesh, Face3D face, Vector3f store) {
    calculateFaceNormalRaw(mesh, face, store);
    store.normalizeLocal();
    return store;
  }

  /**
   * Computes the raw (non-normalized) face normal using Newell's method and writes the result into
   * the provided store vector.
   *
   * <p>The magnitude of the resulting vector is proportional to the face area. This is useful for
   * area-weighted vertex normal computation.
   *
   * <p>If the face has fewer than 3 vertices or is degenerate (zero area), the zero vector is
   * returned.
   *
   * @param mesh the mesh containing the face vertices
   * @param face the face for which to compute the normal
   * @param store the vector to store the result in (must not be null)
   * @return the raw (non-normalized) face normal (same instance as {@code store})
   * @throws IllegalArgumentException if mesh or face is null
   */
  public static Vector3f calculateFaceNormalRaw(Mesh mesh, Face3D face, Vector3f store) {
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

    return store;
  }

  /* ===================================================================== */
  /*  Face Center                                                          */
  /* ===================================================================== */

  /**
   * Computes the geometric center (centroid) of the given face.
   *
   * <p>This method allocates a new {@link Vector3f} instance.
   *
   * <p>The centroid is calculated as the arithmetic mean of all face vertex positions.
   *
   * @param mesh the mesh containing the face vertices
   * @param face the face whose centroid should be computed
   * @return the geometric center of the face
   * @throws IllegalArgumentException if mesh or face is null
   */
  public static Vector3f calculateFaceCenter(Mesh mesh, Face3D face) {
    return calculateFaceCenter(mesh, face, new Vector3f());
  }

  /**
   * Computes the geometric center (centroid) of the given face and writes the result into the
   * provided store vector.
   *
   * <p>The centroid is calculated as the arithmetic mean of all face vertex positions.
   *
   * <p>If the face contains no vertices, the zero vector is returned.
   *
   * @param mesh the mesh containing the face vertices
   * @param face the face whose centroid should be computed
   * @param store the vector to store the result in (must not be null)
   * @return the geometric center of the face (same instance as {@code store})
   * @throws IllegalArgumentException if mesh or face is null
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
