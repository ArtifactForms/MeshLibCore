package mesh.creator.special.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.creator.special.DiamondCreator;
import mesh.selection.FaceSelection;
import util.MeshTestUtil;

public class DiamondCreatorTest {

  @Test
  public void testCreatorImplementsCreatorInterface() {
    DiamondCreator creator = new DiamondCreator();
    assertTrue(creator instanceof IMeshCreator);
  }

  @Test
  public void testGetSegmentsReturnsDefaultValue() {
    int expected = 32;
    assertEquals(expected, new DiamondCreator().getSegments());
  }

  @Test
  public void testGetSetSegments() {
    int expected = 145;
    DiamondCreator creator = new DiamondCreator();
    creator.setSegments(expected);
    assertEquals(expected, creator.getSegments());
  }

  @Test
  public void testGetGirdleRadiusReturnsDefaultValue() {
    float expected = 1;
    float actual = new DiamondCreator().getGirdleRadius();
    assertEquals(expected, actual, 0);
  }

  @Test
  public void testGetSetGirdleRadius() {
    float expected = 0.9562f;
    DiamondCreator creator = new DiamondCreator();
    creator.setGirdleRadius(expected);
    assertEquals(expected, creator.getGirdleRadius(), 0);
  }

  @Test
  public void testGetTableRadiusReturnsDefaultValue() {
    float expected = 0.6f;
    DiamondCreator creator = new DiamondCreator();
    assertEquals(expected, creator.getTableRadius(), 0);
  }

  @Test
  public void testGetSetTableRadius() {
    float expected = 0.65416f;
    DiamondCreator creator = new DiamondCreator();
    creator.setTableRadius(expected);
    assertEquals(expected, creator.getTableRadius(), 0);
  }

  @Test
  public void testGetCrownHeightReturnsDefaulValue() {
    float expected = 0.35f;
    DiamondCreator creator = new DiamondCreator();
    assertEquals(expected, creator.getCrownHeight(), 0);
  }

  @Test
  public void testGetSetCrownHeight() {
    float expected = 67.45f;
    DiamondCreator creator = new DiamondCreator();
    creator.setCrownHeight(expected);
    assertEquals(expected, creator.getCrownHeight(), 0);
  }

  @Test
  public void testGetPavillionReturnsDefaultValue() {
    float expected = 0.8f;
    DiamondCreator creator = new DiamondCreator();
    assertEquals(expected, creator.getPavillionHeight(), 0);
  }

  @Test
  public void testGetSetPavillionHeight() {
    float expected = 9.2536f;
    DiamondCreator creator = new DiamondCreator();
    creator.setPavillionHeight(expected);
    assertEquals(expected, creator.getPavillionHeight(), 0);
  }

  @Test
  public void testVertexCountIsSixtyFiveByDefault() {
    int expected = 65;
    Mesh3D mesh = new DiamondCreator().create();
    assertEquals(expected, mesh.getVertexCount());
  }

  @Test
  public void testCreatedMeshContainsVertexAtCenterPavillionHeight() {
    Vector3f expected = new Vector3f(0, 0.8f, 0);
    Mesh3D mesh = new DiamondCreator().create();
    assertTrue(mesh.vertices.contains(expected));
  }

  @Test
  public void testMeshContainsVertexAtCenterOfSpecifiedPavillionHeight() {
    float pavillionHeight = 9.45f;
    Vector3f expected = new Vector3f(0, pavillionHeight, 0);
    DiamondCreator creator = new DiamondCreator();
    creator.setPavillionHeight(pavillionHeight);
    Mesh3D mesh = creator.create();
    assertTrue(mesh.vertices.contains(expected));
  }

  @Test
  public void testMeshContainsVerticesOfGirdleCircle() {
    int vertices = 32;
    float radius = 1;
    CircleCreator creator = new CircleCreator();
    creator.setRadius(radius);
    creator.setVertices(vertices);
    Mesh3D circle = creator.create();
    Mesh3D mesh = new DiamondCreator().create();
    for (Vector3f v : circle.vertices) {
      assertTrue(mesh.vertices.contains(v));
    }
  }

  @Test
  public void testMeshContainsVerticesOfSpecifiedGirdleCircle() {
    int vertices = 21;
    float radius = 1.9654f;
    CircleCreator creator = new CircleCreator();
    creator.setRadius(radius);
    creator.setVertices(vertices);
    Mesh3D circle = creator.create();
    DiamondCreator creator2 = new DiamondCreator();
    creator2.setGirdleRadius(radius);
    creator2.setSegments(vertices);
    Mesh3D mesh = creator2.create();
    for (Vector3f v : circle.vertices) {
      assertTrue(mesh.vertices.contains(v));
    }
  }

  @Test
  public void testVertexCountIsTwoTimesSegmentsPlusOne() {
    DiamondCreator creator = new DiamondCreator();
    creator.setSegments(6);
    Mesh3D mesh = creator.create();
    assertEquals(13, mesh.getVertexCount());
  }

  @Test
  public void testDefaultFaceCountIsSixtyFive() {
    int expected = 65;
    Mesh3D mesh = new DiamondCreator().create();
    assertEquals(expected, mesh.getFaceCount());
  }

  @Test
  public void testFaceCountIsTwoTimesVertexCountPlusOne() {
    int expected = 23;
    DiamondCreator creator = new DiamondCreator();
    creator.setSegments(11);
    Mesh3D mesh = creator.create();
    assertEquals(expected, mesh.getFaceCount());
  }

  @Test
  public void tesMeshContainsFaceWithVertexCountEqualsToSegments() {
    DiamondCreator creator = new DiamondCreator();
    Mesh3D mesh = creator.create();
    FaceSelection selection = new FaceSelection(mesh);
    selection.selectByVertexCount(32);
    assertEquals(1, selection.size());
  }

  @Test
  public void testMeshContainsFaceWithVertexCountEqualsToSpecifiedSegments() {
    int segments = 19;
    DiamondCreator creator = new DiamondCreator();
    creator.setSegments(segments);
    Mesh3D mesh = creator.create();
    FaceSelection selection = new FaceSelection(mesh);
    selection.selectByVertexCount(segments);
    assertEquals(1, selection.size());
  }

  @Test
  public void testMeshContainsTableCircleVertices() {
    int segments = 32;
    float radius = 0.6f;
    float crownHeight = 0.35f;

    CircleCreator creator = new CircleCreator();
    creator.setVertices(segments);
    creator.setRadius(radius);
    creator.setCenterY(-crownHeight);

    Mesh3D circle = creator.create();
    Mesh3D diamond = new DiamondCreator().create();

    for (Vector3f v : circle.getVertices()) {
      assertTrue(diamond.getVertices().contains(v));
    }
  }

  @Test
  public void testMeshContainsVerticesOfSpecifiedTable() {
    int segments = 16;
    float radius = 0.45f;
    float crownHeight = 0.812f;

    CircleCreator creator = new CircleCreator();
    creator.setVertices(segments);
    creator.setRadius(radius);
    creator.setCenterY(-crownHeight);
    Mesh3D circle = creator.create();

    DiamondCreator creator2 = new DiamondCreator();
    creator2.setSegments(segments);
    creator2.setTableRadius(radius);
    creator2.setCrownHeight(crownHeight);
    Mesh3D diamond = creator2.create();

    for (Vector3f v : circle.getVertices()) {
      assertTrue(diamond.getVertices().contains(v));
    }
  }

  @Test
  public void testMeshContainsThirtyTwoQuadsByDefault() {
    Mesh3D mesh = new DiamondCreator().create();
    FaceSelection selection = new FaceSelection(mesh);
    selection.selectQuads();
    assertEquals(32, selection.size());
  }

  @Test
  public void testMeshContainsThirtyTwoTriangularFacesByDefault() {
    Mesh3D mesh = new DiamondCreator().create();
    FaceSelection selection = new FaceSelection(mesh);
    selection.selectTriangles();
    assertEquals(32, selection.size());
  }

  @Test
  public void testEachTriangularFaceContainsPavillionVertexIndex() {
    float pavillionHeight = 0.8f;
    Vector3f pavillionVertex = new Vector3f(0, pavillionHeight, 0);
    Mesh3D mesh = new DiamondCreator().create();
    int pavillionVertexIndex = mesh.getVertices().indexOf(pavillionVertex);
    FaceSelection selection = new FaceSelection(mesh);
    selection.selectTriangles();
    for (Face3D face : selection.getFaces()) {
      assertTrue(MeshTestUtil.containsVertexIndex(face, pavillionVertexIndex));
    }
  }

  @Test
  public void testEachTriangularFaceContainsGirdleVertexIndex() {
    CircleCreator creator = new CircleCreator();
    creator.setRadius(1);
    creator.setVertices(32);
    Mesh3D circle = creator.create();
    Mesh3D mesh = new DiamondCreator().create();
    FaceSelection selection = new FaceSelection(mesh);
    selection.selectTriangles();
    for (Face3D face : selection.getFaces()) {
      for (int i = 0; i < face.indices.length; i++) {
        Vector3f v = mesh.getVertexAt(face.indices[i]);
        circle.vertices.remove(v);
      }
    }
    assertEquals(0, circle.getVertexCount());
  }

  @Test
  public void testNGonConsistsOfTableVertices() {
    CircleCreator creator = new CircleCreator();
    creator.setRadius(0.6f);
    creator.setCenterY(-0.35f);
    creator.setVertices(32);
    Mesh3D circle = creator.create();
    Mesh3D mesh = new DiamondCreator().create();
    FaceSelection selection = new FaceSelection(mesh);
    selection.selectByVertexCount(32);
    for (Face3D face : selection.getFaces()) {
      for (int i = 0; i < face.indices.length; i++) {
        Vector3f v = mesh.getVertexAt(face.indices[i]);
        circle.vertices.remove(v);
      }
    }
    assertEquals(0, circle.getVertexCount());
  }

  @Test
  public void testSecondVertexIndexOfEachTriangleIsPavillionIndex() {
    Mesh3D mesh = new DiamondCreator().create();
    int pavillionIndex = mesh.vertices.indexOf(new Vector3f(0, 0.8f, 0));
    FaceSelection selection = new FaceSelection(mesh);
    selection.selectTriangles();
    for (Face3D face : selection.getFaces()) {
      int actual = face.indices[1];
      assertEquals(pavillionIndex, actual);
    }
  }

  @Test
  public void testFirstIndexOfQuadFacesReferencesGirdleVertex() {
    CircleCreator creator = new CircleCreator();
    creator.setRadius(1);
    creator.setVertices(32);
    Mesh3D circle = creator.create();
    Mesh3D mesh = new DiamondCreator().create();
    FaceSelection selection = new FaceSelection(mesh);
    selection.selectQuads();
    for (Face3D face : selection.getFaces()) {
      int index = face.indices[0];
      Vector3f v = mesh.getVertexAt(index);
      circle.vertices.remove(v);
    }
    assertEquals(0, circle.vertices.size());
  }

  @Test
  public void testThirdIndexOfEachQuadReferencesTablesVertex() {
    CircleCreator creator = new CircleCreator();
    creator.setRadius(0.6f);
    creator.setVertices(32);
    creator.setCenterY(-0.35f);
    Mesh3D circle = creator.create();
    Mesh3D mesh = new DiamondCreator().create();
    FaceSelection selection = new FaceSelection(mesh);
    selection.selectQuads();
    for (Face3D face : selection.getFaces()) {
      int index = face.indices[2];
      Vector3f v = mesh.getVertexAt(index);
      circle.vertices.remove(v);
    }
    assertEquals(0, circle.vertices.size());
  }

  @Test
  public void testMeshIsManifold() {
    assertTrue(MeshTestUtil.isManifold(new DiamondCreator().create()));
  }

  @Test
  public void testThirdTriangleIndexIsLessThanSegmentsCount() {
    int segments = 65;
    DiamondCreator creator = new DiamondCreator();
    creator.setSegments(segments);
    Mesh3D mesh = creator.create();
    FaceSelection selection = new FaceSelection(mesh);
    selection.selectTriangles();
    for (Face3D face : selection.getFaces()) {
      assertTrue(face.indices[2] < segments);
    }
  }

  @Test
  public void testMeshHasNoLooseVertices() {
    Mesh3D mesh = new DiamondCreator().create();
    assertTrue(MeshTestUtil.meshHasNoLooseVertices(mesh));
  }

  @Test
  public void testMeshHasNoDuplicatedFaces() {
    Mesh3D mesh = new DiamondCreator().create();
    assertTrue(MeshTestUtil.meshHasNoDuplicatedFaces(mesh));
  }

  @Test
  public void testNormalsPointOutwards() {
    Mesh3D mesh = new DiamondCreator().create();
    assertTrue(MeshTestUtil.normalsPointOutwards(mesh));
  }
}
