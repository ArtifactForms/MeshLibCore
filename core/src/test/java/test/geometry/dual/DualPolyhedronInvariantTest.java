package test.geometry.dual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mesh.Mesh3D;
import mesh.creator.archimedian.CuboctahedronCreator;
import mesh.creator.archimedian.IcosidodecahedronCreator;
import mesh.creator.archimedian.RhombicosidodecahedronCreator;
import mesh.creator.archimedian.RhombicuboctahedronCreator;
import mesh.creator.archimedian.SnubCubeCreator;
import mesh.creator.archimedian.SnubDodecahedronCreator;
import mesh.creator.archimedian.TruncatedCubeCreator;
import mesh.creator.archimedian.TruncatedCuboctahedronCreator;
import mesh.creator.archimedian.TruncatedDodecahedronCreator;
import mesh.creator.archimedian.TruncatedIcosahedronCreator;
import mesh.creator.archimedian.TruncatedIcosidodecahedronCreator;
import mesh.creator.archimedian.TruncatedOctahedronCreator;
import mesh.creator.archimedian.TruncatedTetrahedronCreator;
import mesh.creator.special.DualCreator;
import util.MeshTestUtil;

/**
 * Behavioral and invariant-based test suite for {@link DualCreator} using all 13 Archimedean solids
 * as canonical input meshes.
 *
 * <h2>Purpose</h2>
 *
 * <p>This test class verifies the <em>topological correctness</em> and <em>structural
 * invariants</em> of meshes produced by {@link DualCreator}. Each test applies the dual operation
 * to a well-known Archimedean solid and asserts that the resulting dual polyhedron matches the
 * expected combinatorial properties.
 *
 * <h2>What is tested</h2>
 *
 * <ul>
 *   <li>Expected vertex, face, and edge counts
 *   <li>Expected face types (triangles, quads, pentagons)
 *   <li>Manifoldness of the resulting mesh
 *   <li>Fulfillment of the Euler characteristic
 *   <li>Absence of duplicated faces or vertices
 *   <li>Absence of loose (unreferenced) vertices
 *   <li>Consistent outward-facing normals
 * </ul>
 *
 * <h2>What is intentionally not tested</h2>
 *
 * <p>These tests do <strong>not</strong> validate metric or geometric properties such as:
 *
 * <ul>
 *   <li>Exact vertex positions
 *   <li>Edge lengths or angle regularity
 *   <li>Symmetry groups or spatial orientation
 * </ul>
 *
 * <p>The focus is strictly on <em>topological invariants</em> and <em>domain-level
 * correctness</em>, making these tests robust against refactorings or numeric implementation
 * changes.
 *
 * <h2>Test classification</h2>
 *
 * <p>This test suite can be classified as:
 *
 * <ul>
 *   <li>Geometric invariant tests
 *   <li>Behavioral characterization tests
 *   <li>Domain-level integration tests
 * </ul>
 *
 * <p>A failure in this test suite indicates a <strong>behavioral or topological regression</strong>
 * in the dual mesh generation logic and should be reviewed intentionally.
 */
public class DualPolyhedronInvariantTest {

  /**
   * Asserts fundamental topological invariants that are expected to hold for all dual meshes
   * generated from Archimedean solids.
   *
   * <p>This method defines a shared baseline of structural correctness and is intentionally reused
   * across all test cases to ensure consistency.
   *
   * <p>The asserted invariants include:
   *
   * <ul>
   *   <li>Fulfillment of the Euler characteristic
   *   <li>Manifold topology
   *   <li>No duplicated faces
   *   <li>No duplicated vertices
   *   <li>No loose (unreferenced) vertices
   *   <li>Outward-facing normals
   * </ul>
   *
   * <p>These checks collectively define the minimal correctness contract for a valid dual
   * polyhedron in this domain.
   */
  private void assertBasicTopology(Mesh3D mesh) {
    assertTrue(MeshTestUtil.fulfillsEulerCharacteristic(mesh));
    assertTrue(MeshTestUtil.isManifold(mesh));
    assertTrue(MeshTestUtil.meshHasNoDuplicatedFaces(mesh));
    assertTrue(MeshTestUtil.meshHasNoDuplicatedVertices(mesh));
    assertTrue(MeshTestUtil.meshHasNoLooseVertices(mesh));
    assertTrue(MeshTestUtil.normalsPointOutwards(mesh));
  }

  @Test
  public void testCuboctahedronCreatorDual() {
    CuboctahedronCreator creator = new CuboctahedronCreator();
    DualCreator dualCreator = new DualCreator(creator.create());
    Mesh3D actual = dualCreator.create();

    // Expected dual: Rhombic dodecahedron
    int expectedVertexCount = 14;
    int expectedFaceCount = 12;
    int expectedEdgeCount = 24;

    assertEquals(expectedVertexCount, actual.getVertexCount());
    assertEquals(expectedFaceCount, actual.getFaceCount());
    assertEquals(expectedEdgeCount, MeshTestUtil.calculateEdgeCount(actual));

    int expectedQuadCount = 12;

    assertTrue(MeshTestUtil.isQuadCountEquals(actual, expectedQuadCount));

    assertBasicTopology(actual);
  }

  @Test
  public void testIcosidodecahedronCreatorDual() {
    IcosidodecahedronCreator creator = new IcosidodecahedronCreator();
    DualCreator dualCreator = new DualCreator(creator.create());
    Mesh3D actual = dualCreator.create();

    // Expected dual: Rhombic triacontahedron
    int expectedVertexCount = 32;
    int expectedFaceCount = 30;
    int expectedEdgeCount = 60;

    assertEquals(expectedVertexCount, actual.getVertexCount());
    assertEquals(expectedFaceCount, actual.getFaceCount());
    assertEquals(expectedEdgeCount, MeshTestUtil.calculateEdgeCount(actual));

    int expectedQuadCount = 30;

    assertTrue(MeshTestUtil.isQuadCountEquals(actual, expectedQuadCount));

    assertBasicTopology(actual);
  }

  @Test
  public void testRhombicosidodecahedronCreatorDual() {
    RhombicosidodecahedronCreator creator = new RhombicosidodecahedronCreator();
    DualCreator dualCreator = new DualCreator(creator.create());
    Mesh3D actual = dualCreator.create();

    // Expected dual: Deltoidal hexecontahedron
    int expectedVertexCount = 62;
    int expectedFaceCount = 60;
    int expectedEdgeCount = 120;

    assertEquals(expectedVertexCount, actual.getVertexCount());
    assertEquals(expectedFaceCount, actual.getFaceCount());
    assertEquals(expectedEdgeCount, MeshTestUtil.calculateEdgeCount(actual));

    int expectedQuadCount = 60;

    assertTrue(MeshTestUtil.isQuadCountEquals(actual, expectedQuadCount));

    assertBasicTopology(actual);
  }

  @Test
  public void testRhombicuboctahedronCreatorDual() {
    RhombicuboctahedronCreator creator = new RhombicuboctahedronCreator();
    DualCreator dualCreator = new DualCreator(creator.create());
    Mesh3D actual = dualCreator.create();

    // Expected dual: Deltoidal icositetrahedron
    int expectedVertexCount = 26;
    int expectedFaceCount = 24;
    int expectedEdgeCount = 48;

    assertEquals(expectedVertexCount, actual.getVertexCount());
    assertEquals(expectedFaceCount, actual.getFaceCount());
    assertEquals(expectedEdgeCount, MeshTestUtil.calculateEdgeCount(actual));

    int expectedQuadCount = 24;

    assertTrue(MeshTestUtil.isQuadCountEquals(actual, expectedQuadCount));

    assertBasicTopology(actual);
  }

  @Test
  public void testSnubCubeCreatorDual() {
    SnubCubeCreator creator = new SnubCubeCreator();
    DualCreator dualCreator = new DualCreator(creator.create());
    Mesh3D actual = dualCreator.create();

    // Expected dual: Pentagonal icositetrahedron
    int expectedVertexCount = 38;
    int expectedFaceCount = 24;
    int expectedEdgeCount = 60;

    assertEquals(expectedVertexCount, actual.getVertexCount());
    assertEquals(expectedFaceCount, actual.getFaceCount());
    assertEquals(expectedEdgeCount, MeshTestUtil.calculateEdgeCount(actual));

    int expectedPentagonalCount = 24;

    assertTrue(MeshTestUtil.isPentagonCountEquals(actual, expectedPentagonalCount));

    assertBasicTopology(actual);
  }

  @Test
  public void testSnubDodecahedronCreatorDual() {
    SnubDodecahedronCreator creator = new SnubDodecahedronCreator();
    DualCreator dualCreator = new DualCreator(creator.create());
    Mesh3D actual = dualCreator.create();

    // Expected dual: Pentagonal hexecontahedron
    int expectedVertexCount = 92;
    int expectedFaceCount = 60;
    int expectedEdgeCount = 150;

    assertEquals(expectedVertexCount, actual.getVertexCount());
    assertEquals(expectedFaceCount, actual.getFaceCount());
    assertEquals(expectedEdgeCount, MeshTestUtil.calculateEdgeCount(actual));

    int expectedPentagonalCount = 60;

    assertTrue(MeshTestUtil.isPentagonCountEquals(actual, expectedPentagonalCount));

    assertBasicTopology(actual);
  }

  @Test
  public void testTruncatedCubeCreatorDual() {
    TruncatedCubeCreator creator = new TruncatedCubeCreator();
    DualCreator dualCreator = new DualCreator(creator.create());
    Mesh3D actual = dualCreator.create();

    // Expected dual: Triakis octahedron
    int expectedVertexCount = 14;
    int expectedFaceCount = 24;
    int expectedEdgeCount = 36;

    assertEquals(expectedVertexCount, actual.getVertexCount());
    assertEquals(expectedFaceCount, actual.getFaceCount());
    assertEquals(expectedEdgeCount, MeshTestUtil.calculateEdgeCount(actual));

    int expectedTriangleCount = 24;

    assertTrue(MeshTestUtil.isTriangleCountEquals(actual, expectedTriangleCount));

    assertBasicTopology(actual);
  }

  @Test
  public void testTruncatedCuboctahedronCreatorDual() {
    TruncatedCuboctahedronCreator creator = new TruncatedCuboctahedronCreator();
    DualCreator dualCreator = new DualCreator(creator.create());
    Mesh3D actual = dualCreator.create();

    // Expected dual: Disdyakis dodecahedron
    int expectedVertexCount = 26;
    int expectedFaceCount = 48;
    int expectedEdgeCount = 72;

    assertEquals(expectedVertexCount, actual.getVertexCount());
    assertEquals(expectedFaceCount, actual.getFaceCount());
    assertEquals(expectedEdgeCount, MeshTestUtil.calculateEdgeCount(actual));

    int expectedTriangleCount = 48;

    assertTrue(MeshTestUtil.isTriangleCountEquals(actual, expectedTriangleCount));

    assertBasicTopology(actual);
  }

  @Test
  public void testTruncatedDodecahedronCreatorDual() {
    TruncatedDodecahedronCreator creator = new TruncatedDodecahedronCreator();
    DualCreator dualCreator = new DualCreator(creator.create());
    Mesh3D actual = dualCreator.create();

    // Expected dual: Triakis icosahedron
    int expectedVertexCount = 32;
    int expectedFaceCount = 60;
    int expectedEdgeCount = 90;

    assertEquals(expectedVertexCount, actual.getVertexCount());
    assertEquals(expectedFaceCount, actual.getFaceCount());
    assertEquals(expectedEdgeCount, MeshTestUtil.calculateEdgeCount(actual));

    int expectedTriangleCount = 60;

    assertTrue(MeshTestUtil.isTriangleCountEquals(actual, expectedTriangleCount));

    assertBasicTopology(actual);
  }

  @Test
  public void testTruncatedIcosahedronCreatorDual() {
    TruncatedIcosahedronCreator creator = new TruncatedIcosahedronCreator();
    DualCreator dualCreator = new DualCreator(creator.create());
    Mesh3D actual = dualCreator.create();

    // Expected dual: Pentakis dodecahedron
    int expectedVertexCount = 32;
    int expectedFaceCount = 60;
    int expectedEdgeCount = 90;

    assertEquals(expectedVertexCount, actual.getVertexCount());
    assertEquals(expectedFaceCount, actual.getFaceCount());
    assertEquals(expectedEdgeCount, MeshTestUtil.calculateEdgeCount(actual));

    int expectedTriangleCount = 60;

    assertTrue(MeshTestUtil.isTriangleCountEquals(actual, expectedTriangleCount));

    assertBasicTopology(actual);
  }

  @Test
  public void testTruncatedIcosidodecahedronCreatorDual() {
    TruncatedIcosidodecahedronCreator creator = new TruncatedIcosidodecahedronCreator();
    DualCreator dualCreator = new DualCreator(creator.create());
    Mesh3D actual = dualCreator.create();

    // Expected dual: Disdyakis triacontahedron
    int expectedVertexCount = 62;
    int expectedFaceCount = 120;
    int expectedEdgeCount = 180;

    assertEquals(expectedVertexCount, actual.getVertexCount());
    assertEquals(expectedFaceCount, actual.getFaceCount());
    assertEquals(expectedEdgeCount, MeshTestUtil.calculateEdgeCount(actual));

    int expectedTriangleCount = 120;

    assertTrue(MeshTestUtil.isTriangleCountEquals(actual, expectedTriangleCount));

    assertBasicTopology(actual);
  }

  @Test
  public void testTruncatedOctahedronCreatorDual() {
    TruncatedOctahedronCreator creator = new TruncatedOctahedronCreator();
    DualCreator dualCreator = new DualCreator(creator.create());
    Mesh3D actual = dualCreator.create();

    // Expected dual: Tetrakis hexahedron
    int expectedVertexCount = 14;
    int expectedFaceCount = 24;
    int expectedEdgeCount = 36;

    assertEquals(expectedVertexCount, actual.getVertexCount());
    assertEquals(expectedFaceCount, actual.getFaceCount());
    assertEquals(expectedEdgeCount, MeshTestUtil.calculateEdgeCount(actual));

    int expectedTriangleCount = 24;

    assertTrue(MeshTestUtil.isTriangleCountEquals(actual, expectedTriangleCount));

    assertBasicTopology(actual);
  }

  @Test
  public void testTruncatedTetrahedronCreatorDual() {
    TruncatedTetrahedronCreator creator = new TruncatedTetrahedronCreator();
    DualCreator dualCreator = new DualCreator(creator.create());
    Mesh3D actual = dualCreator.create();

    // Expected dual: Triakis tetrahedron
    int expectedVertexCount = 8;
    int expectedFaceCount = 12;
    int expectedEdgeCount = 18;

    assertEquals(expectedVertexCount, actual.getVertexCount());
    assertEquals(expectedFaceCount, actual.getFaceCount());
    assertEquals(expectedEdgeCount, MeshTestUtil.calculateEdgeCount(actual));

    int expectedTriangleCount = 12;

    assertTrue(MeshTestUtil.isTriangleCountEquals(actual, expectedTriangleCount));

    assertBasicTopology(actual);
  }
}
