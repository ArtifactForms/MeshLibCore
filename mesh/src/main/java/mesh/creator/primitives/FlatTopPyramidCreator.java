package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class FlatTopPyramidCreator implements IMeshCreator {

  private float halfExtent;

  private float topScale;

  public FlatTopPyramidCreator() {
    halfExtent = 1f;
    topScale = 0.5f;
  }

  public FlatTopPyramidCreator(float halfExtent, float topScale) {
    this.halfExtent = halfExtent;
    this.topScale = topScale;
  }

  @Override
  public Mesh3D create() {
    Mesh3D mesh = new Mesh3D();

    float top = halfExtent * topScale;

    mesh.addVertex(+top, -halfExtent, -top);
    mesh.addVertex(+top, -halfExtent, +top);
    mesh.addVertex(-top, -halfExtent, +top);
    mesh.addVertex(-top, -halfExtent, -top);

    mesh.addVertex(+halfExtent, +halfExtent, -halfExtent);
    mesh.addVertex(+halfExtent, +halfExtent, +halfExtent);
    mesh.addVertex(-halfExtent, +halfExtent, +halfExtent);
    mesh.addVertex(-halfExtent, +halfExtent, -halfExtent);

    mesh.addFace(3, 0, 1, 2); // top
    mesh.addFace(6, 5, 4, 7);
    mesh.addFace(1, 0, 4, 5);
    mesh.addFace(1, 5, 6, 2);
    mesh.addFace(6, 7, 3, 2);
    mesh.addFace(3, 7, 4, 0);

    return mesh;
  }

  public float getHalfExtent() {
    return halfExtent;
  }

  public void setHalfExtent(float halfExtent) {
    this.halfExtent = halfExtent;
  }

  public float getTopScale() {
    return topScale;
  }

  public void setTopScale(float topScale) {
    this.topScale = topScale;
  }
}
