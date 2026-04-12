package mesh.creator.catalan;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class DisdyakisDodecahedronCreator implements IMeshCreator {

  private float a = (float) (3.0 / (1.0 + 2.0 * Math.sqrt(2.0)));

  private float b = (float) (Math.sqrt(2.0) / (1.0 + Math.sqrt(2.0)));

  private float c = 1.0f;

  @Override
  public Mesh3D create() {
    Mesh3D mesh = new Mesh3D();
    createVertices(mesh);
    createFaces(mesh);
    return mesh;
  }

  private void createVertices(Mesh3D mesh) {
    createEdgeCenterVertices(mesh);
    createCubeVertices(mesh);
    createAxisVertices(mesh);
  }

  private void createEdgeCenterVertices(Mesh3D mesh) {
    mesh.addVertex(0, a, a);
    mesh.addVertex(0, a, -a);
    mesh.addVertex(0, -a, a);
    mesh.addVertex(0, -a, -a);
    mesh.addVertex(a, 0, a);
    mesh.addVertex(a, 0, -a);
    mesh.addVertex(-a, 0, a);
    mesh.addVertex(-a, 0, -a);
    mesh.addVertex(a, a, 0);
    mesh.addVertex(a, -a, 0);
    mesh.addVertex(-a, a, 0);
    mesh.addVertex(-a, -a, 0);
  }

  private void createAxisVertices(Mesh3D mesh) {
    mesh.addVertex(0, 0, c);
    mesh.addVertex(0, 0, -c);
    mesh.addVertex(0, c, 0);
    mesh.addVertex(0, -c, 0);
    mesh.addVertex(c, 0, 0);
    mesh.addVertex(-c, 0, 0);
  }

  private void createCubeVertices(Mesh3D mesh) {
    mesh.addVertex(b, b, b);
    mesh.addVertex(b, b, -b);
    mesh.addVertex(b, -b, b);
    mesh.addVertex(-b, b, b);
    mesh.addVertex(b, -b, -b);
    mesh.addVertex(-b, b, -b);
    mesh.addVertex(-b, -b, b);
    mesh.addVertex(-b, -b, -b);
  }

  private void createFaces(Mesh3D mesh) {
    mesh.addFace(12, 0, 20);
    mesh.addFace(1, 13, 21);
    mesh.addFace(2, 14, 20);
    mesh.addFace(0, 15, 20);
    mesh.addFace(16, 3, 21);
    mesh.addFace(17, 1, 21);
    mesh.addFace(18, 2, 20);
    mesh.addFace(3, 19, 21);
    mesh.addFace(0, 12, 22);
    mesh.addFace(14, 2, 23);
    mesh.addFace(13, 1, 22);
    mesh.addFace(15, 0, 22);
    mesh.addFace(3, 16, 23);
    mesh.addFace(2, 18, 23);
    mesh.addFace(1, 17, 22);
    mesh.addFace(19, 3, 23);
    mesh.addFace(4, 12, 20);
    mesh.addFace(13, 5, 21);
    mesh.addFace(15, 6, 20);
    mesh.addFace(14, 4, 20);
    mesh.addFace(7, 17, 21);
    mesh.addFace(5, 16, 21);
    mesh.addFace(6, 18, 20);
    mesh.addFace(19, 7, 21);
    mesh.addFace(12, 8, 22);
    mesh.addFace(9, 14, 23);
    mesh.addFace(10, 15, 22);
    mesh.addFace(8, 13, 22);
    mesh.addFace(18, 11, 23);
    mesh.addFace(16, 9, 23);
    mesh.addFace(17, 10, 22);
    mesh.addFace(11, 19, 23);
    mesh.addFace(12, 4, 24);
    mesh.addFace(6, 15, 25);
    mesh.addFace(5, 13, 24);
    mesh.addFace(4, 14, 24);
    mesh.addFace(17, 7, 25);
    mesh.addFace(18, 6, 25);
    mesh.addFace(16, 5, 24);
    mesh.addFace(7, 19, 25);
    mesh.addFace(8, 12, 24);
    mesh.addFace(15, 10, 25);
    mesh.addFace(14, 9, 24);
    mesh.addFace(13, 8, 24);
    mesh.addFace(11, 18, 25);
    mesh.addFace(10, 17, 25);
    mesh.addFace(9, 16, 24);
    mesh.addFace(19, 11, 25);
  }
}
