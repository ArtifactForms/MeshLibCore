package mesh.analysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import mesh.Mesh;
import mesh.Mesh3D;

class MeshAnalysisTest {

  @Test
  void edgeCount_triangle() {
    Mesh mesh = new Mesh3D();

    int v0 = mesh.addVertex(0, 0, 0);
    int v1 = mesh.addVertex(1, 0, 0);
    int v2 = mesh.addVertex(0, 1, 0);

    mesh.addFace(v0, v1, v2);

    assertEquals(3, MeshAnalysis.edgeCount(mesh));
  }

  @Test
  void edgeCount_quad() {
    Mesh mesh = new Mesh3D();

    int v0 = mesh.addVertex(0, 0, 0);
    int v1 = mesh.addVertex(1, 0, 0);
    int v2 = mesh.addVertex(1, 1, 0);
    int v3 = mesh.addVertex(0, 1, 0);

    mesh.addFace(v0, v1, v2, v3);

    assertEquals(4, MeshAnalysis.edgeCount(mesh));
  }

  @Test
  void edgeCount_twoTriangles_sharedEdge() {
    Mesh mesh = new Mesh3D();

    int v0 = mesh.addVertex(0, 0, 0);
    int v1 = mesh.addVertex(1, 0, 0);
    int v2 = mesh.addVertex(0, 1, 0);
    int v3 = mesh.addVertex(1, 1, 0);

    // first triangle
    mesh.addFace(v0, v1, v2);

    // second triangle shares Edge (v1, v2)
    mesh.addFace(v2, v1, v3);

    assertEquals(5, MeshAnalysis.edgeCount(mesh));
  }
}
