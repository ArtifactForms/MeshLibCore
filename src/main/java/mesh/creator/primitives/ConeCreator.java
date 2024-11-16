package mesh.creator.primitives;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;

public class ConeCreator implements IMeshCreator {

    private int rotationSegments;

    private int heightSegments;

    private float topRadius;

    private float bottomRadius;

    private float height;

    private Mesh3D mesh;

    public ConeCreator() {
        rotationSegments = 32;
        heightSegments = 10;
        topRadius = 0;
        bottomRadius = 1;
        height = 2;
    }

    private float getSegmentHeight() {
        return height / heightSegments;
    }

    private float getRadius(int i) {
        float radius = Mathf.map(i, 0, heightSegments, topRadius, bottomRadius);
        return radius;
    }

    private void createVertices() {
        int start = topRadius > 0 ? 0 : 1;
        int end = bottomRadius > 0 ? 0 : 1;
        float angle = Mathf.TWO_PI / rotationSegments;
        for (int i = start; i <= heightSegments - end; i++) {
            for (int j = 0; j < rotationSegments; j++) {
                float x = getRadius(i) * Mathf.cos(angle * j);
                float y = getSegmentHeight() * i - (height * 0.5f);
                float z = getRadius(i) * Mathf.sin(angle * j);
                mesh.addVertex(x, y, z);
            }
        }
    }

    private void addFace(int i, int j) {
        int idx0 = toOneDimensionalIndex(i, j);
        int idx1 = toOneDimensionalIndex(i + 1, j);
        int idx2 = toOneDimensionalIndex(i + 1, j + 1);
        int idx3 = toOneDimensionalIndex(i, j + 1);
        mesh.addFace(idx0, idx1, idx2, idx3);
    }

    private int toOneDimensionalIndex(int i, int j) {
        int cols = rotationSegments;
        return Mathf.toOneDimensionalIndex(i, j % cols, cols);
    }

    private void createQuadFaces() {
        int end = topRadius > 0 ? 0 : 1;
        end += bottomRadius > 0 ? 0 : 1;
        for (int i = 0; i < heightSegments - end; i++) {
            for (int j = 0; j < rotationSegments; j++) {
                addFace(i, j);
            }
        }
    }

    private void createTopCap() {
        int[] indices = new int[rotationSegments];

        for (int i = 0; i < indices.length; i++)
            indices[i] = i;

        mesh.addFace(indices);
    }

    private void createBottomCap() {
        int index = mesh.getVertexCount() - 1;
        int[] indices = new int[rotationSegments];

        for (int i = 0; i < indices.length; i++)
            indices[i] = index - i;

        mesh.addFace(indices);
    }

    private void splitTop() {
        if (topRadius > 0)
            return;

        splitFace(mesh.getFaceCount() - 2, -getSegmentHeight());
    }

    private void splitBottom() {
        if (bottomRadius > 0)
            return;

        splitFace(mesh.getFaceCount() - 1, getSegmentHeight());
    }

    private void splitFace(int faceIndex, float offsetY) {
        int index = mesh.vertices.size();
        Face3D face = mesh.getFaceAt(faceIndex);
        new PlanarVertexCenterModifier().modify(mesh, face);
        mesh.getVertexAt(index).addLocal(0, offsetY, 0);
    }

    @Override
    public Mesh3D create() {
        if (shouldNotCreate())
            return new Mesh3D();

        initializeMesh();
        createVertices();
        createQuadFaces();
        createTopCap();
        createBottomCap();
        splitTop();
        splitBottom();

        return mesh;
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    private boolean shouldNotCreate() {
        return radiiAreZero() || height == 0 || rotationSegments == 0
                || heightSegments == 0;
    }

    private boolean radiiAreZero() {
        return topRadius == 0 && bottomRadius == 0;
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

    public float getTopRadius() {
        return topRadius;
    }

    public void setTopRadius(float topRadius) {
        this.topRadius = topRadius;
    }

    public float getBottomRadius() {
        return bottomRadius;
    }

    public void setBottomRadius(float bottomRadius) {
        this.bottomRadius = bottomRadius;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

}
