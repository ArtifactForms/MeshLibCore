package mesh.creator.catalan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.util.EnumSet;

import org.junit.jupiter.api.Test;

import math.Bounds;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.io.SimpleObjectReader;
import mesh.util.MeshBoundsCalculator;
import util.MeshTestUtil;

public class DisdyakisDodecahedronCreatorTest {
  enum MeshCapabilities {
    HAS_FACES,
    IS_MANIFOLD,
    HAS_VOLUME,
    ALLOWS_LOOSE_VERTICES,
    CALCULATES_VERTEX_NORMALS
  }

  private static final EnumSet<MeshCapabilities> CAPABILITIES =
      EnumSet.of(
          MeshCapabilities.HAS_FACES, MeshCapabilities.IS_MANIFOLD, MeshCapabilities.HAS_VOLUME);

  private static final int DEFAULT_VERTEX_COUNT = 26;

  private static final int DEFAULT_FACE_COUNT = 48;

  private static final int EXPECTED_DEFAULT_TRIANGLE_COUNT = 48;

  private static final int EXPECTED_DEFAULT_QUAD_COUNT = 0;

  @Test
  public void testImplementsMeshCreatorInstance() {
    DisdyakisDodecahedronCreator creator = new DisdyakisDodecahedronCreator();
    assertTrue(creator instanceof IMeshCreator);
  }

  @Test
  public void testCreatedMeshIsNotNullByDefault() {
    DisdyakisDodecahedronCreator creator = new DisdyakisDodecahedronCreator();
    assertNotNull(creator.create());
  }

  @Test
  public void testVerticesContainNoNaNOrInfinity() {
    Mesh3D mesh = new DisdyakisDodecahedronCreator().create();
    for (Vector3f v : mesh.getVertices()) {
      assertFalse(Float.isNaN(v.x) || Float.isNaN(v.y) || Float.isNaN(v.z));
      assertFalse(Float.isInfinite(v.x) || Float.isInfinite(v.y) || Float.isInfinite(v.z));
    }
  }

  @Test
  public void testMeshHasValidBoundingBox() {
    assumeTrue(CAPABILITIES.contains(MeshCapabilities.HAS_VOLUME));

    Mesh3D mesh = new DisdyakisDodecahedronCreator().create();
    Bounds bounds = MeshBoundsCalculator.calculateBounds(mesh);

    assertTrue(bounds.getWidth() > 0);
    assertTrue(bounds.getHeight() > 0);
    assertTrue(bounds.getDepth() > 0);
  }

  @Test
  public void testCreatesUniqueInstances() {
    DisdyakisDodecahedronCreator creator = new DisdyakisDodecahedronCreator();
    Mesh3D mesh0 = creator.create();
    Mesh3D mesh1 = creator.create();
    Mesh3D mesh2 = creator.create();
    assertTrue(mesh0 != mesh1);
    assertTrue(mesh1 != mesh2);
  }

  @Test
  public void testDefaultVertexCount() {
    Mesh3D mesh = new DisdyakisDodecahedronCreator().create();
    assertEquals(DEFAULT_VERTEX_COUNT, mesh.getVertexCount());
    assertEquals(DEFAULT_VERTEX_COUNT, mesh.getVertices().size());
  }

  @Test
  public void testDefaultFaceCount() {
    Mesh3D mesh = new DisdyakisDodecahedronCreator().create();
    assertEquals(DEFAULT_FACE_COUNT, mesh.getFaceCount());
    assertEquals(DEFAULT_FACE_COUNT, mesh.getFaces().size());
  }

  @Test
  public void testDefaultTriangleCount() {
    Mesh3D mesh = new DisdyakisDodecahedronCreator().create();
    int actual = 0;

    for (Face3D face : mesh.getFaces()) {
      if (face.getVertexCount() == 3) {
        actual++;
      }
    }
    assertEquals(EXPECTED_DEFAULT_TRIANGLE_COUNT, actual);
  }

  @Test
  public void testDefaultQuadCount() {
    Mesh3D mesh = new DisdyakisDodecahedronCreator().create();
    int actual = 0;

    for (Face3D face : mesh.getFaces()) {
      if (face.getVertexCount() == 4) {
        actual++;
      }
    }
    assertEquals(EXPECTED_DEFAULT_QUAD_COUNT, actual);
  }

  @Test
  public void testFacesReferenceValidVertices() {
    Mesh3D mesh = new DisdyakisDodecahedronCreator().create();
    int vertexCount = mesh.getVertexCount();

    for (Face3D face : mesh.getFaces()) {
      for (int i = 0; i < face.getVertexCount(); i++) {
        int idx = face.getIndexAt(i);
        assertTrue(idx >= 0 && idx < vertexCount);
      }
    }
  }

  @Test
  public void testVertexPositionsAreDeterministic() {
    DisdyakisDodecahedronCreator creator = new DisdyakisDodecahedronCreator();
    Mesh3D a = creator.create();
    Mesh3D b = creator.create();

    for (int i = 0; i < a.getVertexCount(); i++) {
      assertEquals(a.getVertices().get(i), b.getVertices().get(i));
    }
  }

  @Test
  public void testVertexDataIsNotSharedBetweenInstances() {
    DisdyakisDodecahedronCreator creator = new DisdyakisDodecahedronCreator();

    Mesh3D mesh0 = creator.create();
    mesh0.clearVertices();

    Mesh3D mesh1 = creator.create();

    assertEquals(DEFAULT_VERTEX_COUNT, mesh1.getVertexCount());
  }

  @Test
  public void testFaceDataIsNotSharedBetweenInstances() {
    DisdyakisDodecahedronCreator creator = new DisdyakisDodecahedronCreator();

    Mesh3D mesh0 = creator.create();
    mesh0.clearFaces();

    Mesh3D mesh1 = creator.create();

    assertEquals(DEFAULT_FACE_COUNT, mesh1.getFaceCount());
  }

  @Test
  public void testVertexListIsNotEmpty() {
    Mesh3D mesh = new DisdyakisDodecahedronCreator().create();
    assertFalse(mesh.getVertices().isEmpty());
    assertEquals(mesh.getVertexCount(), mesh.getVertices().size());
  }

  @Test
  public void testMeshHasNoLooseVertices() {
    assumeFalse(CAPABILITIES.contains(MeshCapabilities.ALLOWS_LOOSE_VERTICES));

    Mesh3D mesh = new DisdyakisDodecahedronCreator().create();
    assertTrue(MeshTestUtil.meshHasNoLooseVertices(mesh));
  }

  @Test
  public void testVertexCountIsConsistent() {
    DisdyakisDodecahedronCreator creator = new DisdyakisDodecahedronCreator();
    Mesh3D mesh0 = creator.create();
    Mesh3D mesh1 = creator.create();
    assertEquals(mesh0.getVertexCount(), mesh1.getVertexCount());
  }

  @Test
  public void testFaceCountIsConsistent() {
    DisdyakisDodecahedronCreator creator = new DisdyakisDodecahedronCreator();
    Mesh3D mesh0 = creator.create();
    Mesh3D mesh1 = creator.create();
    assertEquals(mesh0.getFaceCount(), mesh1.getFaceCount());
  }

  @Test
  public void testMeshHasNoDuplicatedFaces() {
    Mesh3D mesh = new DisdyakisDodecahedronCreator().create();
    assertTrue(MeshTestUtil.meshHasNoDuplicatedFaces(mesh));
  }

  @Test
  public void testIsManifold() {
    assumeTrue(CAPABILITIES.contains(MeshCapabilities.IS_MANIFOLD));

    Mesh3D mesh = new DisdyakisDodecahedronCreator().create();
    assertTrue(MeshTestUtil.isManifold(mesh));
  }

  @Test
  public void testCreatorDoesNotModifyFaceTags() {
    Mesh3D mesh = new DisdyakisDodecahedronCreator().create();
    String expected = "";

    for (Face3D face : mesh.getFaces()) {
      String actual = face.getTag();
      assertEquals(expected, actual);
    }
  }

  @Test
  public void testMatchesReferenceMesh() {
    File file =
        new File("./src/test/java/resources/characterization/DisdyakisDodecahedronCreator.obj");
    SimpleObjectReader reader = new SimpleObjectReader();
    Mesh3D referenceMesh = reader.read(file);
    Mesh3D actual = new DisdyakisDodecahedronCreator().create();

    for (int i = 0; i < actual.getVertexCount(); i++) {
      Vector3f actualV = actual.getVertexAt(i);
      Vector3f referenceV = referenceMesh.getVertexAt(i);

      assertEquals(referenceV.x, actualV.x, 0.0001f);
      assertEquals(referenceV.y, actualV.y, 0.0001f);
      assertEquals(referenceV.z, actualV.z, 0.0001f);
    }

    for (int i = 0; i < actual.getFaceCount(); i++) {
      Face3D actualF = actual.getFaceAt(i);
      Face3D referenceF = referenceMesh.getFaceAt(i);
      for (int j = 0; j < actualF.getVertexCount(); j++) {
        assertEquals(referenceF.getIndexAt(j), actualF.getIndexAt(j));
      }
    }
  }
}
