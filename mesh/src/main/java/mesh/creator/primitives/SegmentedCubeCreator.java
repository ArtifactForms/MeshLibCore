package mesh.creator.primitives;

import java.lang.System.Logger.Level;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.repair.RemoveDoubleVerticesModifier;
import mesh.modifier.transform.RotateModifier;
import mesh.modifier.transform.RotateXModifier;
import mesh.modifier.transform.ScaleModifier;
import mesh.modifier.transform.TransformAxis;
import mesh.modifier.transform.TranslateModifier;

public class SegmentedCubeCreator implements IMeshCreator {

  private int segments;

  private float size;

  private float creationSize;

  private Mesh3D mesh;

  public SegmentedCubeCreator() {
    this(10, 1);
  }

  public SegmentedCubeCreator(int segments, float size) {
    this.segments = segments;
    this.size = size;
    this.creationSize = (float) segments;
  }

  private Mesh3D createSide() {
    return new GridCreator(segments, segments, creationSize).create();
  }

  private void append(Mesh3D mesh) {
    this.mesh.append(mesh);
  }

  private void createTop() {
    Mesh3D top = createSide();
    new TranslateModifier(0, -creationSize, 0).modify(top);
    append(top);
  }

  private void createBottom() {
    Mesh3D bottom = createSide();
    new RotateXModifier(Mathf.PI).modify(bottom);
    new TranslateModifier(0, creationSize, 0).modify(bottom);
    append(bottom);
  }

  private void createFront() {
    Mesh3D front = createSide();
    new RotateXModifier(Mathf.HALF_PI).modify(front);
    new TranslateModifier(-creationSize, TransformAxis.Z).modify(front);
    append(front);
  }

  private void createBack() {
    Mesh3D back = createSide();
    new RotateXModifier(-Mathf.HALF_PI).modify(back);
    new TranslateModifier(creationSize, TransformAxis.Z).modify(back);
    append(back);
  }

  private void createLeft() {
    Mesh3D left = createSide();
    new RotateModifier(-Mathf.HALF_PI, TransformAxis.Z).modify(left);
    new TranslateModifier(-creationSize, 0, 0).modify(left);
    append(left);
  }

  private void createRight() {
    Mesh3D right = createSide();
    new RotateModifier(Mathf.HALF_PI, TransformAxis.Z).modify(right);
    new TranslateModifier(creationSize, 0, 0).modify(right);
    append(right);
  }

  private void removeDoubles() {
    int roundToDecimalPlaces = 2;
    new RemoveDoubleVerticesModifier(roundToDecimalPlaces).modify(mesh);
  }

  private void scale() {
    float scale = 1.0f / creationSize;
    new ScaleModifier(scale).modify(mesh);
    new ScaleModifier(size).modify(mesh);
  }

  private void initializeMesh() {
    mesh = new Mesh3D();
  }

  @Override
  public Mesh3D create() {
    initializeMesh();
    createTop();
    createBottom();
    createFront();
    createBack();
    createLeft();
    createRight();
    removeDoubles();
    scale();
    return mesh;
  }

  public int getSegments() {
    return segments;
  }

  public void setSegments(int segments) {
    this.segments = segments;
  }

  public float getSize() {
    return size;
  }

  public void setSize(float size) {
    this.size = size;
  }
}
