package mesh.creator.assets;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.ExtrudeModifier;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;
import mesh.selection.FaceSelection;

public class SimpleSciFiCrateCreator implements IMeshCreator {

    private float extrudeAmount;

    private float extrudeScale;

    private Mesh3D mesh;

    private FaceSelection faceSelection;

    public SimpleSciFiCrateCreator() {
        extrudeAmount = 0.05f;
        extrudeScale = 0.9f;
    }

    @Override
    public Mesh3D create() {
        createCube();
        initializeFaceSelection();
        selectAllFaces();
        splitFaces();
        clearSelection();
        selectTriangles();
        extrudeSelectedFaces();
        snapToGround();
        return mesh;
    }

    private void clearSelection() {
        faceSelection.clear();
    }

    private void selectTriangles() {
        faceSelection.selectTriangles();
    }

    private void splitFaces() {
        new PlanarVertexCenterModifier().modify(mesh, faceSelection.getFaces());
    }

    private void extrudeSelectedFaces() {
        new ExtrudeModifier(extrudeScale, extrudeAmount).modify(mesh, faceSelection.getFaces());
        ;
    }

    private void selectAllFaces() {
        faceSelection.selectAll();
    }

    private void initializeFaceSelection() {
        faceSelection = new FaceSelection(mesh);
    }

    private void createCube() {
        CubeCreator creator = new CubeCreator();
        mesh = creator.create();
    }

    private void snapToGround() {
        mesh.translateY(-1 - extrudeAmount);
    }

    public float getExtrudeAmount() {
        return extrudeAmount;
    }

    public void setExtrudeAmount(float extrudeAmount) {
        this.extrudeAmount = extrudeAmount;
    }

    public float getExtrudeScale() {
        return extrudeScale;
    }

    public void setExtrudeScale(float extrudeScale) {
        this.extrudeScale = extrudeScale;
    }

}
