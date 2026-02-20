package mesh.creator.special.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.creator.special.GemCreator;
import mesh.modifier.transform.RotateModifier;
import mesh.modifier.transform.TransformAxis;
import mesh.selection.FaceSelection;
import util.MeshTestUtil;

public class GemCreatorTest {

  @Test
  public void testImplementsCreatorInterface() {
    GemCreator creator = new GemCreator();
    assertTrue(creator instanceof IMeshCreator);
  }

  @Test
  public void testCreatedMeshIsNotNullByDefault() {
    GemCreator creator = new GemCreator();
    assertNotNull(creator.create());
  }

  @Test
  public void testGetSegmentsReturnsDefaultValue() {
    int expected = 8;
    GemCreator creator = new GemCreator();
    assertEquals(expected, creator.getSegments());
  }

  @Test
  public void testGetRadiusReturnsDefaultValue() {
    float expected = 1;
    GemCreator creator = new GemCreator();
    assertEquals(expected, creator.getPavillionRadius(), 0);
  }

  @Test
  public void testGetTableRadiusReturnsDefaultValue() {
    float expected = 0.6f;
    GemCreator creator = new GemCreator();
    assertEquals(expected, creator.getTableRadius(), 0);
  }

  @Test
  public void testGetTableHeightReturnsDefaultValue() {
    float expected = 0.35f;
    GemCreator creator = new GemCreator();
    assertEquals(expected, creator.getTableHeight(), 0);
  }

  @Test
  public void testGetPavillionHeightReturnsDefaultValue() {
    float expected = 0.8f;
    GemCreator creator = new GemCreator();
    assertEquals(expected, creator.getPavillionHeight(), 0);
  }

  @Test
  public void testGetSetSegments() {
    int expected = 76;
    GemCreator creator = new GemCreator();
    creator.setSegments(expected);
    assertEquals(expected, creator.getSegments());
  }

  @Test
  public void getGetSetRadius() {
    float expected = 0.8253f;
    GemCreator creator = new GemCreator();
    creator.setPavillionRadius(expected);
    assertEquals(expected, creator.getPavillionRadius(), 0);
  }

  @Test
  public void testGetSetTableRadius() {
    float expected = 542.21f;
    GemCreator creator = new GemCreator();
    creator.setTableRadius(expected);
    assertEquals(expected, creator.getTableRadius(), 0);
  }

  @Test
  public void testGetSetTableHeight() {
    float expected = 762.1234f;
    GemCreator creator = new GemCreator();
    creator.setTableHeight(expected);
    assertEquals(expected, creator.getTableHeight(), 0);
  }

  @Test
  public void testGetSetPavillionHeight() {
    float expected = 62.1209f;
    GemCreator creator = new GemCreator();
    creator.setPavillionHeight(expected);
    assertEquals(expected, creator.getPavillionHeight(), 0);
  }

  @Test
  public void testCreatedMeshHasFourtyEightFacesByDefault() {
    int expected = 48;
    Mesh3D gem = new GemCreator().create();
    assertEquals(expected, gem.getFaceCount());
  }

  @Test
  public void testCreatedMeshThirtyFourVerticesByDefault() {
    int expected = 34;
    Mesh3D gem = new GemCreator().create();
    assertEquals(expected, gem.getVertexCount());
  }

  @Test
  public void testCreatedMeshHasThirtyTwoTriangularFaces() {
    Mesh3D gem = new GemCreator().create();
    FaceSelection selection = new FaceSelection(gem);
    selection.selectTriangles();
    assertEquals(32, selection.size());
  }

  @Test
  public void testCreatedMeshHasSixteenQuadFaces() {
    Mesh3D gem = new GemCreator().create();
    FaceSelection selection = new FaceSelection(gem);
    selection.selectQuads();
    assertEquals(16, selection.size());
  }

  @Test
  public void testTrianglesCountDependsOnSegments() {
    int expected = 44;
    GemCreator creator = new GemCreator();
    creator.setSegments(11);
    Mesh3D gem = creator.create();
    FaceSelection selection = new FaceSelection(gem);
    selection.selectTriangles();
    assertEquals(expected, selection.size());
  }

  @Test
  public void testQuadCountDependsOnSegments() {
    int expected = 54;
    GemCreator creator = new GemCreator();
    creator.setSegments(27);
    Mesh3D gem = creator.create();
    FaceSelection selection = new FaceSelection(gem);
    selection.selectQuads();
    assertEquals(expected, selection.size());
  }

  @Test
  public void testCreatedMeshContainsCenterCircleVertices() {
    CircleCreator circleCreator = new CircleCreator();
    circleCreator.setRadius(1);
    circleCreator.setVertices(8);
    Mesh3D circle = circleCreator.create();
    new RotateModifier(-Mathf.TWO_PI / 8.0f, TransformAxis.Y).modify(circle);
    Mesh3D gem = new GemCreator().create();
    for (Vector3f v : circle.getVertices()) {
      assertTrue(gem.getVertices().contains(v));
    }
  }

  @Test
  public void testCreatedMeshContainsTableCircleVertices() {
    CircleCreator circleCreator = new CircleCreator();
    circleCreator.setRadius(0.6f);
    circleCreator.setVertices(8);
    circleCreator.setCenterY(-0.35f);
    Mesh3D circle = circleCreator.create();
    Mesh3D gem = new GemCreator().create();
    for (Vector3f v : circle.getVertices()) {
      assertTrue(gem.getVertices().contains(v));
    }
  }

  @Test
  public void testCreatedMeshContainsPavillionVertex() {
    float pavillionHeight = 0.8f;
    Vector3f v = new Vector3f(0, pavillionHeight, 0);
    Mesh3D gem = new GemCreator().create();
    assertTrue(gem.getVertices().contains(v));
  }

  @Test
  public void testCreatedMeshContainsTableVertex() {
    float tableHeight = 0.35f;
    Vector3f v = new Vector3f(0, -tableHeight, 0);
    Mesh3D gem = new GemCreator().create();
    assertTrue(gem.getVertices().contains(v));
  }

  @Test
  public void testCreatedMeshContainsVerticesAtTableHeight() {
    int segments = 26;
    float tableRadius = 0.22f;
    float tableHeight = 12.2114f;

    GemCreator creator = new GemCreator();
    creator.setTableHeight(tableHeight);
    creator.setSegments(segments);
    creator.setTableRadius(tableRadius);
    Mesh3D gem = creator.create();

    CircleCreator circleCreator = new CircleCreator();
    circleCreator.setRadius(tableRadius);
    circleCreator.setCenterY(-tableHeight);
    circleCreator.setVertices(segments);

    Mesh3D circle = circleCreator.create();

    for (Vector3f v : circle.getVertices()) {
      assertTrue(gem.getVertices().contains(v));
    }
  }

  @Test
  public void testCreatedMeshContainsVerticesAtCenterHeight() {
    int segments = 56;
    float radius = 2.34f;
    GemCreator creator = new GemCreator();
    creator.setSegments(segments);
    creator.setPavillionRadius(radius);
    Mesh3D gem = creator.create();
    CircleCreator circleCreator = new CircleCreator();
    circleCreator.setRadius(radius);
    circleCreator.setVertices(segments);
    Mesh3D circle = circleCreator.create();
    new RotateModifier(-Mathf.TWO_PI / segments, TransformAxis.Y).modify(circle);
    for (Vector3f v : circle.getVertices()) {
      assertTrue(gem.getVertices().contains(v));
    }
  }

  @Test
  public void testCreatedMeshContainsTableMidVertices() {
    float gemCenterRadius = 1;
    float tableRadius = 0.6f;
    int segments = 8;
    float angleStep = Mathf.TWO_PI / segments;
    float offset = angleStep / 2.0f;
    float tableHeight = 0.35f;
    float radius = ((gemCenterRadius + tableRadius) / 2.0f) / Mathf.cos(offset);

    CircleCreator circleCreator = new CircleCreator();
    circleCreator.setCenterY(-tableHeight / 2.0f);
    circleCreator.setRadius(radius);
    circleCreator.setVertices(8);
    Mesh3D circle = circleCreator.create();
    new RotateModifier(-offset, TransformAxis.Y).modify(circle);

    Mesh3D gem = new GemCreator().create();

    for (Vector3f v : circle.getVertices()) {
      assertTrue(gem.getVertices().contains(v));
    }
  }

  @Test
  public void testCreatedMeshContainsTableMidVerticesWithParameters() {
    float gemCenterRadius = 65.45f;
    float tableRadius = 0.232f;
    int segments = 11;
    float angleStep = Mathf.TWO_PI / segments;
    float offset = -angleStep / 2.0f;
    float tableHeight = 0.2412f;
    float radius = ((gemCenterRadius + tableRadius) / 2.0f) / Mathf.cos(offset);

    CircleCreator circleCreator = new CircleCreator();
    circleCreator.setCenterY(-tableHeight / 2.0f);
    circleCreator.setRadius(radius);
    circleCreator.setVertices(segments);
    Mesh3D circle = circleCreator.create();
    new RotateModifier(offset, TransformAxis.Y).modify(circle);

    GemCreator creator = new GemCreator();
    creator.setPavillionRadius(gemCenterRadius);
    creator.setTableRadius(tableRadius);
    creator.setSegments(segments);
    creator.setTableHeight(tableHeight);
    Mesh3D gem = creator.create();

    for (Vector3f v : circle.getVertices()) {
      assertTrue(gem.getVertices().contains(v));
    }
  }

  @Test
  public void testHasEightTopFaces() {
    GemCreator creator = new GemCreator();
    Mesh3D gem = creator.create();
    FaceSelection selection = new FaceSelection(gem);
    selection.selectSimilarNormal(new Vector3f(0, -1, 0), 0.00001f);
    assertEquals(8, selection.size());
  }

  @Test
  public void testHasEightTriangularTopFaces() {
    GemCreator creator = new GemCreator();
    Mesh3D gem = creator.create();
    FaceSelection selection = new FaceSelection(gem);
    selection.selectSimilarNormal(new Vector3f(0, -1, 0), 0.00001f);
    for (Face3D face : selection.getFaces()) {
      assertEquals(3, face.indices.length);
    }
  }

  @Test
  public void testCreatedMeshIsManifold() {
    GemCreator creator = new GemCreator();
    Mesh3D gem = creator.create();
    assertTrue(MeshTestUtil.isManifold(gem));
  }

  @Test
  public void testCreatedMeshHasNoLooseVertices() {
    GemCreator creator = new GemCreator();
    Mesh3D gem = creator.create();
    assertTrue(MeshTestUtil.meshHasNoLooseVertices(gem));
  }

  @Test
  public void testCreatedMeshHasNoDuplicatedFaces() {
    GemCreator creator = new GemCreator();
    Mesh3D gem = creator.create();
    assertTrue(MeshTestUtil.meshHasNoDuplicatedFaces(gem));
  }

  @Test
  public void testNormalsPointOutwards() {
    GemCreator creator = new GemCreator();
    Mesh3D gem = creator.create();
    assertTrue(MeshTestUtil.normalsPointOutwards(gem));
  }
}
