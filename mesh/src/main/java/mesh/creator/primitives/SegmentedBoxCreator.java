package mesh.creator.primitives;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.repair.RemoveDoubleVerticesModifier;
import mesh.modifier.transform.RotateModifier;
import mesh.modifier.transform.RotateXModifier;
import mesh.modifier.transform.RotateYModifier;
import mesh.modifier.transform.TransformAxis;
import mesh.modifier.transform.TranslateModifier;

public class SegmentedBoxCreator implements IMeshCreator {

  private int segmentsX;

  private int segmentsY;

  private int segmentsZ;

  private float width;

  private float height;

  private float depth;

  private float segmentSizeX;

  private float segmentSizeY;

  private float segmentSizeZ;

  private Mesh3D mesh;

  public SegmentedBoxCreator() {
    segmentsX = 10;
    segmentsY = 10;
    segmentsZ = 10;
    width = 2;
    height = 2;
    depth = 2;
  }

  @Override
  public Mesh3D create() {
    initializeMesh();
    refreshSegmentSize();
    createFront();
    createBack();
    createLeft();
    createRight();
    createTop();
    createBottom();
    removeDoubles();
    return mesh;
  }

  private void refreshSegmentSize() {
    segmentSizeX = width / (float) segmentsX;
    segmentSizeY = height / (float) segmentsY;
    segmentSizeZ = depth / (float) segmentsZ;
  }

  private void removeDoubles() {
    int roundToDecimalPlaces = 4;
    new RemoveDoubleVerticesModifier(roundToDecimalPlaces).modify(mesh);
  }

  private void initializeMesh() {
    mesh = new Mesh3D();
  }

  private Mesh3D createGrid(
      int subdivisionsX, int subdivisionsZ, float tileSizeX, float tileSizeZ) {
    GridCreator creator = new GridCreator(subdivisionsX, subdivisionsZ, tileSizeX, tileSizeZ);
    return creator.create();
  }

  private void createFront() {
    Mesh3D front = createGrid(segmentsX, segmentsY, segmentSizeX, segmentSizeY);
    new RotateXModifier(-Mathf.HALF_PI).modify(front);
    new TranslateModifier(depth / 2f, TransformAxis.Z).modify(front);
    mesh.append(front);
  }

  private void createBack() {
    Mesh3D back = createGrid(segmentsX, segmentsY, segmentSizeX, segmentSizeY);
    new RotateXModifier(Mathf.HALF_PI).modify(back);
    new TranslateModifier(-depth / 2f, TransformAxis.Z).modify(back);
    mesh.append(back);
  }

  private void createLeft() {
    Mesh3D left = createGrid(segmentsZ, segmentsY, segmentSizeZ, segmentSizeY);
    new RotateXModifier(Mathf.HALF_PI).modify(left);
    new RotateYModifier(Mathf.HALF_PI).modify(left);
    new TranslateModifier(-width / 2f, 0, 0).modify(left);
    mesh.append(left);
  }

  private void createRight() {
    Mesh3D right = createGrid(segmentsZ, segmentsY, segmentSizeZ, segmentSizeY);
    new RotateXModifier(Mathf.HALF_PI).modify(right);
    new RotateModifier(-Mathf.HALF_PI, TransformAxis.Y).modify(right);
    new TranslateModifier(width / 2f, 0, 0).modify(right);
    mesh.append(right);
  }

  private void createTop() {
    Mesh3D top = createGrid(segmentsX, segmentsZ, segmentSizeX, segmentSizeZ);
    new TranslateModifier(0, -height / 2f, 0).modify(top);
    mesh.append(top);
  }

  private void createBottom() {
    Mesh3D bottom = createGrid(segmentsX, segmentsZ, segmentSizeX, segmentSizeZ);
    new RotateXModifier(-Mathf.PI).modify(bottom);
    new TranslateModifier(0, height / 2f, 0).modify(bottom);
    mesh.append(bottom);
  }

  public int getSegmentsX() {
    return segmentsX;
  }

  public void setSegmentsX(int segmentsX) {
    this.segmentsX = segmentsX;
  }

  public int getSegmentsY() {
    return segmentsY;
  }

  public void setSegmentsY(int segmentsY) {
    this.segmentsY = segmentsY;
  }

  public int getSegmentsZ() {
    return segmentsZ;
  }

  public void setSegmentsZ(int segmentsZ) {
    this.segmentsZ = segmentsZ;
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
}
