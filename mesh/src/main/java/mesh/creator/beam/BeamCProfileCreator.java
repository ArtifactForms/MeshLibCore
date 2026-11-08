package mesh.creator.beam;

import math.Mathf;
import mesh.Mesh3D;
import mesh.modifier.topology.SolidifyModifier;
import mesh.modifier.transform.RotateModifier;
import mesh.modifier.transform.TransformAxis;
import mesh.modifier.transform.TranslateModifier;

public class BeamCProfileCreator implements IBeamCreator {

  private static final float DEFAULT_HEIGHT = 0.85f;

  private static final float DEFAULT_WIDTH = 0.5f;

  private static final float DEFAULT_DEPTH = 2.0f;

  private static final float DEFAULT_THICKNESS = 0.1f;

  private static final float DEFAULT_TAPER = 0.0f;

  private float width;

  private float height;

  private float depth;

  private float thickness;

  private float taper;

  public BeamCProfileCreator() {
    width = DEFAULT_WIDTH;
    height = DEFAULT_HEIGHT;
    depth = DEFAULT_DEPTH;
    thickness = DEFAULT_THICKNESS;
    taper = DEFAULT_TAPER;
  }

  @Override
  public Mesh3D create() {
    Mesh3D mesh = new Mesh3D();

    createVertices(mesh);
    createFaces(mesh);

    applyModifiers(mesh);

    return mesh;
  }

  private void createVertices(Mesh3D mesh) {
    float halfHeight = height / 2f;

    mesh.addVertex(-halfHeight, 0, 0);
    mesh.addVertex(+halfHeight, 0, 0);
    mesh.addVertex(-halfHeight, -width, 0);
    mesh.addVertex(+halfHeight, -width, 0);
    mesh.addVertex(-halfHeight + thickness, -thickness, 0);
    mesh.addVertex(+halfHeight - thickness, -thickness, 0);
    mesh.addVertex(-halfHeight + (thickness * (1f - taper)), -width, 0);
    mesh.addVertex(+halfHeight - (thickness * (1f - taper)), -width, 0);
  }

  private void createFaces(Mesh3D mesh) {
    mesh.addFace(0, 4, 5, 1);
    mesh.addFace(0, 2, 6, 4);
    mesh.addFace(5, 7, 3, 1);
  }

  private void applyModifiers(Mesh3D mesh) {
    rotate(mesh);
    solidify(mesh);
    center(mesh);
  }

  private void rotate(Mesh3D mesh) {
    new RotateModifier(Mathf.HALF_PI, TransformAxis.Z).modify(mesh);
  }

  private void solidify(Mesh3D mesh) {
    new SolidifyModifier(depth).modify(mesh);
  }

  private void center(Mesh3D mesh) {
    new TranslateModifier(-width / 2f, 0, depth / 2f).modify(mesh);
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

  public float getThickness() {
    return thickness;
  }

  public void setThickness(float thickness) {
    this.thickness = thickness;
  }

  public float getTaper() {
    return taper;
  }

  public void setTaper(float taper) {
    this.taper = Mathf.clamp(taper, 0.0f, 1.0f);
  }
}
