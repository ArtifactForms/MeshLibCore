package mesh.creator.special;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class MobiusStripCreator implements IMeshCreator {

    private int rings;

    private int segments;

    private float radius;

    private Mesh3D mesh;

    public MobiusStripCreator() {
        rings = 5;
        segments = 32;
        radius = 1;
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

    private void createVertices() {
        for (int i = 0; i <= rings; i++) {
            for (int j = 0; j < segments; j++) {
                createVertexAt(i, j);
            }
        }
    }

    private void createVertexAt(int ring, int segment) {
        float r = mapToValueBetweenNegativeOneAndOne(ring);
        float a = mapToValueBetweenZeroAndTwoPi(segment);
        float x = x(r, a);
        float z = z(r, a);
        float y = y(r, a);
        addVertex(x, y, z);
    }

    private float mapToValueBetweenZeroAndTwoPi(int segment) {
        return Mathf.map(segment, 0, segments, 0, Mathf.TWO_PI);
    }

    private float mapToValueBetweenNegativeOneAndOne(int ring) {
        return Mathf.map(ring, 0, rings, -1, 1);
    }

    private float x(float r, float a) {
        return Mathf.cos(a) * calc(r, a);
    }

    private float z(float r, float a) {
        return Mathf.sin(a) * calc(r, a);
    }

    private float y(float r, float a) {
        return (r * 0.5f) * Mathf.sin(a * 0.5f);
    }

    private float calc(float r, float a) {
        return (radius + (r * 0.5f) * Mathf.cos(a * 0.5f));
    }

    private void createFaces() {
        for (int row = 0; row < rings; row++) {
            for (int col = 0; col < segments - 1; col++) {
                createFaceAt(row, col);
            }
            createLastFaceAt(row);
        }
    }

    private void createLastFaceAt(int row) {
        int a = getIndex(row, segments - 1);
        int b = getIndex(row + 1, segments - 1);
        int c = getIndex(rings - row - 1, 0);
        int d = getIndex(rings - row, 0);
        addFace(a, b, c, d);
    }

    private void createFaceAt(int row, int col) {
        int a = getIndex(row, col + 1);
        int b = getIndex(row + 1, col + 1);
        int c = getIndex(row + 1, col);
        int d = getIndex(row, col);
        addFace(a, b, c, d);
    }

    private int getIndex(int row, int col) {
        return Mathf.toOneDimensionalIndex(row, col % segments, segments);
    }

    private void addFace(int... indices) {
        mesh.add(new Face3D(indices));
    }

    private void addVertex(float x, float y, float z) {
        mesh.addVertex(x, y, z);
    }

    public int getRings() {
        return rings;
    }

    public void setRings(int rings) {
        this.rings = rings;
    }

    public int getSegments() {
        return segments;
    }

    public void setSegments(int segments) {
        this.segments = segments;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

}
