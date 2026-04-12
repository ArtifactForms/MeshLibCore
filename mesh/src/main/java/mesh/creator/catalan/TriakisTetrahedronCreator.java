package mesh.creator.catalan;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TriakisTetrahedronCreator implements IMeshCreator {

  /**
   * Scale factor for tetrahedron vertices so that the resulting triakis tetrahedron has symmetric
   * (congruent) faces.
   */
  private static final float DEFAULT_TRIAKIS_TETRAHEDRON_SCALE = 5f / 3f;

  private float scale;

  private Mesh3D mesh;

  public TriakisTetrahedronCreator() {
    this(DEFAULT_TRIAKIS_TETRAHEDRON_SCALE);
  }

  public TriakisTetrahedronCreator(float scale) {
    this.scale = scale;
  }

  @Override
  public Mesh3D create() {
    initializeMesh();
    createVertices();
    createFaces();
    return mesh;
  }

  private void createVertices() {
    createTetrahedronVertices();
    createInnerVertices();
  }

  private void createTetrahedronVertices() {
    addVertex(scale, scale, scale);
    addVertex(scale, -scale, -scale);
    addVertex(-scale, -scale, scale);
    addVertex(-scale, scale, -scale);
  }

  private void createInnerVertices() {
    addVertex(1, -1, 1);
    addVertex(-1, 1, 1);
    addVertex(1, 1, -1);
    addVertex(-1, -1, -1);
  }

  public void createFaces() {
    addFace(4, 0, 5);
    addFace(6, 1, 7);
    addFace(2, 4, 5);
    addFace(3, 6, 7);
    addFace(5, 0, 6);
    addFace(1, 4, 7);
    addFace(4, 2, 7);
    addFace(3, 5, 6);
    addFace(0, 4, 6);
    addFace(4, 1, 6);
    addFace(2, 5, 7);
    addFace(5, 3, 7);
  }

  private void initializeMesh() {
    mesh = new Mesh3D();
  }

  private void addVertex(float x, float y, float z) {
    mesh.addVertex(x, y, z);
  }

  private void addFace(int... indices) {
    mesh.addFace(indices);
  }
}
