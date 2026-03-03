package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;

public class CylinderCreator implements IMeshCreator {

  private int vertices;

  private float topRadius;

  private float bottomRadius;

  private float height;

  private FillType topCapFillType;

  private FillType bottomCapFillType;

  public CylinderCreator() {
    vertices = 32;
    topRadius = 1;
    bottomRadius = 1;
    height = 2;
    topCapFillType = FillType.N_GON;
    bottomCapFillType = FillType.N_GON;
  }

  @Override
  public Mesh3D create() {
    SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
    creator.setBottomCapFillType(bottomCapFillType);
    creator.setTopCapFillType(topCapFillType);
    creator.setHeight(height);
    creator.setBottomRadius(bottomRadius);
    creator.setTopRadius(topRadius);
    creator.setRotationSegments(vertices);
    creator.setHeightSegments(1);
    return creator.create();
  }

  public int getVertices() {
    return vertices;
  }

  public void setVertices(int vertices) {
    this.vertices = vertices;
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
