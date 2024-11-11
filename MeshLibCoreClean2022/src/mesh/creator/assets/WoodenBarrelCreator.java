package mesh.creator.assets;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.SegmentedCylinderCreator;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;
import mesh.selection.FaceSelection;
import mesh.util.Mesh3DUtil;

public class WoodenBarrelCreator implements IMeshCreator {

    private float radius;

    private float height;

    private float inset;

    private float bendFactor;

    private int rotationSegments;

    private int heightSegments;

    private Mesh3D mesh;

    public WoodenBarrelCreator() {
        radius = 1.0f;
        height = 2.0f;
        inset = 0.05f;
        bendFactor = 0.75f;
        rotationSegments = 16;
        heightSegments = 8;
    }

    @Override
    public Mesh3D create() {
        createBaseCylinder();
        bend();
        createInsets();
        center();
        return mesh;
    }

    private void createBaseCylinder() {
        SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
        creator.setHeight(height);
        creator.setRotationSegments(rotationSegments);
        creator.setHeightSegments(heightSegments);
        creator.setTopRadius(radius);
        creator.setBottomRadius(radius);
        creator.setCapFillType(FillType.N_GON);
        mesh = creator.create();
    }

    private void bend() {
        for (Vector3f v : mesh.getVertices(0, mesh.getVertexCount())) {
            float y = v.getY();
            float m = Mathf.cos(Mathf.abs(y * bendFactor));
            v.multLocal(new Vector3f(m, 1, m));
        }
    }

    private void createInsets() {
        FaceSelection selection = new FaceSelection(mesh);
        selection.selectTopFaces();
        selection.selectBottomFaces();

        for (Face3D face : selection.getFaces()) {
            Mesh3DUtil.extrudeFace(mesh, face, 0.9f, 0.0f);
            Mesh3DUtil.extrudeFace(mesh, face, 1.0f, -inset);
            Mesh3DUtil.extrudeFace(mesh, face, 0.9f, 0.0f);
            new PlanarVertexCenterModifier().modify(mesh, face);
        }
    }

    private void center() {
        mesh.translateY(-height * 0.5f);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getInset() {
        return inset;
    }

    public void setInset(float inset) {
        this.inset = inset;
    }

    public float getBendFactor() {
        return bendFactor;
    }

    public void setBendFactor(float bendFactor) {
        this.bendFactor = bendFactor;
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

}
