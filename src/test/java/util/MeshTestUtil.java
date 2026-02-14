package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.util.TraverseHelper;

/**
 * Utility class providing structural validation helpers for {@link Mesh3D}.
 *
 * <p>All methods in this class are intended for test-side mesh validation and characterization.
 * They are not part of the core mesh API contract.
 *
 * <p>The checks focus on structural integrity, topological consistency, and geometric sanity of
 * generated meshes.
 */
public class MeshTestUtil {

  /**
   * Checks whether the given mesh contains structurally duplicated faces.
   *
   * <p>A face is considered a duplicate if:
   *
   * <ul>
   *   <li>It has the same number of vertex indices
   *   <li>It references exactly the same set of vertex indices
   *   <li>The order of indices is irrelevant
   * </ul>
   *
   * <p>This method performs a canonicalization step by sorting the indices of each face and uses a
   * {@link HashSet} to detect duplicates in linear time.
   *
   * <p><b>Time Complexity:</b> O(N), where N is the number of faces. Each face is normalized once
   * and inserted into a hash-based set.
   *
   * <p>This is a structural integrity check intended for test validation and mesh consistency
   * verification.
   *
   * @param mesh the mesh to validate
   * @return {@code true} if no duplicated faces exist, {@code false} otherwise
   */
  public static boolean meshHasNoDuplicatedFaces(Mesh3D mesh) {
    Set<FaceKey> seen = new HashSet<>();

    for (Face3D face : mesh.getFaces()) {
      FaceKey key = FaceKey.of(face);

      if (!seen.add(key)) {
        return false; // duplicate found
      }
    }

    return true;
  }

  /**
   * Canonical key representation of a face used for duplicate detection.
   *
   * <p>The indices of a face are copied and sorted to create a stable, order-independent
   * representation. Two faces are considered equal if their sorted index arrays are equal.
   *
   * <p>This class is used exclusively for structural validation and does not modify the original
   * {@link Face3D}.
   */
  private static final class FaceKey {

    private final int[] sortedIndices;

    private FaceKey(int[] sortedIndices) {
      this.sortedIndices = sortedIndices;
    }

    static FaceKey of(Face3D face) {
      int[] copy = Arrays.copyOf(face.indices, face.indices.length);
      Arrays.sort(copy); // canonical representation
      return new FaceKey(copy);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof FaceKey)) return false;
      FaceKey other = (FaceKey) o;
      return Arrays.equals(this.sortedIndices, other.sortedIndices);
    }

    @Override
    public int hashCode() {
      return Arrays.hashCode(sortedIndices);
    }
  }

  /**
   * Checks whether the mesh contains duplicated vertex instances.
   *
   * <p>Vertices are compared using their {@code equals()} and {@code hashCode()} implementations.
   * This method assumes that {@link Vector3f} correctly implements structural equality.
   *
   * <p><b>Time Complexity:</b> O(V), where V is the number of vertices.
   *
   * @param mesh the mesh to validate
   * @return {@code true} if all vertices are unique, {@code false} otherwise
   */
  public static boolean meshHasNoDuplicatedVertices(Mesh3D mesh) {
    HashSet<Vector3f> map = new HashSet<Vector3f>(mesh.vertices);
    return map.size() == mesh.vertices.size();
  }

  /**
   * Checks whether the mesh contains any loose (unreferenced) vertices.
   *
   * <p>A vertex is considered <i>loose</i> if it is not referenced by any face index in the mesh.
   *
   * <p>This method operates purely on vertex indices and does not mutate the mesh or rely on
   * defensive copies. It marks all indices referenced by faces and verifies that every vertex index
   * in the range {@code [0, vertexCount)} is used at least once.
   *
   * <p><b>Time Complexity:</b> O(V + F), where
   *
   * <ul>
   *   <li>V = number of vertices
   *   <li>F = total number of face indices
   * </ul>
   *
   * <p>This is a structural integrity check intended for test-side validation and mesh consistency
   * verification.
   *
   * @param mesh the mesh to validate
   * @return {@code true} if every vertex is referenced by at least one face, {@code false} if one
   *     or more loose vertices are found
   */
  public static boolean meshHasNoLooseVertices(Mesh3D mesh) {
    int vertexCount = mesh.getVertexCount();
    boolean[] used = new boolean[vertexCount];

    for (Face3D face : mesh.getFaces()) {
      for (int index : face.indices) {
        if (index >= 0 && index < vertexCount) {
          used[index] = true;
        }
      }
    }

    for (boolean isUsed : used) {
      if (!isUsed) {
        return false; // found loose vertex
      }
    }

    return true;
  }

  public static boolean normalsPointOutwards(Mesh3D mesh) {
    Vector3f center = new Vector3f();
    for (Face3D face : mesh.getFaces()) {
      Vector3f faceNormal = mesh.calculateFaceNormal(face);
      Vector3f faceCenter = mesh.calculateFaceCenter(face);
      Vector3f a = faceCenter.subtract(center);
      float dotProduct = faceNormal.dot(a);
      if (dotProduct < 0) {
        return false;
      }
    }
    return true;
  }

  public static int calculateEdgeCount(Mesh3D mesh) {
    HashSet<Edge3D> edges = new HashSet<Edge3D>();

    for (Face3D face : mesh.faces) {
      for (int i = 0; i < face.indices.length; i++) {
        int fromIndex = face.indices[i];
        int toIndex = face.indices[(i + 1) % face.indices.length];
        if (fromIndex == toIndex) continue;
        if (fromIndex < 0 || toIndex < 0) continue;
        Edge3D edge = new Edge3D(fromIndex, toIndex);
        Edge3D pair = edge.createPair();
        if (!edges.contains(pair)) edges.add(edge);
      }
    }
    return edges.size();
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
  public static boolean fulfillsEulerCharacteristic(Mesh3D mesh) {
    int edgeCount = MeshTestUtil.calculateEdgeCount(mesh);
    int faceCount = mesh.getFaceCount();
    int vertexCount = mesh.getVertexCount();
    int actual = vertexCount - edgeCount + faceCount;
    return actual == 2;
  }

  public static void assertMeshHasEdgesWithLengthOf(
      Mesh3D mesh, int expectedEdgeCount, float expectedLength) {
    int actualEdgeCount = 0;

    TraverseHelper helper = new TraverseHelper(mesh);
    List<Edge3D> edges = helper.getAllEdges();
    for (Edge3D edge : edges) {
      Vector3f from = mesh.getVertexAt(edge.getFromIndex());
      Vector3f to = mesh.getVertexAt(edge.getToIndex());
      float edgeLength = from.distance(to);
      if (edgeLength == expectedLength) actualEdgeCount++;
    }

    if (expectedEdgeCount != actualEdgeCount) {
      throw new AssertionError(
          "Expected "
              + expectedEdgeCount
              + " edges with length "
              + expectedLength
              + ", but found "
              + actualEdgeCount);
    }
  }

  public static void assertEdgeCountEquals(Mesh3D mesh, int expectedEdgeCount) {
    Assert.assertEquals(calculateEdgeCount(mesh), expectedEdgeCount);
  }

  public static void assertEveryEdgeHasALengthOf(
      Mesh3D mesh, float expectedEdgeLength, float delta) {
    for (Face3D face : mesh.faces) {
      for (int i = 0; i < face.indices.length; i++) {
        Vector3f v0 = mesh.vertices.get(face.indices[i]);
        Vector3f v1 = mesh.vertices.get(face.indices[(i + 1) % face.indices.length]);
        Assert.assertEquals(expectedEdgeLength, v0.distance(v1), delta);
      }
    }
  }

  private static boolean hasCorrectNumberOfFacesWithVertexCount(
      Mesh3D mesh, int vertices, int expected) {
    int count = 0;
    for (Face3D face : mesh.faces) {
      if (face.getVertexCount() == vertices) count++;
    }
    return count == expected;
  }
  
  public static boolean isTriangleCountEquals(Mesh3D mesh, int expectedTriangleCount) {
    return hasCorrectNumberOfFacesWithVertexCount(mesh, 3, expectedTriangleCount);
  }

  public static boolean isQuadCountEquals(Mesh3D mesh, int expectedQuadCount) {
    return hasCorrectNumberOfFacesWithVertexCount(mesh, 4, expectedQuadCount);
  }

  public static boolean isHexagonCountEquals(Mesh3D mesh, int expectedHexagonCount) {
    return hasCorrectNumberOfFacesWithVertexCount(mesh, 6, expectedHexagonCount);
  }

  public static boolean isPentagonCountEquals(Mesh3D mesh, int expectedPentagonCount) {
    return hasCorrectNumberOfFacesWithVertexCount(mesh, 5, expectedPentagonCount);
  }

  public static boolean isOctagonCountEquals(Mesh3D mesh, int expectedOctagonCount) {
    return hasCorrectNumberOfFacesWithVertexCount(mesh, 8, expectedOctagonCount);
  }

  public static boolean isDecagonCountEquals(Mesh3D mesh, int expectedDecagonCount) {
    return hasCorrectNumberOfFacesWithVertexCount(mesh, 10, expectedDecagonCount);
  }

  public static boolean isManifold(Mesh3D mesh) {
    return new ManifoldTest(mesh).isManifold();
  }

  public static boolean containsVertexIndex(Face3D face, int expectedVertexIndex) {
    int count = 0;
    for (int i = 0; i < face.indices.length; i++)
      count += face.indices[i] == expectedVertexIndex ? 1 : 0;
    return count == 1;
  }
}
