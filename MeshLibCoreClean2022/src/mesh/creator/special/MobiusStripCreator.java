package mesh.creator.special;

import math.Mathf;
import math.Vector3f;
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

    private void createVertices() {
        for (int i = 0; i <= rings; i++) {
            for (int j = 0; j < segments; j++) {
                float s = Mathf.map(i, 0, rings, -1, 1);
                float t = Mathf.map(j, 0, segments, 0, Mathf.TWO_PI);
                float x = (radius + (s * 0.5f) * Mathf.cos(t * 0.5f)) * Mathf.cos(t);
                float z = (radius + (s * 0.5f) * Mathf.cos(t * 0.5f)) * Mathf.sin(t);
                float y = (s * 0.5f) * Mathf.sin(t * 0.5f);
                mesh.add(new Vector3f(x, y, z));
            }
        }
    }

    private int getIndex(int row, int col) {
        int idx = segments * row + col;
        return idx % mesh.vertices.size();
    }

    private void createFaces() {
        for (int row = 0; row < rings; row++) {
            for (int col = 0; col < segments - 1; col++) {
                int a = getIndex(row, (col + 1) % segments);
                int b = getIndex(row + 1, (col + 1) % segments);
                int c = getIndex(row + 1, col);
                int d = getIndex(row, col);
                mesh.add(new Face3D(a, b, c, d));
            }
        }
        for (int i = 0; i < rings; i++) {
            int a = getIndex(i, segments - 1);
            int b = getIndex(i + 1, segments - 1);
            int c = getIndex(rings - i - 1, 0);
            int d = getIndex(rings - i, 0);
            mesh.add(new Face3D(a, b, c, d));
        }
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    @Override
    public Mesh3D create() {
        initializeMesh();
        createVertices();
        createFaces();
        return mesh;
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
