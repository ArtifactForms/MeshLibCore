package mesh.creator.assets;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.topology.ExtrudeModifier;
import mesh.selection.FaceSelection;

public class CubedPillarCreator implements IMeshCreator {

    private int segmentCount;

    private float segmentRadius;

    private float extrude;

    private Mesh3D mesh;

    private FaceSelection selection;

    public CubedPillarCreator() {
        this(5, 0.5f, 0.05f);
    }

    public CubedPillarCreator(int segmentCount, float segmentRadius,
            float extrude) {
        this.segmentCount = segmentCount;
        this.segmentRadius = segmentRadius;
        this.extrude = extrude;
    }

    @Override
    public Mesh3D create() {
        createBaseMesh();
        select();
        extrude();
        snapToGround();
        return mesh;
    }

    private void extrude() {
        for (int i = 0; i < segmentCount - 1; i++) {
            extrude(0.9f, extrude);
            extrude(1.1f, extrude);
            extrude(1, getExtrudeHeight());
        }
    }

    private void extrude(float scale, float amount) {
        new ExtrudeModifier(scale, amount).modify(mesh, selection.getFaces());
    }

    private void select() {
        selection = new FaceSelection(mesh);
        selection.selectTopFaces();
    }

    private void createBaseMesh() {
        CubeCreator creator = new CubeCreator(segmentRadius);
        mesh = creator.create();
    }

    private void snapToGround() {
        mesh.translateY(-segmentRadius);
    }

    private float getExtrudeHeight() {
        return segmentRadius + segmentRadius - extrude - extrude;
    }

    public float getExtrude() {
        return extrude;
    }

    public void setExtrude(float extrude) {
        this.extrude = extrude;
    }

    public int getSegmentCount() {
        return segmentCount;
    }

    public void setSegmentCount(int segmentCount) {
        this.segmentCount = segmentCount;
    }

    public float getSegmentRadius() {
        return segmentRadius;
    }

    public void setSegmentRadius(float segmentRadius) {
        this.segmentRadius = segmentRadius;
    }

}
