package mesh.creator.special;

import math.Bounds;
import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.modifier.CenterAtModifier;
import mesh.modifier.ExtrudeModifier;
import mesh.modifier.RotateYModifier;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.TranslateModifier;
import mesh.selection.FaceSelection;
import mesh.util.MeshBoundsCalculator;

/**
 * Procedural mesh creator for hexagonal honeycomb structures. Uses modifiers internally as
 * construction steps.
 */
public class HoneyCombCreator implements IMeshCreator {

  private int rowCount;

  private int colCount;

  private float cellRadius;

  private float height;

  private float innerScale;

  private Mesh3D mesh;

  public HoneyCombCreator() {
    rowCount = 2;
    colCount = 2;
    cellRadius = 0.5f;
    height = 0.2f;
    innerScale = 0.9f;
  }

  @Override
  public Mesh3D create() {
    initializeMesh();
    createSegments();
    removeDoubleVertices();
    createInsets();
    solidify();
    centerAtOrigin();
    return mesh;
  }

  private void solidify() {
    if (height == 0) return;
    new SolidifyModifier(height).modify(mesh);
  }

  private void centerAtOrigin() {
    new CenterAtModifier().modify(mesh);
  }

  private void createInsets() {
    ExtrudeModifier modifier = new ExtrudeModifier(innerScale, 0);
    FaceSelection selection = new FaceSelection(mesh);
    selection.selectAll();
    modifier.modify(mesh, selection.getFaces());
    mesh.removeFaces(selection.getFaces());
  }

  private void removeDoubleVertices() {
    mesh.removeDoubles(4);
  }

  private void createSegments() {
    for (int i = 0; i < colCount; i++) {
      for (int j = 0; j < rowCount; j++) {

        Mesh3D segment = createHexSegment();
        Bounds bounds = MeshBoundsCalculator.calculateBounds(segment);

        float width = bounds.getWidth();
        float depth = bounds.getDepth();

        float tx = j % 2 == 1 ? (i * width) + (width / 2.0f) : i * width;
        float tz = j * (depth - cellRadius / 2f);

        new TranslateModifier(tx, 0, tz).modify(segment);

        mesh.append(segment);
      }
    }
  }

  private CircleCreator createCircleCreator() {
    CircleCreator creator = new CircleCreator();
    creator.setFillType(FillType.N_GON);
    creator.setRadius(cellRadius);
    creator.setVertices(6);
    return creator;
  }

  private Mesh3D createHexSegment() {
    Mesh3D segment = createCircleCreator().create();
    new RotateYModifier(Mathf.HALF_PI).modify(segment);
    return segment;
  }

  private void initializeMesh() {
    mesh = new Mesh3D();
  }

  public int getRowCount() {
    return rowCount;
  }

  public void setRowCount(int rowCount) {
    this.rowCount = rowCount;
  }

  public int getColCount() {
    return colCount;
  }

  public void setColCount(int colCount) {
    this.colCount = colCount;
  }

  public float getCellRadius() {
    return cellRadius;
  }

  public void setCellRadius(float cellRadius) {
    this.cellRadius = cellRadius;
  }

  public float getHeight() {
    return height;
  }

  public void setHeight(float height) {
    this.height = height;
  }

  public float getInnerScale() {
    return innerScale;
  }

  public void setInnerScale(float innerScale) {
    this.innerScale = innerScale;
  }
}
