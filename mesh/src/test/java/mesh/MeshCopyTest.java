package mesh;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class MeshCopyTest {

  // ------------------------------------------------------------
  // Vertex Deep Copy
  // ------------------------------------------------------------

  @Test
  void copyShouldDeepCopyVertices() {
    Mesh3D mesh = new Mesh3D();
    mesh.addVertex(1, 2, 3);

    Mesh3D copy = mesh.copy();

    copy.getVertexAt(0).x = 999;

    assertNotEquals(mesh.getVertexAt(0).x, copy.getVertexAt(0).x);
  }

  @Test
  void copyShouldPreserveVertexValues() {
    Mesh3D mesh = new Mesh3D();
    mesh.addVertex(1, 2, 3);

    Mesh3D copy = mesh.copy();

    assertEquals(1f, copy.getVertexAt(0).x);
    assertEquals(2f, copy.getVertexAt(0).y);
    assertEquals(3f, copy.getVertexAt(0).z);
  }

  // ------------------------------------------------------------
  // Face Deep Copy
  // ------------------------------------------------------------

  @Test
  void copyShouldDeepCopyFaces() {
    Mesh3D mesh = createSimpleTriangle();

    Mesh3D copy = mesh.copy();

    copy.getFaceAt(0).indices[0] = 2;

    assertNotEquals(mesh.getFaceAt(0).indices[0], copy.getFaceAt(0).indices[0]);
  }

  @Test
  void copyShouldPreserveFaceIndices() {
    Mesh3D mesh = createSimpleTriangle();

    Mesh3D copy = mesh.copy();

    assertArrayEquals(mesh.getFaceAt(0).indices, copy.getFaceAt(0).indices);
  }

  // ------------------------------------------------------------
  // Surface Layer Deep Copy
  // ------------------------------------------------------------

  @Test
  void copyShouldDeepCopySurfaceLayer_uvList() {
    Mesh3D mesh = new Mesh3D();
    mesh.getSurfaceLayer().addUV(0.5f, 0.5f);

    Mesh3D copy = mesh.copy();
    copy.getSurfaceLayer().getUvAt(0).x = 999;

    assertNotEquals(mesh.getSurfaceLayer().getUvAt(0).x, copy.getSurfaceLayer().getUvAt(0).x);
  }

  @Test
  void copyShouldDeepCopySurfaceLayer_faceUvIndices() {
    Mesh3D mesh = createSimpleTriangle();
    mesh.getSurfaceLayer().setFaceUVIndices(0, new int[] {0, 1, 2});

    Mesh3D copy = mesh.copy();

    copy.getSurfaceLayer().getFaceUVIndices(0)[0] = 99;

    assertNotEquals(
        mesh.getSurfaceLayer().getFaceUVIndices(0)[0],
        copy.getSurfaceLayer().getFaceUVIndices(0)[0]);
  }

  // ------------------------------------------------------------
  // Geometry Integrity
  // ------------------------------------------------------------

  @Test
  void copyShouldPreserveCounts() {
    Mesh3D mesh = createSimpleTriangle();

    Mesh3D copy = mesh.copy();

    assertEquals(mesh.getVertexCount(), copy.getVertexCount());
    assertEquals(mesh.getFaceCount(), copy.getFaceCount());
  }

  // ------------------------------------------------------------
  // Helper
  // ------------------------------------------------------------

  private Mesh3D createSimpleTriangle() {
    Mesh3D mesh = new Mesh3D();
    mesh.addVertex(0, 0, 0);
    mesh.addVertex(1, 0, 0);
    mesh.addVertex(0, 1, 0);
    mesh.addFace(0, 1, 2);
    return mesh;
  }
}
