package mesh.creator.archimedian;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

/**
 * Generates a cuboctahedron mesh (Archimedean solid).
 *
 * <p>Mesh properties:
 *
 * <ul>
 *   <li>Vertices: 12
 *   <li>Faces: 14 (8 triangles, 6 squares)
 *   <li>Edges: 24
 * </ul>
 *
 * <p>Scaling is controlled via `scale`. All coordinates are multiplied by this value.
 *
 * <p>Usage:
 *
 * <pre>
 * CuboctahedronCreator creator = new CuboctahedronCreator();
 * Mesh3D mesh = creator.create();
 *
 * // Or custom scale
 * CuboctahedronCreator creator2 = new CuboctahedronCreator(2.0f);
 * Mesh3D mesh2 = creator2.create();
 * </pre>
 */
public class CuboctahedronCreator implements IMeshCreator {

  private final float scale;

  /** Default constructor with scale = 1.0 */
  public CuboctahedronCreator() {
    this(1.0f);
  }

  /** Constructor with custom scale */
  public CuboctahedronCreator(float scale) {
    this.scale = scale;
  }

  @Override
  public Mesh3D create() {
    Mesh3D mesh = new Mesh3D();
    createVertices(mesh);
    createFaces(mesh);
    return mesh;
  }

  private void createVertices(Mesh3D mesh) {
    float a = scale;

    mesh.addVertex(0, -a, +a);
    mesh.addVertex(+a, 0, +a);
    mesh.addVertex(0, +a, +a);
    mesh.addVertex(-a, 0, +a);
    mesh.addVertex(+a, -a, 0);
    mesh.addVertex(+a, +a, 0);
    mesh.addVertex(-a, +a, 0);
    mesh.addVertex(-a, -a, 0);
    mesh.addVertex(0, -a, -a);
    mesh.addVertex(+a, 0, -a);
    mesh.addVertex(0, +a, -a);
    mesh.addVertex(-a, 0, -a);
  }

  private void createFaces(Mesh3D mesh) {
    createQuads(mesh);
    createTriangles(mesh);
  }

  private void createQuads(Mesh3D mesh) {
    mesh.addFace(0, 1, 2, 3);
    mesh.addFace(4, 9, 5, 1);
    mesh.addFace(2, 5, 10, 6);
    mesh.addFace(3, 6, 11, 7);
    mesh.addFace(0, 7, 8, 4);
    mesh.addFace(8, 11, 10, 9);
  }

  private void createTriangles(Mesh3D mesh) {
    mesh.addFace(0, 4, 1);
    mesh.addFace(1, 5, 2);
    mesh.addFace(2, 6, 3);
    mesh.addFace(3, 7, 0);
    mesh.addFace(4, 8, 9);
    mesh.addFace(5, 9, 10);
    mesh.addFace(6, 10, 11);
    mesh.addFace(7, 11, 8);
  }
}
