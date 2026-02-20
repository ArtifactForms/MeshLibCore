package mesh.creator.assets;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.transform.RotateModifier;
import mesh.modifier.transform.TransformAxis;
import mesh.modifier.transform.TranslateModifier;

public class ChainLinkCreator implements IMeshCreator {

  private float centerPieceSize;

  private float majorRadius;

  private float minorRadius;

  private int majorSegments;

  private int minorSegments;

  private Mesh3D partOne;

  private Mesh3D partTwo;

  private Mesh3D mesh;

  public ChainLinkCreator() {
    centerPieceSize = 1.5f;
    majorRadius = 1f;
    minorRadius = 0.25f;
    majorSegments = 12;
    minorSegments = 12;
  }

  private void createVertices() {
    for (int i = 0; i <= majorSegments; i++)
      for (int j = 0; j < minorSegments; j++) createVertexAt(i, j);
  }

  private void createVertexAt(int i, int j) {
    float u = j * Mathf.TWO_PI / minorSegments;
    float v = i * Mathf.TWO_PI / (majorSegments * 2);
    float x = x(u, v);
    float y = y(u);
    float z = z(u, v);
    addVertex(x, y, z);
  }

  private float x(float u, float v) {
    return (majorRadius + minorRadius * Mathf.cos(u)) * Mathf.cos(v);
  }

  private float y(float u) {
    return minorRadius * Mathf.sin(u);
  }

  private float z(float u, float v) {
    return (majorRadius + minorRadius * Mathf.cos(u)) * Mathf.sin(v);
  }

  private void createFaces() {
    for (int j = 0; j < (majorSegments * 2) + 2; j++)
      for (int i = 0; i < minorSegments; i++) createFaceAt(i, j);
  }

  private void createFaceAt(int i, int j) {
    int maj = majorSegments * 2 + 2;
    int min = minorSegments;
    int index0 = (j + 1) % maj * min + (i + 0) % min;
    int index1 = (j + 0) % maj * min + (i + 0) % min;
    int index2 = (j + 1) % maj * min + (i + 1) % min;
    int index3 = (j + 0) % maj * min + (i + 1) % min;
    addFace(index0, index1, index3, index2);
  }

  private void createPartOne() {
    partOne = new Mesh3D();
    mesh = partOne;
  }

  private void createPartTwo() {
    partTwo = partOne.copy();
    new RotateModifier(Mathf.PI, TransformAxis.Y).modify(partTwo);
  }

  private void translateParts() {
    new TranslateModifier(centerPieceSize * 0.5f, TransformAxis.Z).modify(partOne);
    new TranslateModifier(-centerPieceSize * 0.5f, TransformAxis.Z).modify(partTwo);
  }

  private void appendParts() {
    mesh.append(partTwo);
  }

  @Override
  public Mesh3D create() {
    createPartOne();
    createVertices();
    createPartTwo();
    translateParts();
    appendParts();
    createFaces();
    return mesh;
  }

  private void addFace(int... indices) {
    mesh.addFace(indices);
  }

  private void addVertex(float x, float y, float z) {
    mesh.addVertex(x, y, z);
  }

  public float getCenterPieceSize() {
    return centerPieceSize;
  }

  public void setCenterPieceSize(float centerPieceSize) {
    this.centerPieceSize = centerPieceSize;
  }

  public float getMajorRadius() {
    return majorRadius;
  }

  public void setMajorRadius(float majorRadius) {
    this.majorRadius = majorRadius;
  }

  public float getMinorRadius() {
    return minorRadius;
  }

  public void setMinorRadius(float minorRadius) {
    this.minorRadius = minorRadius;
  }

  public int getMajorSegments() {
    return majorSegments;
  }

  public void setMajorSegments(int majorSegments) {
    this.majorSegments = majorSegments;
  }

  public int getMinorSegments() {
    return minorSegments;
  }

  public void setMinorSegments(int minorSegments) {
    this.minorSegments = minorSegments;
  }
}
