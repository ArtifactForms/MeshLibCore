package mesh.creator.primitives;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class ArcCreator implements IMeshCreator {

  private float startAngle;

  private float endAngle;

  private float radius;

  private int vertices;

  private Vector3f center;

  private Mesh3D mesh;

  public ArcCreator() {
    startAngle = 0;
    endAngle = Mathf.TWO_PI;
    radius = 1;
    vertices = 32;
    center = new Vector3f();
  }

  @Override
  public Mesh3D create() {
    initializeMesh();
    createVertices();
    return mesh;
  }

  private void initializeMesh() {
    mesh = new Mesh3D();
  }

  private void createVertices() {
    float angleBetweenPoints = calculateAngleBetweenPoints();
    for (int i = 0; i < vertices; i++) {
      float currentAngle = startAngle + angleBetweenPoints * i;
      float x = center.x + radius * Mathf.cos(currentAngle);
      float z = center.z + radius * Mathf.sin(currentAngle);
      addVertex(x, center.y, z);
    }
  }

  private void addVertex(float x, float y, float z) {
    mesh.addVertex(x, y, z);
  }

  private float calculateAngleBetweenPoints() {
    return (endAngle - startAngle) / ((float) vertices - 1);
  }

  public float getStartAngle() {
    return startAngle;
  }

  public void setStartAngle(float startAngle) {
    this.startAngle = startAngle;
  }

  public float getEndAngle() {
    return endAngle;
  }

  public void setEndAngle(float endAngle) {
    this.endAngle = endAngle;
  }

  public float getRadius() {
    return radius;
  }

  public void setRadius(float radius) {
    this.radius = radius;
  }

  public int getVertices() {
    return vertices;
  }

  public void setVertices(int vertices) {
    if (vertices < 2) {
      throw new IllegalArgumentException("Vertex count must be at least 2.");
    }
    this.vertices = vertices;
  }

  public Vector3f getCenter() {
    return new Vector3f(center);
  }

  public void setCenter(Vector3f center) {
    if (center == null) {
      throw new IllegalArgumentException("Center cannot be null.");
    }
    this.center.set(center);
  }
}
