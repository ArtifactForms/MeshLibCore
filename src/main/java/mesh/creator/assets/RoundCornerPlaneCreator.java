package mesh.creator.assets;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.ArcCreator;
import mesh.modifier.CenterAtModifier;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.subdivision.QuadsToTrianglesModifier;

public class RoundCornerPlaneCreator implements IMeshCreator {

  private float width = 3;

  private float height = 0;

  private float depth = 6;

  private float radius = 1f;

  private int segments = 16;

  private boolean triangulateFaces = true;

  private Mesh3D mesh;

  @Override
  public Mesh3D create() {
    mesh = new Mesh3D();
    createVertices();
    createAllSideFaces();
    createAllCornerFaces();
    createCenterFace();
    solidify();
    centerAtOrigin();
    triangulateQuads();
    return mesh;
  }

  private Vector3f createCornerVertex(float xSign, float zSign) {
    float x = (xSign * (width / 2)) + (xSign * -radius);
    float z = (zSign * (depth / 2)) + (zSign * -radius);
    
    return new Vector3f(x, 0, z);
  }

  private void createVertices() {
    Vector3f topLeft = createCornerVertex(-1, -1);
    Vector3f topRight = createCornerVertex(1, -1);
    Vector3f bottomRight = createCornerVertex(1, 1);
    Vector3f bottomLeft = createCornerVertex(-1, 1);

    createCornerVertices(topLeft, Mathf.PI, Mathf.PI + Mathf.HALF_PI);
    createCornerVertices(topRight, Mathf.PI + Mathf.HALF_PI, Mathf.TWO_PI);
    createCornerVertices(bottomRight, 0, Mathf.HALF_PI);
    createCornerVertices(bottomLeft, Mathf.HALF_PI, Mathf.PI);

    mesh.add(topLeft);
    mesh.add(topRight);
    mesh.add(bottomRight);
    mesh.add(bottomLeft);
  }

  private void solidify() {
    if (height == 0) return;
    mesh.apply(new SolidifyModifier(height));
  }

  private void centerAtOrigin() {
    mesh.apply(new CenterAtModifier());
  }

  private void triangulateQuads() {
    if (!triangulateFaces) return;
    mesh.apply(new QuadsToTrianglesModifier());
  }

  private void createAllCornerFaces() {
    if (triangulateFaces) {
      createAllCornerTriangles();
    } else {
      createAllCornerNGons();
    }
  }

  private void createAllCornerTriangles() {
    for (int i = 0; i < 4; i++) {
      createCornerTriangles(i);
    }
  }

  private void createAllCornerNGons() {
    for (int i = 0; i < 4; i++) {
      createCornerNGons(i);
    }
  }

  private void createAllSideFaces() {
    for (int i = 0; i < 4; i++) {
      createSideFace(i);
    }
  }

  private void createSideFace(int index) {
    int index0 = calculateTotalVertexCount() - (4 - index);
    int index1 = (((index + 1) * segments) + index) % (4 * segments + 4);
    int index2 = (index1 + 1) % (4 * segments + 4);
    int index3 = calculateTotalVertexCount() - (3 - index);
    index3 = index3 == calculateTotalVertexCount() ? index3 - 4 : index3;
    
    mesh.addFace(index0, index1, index2, index3);
  }

  private void createCornerNGons(int index) {
    int[] indices = new int[segments + 2];
    indices[0] = calculateTotalVertexCount() - (4 - index);
    for (int i = 0; i < segments + 1; i++) {
      indices[i + 1] = (index * (segments + 1)) + i;
    }
    mesh.addFace(indices);
  }

  private void createCornerTriangles(int index) {
    int centerIndex = calculateTotalVertexCount() - (4 - index);
    int baseIndex = index * (segments + 1);

    for (int i = 0; i < segments; i++) {
      int index1 = baseIndex + i;
      int index2 = baseIndex + i + 1;
      mesh.addFace(centerIndex, index1, index2);
    }
  }

  private void createCenterFace() {
    int index = calculateTotalVertexCount();
    mesh.addFace(index - 4, index - 3, index - 2, index - 1);
  }

  private void createCornerVertices(Vector3f center, float startAngle, float endAngle) {
    ArcCreator creator = new ArcCreator();
    creator.setRadius(radius);
    creator.setVertices(segments + 1);
    creator.setStartAngle(startAngle);
    creator.setEndAngle(endAngle);
    creator.setCenter(center);

    Mesh3D arc = creator.create();
    mesh.addVertices(arc.getVertices());
  }

  private int calculateTotalVertexCount() {
    return 4 + (4 * (segments + 1));
  }

  public float getWidth() {
    return width;
  }

  public void setWidth(float width) {
    this.width = width;
  }

  public float getHeight() {
    return height;
  }

  public void setHeight(float height) {
    this.height = height;
  }

  public float getDepth() {
    return depth;
  }

  public void setDepth(float depth) {
    this.depth = depth;
  }

  public float getRadius() {
    return radius;
  }

  public void setRadius(float radius) {
    this.radius = radius;
  }

  public int getSegments() {
    return segments;
  }

  public void setSegments(int segments) {
    this.segments = segments;
  }

  public boolean isTriangulateFaces() {
    return triangulateFaces;
  }

  public void setTriangulateFaces(boolean triangulateFaces) {
    this.triangulateFaces = triangulateFaces;
  }
}
