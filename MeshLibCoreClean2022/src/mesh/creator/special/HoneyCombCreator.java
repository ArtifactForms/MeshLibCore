package mesh.creator.special;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.modifier.SolidifyModifier;
import mesh.selection.FaceSelection;
import mesh.util.Bounds3;
import mesh.util.Mesh3DUtil;

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
        center();
        return mesh;
    }

    private void solidify() {
        if (height == 0)
            return;
        new SolidifyModifier(height).modify(mesh);
    }

    private void center() {
        Bounds3 bounds = mesh.calculateBounds();
        mesh.translateX(-bounds.getCenterX());
        mesh.translateZ(-bounds.getCenterZ());
        mesh.translateY(-height / 2f);
    }

    private void createInsets() {
        FaceSelection selection = new FaceSelection(mesh);
        selection.selectAll();
        for (Face3D face : selection.getFaces()) {
            Mesh3DUtil.extrudeFace(mesh, face, innerScale, 0);
        }
        mesh.removeFaces(selection.getFaces());
    }

    private void removeDoubleVertices() {
        mesh.removeDoubles(4);
    }

    private void createSegments() {
        for (int i = 0; i < colCount; i++) {
            for (int j = 0; j < rowCount; j++) {
                Mesh3D segment = createHexSegment();
                Bounds3 bounds = segment.calculateBounds();
                float width = bounds.getWidth();
                float depth = bounds.getDepth();
                segment.translateX(i * width);
                segment.translateZ(j * (depth - cellRadius / 2f));
                if (j % 2 == 1)
                    segment.translateX(width / 2.0f);
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
        segment.rotateY(Mathf.HALF_PI);
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
