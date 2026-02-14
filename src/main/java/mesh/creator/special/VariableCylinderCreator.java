package mesh.creator.special;

import java.util.ArrayList;
import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.modifier.repair.RemoveDoubleVerticesModifier;
import mesh.modifier.transform.RotateXModifier;
import mesh.modifier.transform.TranslateModifier;

/**
 * VariableCylinderCreator
 *
 * <p>A procedural mesh creator that generates a vertically segmented cylinder with variable radius
 * per height segment.
 *
 * <h2>Concept</h2>
 *
 * The cylinder is defined as a stack of circular cross-sections (rings) along the Y-axis. Each
 * segment is defined by:
 *
 * <ul>
 *   <li>a radius
 *   <li>a height offset
 * </ul>
 *
 * The final cylinder height equals the sum of all provided segment heights.
 *
 * <h2>Modeling Capabilities</h2>
 *
 * This creator enables convenient procedural / code-driven modeling comparable to:
 *
 * <ul>
 *   <li>"Loop cut" behavior (adding vertical segments)
 *   <li>Scaling rings individually
 *   <li>Profile-based cylinder shaping
 * </ul>
 *
 * By varying the radius per segment, the resulting mesh can represent:
 *
 * <ul>
 *   <li>Cones
 *   <li>Barrels
 *   <li>Pillars
 *   <li>Vases
 *   <li>Organic tapered forms
 * </ul>
 *
 * <h2>Caps</h2>
 *
 * The bottom and top of the cylinder can be filled independently using {@link FillType}:
 *
 * <ul>
 *   <li>{@code TRIANGLE_FAN}
 *   <li>{@code N_GON}
 *   <li>{@code NOTHING}
 * </ul>
 *
 * <h2>Determinism</h2>
 *
 * The resulting mesh is deterministic for identical input parameters. Vertices are rounded before
 * welding to ensure stable double removal.
 *
 * <h2>Topology</h2>
 *
 * If caps are enabled and at least two segments are provided, the resulting mesh is manifold.
 *
 * <h2>Usage Example</h2>
 *
 * <pre>
 * VariableCylinderCreator creator = new VariableCylinderCreator();
 * creator.setRotationSegments(32);
 *
 * creator.add(1.0f, 0.0f);   // base
 * creator.add(0.8f, 1.0f);   // taper
 * creator.add(0.8f, 1.5f);   // straight
 * creator.add(0.4f, 2.8f);   // top taper
 *
 * creator.setCapBottomFillType(FillType.N_GON);
 * creator.setCapTopFillType(FillType.TRIANGLE_FAN);
 *
 * Mesh3D mesh = creator.create();
 * </pre>
 */
public class VariableCylinderCreator implements IMeshCreator {

  private Mesh3D mesh;

  private float lastY;

  private int rotationSegments;

  private FillType capBottomFillType;

  private FillType capTopFillType;

  private List<Float> radii;

  private List<Float> yCoordinates;

  public VariableCylinderCreator() {
    radii = new ArrayList<Float>();
    rotationSegments = 16;
    yCoordinates = new ArrayList<Float>();
    capBottomFillType = FillType.N_GON;
    capTopFillType = FillType.N_GON;
  }

  @Override
  public Mesh3D create() {
    initializeMesh();

    if (yCoordinates.size() <= 1) return mesh;

    createVertices();
    createFaces();
    capEnds();
    removeDoubles();

    return mesh;
  }

  private void initializeMesh() {
    mesh = new Mesh3D();
  }

  private void capEnds() {
    capBottom();
    capTop();
  }

  private void removeDoubles() {
    for (Vector3f v : mesh.vertices) v.roundLocalDecimalPlaces(4);
    new RemoveDoubleVerticesModifier().modify(mesh);
  }

  private void capBottom() {
    if (capBottomFillType == FillType.NOTHING) return;

    CircleCreator creator = new CircleCreator();
    creator.setFillType(capBottomFillType);
    creator.setRadius(radii.get(0));
    creator.setVertices(rotationSegments);

    Mesh3D bottom = creator.create();

    new RotateXModifier(Mathf.PI).modify(bottom);
    new TranslateModifier(0, yCoordinates.get(0), 0).modify(bottom);

    mesh.append(bottom);
  }

  private void capTop() {
    if (capTopFillType == FillType.NOTHING) return;

    CircleCreator creator = new CircleCreator();
    creator.setFillType(capTopFillType);
    creator.setRadius(radii.get(radii.size() - 1));
    creator.setVertices(rotationSegments);

    Mesh3D top = creator.create();

    new TranslateModifier(0, yCoordinates.get(yCoordinates.size() - 1), 0).modify(top);

    mesh.append(top);
  }

  private void createVertices() {
    float angleStep = Mathf.TWO_PI / rotationSegments;
    for (int i = 0; i < radii.size(); i++) {
      for (int j = 0; j < rotationSegments; j++) {
        float radius = radii.get(i);
        float x = radius * Mathf.cos(angleStep * j);
        float z = radius * Mathf.sin(angleStep * j);
        float y = yCoordinates.get(i);
        mesh.add(new Vector3f(x, y, z));
      }
    }
  }

  private void createFaces() {
    createQuadFaces();
  }

  private void createQuadFaces() {
    for (int i = 0; i < yCoordinates.size() - 1; i++) {
      for (int j = 0; j < rotationSegments; j++) {
        addFace(i, j);
      }
    }
  }

  private void addFace(int i, int j) {
    int idx0 = toOneDimensionalIndex(i, j);
    int idx1 = toOneDimensionalIndex(i + 1, j);
    int idx2 = toOneDimensionalIndex(i + 1, j + 1);
    int idx3 = toOneDimensionalIndex(i, j + 1);
    mesh.addFace(idx3, idx2, idx1, idx0);
  }

  private int toOneDimensionalIndex(int i, int j) {
    int n = rotationSegments;
    return Mathf.toOneDimensionalIndex(i, j % n, n);
  }

  public void addRingSegment(float radius, float height) {
    float y1 = -height;
    radii.add(radius);
    yCoordinates.add(y1 + lastY);
    lastY = y1 + lastY;
  }

  public void clear() {
    lastY = 0; // Fixed
    radii.clear();
    yCoordinates.clear();
  }

  public int getRotationSegments() {
    return rotationSegments;
  }

  public void setRotationSegments(int rotationSegments) {
    this.rotationSegments = rotationSegments;
  }

  public FillType getCapBottomFillType() {
    return capBottomFillType;
  }

  public void setCapBottomFillType(FillType capBottomFillType) {
    this.capBottomFillType = capBottomFillType;
  }

  public FillType getCapTopFillType() {
    return capTopFillType;
  }

  public void setCapTopFillType(FillType capTopFillType) {
    this.capTopFillType = capTopFillType;
  }
}
