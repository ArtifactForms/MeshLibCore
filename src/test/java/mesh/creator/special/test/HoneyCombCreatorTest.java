package mesh.creator.special.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import mesh.Mesh3D;
import mesh.creator.special.HoneyCombCreator;
import util.ManifoldTest;
import util.MeshTestUtil;

public class HoneyCombCreatorTest {

  private HoneyCombCreator creator;

  @BeforeEach
  public void setUp() {
    creator = new HoneyCombCreator();
    creator.setCellRadius(1.0f);
  }

  @Test
  @DisplayName("Single Cell: Topology without Height")
  public void testSingleCellNoHeight() {
    creator.setRowCount(1);
    creator.setColCount(1);
    creator.setHeight(0);

    Mesh3D mesh = creator.create();

    // 6 Outer + 6 Inner vertices = 12
    assertEquals(12, mesh.getVertexCount(), "Vertex count mismatch");
    assertEquals(6, mesh.getFaceCount(), "Face count mismatch");
    MeshTestUtil.assertEdgeCountEquals(mesh, 18);
  }

  @Test
  @DisplayName("Two Cells: Shared Topology without Height")
  public void testTwoCellsNoHeight() {
    creator.setRowCount(2);
    creator.setColCount(1);
    creator.setHeight(0);

    Mesh3D mesh = creator.create();

    // 12 vertices per cell, but 2 are shared at the common edge seam
    // Calculation: 12 + 12 - 2 = 22
    assertEquals(22, mesh.getVertexCount(), "Vertex count mismatch for shared edge");
    assertEquals(12, mesh.getFaceCount(), "Face count mismatch");
  }

  @Test
  @DisplayName("Single Cell: 3D Extrusion Topology")
  public void testSingleCellWithHeight() {
    creator.setRowCount(1);
    creator.setColCount(1);
    creator.setHeight(1.0f);

    Mesh3D mesh = creator.create();

    // 12 vertices on top, 12 on bottom = 24 total
    // Faces: 6 top, 6 bottom, 6 outer walls, 6 inner walls = 24 total
    assertEquals(24, mesh.getVertexCount(), "Vertex count mismatch for extruded cell");
    assertEquals(24, mesh.getFaceCount(), "Face count mismatch for extruded cell");
  }

  @Test
  @DisplayName("Two Cells: 3D Extrusion Topology (The Failing Test)")
  public void testTwoCellWithHeight() {
    creator.setRowCount(2);
    creator.setColCount(1);
    creator.setHeight(1.0f);

    Mesh3D mesh = creator.create();

    /* * DERIVATION OF EXPECTED VALUES:
     * Vertices: (12+12-2) top + (12+12-2) bottom = 44
     * Faces: 12 top + 12 bottom + (6+6-1) outer walls + (6+6-1) inner walls = 46
     * (Note: -1 represents the shared wall which must not be duplicated for manifold geometry)
     */
    assertEquals(44, mesh.getVertexCount(), "Vertices should be shared at the seam");
    assertEquals(46, mesh.getFaceCount(), "Check if shared internal walls are handled correctly");
  }

  @Test
  @DisplayName("Mesh Integrity and Manifoldness")
  public void testMeshIntegrity() {
    creator.setRowCount(3);
    creator.setColCount(2);
    creator.setHeight(0.5f);
    Mesh3D mesh = creator.create();

    assertTrue(new ManifoldTest(mesh).isManifold(), "Mesh must be manifold (watertight)");
    assertTrue(MeshTestUtil.meshHasNoDuplicatedVertices(mesh), "Mesh contains duplicate vertices");
    assertTrue(MeshTestUtil.meshHasNoDuplicatedFaces(mesh), "Mesh contains duplicate faces");
    assertTrue(
        MeshTestUtil.meshHasNoLooseVertices(mesh), "Mesh contains loose (unconnected) vertices");
  }

  @Test
  @DisplayName("Verify Quad-only Topology")
  public void testOnlyQuads() {
    creator.setRowCount(2);
    creator.setColCount(2);
    creator.setHeight(0.1f);
    Mesh3D mesh = creator.create();

    // Validates that every face consists of exactly 4 indices
    mesh.getFaces()
        .forEach(f -> assertEquals(4, f.indices.length, "Face is not a quad (four-sided polygon)"));
  }
}
