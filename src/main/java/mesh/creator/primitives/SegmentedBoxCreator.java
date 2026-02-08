package mesh.creator.primitives;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.transform.RotateXModifier;

public class SegmentedBoxCreator implements IMeshCreator {

    private int segmentsX;

    private int segmentsY;

    private int segmentsZ;

    private float width;

    private float height;

    private float depth;

    private float segmentSizeX;

    private float segmentSizeY;

    private float segmentSizeZ;

    private Mesh3D mesh;

    public SegmentedBoxCreator() {
        segmentsX = 10;
        segmentsY = 10;
        segmentsZ = 10;
        width = 2;
        height = 2;
        depth = 2;
    }

    @Override
    public Mesh3D create() {
        initializeMesh();
        refreshSegmentSize();
        createFront();
        createBack();
        createLeft();
        createRight();
        createTop();
        createBottom();
        removeDoubles();
        return mesh;
    }

    private void refreshSegmentSize() {
        segmentSizeX = width / (float) segmentsX;
        segmentSizeY = height / (float) segmentsY;
        segmentSizeZ = depth / (float) segmentsZ;
    }

    private void removeDoubles() {
        int roundToDecimalPlaces = 4;
        mesh.removeDoubles(roundToDecimalPlaces);
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    private Mesh3D createGrid(int subdivisionsX, int subdivisionsZ,
            float tileSizeX, float tileSizeZ) {
        GridCreator creator = new GridCreator(subdivisionsX, subdivisionsZ,
                tileSizeX, tileSizeZ);
        return creator.create();
    }

    private void createFront() {
        Mesh3D front = createGrid(segmentsX, segmentsY, segmentSizeX,
                segmentSizeY);
        front.apply(new RotateXModifier(-Mathf.HALF_PI));
        front.translateZ(depth / 2f);
        mesh.append(front);
    }

    private void createBack() {
        Mesh3D back = createGrid(segmentsX, segmentsY, segmentSizeX,
                segmentSizeY);
        back.apply(new RotateXModifier(Mathf.HALF_PI));
        back.translateZ(-depth / 2f);
        mesh.append(back);
    }

    private void createLeft() {
        Mesh3D left = createGrid(segmentsZ, segmentsY, segmentSizeZ,
                segmentSizeY);
        left.apply(new RotateXModifier(Mathf.HALF_PI));
        left.rotateY(Mathf.HALF_PI);
        left.translateX(-width / 2f);
        mesh.append(left);
    }

    private void createRight() {
        Mesh3D right = createGrid(segmentsZ, segmentsY, segmentSizeZ,
                segmentSizeY);
        right.apply(new RotateXModifier(Mathf.HALF_PI));
        right.rotateY(-Mathf.HALF_PI);
        right.translateX(width / 2f);
        mesh.append(right);
    }

    private void createTop() {
        Mesh3D top = createGrid(segmentsX, segmentsZ, segmentSizeX,
                segmentSizeZ);
        top.translateY(-height / 2f);
        mesh.append(top);
    }

    private void createBottom() {
        Mesh3D bottom = createGrid(segmentsX, segmentsZ, segmentSizeX,
                segmentSizeZ);
        bottom.apply(new RotateXModifier(-Mathf.PI));
        bottom.translateY(height / 2f);
        mesh.append(bottom);
    }

    public int getSegmentsX() {
        return segmentsX;
    }

    public void setSegmentsX(int segmentsX) {
        this.segmentsX = segmentsX;
    }

    public int getSegmentsY() {
        return segmentsY;
    }

    public void setSegmentsY(int segmentsY) {
        this.segmentsY = segmentsY;
    }

    public int getSegmentsZ() {
        return segmentsZ;
    }

    public void setSegmentsZ(int segmentsZ) {
        this.segmentsZ = segmentsZ;
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

}
