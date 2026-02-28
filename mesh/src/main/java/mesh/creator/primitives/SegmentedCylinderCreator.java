package mesh.creator.primitives;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;

public class SegmentedCylinderCreator implements IMeshCreator {

  private float topRadius;

  private float bottomRadius;

  private float height;

  private int rotationSegments;

  private int heightSegments;

  private boolean capTop;

  private boolean capBottom;

  private FillType topCapFillType;

  private FillType bottomCapFillType;

  private Mesh3D mesh;

  public SegmentedCylinderCreator() {
    topRadius = 1;
    bottomRadius = 1;
    height = 2;
    rotationSegments = 32;
    heightSegments = 1;
    capTop = true;
    capBottom = true;
    topCapFillType = FillType.TRIANGLE_FAN;
    bottomCapFillType = FillType.TRIANGLE_FAN;
  }

  private void addFace(int i, int j) {
    int idx0 = toOneDimensionalIndex(i, j);
    int idx1 = toOneDimensionalIndex(i + 1, j);
    int idx2 = toOneDimensionalIndex(i + 1, (j + 1) % rotationSegments);
    int idx3 = toOneDimensionalIndex(i, (j + 1) % rotationSegments);
    mesh.addFace(idx0, idx1, idx2, idx3);
  }

  private int toOneDimensionalIndex(int i, int j) {
    return Mathf.toOneDimensionalIndex(i, j, rotationSegments);
  }

  private void createVertices() {
    float radiusStep = (topRadius - bottomRadius) / (float) heightSegments;
    float angle = Mathf.TWO_PI / (float) rotationSegments;
    float segmentHeight = height / (float) heightSegments;
    for (int i = 0; i <= heightSegments; i++) {
      for (int j = 0; j < rotationSegments; j++) {
        float x = (topRadius - (i * radiusStep)) * (Mathf.cos(j * angle));
        float y = i * segmentHeight - height / 2f;
        float z = (topRadius - (i * radiusStep)) * (Mathf.sin(j * angle));
        mesh.addVertex(x, y, z);
      }
    }
  }

  private void createQuadFaces() {
    for (int i = 0; i < heightSegments; i++) {
      for (int j = 0; j < rotationSegments; j++) {
        addFace(i, j);
      }
    }
  }

  private void createTriangleFan(int offset, float y) {
    int idx = mesh.getVertexCount();
    mesh.addVertex(0, y, 0);
    for (int i = 0; i < rotationSegments; i++) {
      int idx0 = i + offset;
      int idx1 = (i + 1) % rotationSegments + offset;
      if (offset == 0) {
        mesh.addFace(idx0, idx1, idx);
      } else {
        mesh.addFace(idx1, idx0, idx);
      }
    }
  }

  private void createTopNGon() {
    int[] indices = new int[rotationSegments];
    for (int i = 0; i < indices.length; i++) {
      indices[i] = i;
    }
    mesh.addFace(indices);
  }

  private void createBottomNGon() {
    int max = rotationSegments * (heightSegments + 1) - 1;
    int[] indices = new int[rotationSegments];
    for (int i = 0; i < rotationSegments; i++) {
      indices[i] = max - i;
    }
    mesh.addFace(indices);
  }

  private void createCaps() {
    switch (topCapFillType) {
      case TRIANGLE_FAN:
        createTriangleFan(0, -height / 2);
        break;
      case N_GON:
        createTopNGon();
        break;
      case NOTHING:
        break;
      default:
        break;
    }

    switch (bottomCapFillType) {
      case TRIANGLE_FAN:
        int offset = (heightSegments * rotationSegments);
        createTriangleFan(offset, height / 2);
        break;
      case N_GON:
        createBottomNGon();
        break;
      case NOTHING:
        break;
      default:
        break;
    }
  }

  @Override
  public Mesh3D create() {
    mesh = new Mesh3D();
    createVertices();
    createQuadFaces();
    createCaps();
    return mesh;
  }

  public float getTopRadius() {
    return topRadius;
  }

  public void setTopRadius(float topRadius) {
    this.topRadius = topRadius;
  }

  public float getBottomRadius() {
    return bottomRadius;
  }

  public void setBottomRadius(float bottomRadius) {
    this.bottomRadius = bottomRadius;
  }

  public float getHeight() {
    return height;
  }

  public void setHeight(float height) {
    this.height = height;
  }

  public int getRotationSegments() {
    return rotationSegments;
  }

  public void setRotationSegments(int rotationSegments) {
    this.rotationSegments = rotationSegments;
  }

  public int getHeightSegments() {
    return heightSegments;
  }

  public void setHeightSegments(int heightSegments) {
    this.heightSegments = heightSegments;
  }

  public boolean isCapTop() {
    return capTop;
  }

  public void setCapTop(boolean capTop) {
    this.capTop = capTop;
  }

  public boolean isCapBottom() {
    return capBottom;
  }

  public void setCapBottom(boolean capBottom) {
    this.capBottom = capBottom;
  }

  public FillType getTopCapFillType() {
    return topCapFillType;
  }

  public void setTopCapFillType(FillType topCapFillType) {
    this.topCapFillType = topCapFillType;
  }

  public FillType getBottomCapFillType() {
    return bottomCapFillType;
  }

  public void setBottomCapFillType(FillType bottomCapFillType) {
    this.bottomCapFillType = bottomCapFillType;
  }
}
