package mesh.creator.assets;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.topology.SolidifyModifier;
import mesh.modifier.transform.TranslateModifier;

public class ArchCreator implements IMeshCreator {

  private int segments;

  private float radius;

  private float extendTop;

  private float extendBottom;

  private float extendLeft;

  private float extendRight;

  private float depth;

  private Mesh3D mesh;

  public ArchCreator() {
    segments = 15;
    radius = 1;
    extendTop = 0.5f;
    extendBottom = 2;
    extendLeft = 1;
    extendRight = 1;
    depth = 1;
  }

  @Override
  public Mesh3D create() {
    initializeMesh();
    createVertices();
    createFaces();
    createArc();
    solidify();
    snapToGroundAndCenterZ();
    return mesh;
  }

  private void createVertices() {
    createLeftVertices();
    createRightVertices();
  }

  private void createFaces() {
    createLeftFace();
    createRightFace();
  }

  private void createArc() {
    float extendStep = calculateWidth() / segments;
    float offsetLeft = -radius - extendLeft;

    for (int i = 0; i <= segments; i++) {
      float x = offsetLeft + (i * extendStep);
      Vector3f v1 = new Vector3f(x, -radius - extendTop, 0);
      Vector3f v0 = createPointOnCircleAt(i);

      if (i > 0 && i < segments) v1.setX(v0.getX());

      addFaceAt(i);
      mesh.addVertex(v0.x, v0.y, v0.z);
      mesh.addVertex(v1.x, v1.y, v1.z);
    }
  }

  private Vector3f createPointOnCircleAt(int i) {
    float angle = Mathf.PI + (i * (Mathf.PI / segments));
    return pointOnCircle(angle);
  }

  private Vector3f pointOnCircle(float angrad) {
    float x = radius * Mathf.cos(angrad);
    float y = radius * Mathf.sin(angrad);
    return new Vector3f(x, y, 0);
  }

  private float calculateWidth() {
    return radius + radius + extendLeft + extendRight;
  }

  private void snapToGroundAndCenterZ() {
    new TranslateModifier(0, -extendBottom, depth / 2f).modify(mesh);
  }

  private void initializeMesh() {
    mesh = new Mesh3D();
  }

  private void solidify() {
    new SolidifyModifier(depth).modify(mesh);
  }

  private void createLeftFace() {
    mesh.addFace(0, 1, 5, 4);
  }

  private void createRightFace() {
    int a = 2 * (segments + 1) + 4;
    addFace(3, 2, a - 2, a - 1);
  }

  private void addFace(int... indices) {
    mesh.addFace(indices);
  }

  private void createLeftVertices() {
    addVertex(-radius, extendBottom, 0);
    addVertex(-radius - extendLeft, extendBottom, 0);
  }

  private void createRightVertices() {
    addVertex(radius, extendBottom, 0);
    addVertex(radius + extendRight, extendBottom, 0);
  }

  private void addFaceAt(int i) {
    if (i >= segments) return;
    int index = (i * 2) + 4;
    int index0 = index;
    int index1 = index + 1;
    int index2 = index + 3;
    int index3 = index + 2;
    mesh.addFace(index0, index1, index2, index3);
  }

  private void addVertex(float x, float y, float z) {
    mesh.addVertex(x, y, z);
  }

  public int getSegments() {
    return segments;
  }

  public void setSegments(int segments) {
    this.segments = segments;
  }

  public float getRadius() {
    return radius;
  }

  public void setRadius(float radius) {
    this.radius = radius;
  }

  public float getExtendTop() {
    return extendTop;
  }

  public void setExtendTop(float extendTop) {
    this.extendTop = extendTop;
  }

  public float getExtendBottom() {
    return extendBottom;
  }

  public void setExtendBottom(float extendBottom) {
    this.extendBottom = extendBottom;
  }

  public float getExtendLeft() {
    return extendLeft;
  }

  public void setExtendLeft(float extendLeft) {
    this.extendLeft = extendLeft;
  }

  public float getExtendRight() {
    return extendRight;
  }

  public void setExtendRight(float extendRight) {
    this.extendRight = extendRight;
  }

  public float getDepth() {
    return depth;
  }

  public void setDepth(float depth) {
    this.depth = depth;
  }
}
