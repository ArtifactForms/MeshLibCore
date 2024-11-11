package mesh.creator.assets;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.GridCreator;
import mesh.modifier.ExtrudeModifier;
import mesh.modifier.SolidifyModifier;
import mesh.selection.FaceSelection;

public class FloorPatternCreator implements IMeshCreator {

    private float height;

    private float radius;

    private int subdivisions;

    private Mesh3D mesh;

    private FaceSelection faceSelection;

    public FloorPatternCreator() {
        this(0.2f, 2, 4);
    }

    public FloorPatternCreator(float height, float radius, int subdivisions) {
        this.height = height;
        this.radius = radius;
        this.subdivisions = subdivisions;
    }

    @Override
    public Mesh3D create() {
        createGrid();
        initializeFaceSelection();
        selectAllFaces();
        solidify();
        extrude();
        snapToGround();
        return mesh;
    }

    private void snapToGround() {
        mesh.translateY(-height * 0.5f);
    }

    private void extrude() {
        ExtrudeModifier extrude = new ExtrudeModifier();
        extrude.setScale(0.9f);
        extrude.setAmount(height * 0.5f);
        extrude.setFacesToExtrude(faceSelection.getFaces());
        extrude.modify(mesh);
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
