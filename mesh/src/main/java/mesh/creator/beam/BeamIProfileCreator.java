package mesh.creator.beam;

import math.Mathf;
import mesh.Mesh3D;
import mesh.modifier.topology.SolidifyModifier;
import mesh.modifier.transform.TranslateModifier;

public class BeamIProfileCreator implements IBeamCreator {

  private static final float DEFAULT_WIDTH = 0.5f;

  private static final float DEFAULT_HEIGHT = 0.85f;

  private static final float DEFAULT_DEPTH = 2.0f;

  private static final float DEFAULT_THICKNESS = 0.1f;

  private static final float DEFAULT_TAPER = 0.0f;

  private float width;

  private float height;

  private float depth;

  private float thickness;

  private float taper;

  public BeamIProfileCreator() {
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
    float halfWidth = width / 2f;
    float halfThickness = thickness / 2f;
    float thicknessTaper = thickness * (1f - taper);

    mesh.addVertex(-halfThickness, -thickness, 0);
    mesh.addVertex(+halfThickness, -thickness, 0);
    mesh.addVertex(-halfThickness, -height + thickness, 0);
    mesh.addVertex(+halfThickness, -height + thickness, 0);
    mesh.addVertex(-halfThickness, -height, 0);
    mesh.addVertex(+halfThickness, -height, 0);
    mesh.addVertex(-halfWidth, -height + thicknessTaper, 0);
    mesh.addVertex(+halfWidth, -height + thicknessTaper, 0);
    mesh.addVertex(-halfWidth, -height, 0);
    mesh.addVertex(+halfWidth, -height, 0);
    mesh.addVertex(-thicknessTaper / 2f, 0, 0);
    mesh.addVertex(+thicknessTaper / 2, 0, 0);
    mesh.addVertex(-halfWidth, 0, 0);
    mesh.addVertex(+halfWidth, 0, 0);
    mesh.addVertex(-halfWidth, -thicknessTaper, 0);
    mesh.addVertex(+halfWidth, -thicknessTaper, 0);
  }

  private void createFaces(Mesh3D mesh) {
    mesh.addFace(0, 2, 3, 1);
    mesh.addFace(2, 4, 5, 3);
    mesh.addFace(6, 8, 4, 2);
    mesh.addFace(3, 5, 9, 7);
    mesh.addFace(10, 0, 1, 11);
    mesh.addFace(12, 14, 0, 10);
    mesh.addFace(11, 1, 15, 13);
  }

  private void applyModifiers(Mesh3D mesh) {
    solidify(mesh);
    center(mesh);
  }

  private void solidify(Mesh3D mesh) {
    new SolidifyModifier(depth).modify(mesh);
  }

  private void center(Mesh3D mesh) {
    new TranslateModifier(0, height / 2f, depth / 2f).modify(mesh);
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
