package mesh.analysis;

import java.util.HashSet;
import java.util.Set;

import mesh.FaceView;
import mesh.Mesh;

public final class MeshAnalysis {

  private MeshAnalysis() {}

  /**
   * Represents a unique edge in the mesh, defined by two vertex indices. The order of indices is
   * normalized (smaller index first) to allow for correct hash-based comparison in a Set.
   */
  private record Edge(int a, int b) {}

  /**
   * Calculates the total number of unique edges in the mesh.
   *
   * <p>This method iterates through all faces and their edges. To ensure that shared edges between
   * adjacent faces are only counted once, it normalizes each edge by storing the smaller vertex
   * index first.
   *
   * <p><b>Time Complexity:</b> O(F * N), where
   *
   * <ul>
   *   <li>F = total number of faces in the mesh
   *   <li>N = maximum number of vertices per face
   * </ul>
   *
   * @param mesh the mesh to analyze
   * @return the number of unique edges in the mesh
   */
  public static int edgeCount(Mesh mesh) {
    Set<Edge> edges = new HashSet<>();

    for (int faceIndex = 0; faceIndex < mesh.getFaceCount(); faceIndex++) {
      FaceView face = mesh.getFaceAt(faceIndex);
      int n = face.getVertexCount();

      for (int vertexIndex = 0; vertexIndex < n; vertexIndex++) {
        int v0 = face.getIndexAt(vertexIndex);
        int v1 = face.getIndexAt((vertexIndex + 1) % n);

        int a = Math.min(v0, v1);
        int b = Math.max(v0, v1);

        edges.add(new Edge(a, b));
      }
    }

    return edges.size();
  }

  /**
   * Checks whether the mesh contains any loose (unreferenced) vertices or invalid indices.
   *
   * <p>A vertex is considered <i>loose</i> if it is not referenced by any face index in the mesh.
   * An index is considered <i>invalid</i> if it is outside the range {@code [0, vertexCount)}.
   *
   * <p>This method operates purely on vertex indices and does not mutate the mesh.
   *
   * <p><b>Time Complexity:</b> O(V + F), where
   *
   * <ul>
   *   <li>V = number of vertices
   *   <li>F = total number of face indices
   * </ul>
   *
   * @param mesh the mesh to validate
   * @return {@code true} if every vertex is referenced by at least one face, {@code false} if one
   *     or more loose vertices are found
   * @throws IllegalArgumentException if a face contains an index outside the valid vertex range
   */
  public static boolean meshHasNoLooseVertices(Mesh mesh) {
    int vertexCount = mesh.getVertexCount();
    boolean[] used = new boolean[vertexCount];

    for (int faceIndex = 0; faceIndex < mesh.getFaceCount(); faceIndex++) {
      FaceView face = mesh.getFaceAt(faceIndex);
      for (int i = 0; i < face.getVertexCount(); i++) {
        int index = face.getIndexAt(i);

        if (index < 0 || index >= vertexCount) {
          throw new IllegalArgumentException(
              "Face "
                  + faceIndex
                  + " contains invalid vertex index: "
                  + index
                  + ". Vertex count is: "
                  + vertexCount);
        }

        used[index] = true;
      }
    }

    for (boolean isUsed : used) {
      if (!isUsed) {
        return false; // found loose vertex
      }
    }

    return true;
  }

  /**
   * Checks if all faces in the mesh are valid (non-degenerate). A face is degenerate if it has
   * fewer than 3 vertices or if the vertices are not unique.
   *
   * <p><b>Time Complexity:</b> O(F * N), where
   *
   * <ul>
   *   <li>F = number of faces
   *   <li>N = maximum number of vertices per face (usually 3 or 4)
   * </ul>
   *
   * @param mesh the mesh to validate
   * @return true if all faces are valid, false if one or more degenerate faces are found
   */
  public static boolean checkFaceIntegrity(Mesh mesh) {
    for (int faceIndex = 0; faceIndex < mesh.getFaceCount(); faceIndex++) {
      FaceView face = mesh.getFaceAt(faceIndex);

      if (face.getVertexCount() < 3) {
        return false;
      }

      Set<Integer> uniqueIndices = new HashSet<>();
      for (int i = 0; i < face.getVertexCount(); i++) {
        uniqueIndices.add(face.getIndexAt(i));
      }

      if (uniqueIndices.size() < 3) {
        return false;
      }
    }
    return true;
  }

  /**
   * Verifies whether the mesh satisfies the Euler characteristic for closed, genus-0 manifold
   * meshes.
   *
   * <p>The Euler characteristic is defined as:
   *
   * <pre>
   * V - E + F = 2
   * </pre>
   *
   * where:
   *
   * <ul>
   *   <li>V = number of vertices
   *   <li>E = number of edges
   *   <li>F = number of faces
   * </ul>
   *
   * <p>This condition holds for closed convex polyhedra (topological spheres).
   *
   * <p><b>Note:</b> This check is only valid for closed manifold meshes without holes or
   * boundaries.
   *
   * @param mesh the mesh to validate
   * @return {@code true} if the Euler characteristic equals 2, {@code false} otherwise
   */
  public static boolean fulfillsEulerCharacteristic(Mesh mesh) {
    int edgeCount = edgeCount(mesh);
    int faceCount = mesh.getFaceCount();
    int vertexCount = mesh.getVertexCount();
    int actual = vertexCount - edgeCount + faceCount;
    return actual == 2;
  }
}
