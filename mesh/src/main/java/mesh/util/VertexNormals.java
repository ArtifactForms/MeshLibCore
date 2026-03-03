package mesh.util;

import java.util.ArrayList;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.geometry.MeshGeometryUtil;

/**
 * Utility class for computing per-vertex normals of a {@link Mesh3D}.
 *
 * <p>The computed normals are <b>area-weighted</b>. This means that each face contributes to its
 * adjacent vertices proportionally to its surface area. This produces physically more accurate and
 * visually smoother shading compared to averaging already normalized face normals.
 *
 * <p>The algorithm works as follows:
 *
 * <ol>
 *   <li>Initialize one zero-vector normal per vertex.
 *   <li>For each face, compute its raw (non-normalized) normal using {@link
 *       MeshGeometryUtil#calculateFaceNormalRaw}.
 *   <li>Add the face normal to all vertices referenced by that face.
 *   <li>Normalize all accumulated vertex normals.
 * </ol>
 *
 * <p>This class is stateless and thread-safe as long as the provided mesh is not modified
 * concurrently.
 */
public final class VertexNormals {

  private VertexNormals() {
    // Prevent instantiation
    throw new AssertionError("No instances allowed.");
  }

  /**
   * Computes area-weighted vertex normals for the given mesh.
   *
   * <p>The returned list contains one normal per vertex. The list index corresponds directly to the
   * vertex index in the mesh.
   *
   * <p>If a vertex is not referenced by any face or only by degenerate faces (zero area), its
   * resulting normal may be the zero vector.
   *
   * @param mesh the mesh for which vertex normals should be computed
   * @return a list containing one normalized {@link Vector3f} per vertex
   * @throws NullPointerException if {@code mesh} is null
   */
  public static List<Vector3f> calculate(Mesh3D mesh) {
    int vertexCount = mesh.getVertexCount();
    List<Vector3f> normals = new ArrayList<>(vertexCount);

    // Initialize normals with (0,0,0)
    for (int i = 0; i < vertexCount; i++) {
      normals.add(new Vector3f());
    }

    int faceCount = mesh.getFaceCount();
    Vector3f store = new Vector3f();

    for (int i = 0; i < faceCount; i++) {
      Face3D face = mesh.getFaceAt(i);

      // Area-weighted face normal (non-normalized)
      Vector3f faceNormal = MeshGeometryUtil.calculateFaceNormalRaw(mesh, face, store);

      int faceVertexCount = face.getVertexCount();
      for (int j = 0; j < faceVertexCount; j++) {
        int vertexIndex = face.getIndexAt(j);
        normals.get(vertexIndex).addLocal(faceNormal);
      }
    }

    // Final normalization per vertex
    for (Vector3f normal : normals) {
      normal.normalizeLocal();
    }

    return normals;
  }
}
