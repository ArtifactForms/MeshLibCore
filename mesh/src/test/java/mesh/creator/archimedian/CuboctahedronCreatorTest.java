package mesh.creator.archimedian;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import util.MeshTestUtil;

public class CuboctahedronCreatorTest {

  private static final float ALLOWED_EDGE_LENGTH_DELTA = 0.0001f;

  private Mesh3D mesh;

  @BeforeEach
  public void setup() {
    mesh = new CuboctahedronCreator().create();
  }

  @Test
  public void testFaceCount() {
    assertEquals(14, mesh.getFaceCount());
  }

  @Test
  public void testVertexCount() {
    assertEquals(12, mesh.getVertexCount());
  }

  @Test
  public void testEdgeCount() {
    int actual = MeshTestUtil.calculateEdgeCount(mesh);
    assertEquals(24, actual);
  }

  @Test
  public void testDefaultEdgeLength() {
    float expectedEdgeLength = Mathf.sqrt(2);
    assertAllEdgesHaveLength(this.mesh, expectedEdgeLength);
  }

  @Test
  public void testTriangularFaceCount() {
    assertTrue(MeshTestUtil.isTriangleCountEquals(mesh, 8));
  }

  @Test
  public void testQuadFaceCount() {
    assertTrue(MeshTestUtil.isQuadCountEquals(mesh, 6));
  }

  @Test
  public void testCreatedMeshFulfillsEulerCharacteristic() {
    assertTrue(MeshTestUtil.fulfillsEulerCharacteristic(mesh));
  }

  @Test
  public void testCreatedMeshIsManifold() {
    assertTrue(MeshTestUtil.isManifold(mesh));
  }

  @Test
  public void testCreatedMeshHasNoDuplicatedFaces() {
    assertTrue(MeshTestUtil.meshHasNoDuplicatedFaces(mesh));
  }

  @Test
  public void testCreatedMeshHasNoDuplicatedVertices() {
    assertTrue(MeshTestUtil.meshHasNoDuplicatedVertices(mesh));
  }

  @Test
  public void testCreatedMeshHasNoLooseVertices() {
    assertTrue(MeshTestUtil.meshHasNoLooseVertices(mesh));
  }

  @Test
  public void testCreatedMeshFaceNormalsPointOutwards() {
    assertTrue(MeshTestUtil.normalsPointOutwards(mesh));
  }

  @ParameterizedTest
  @ValueSource(floats = {0.5f, 1.0f, 1.45f, 2.5f, 10.0f})
  public void testCustomEdgeLength(float expectedEdgeLength) {
    Mesh3D mesh = new CuboctahedronCreator(expectedEdgeLength).create();

    assertAllEdgesHaveLength(mesh, expectedEdgeLength);
  }

  @ParameterizedTest
  @ValueSource(floats = {0.0f, -0.1f, -5.0f})
  public void testInvalidEdgeLengthThrowsException(float invalidEdgeLength) {
    assertThrows(
        IllegalArgumentException.class,
        () -> new CuboctahedronCreator(invalidEdgeLength),
        "Expected IllegalArgumentException for edge length " + invalidEdgeLength);
  }

  /** Asserts that every edge in all faces of the mesh matches the expected length. */
  private void assertAllEdgesHaveLength(Mesh3D mesh, float expectedLength) {
    for (int i = 0; i < mesh.getFaceCount(); i++) {
      Face3D face = mesh.getFaceAt(i);
      for (int j = 0; j < face.getVertexCount(); j++) {
        Vector3f start = mesh.getVertexAt(face.getIndexAt(j));
        Vector3f end = mesh.getVertexAt(face.getIndexAt((j + 1) % face.getVertexCount()));

        float actualEdgeLength = start.subtract(end).length();
        assertEquals(expectedLength, actualEdgeLength, ALLOWED_EDGE_LENGTH_DELTA);
      }
    }
  }
}