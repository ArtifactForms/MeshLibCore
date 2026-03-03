package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.analysis.MeshAnalysis;
import mesh.geometry.MeshGeometryUtil;
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
    HashSet<Vector3f> map = new HashSet<Vector3f>(mesh.getVertices());
    return map.size() == mesh.getVertexCount();
  }

  public static boolean meshHasNoLooseVertices(Mesh3D mesh) {
    return MeshAnalysis.meshHasNoLooseVertices(mesh);
  }

  public static boolean normalsPointOutwards(Mesh3D mesh) {
    Vector3f center = new Vector3f();
    for (Face3D face : mesh.getFaces()) {
      Vector3f faceNormal = MeshGeometryUtil.calculateFaceNormal(mesh, face);
      Vector3f faceCenter = MeshGeometryUtil.calculateFaceCenter(mesh, face);
      Vector3f a = faceCenter.subtract(center);
      float dotProduct = faceNormal.dot(a);
      if (dotProduct < 0) {
        return false;
      }
    }
    return true;
  }

  public static int calculateEdgeCount(Mesh3D mesh) {
    return MeshAnalysis.edgeCount(mesh);
  }

  public static boolean fulfillsEulerCharacteristic(Mesh3D mesh) {
    return MeshAnalysis.fulfillsEulerCharacteristic(mesh);
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
    assertEquals(calculateEdgeCount(mesh), expectedEdgeCount);
  }

  public static void assertEveryEdgeHasALengthOf(
      Mesh3D mesh, float expectedEdgeLength, float delta) {
    for (Face3D face : mesh.getFaces()) {
      for (int i = 0; i < face.indices.length; i++) {
        Vector3f v0 = mesh.getVertexAt(face.indices[i]);
        Vector3f v1 = mesh.getVertexAt(face.indices[(i + 1) % face.indices.length]);
        assertEquals(expectedEdgeLength, v0.distance(v1), delta);
      }
    }
  }

  private static boolean hasCorrectNumberOfFacesWithVertexCount(
      Mesh3D mesh, int vertices, int expected) {
    int count = 0;
    for (Face3D face : mesh.getFaces()) {
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
