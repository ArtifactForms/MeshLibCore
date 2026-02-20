package mesh.creator.assets;

import java.util.Random;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.GridCreator;
import mesh.modifier.topology.ExtrudeModifier;
import mesh.modifier.topology.SolidifyModifier;
import mesh.modifier.transform.TranslateModifier;
import mesh.selection.FaceSelection;
import mesh.util.Mesh3DUtil;

/** Inspiration: https://www.youtube.com/watch?v=aFkBHigEOdE */
public class FloorPatternNoiseCreator implements IMeshCreator {

  private float height;

  private float radius;

  private int subdivisions;

  private long seed;

  private Mesh3D mesh;

  private FaceSelection faceSelection;

  private Random random;

  public FloorPatternNoiseCreator() {
    this(0.2f, 2, 4);
  }

  public FloorPatternNoiseCreator(float height, float radius, int subdivisions) {
    this.height = height;
    this.radius = radius;
    this.subdivisions = subdivisions;
  }

  @Override
  public Mesh3D create() {
    initializeRandomWithSeed();
    createGrid();
    initializeFaceSelection();
    selectAllFaces();
    solidify();
    extrude();
    snapToGround();
    rotateFaces();
    return mesh;
  }

  private void initializeRandomWithSeed() {
    this.random = new Random(seed);
  }

  private void rotateFaces() {
    for (Face3D face : faceSelection.getFaces()) {
      Mesh3DUtil.rotateFaceZ(mesh, face, randomAngle());
      Mesh3DUtil.rotateFaceX(mesh, face, randomAngle());
    }
  }

  private float randomAngle() {
    return Mathf.toRadians(createRandomValue(0, 2));
  }

  private float createRandomValue(float minimum, float maximum) {
    return minimum + random.nextFloat() * (maximum - minimum);
  }

  private void snapToGround() {
    new TranslateModifier(0, -height * 0.5f, 0).modify(mesh);
  }

  private void extrude() {
    new ExtrudeModifier(0.9f, height * 0.5f).modify(mesh, faceSelection.getFaces());
  }

  private void solidify() {
    new SolidifyModifier(height * 0.5f).modify(mesh);
  }

  private void initializeFaceSelection() {
    faceSelection = new FaceSelection(mesh);
  }

  private void selectAllFaces() {
    faceSelection.selectAll();
  }

  private void createGrid() {
    mesh = new GridCreator(subdivisions, subdivisions, radius).create();
  }

  public long getSeed() {
    return seed;
  }

  public void setSeed(long seed) {
    this.seed = seed;
    random = new Random(seed);
  }

  public float getHeight() {
    return height;
  }

  public void setHeight(float height) {
    this.height = height;
  }

  public float getRadius() {
    return radius;
  }

  public void setRadius(float radius) {
    this.radius = radius;
  }

  public int getSubdivisions() {
    return subdivisions;
  }

  public void setSubdivisions(int subdivisions) {
    this.subdivisions = subdivisions;
  }
}
