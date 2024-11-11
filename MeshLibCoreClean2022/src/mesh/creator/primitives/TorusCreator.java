package mesh.creator.primitives;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TorusCreator implements IMeshCreator {

    private float majorRadius;

    private float minorRadius;

    private int majorSegments;

    private int minorSegments;

    private Mesh3D mesh;

    public TorusCreator() {
        majorRadius = 1f;
        minorRadius = 0.25f;
        majorSegments = 48;
        minorSegments = 12;
    }

    private void createVertices() {
        float stepU = Mathf.TWO_PI / minorSegments;
        float stepV = Mathf.TWO_PI / majorSegments;
        for (int i = 0; i < majorSegments; i++) {
            float v = i * stepV;
            for (int j = 0; j < minorSegments; j++) {
                float u = j * stepU;
                float x = (majorRadius + minorRadius * Mathf.cos(u)) * Mathf.cos(v);
                float y = minorRadius * Mathf.sin(u);
                float z = (majorRadius + minorRadius * Mathf.cos(u)) * Mathf.sin(v);
                addVertex(x, y, z);
            }
        }
    }

    private void createFaces() {
        for (int i = 0; i < minorSegments; i++)
            for (int j = 0; j < majorSegments; j++)
                createFaceAt(i, j);
    }

    private void createFaceAt(int i, int j) {
        int index0 = toOneDimensionalIndex(i, j + 1);
        int index1 = toOneDimensionalIndex(i, j);
        int index2 = toOneDimensionalIndex(i + 1, j);
        int index3 = toOneDimensionalIndex(i + 1, j + 1);
        addFace(index0, index1, index2, index3);
    }

    private int toOneDimensionalIndex(int i, int j) {
        return (j % majorSegments) * minorSegments + (i % minorSegments);
    }

    private void addVertex(float x, float y, float z) {
        mesh.add(new Vector3f(x, y, z));
    }

    private void addFace(int... indices) {
        mesh.add(new Face3D(indices));
    }

    @Override
    public Mesh3D create() {
        initializeMesh();
        createVertices();
        createFaces();
        return mesh;
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    public float getMajorRadius() {
        return majorRadius;
    }

    public void setMajorRadius(float majorRadius) {
        this.majorRadius = majorRadius;
    }

    public float getMinorRadius() {
        return minorRadius;
    }

    public void setMinorRadius(float minorRadius) {
        this.minorRadius = minorRadius;
    }

    public int getMajorSegments() {
        return majorSegments;
    }

    public void setMajorSegments(int majorSegments) {
        this.majorSegments = majorSegments;
    }

    public int getMinorSegments() {
        return minorSegments;
    }

    public void setMinorSegments(int minorSegments) {
        this.minorSegments = minorSegments;
    }

}
