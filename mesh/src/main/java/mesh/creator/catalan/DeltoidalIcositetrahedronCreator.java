package mesh.creator.catalan;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class DeltoidalIcositetrahedronCreator implements IMeshCreator {

  private float a = (float) Math.sqrt((2.0 - Math.sqrt(2.0)) / 2.0);

  private float b = (float) (1.0 / Math.sqrt(2.0));

  private float c = 1.0f;

  @Override
  public Mesh3D create() {
    Mesh3D mesh = new Mesh3D();
    createVertices(mesh);
    createFaces(mesh);
    return mesh;
  }

  private void createVertices(Mesh3D mesh) {
    createCubeVertices(mesh);
    createAxisVertices(mesh);
    createEdgeVertices(mesh);
  }

  private void createCubeVertices(Mesh3D mesh) {
    mesh.addVertex(a, a, a);
    mesh.addVertex(a, a, -a);
    mesh.addVertex(a, -a, a);
    mesh.addVertex(-a, a, a);
    mesh.addVertex(a, -a, -a);
    mesh.addVertex(-a, a, -a);
    mesh.addVertex(-a, -a, a);
    mesh.addVertex(-a, -a, -a);
  }

  private void createAxisVertices(Mesh3D mesh) {
    mesh.addVertex(0, 0, c);
    mesh.addVertex(0, 0, -c);
    mesh.addVertex(0, c, 0);
    mesh.addVertex(0, -c, 0);
    mesh.addVertex(c, 0, 0);
    mesh.addVertex(-c, 0, 0);
  }

  private void createEdgeVertices(Mesh3D mesh) {
    // (±b, ±b, 0)
    mesh.addVertex(b, b, 0);
    mesh.addVertex(b, -b, 0);
    mesh.addVertex(-b, b, 0);
    mesh.addVertex(-b, -b, 0);

    // (±b, 0, ±b)
    mesh.addVertex(b, 0, b);
    mesh.addVertex(b, 0, -b);
    mesh.addVertex(-b, 0, b);
    mesh.addVertex(-b, 0, -b);

    // (0, ±b, ±b)
    mesh.addVertex(0, b, b);
    mesh.addVertex(0, b, -b);
    mesh.addVertex(0, -b, b);
    mesh.addVertex(0, -b, -b);
  }

  private void createFaces(Mesh3D mesh) {
    mesh.addFace(8, 18, 0, 22);
    mesh.addFace(9, 23, 1, 19);
    mesh.addFace(2, 18, 8, 24);
    mesh.addFace(8, 22, 3, 20);
    mesh.addFace(9, 19, 4, 25);
    mesh.addFace(5, 23, 9, 21);
    mesh.addFace(6, 24, 8, 20);
    mesh.addFace(9, 25, 7, 21);

    mesh.addFace(10, 22, 0, 14);
    mesh.addFace(1, 23, 10, 14);
    mesh.addFace(15, 2, 24, 11);
    mesh.addFace(3, 22, 10, 16);
    mesh.addFace(25, 4, 15, 11);
    mesh.addFace(10, 23, 5, 16);
    mesh.addFace(11, 24, 6, 17);
    mesh.addFace(7, 25, 11, 17);

    mesh.addFace(14, 0, 18, 12);
    mesh.addFace(19, 1, 14, 12);
    mesh.addFace(18, 2, 15, 12);
    mesh.addFace(20, 3, 16, 13);
    mesh.addFace(15, 4, 19, 12);
    mesh.addFace(16, 5, 21, 13);
    mesh.addFace(17, 6, 20, 13);
    mesh.addFace(21, 7, 17, 13);
  }
}
