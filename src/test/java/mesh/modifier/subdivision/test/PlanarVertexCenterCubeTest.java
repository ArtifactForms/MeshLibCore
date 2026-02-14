package mesh.modifier.subdivision.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;
import mesh.selection.FaceSelection;
import util.MeshTestUtil;

public class PlanarVertexCenterCubeTest {

  private Mesh3D cube;

  private PlanarVertexCenterModifier modifier;

  @BeforeEach
  public void setUp() {
    cube = new CubeCreator().create();
    modifier = new PlanarVertexCenterModifier();
    modifier.modify(cube);
  }

  @Test
  public void resultMeshHasFourteenVertices() {
    int expectedVertexCount = 14;
    assertEquals(expectedVertexCount, cube.getVertexCount());
    assertEquals(expectedVertexCount, cube.vertices.size());
    assertEquals(expectedVertexCount, cube.getVertices().size());
  }

  @Test
  public void resultMeshHasTwentyFourFaces() {
    int expectedFaceCount = 24;
    assertEquals(expectedFaceCount, cube.getFaceCount());
    assertEquals(expectedFaceCount, cube.faces.size());
    assertEquals(expectedFaceCount, cube.getFaces().size());
    assertEquals(expectedFaceCount, cube.getFaces(0, cube.getFaceCount()).size());
  }

  @Test
  public void resultMeshHasTwentyFourTriangularFaces() {
    assertTrue(MeshTestUtil.isTriangleCountEquals(cube, 24));
  }

  @Test
  public void resultMeshHasThirtySixEdges() {
    MeshTestUtil.assertEdgeCountEquals(cube, 36);
  }

  @Test
  public void resultMeshHasNoLooseVertices() {
    assertTrue(MeshTestUtil.meshHasNoLooseVertices(cube));
  }

  @Test
  public void resultMeshHasNoDuplicatedFaces() {
    assertTrue(MeshTestUtil.meshHasNoDuplicatedFaces(cube));
  }

  @Test
  public void resultMeshIsManifold() {
    assertTrue(MeshTestUtil.isManifold(cube));
  }

  @Test
  public void resultMeshContainsFaceCentersOfOriginalCube() {
    Mesh3D original = new CubeCreator().create();
    for (Face3D face : original.getFaces()) {
      Vector3f faceCenter = original.calculateFaceCenter(face);
      assertTrue(cube.getVertices().contains(faceCenter));
    }
  }

  @Test
  public void resultMeshContainsOriginalCubeCoordinates() {
    Mesh3D original = new CubeCreator().create();
    for (Vector3f v : original.getVertices()) assertTrue(cube.getVertices().contains(v));
  }

  @Test
  public void testTopFaceNormalsDirection() {
    FaceSelection selection = new FaceSelection(cube);
    selection.selectTopFaces();
    for (Face3D face : selection.getFaces()) {
      for (int i = 0; i < face.indices.length; i++) {
        Vector3f v = cube.getVertexAt(face.indices[i]);
        assertEquals(-1, v.getY(), 0);
      }
    }
  }

  @Test
  public void testBottomFaceNormalsDirection() {
    FaceSelection selection = new FaceSelection(cube);
    selection.selectBottomFaces();
    for (Face3D face : selection.getFaces()) {
      for (int i = 0; i < face.indices.length; i++) {
        Vector3f v = cube.getVertexAt(face.indices[i]);
        assertEquals(1, v.getY(), 0);
      }
    }
  }

  @Test
  public void testLeftFaceNormalsDirection() {
    FaceSelection selection = new FaceSelection(cube);
    selection.selectLeftFaces();
    for (Face3D face : selection.getFaces()) {
      for (int i = 0; i < face.indices.length; i++) {
        Vector3f v = cube.getVertexAt(face.indices[i]);
        assertEquals(-1, v.getX(), 0);
      }
    }
  }

  @Test
  public void testRightFaceNormalsDirection() {
    FaceSelection selection = new FaceSelection(cube);
    selection.selectRightFaces();
    for (Face3D face : selection.getFaces()) {
      for (int i = 0; i < face.indices.length; i++) {
        Vector3f v = cube.getVertexAt(face.indices[i]);
        assertEquals(1, v.getX(), 0);
      }
    }
  }

  @Test
  public void testBackFaceNormalsDirection() {
    FaceSelection selection = new FaceSelection(cube);
    selection.selectBackFaces();
    for (Face3D face : selection.getFaces()) {
      for (int i = 0; i < face.indices.length; i++) {
        Vector3f v = cube.getVertexAt(face.indices[i]);
        assertEquals(-1, v.getZ(), 0);
      }
    }
  }

  @Test
  public void testFrontFaceNormalsDirection() {
    FaceSelection selection = new FaceSelection(cube);
    selection.selectFrontFaces();
    for (Face3D face : selection.getFaces()) {
      for (int i = 0; i < face.indices.length; i++) {
        Vector3f v = cube.getVertexAt(face.indices[i]);
        assertEquals(1, v.getZ(), 0);
      }
    }
  }

  @Test
  public void resultMeshHasFourTopFaces() {
    FaceSelection selection = new FaceSelection(cube);
    selection.selectTopFaces();
    assertEquals(4, selection.getFaces().size());
  }

  @Test
  public void resultMeshHasFourBottomFaces() {
    FaceSelection selection = new FaceSelection(cube);
    selection.selectBottomFaces();
    assertEquals(4, selection.getFaces().size());
  }

  @Test
  public void resultMeshHasFourLeftFaces() {
    FaceSelection selection = new FaceSelection(cube);
    selection.selectLeftFaces();
    assertEquals(4, selection.getFaces().size());
  }

  @Test
  public void resultMeshHasFourRightFaces() {
    FaceSelection selection = new FaceSelection(cube);
    selection.selectRightFaces();
    assertEquals(4, selection.getFaces().size());
  }

  @Test
  public void resultMeshHasFourFrontFaces() {
    FaceSelection selection = new FaceSelection(cube);
    selection.selectFrontFaces();
    assertEquals(4, selection.getFaces().size());
  }

  @Test
  public void resultMeshHasFourBackFaces() {
    FaceSelection selection = new FaceSelection(cube);
    selection.selectBackFaces();
    assertEquals(4, selection.getFaces().size());
  }
}
