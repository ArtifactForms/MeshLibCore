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

    @Override
    public Mesh3D create() {
        initializeMesh();
        createTorus();
        return mesh;
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    private void createTorus() {
        for (int i = 0; i < majorSegments; i++) {
            for (int j = 0; j < minorSegments; j++) {
                float u = j * Mathf.TWO_PI / minorSegments;
                float v = i * Mathf.TWO_PI / majorSegments;
                createVertexAt(u, v);
                createFaceAt(i, j);
            }
        }
    }

    private void createVertexAt(float u, float v) {
        float x = x(u, v);
        float y = y(u, v);
        float z = z(u, v);
        addVertex(x, y, z);
    }

    private float x(float u, float v) {
        return (majorRadius + minorRadius * Mathf.cos(u)) * Mathf.cos(v);
    }

    private float z(float u, float v) {
        return (majorRadius + minorRadius * Mathf.cos(u)) * Mathf.sin(v);
    }

    private float y(float u, float v) {
        return minorRadius * Mathf.sin(u);
    }
    
    private void addVertex(float x, float y, float z) {
        mesh.addVertex(x, y, z);
    }

    private void createFaceAt(int i, int j) {
        int index0 = getIndex(j, i + 1);
        int index1 = getIndex(j, i);
        int index2 = getIndex(j + 1, i);
        int index3 = getIndex(j + 1, i + 1);
        addFace(index0, index1, index2, index3);
    }

    private int getIndex(int i, int j) {
        int rowIndex = j % majorSegments;
        int colIndex = i % minorSegments;
        int numberOfColumns = minorSegments;
        return Mathf.toOneDimensionalIndex(rowIndex, colIndex, numberOfColumns);
    }

    private void addFace(int... indices) {
        mesh.add(new Face3D(indices));
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
