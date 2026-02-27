package mesh.creator.special;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class DiamondCreator implements IMeshCreator {

  private int segments;

  private float girdleRadius;

  private float tableRadius;

  private float crownHeight;

  private float pavillionHeight;

  private Mesh3D mesh;

  public DiamondCreator() {
    segments = 32;
    girdleRadius = 1;
    tableRadius = 0.6f;
    crownHeight = 0.35f;
    pavillionHeight = 0.8f;
  }

  @Override
  public Mesh3D create() {
    initializeMesh();
    createVertices();
    createFaces();
    return mesh;
  }

  private void createVertices() {
    createGirdleVertices();
    createTableVertices();
    createPavillionVertex();
  }

  private void createFaces() {
    createTriangles();
    createQuads();
    createTableFace();
  }

  private void createQuads() {
    for (int i = 0; i < segments; i++) {
      int index0 = i;
      int index1 = (i + 1) % segments;
      int index2 = segments + index1;
      int index3 = segments + index0;
      addFace(index0, index1, index2, index3);
    }
  }

  private void createTriangles() {
    int pavillionIndex = segments + segments;
    for (int i = 0; i < segments; i++) addFace(i, pavillionIndex, (i + 1) % segments);
  }

  private void createTableFace() {
    int[] indices = new int[segments];
    for (int i = 0; i < segments; i++) indices[i] = segments + i;
    addFace(indices);
  }

  private void createPavillionVertex() {
    mesh.addVertex(0, pavillionHeight, 0);
  }

  private void createTableVertices() {
    for (int i = 0; i < segments; i++) {
      float angle = i * (Mathf.TWO_PI / segments);
      float x = tableRadius * Mathf.cos(angle);
      float z = tableRadius * Mathf.sin(angle);
      mesh.addVertex(x, -crownHeight, z);
    }
  }

  private void createGirdleVertices() {
    for (int i = 0; i < segments; i++) {
      float angle = i * (Mathf.TWO_PI / segments);
      float x = girdleRadius * Mathf.cos(angle);
      float z = girdleRadius * Mathf.sin(angle);
      mesh.addVertex(x, 0, z);
    }
  }

  private void initializeMesh() {
    mesh = new Mesh3D();
  }

  private void addFace(int... indices) {
    mesh.addFace(indices);
  }

  public int getSegments() {
    return segments;
  }

  public void setSegments(int segments) {
    this.segments = segments;
  }

  public float getGirdleRadius() {
    return girdleRadius;
  }

  public void setGirdleRadius(float girdleRadius) {
    this.girdleRadius = girdleRadius;
  }

  public float getTableRadius() {
    return tableRadius;
  }

  public void setTableRadius(float tableRadius) {
    this.tableRadius = tableRadius;
  }

  public float getCrownHeight() {
    return crownHeight;
  }

  public void setCrownHeight(float crownHeight) {
    this.crownHeight = crownHeight;
  }

  public float getPavillionHeight() {
    return pavillionHeight;
  }

  public void setPavillionHeight(float pavillionHeight) {
    this.pavillionHeight = pavillionHeight;
  }
}
