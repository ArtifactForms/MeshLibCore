package mesh.creator.assets;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.SolidifyModifier;

public class ModularKitDoorSegmentCreator implements IMeshCreator {

    private float doorWidth;

    private float doorHeight;

    private float segmentWidth;

    private float segmentHeight;

    private float segmentDepth;

    private float doorWidthHalf;

    private float segmentWidthHalf;

    private Mesh3D mesh;

    public ModularKitDoorSegmentCreator() {
        doorWidth = 1.0f;
        doorHeight = 2.0f;
        segmentWidth = 2.0f;
        segmentHeight = 3.0f;
        segmentDepth = 0.0f;
    }

    @Override
    public Mesh3D create() {
        initializeMesh();
        refreshHelperVariables();
        createVertices();
        createFaces();
        solidify();
        return mesh;
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    private void refreshHelperVariables() {
        doorWidthHalf = doorWidth * 0.5f;
        segmentWidthHalf = segmentWidth * 0.5f;
    }

    private void createVertices() {
        createVerticesAtGroundLevel();
        createVerticesAtDoorHeightLevel();
        createVeticesAtSegmentHeightLevel();
    }

    private void createVerticesAtGroundLevel() {
        addVertex(-segmentWidthHalf, 0, 0);
        addVertex(-doorWidthHalf, 0, 0);
        addVertex(doorWidthHalf, 0, 0);
        addVertex(segmentWidthHalf, 0, 0);
    }

    private void createVerticesAtDoorHeightLevel() {
        addVertex(-segmentWidthHalf, -doorHeight, 0);
        addVertex(-doorWidthHalf, -doorHeight, 0);
        addVertex(doorWidthHalf, -doorHeight, 0);
        addVertex(segmentWidthHalf, -doorHeight, 0);
    }

    private void createVeticesAtSegmentHeightLevel() {
        addVertex(-segmentWidthHalf, -segmentHeight, 0);
        addVertex(-doorWidthHalf, -segmentHeight, 0);
        addVertex(doorWidthHalf, -segmentHeight, 0);
        addVertex(segmentWidthHalf, -segmentHeight, 0);
    }

    private void createFaces() {
        addFace(8, 9, 5, 4);
        addFace(9, 10, 6, 5);
        addFace(10, 11, 7, 6);
        addFace(4, 5, 1, 0);
        addFace(6, 7, 3, 2);
    }

    private void addFace(int... indices) {
        mesh.addFace(indices);
    }

    private void addVertex(float x, float y, float z) {
        mesh.addVertex(x, y, z);
    }

    private void solidify() {
        if (segmentDepth <= 0)
            return;
        new SolidifyModifier(segmentDepth).modify(mesh);
        mesh.translateZ(segmentDepth);
    }

    public float getDoorWidth() {
        return doorWidth;
    }

    public void setDoorWidth(float doorWidth) {
        this.doorWidth = doorWidth;
    }

    public float getDoorHeight() {
        return doorHeight;
    }

    public void setDoorHeight(float doorHeight) {
        this.doorHeight = doorHeight;
    }

    public float getSegmentWidth() {
        return segmentWidth;
    }

    public void setSegmentWidth(float segmentWidth) {
        this.segmentWidth = segmentWidth;
    }

    public float getSegmentHeight() {
        return segmentHeight;
    }

    public void setSegmentHeight(float segmentHeight) {
        this.segmentHeight = segmentHeight;
    }

    public float getSegmentDepth() {
        return segmentDepth;
    }

    public void setSegmentDepth(float segmentDepth) {
        this.segmentDepth = segmentDepth;
    }

}
