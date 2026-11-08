package mesh.creator.archimedian;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

/** Creator for a Cuboctahedron (an Archimedean solid with 8 triangular and 6 square faces). */
public class CuboctahedronCreator implements IMeshCreator {

  /** The actual coordinate factor derived from the target edge length. */
  private final float coordinateFactor;

  /**
   * Creates a cuboctahedron with the default coordinate factor (resulting in an edge length of
   * sqrt(2)).
   */
  public CuboctahedronCreator() {
    this.coordinateFactor = 1.0f;
  }

  /**
   * Creates a cuboctahedron with the specified edge length.
   *
   * @param edgeLength the exact desired edge length of the resulting mesh (must be positive)
   * @throws IllegalArgumentException if edgeLength is less than or equal to zero
   */
  public CuboctahedronCreator(float edgeLength) {
    if (edgeLength <= 0) {
      throw new IllegalArgumentException("Edge length must be greater than zero.");
    }
    // Mathematically, the edge length of a cuboctahedron with coordinate factor 'a'
    // is a * sqrt(2). Therefore, to get the desired edgeLength, we divide by sqrt(2).
    this.coordinateFactor = edgeLength / (float) Math.sqrt(2.0);
  }

  @Override
  public Mesh3D create() {
    Mesh3D mesh = new Mesh3D();
    createVertices(mesh);
    createFaces(mesh);
    return mesh;
  }

  private void createVertices(Mesh3D mesh) {
    float f = coordinateFactor;

    mesh.addVertex(0, -f, +f);
    mesh.addVertex(+f, 0, +f);
    mesh.addVertex(0, +f, +f);
    mesh.addVertex(-f, 0, +f);
    mesh.addVertex(+f, -f, 0);
    mesh.addVertex(+f, +f, 0);
    mesh.addVertex(-f, +f, 0);
    mesh.addVertex(-f, -f, 0);
    mesh.addVertex(0, -f, -f);
    mesh.addVertex(+f, 0, -f);
    mesh.addVertex(0, +f, -f);
    mesh.addVertex(-f, 0, -f);
  }

  private void createFaces(Mesh3D mesh) {
    createTriangularFaces(mesh);
    createQuadFaces(mesh);
  }

  private void createTriangularFaces(Mesh3D mesh) {
    mesh.addFace(0, 4, 1);
    mesh.addFace(1, 5, 2);
    mesh.addFace(2, 6, 3);
    mesh.addFace(3, 7, 0);
    mesh.addFace(4, 8, 9);
    mesh.addFace(7, 11, 8);
    mesh.addFace(5, 9, 10);
    mesh.addFace(6, 10, 11);
  }

  private void createQuadFaces(Mesh3D mesh) {
    mesh.addFace(0, 1, 2, 3);
    mesh.addFace(4, 9, 5, 1);
    mesh.addFace(2, 5, 10, 6);
    mesh.addFace(3, 6, 11, 7);
    mesh.addFace(0, 7, 8, 4);
    mesh.addFace(8, 11, 10, 9);
  }
}
