package test.geometry.dual;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.archimedian.ArchimedianSolid;
import mesh.creator.archimedian.ArchimedianSolidCreator;
import mesh.creator.platonic.PlatonicSolid;
import mesh.creator.platonic.PlatonicSolidCreator;
import mesh.creator.special.DualCreator;
import util.MeshTestUtil;

/**
 * Verifies the topological reciprocity property of the dual operation.
 *
 * <p>For convex polyhedra, applying the dual operation twice should restore the original topology:
 *
 * <pre>
 * Dual(Dual(Mesh)) == Mesh  (topologically)
 * </pre>
 *
 * <h3>Guaranteed by this test</h3>
 *
 * <ul>
 *   <li><b>Topological Consistency:</b> Vertex and face counts are preserved after a double dual
 *       operation.
 *   <li><b>Manifold Integrity:</b> The resulting mesh remains a valid manifold surface.
 *   <li><b>Euler Characteristic Preservation:</b> The fundamental topological invariant (V - E + F)
 *       remains valid.
 *   <li><b>Structural Soundness:</b> No duplicated faces, duplicated vertices, or loose vertices
 *       exist.
 * </ul>
 *
 * <h3>Important limitations</h3>
 *
 * <p>This test validates <b>topology only</b>. It does <b>not</b> guarantee:
 *
 * <ul>
 *   <li>Correct geometric vertex positions
 *   <li>Preservation of metric distances or scale
 *   <li>Exact symmetry restoration
 *   <li>Angular regularity of faces
 * </ul>
 *
 * <p>A passing test confirms that the combinatorial "DNA" of the mesh is preserved, but not
 * necessarily its exact geometric embedding in space.
 *
 * <p>All Archimedian and Platonic solids are used as input meshes.
 */
class DualReciprocityTest {

  /** Combined data source for all standard convex polyhedra. */
  private static Stream<Arguments> provideCreators() {
    return Stream.concat(
        Stream.of(ArchimedianSolid.values())
            .map(s -> Arguments.of(s.name(), new ArchimedianSolidCreator(s))),
        Stream.of(PlatonicSolid.values())
            .map(s -> Arguments.of(s.name(), new PlatonicSolidCreator(s))));
  }

  @ParameterizedTest(name = "Dual reciprocity check for {0}")
  @MethodSource("provideCreators")
  void testDualReciprocity(String name, IMeshCreator creator) {

    // ---------------------------------------------------------------------
    // Arrange
    // ---------------------------------------------------------------------
    Mesh3D originalMesh = creator.create();

    // ---------------------------------------------------------------------
    // Act
    // ---------------------------------------------------------------------
    Mesh3D firstDual = new DualCreator(originalMesh).create();
    Mesh3D doubleDual = new DualCreator(firstDual).create();

    // ---------------------------------------------------------------------
    // Assert
    // ---------------------------------------------------------------------
    assertAll(
        "Topological reciprocity invariants for " + name,
        () ->
            assertEquals(
                originalMesh.getVertexCount(),
                doubleDual.getVertexCount(),
                () ->
                    String.format(
                        "[%s] Vertex count mismatch after double dual (expected %d but was %d)",
                        name, originalMesh.getVertexCount(), doubleDual.getVertexCount())),
        () ->
            assertEquals(
                originalMesh.getFaceCount(),
                doubleDual.getFaceCount(),
                () ->
                    String.format(
                        "[%s] Face count mismatch after double dual (expected %d but was %d)",
                        name, originalMesh.getFaceCount(), doubleDual.getFaceCount())),
        () -> assertBasicTopology(doubleDual, name));
  }

  /**
   * Validates fundamental topological invariants of a mesh.
   *
   * @param mesh the mesh to validate
   * @param name name of the tested solid (for diagnostic output)
   */
  private void assertBasicTopology(Mesh3D mesh, String name) {

    assertTrue(
        MeshTestUtil.fulfillsEulerCharacteristic(mesh),
        () -> "[" + name + "] Euler characteristic invariant violated");

    assertTrue(
        MeshTestUtil.isManifold(mesh),
        () -> "[" + name + "] Mesh is no longer manifold after double dual");

    assertTrue(
        MeshTestUtil.meshHasNoDuplicatedFaces(mesh),
        () -> "[" + name + "] Mesh contains duplicated faces after double dual");

    assertTrue(
        MeshTestUtil.meshHasNoDuplicatedVertices(mesh),
        () -> "[" + name + "] Mesh contains duplicated vertices after double dual");

    assertTrue(
        MeshTestUtil.meshHasNoLooseVertices(mesh),
        () -> "[" + name + "] Mesh contains loose (unreferenced) vertices after double dual");

    assertTrue(
        MeshTestUtil.normalsPointOutwards(mesh),
        () -> "[" + name + "] Face normals are not consistently outward-pointing");
  }
}
