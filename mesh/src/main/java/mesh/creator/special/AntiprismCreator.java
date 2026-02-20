package mesh.creator.special;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class AntiprismCreator implements IMeshCreator {

    private int n;

    private int n2;

    private float height;

    private Mesh3D mesh;

    public AntiprismCreator() {
        this(6);
    }

    public AntiprismCreator(int n) {
        this.n = n;
    }

    @Override
    public Mesh3D create() {
        n2 = n + n;
        initializeMesh();
        initializeHeight();
        createVertices();
        createFaces();
        return mesh;
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    private void createVertices() {
        for (int k = 0; k < 2 * n; k++)
            addVertex(x(k), y(k), z(k));
    }

    private void initializeHeight() {
        float a = Mathf.cos(Mathf.PI / n);
        float b = Mathf.cos(Mathf.TWO_PI / n);
        height = Mathf.sqrt((a - b) / 2.0f);
    }

    private float x(int k) {
        return Mathf.cos(k * Mathf.PI / n);
    }

    private float z(int k) {
        return Mathf.sin(k * Mathf.PI / n);
    }

    private float y(int k) {
        return Mathf.pow(-1, k) * height;
    }

    private void createFaces() {
        createSideFaces();
        createTopFace();
        createBottomFace();
    }

    private void createSideFaces() {
        for (int i = 0; i < n2; i++)
            createSideFaceAt(i);
    }

    private void createSideFaceAt(int i) {
        int a = i % n2;
        int b = (i + 1) % n2;
        int c = (i + 2) % n2;
        if (i % 2 == 1) {
            addFace(a, b, c);
        } else {
            addFace(a, c, b);
        }
    }

    private void createBottomFace() {
        int[] indices = new int[n];
        for (int i = 0; i < n; i++)
            indices[n - 1 - i] = i * 2;
        addFace(indices);
    }

    private void createTopFace() {
        int[] indices = new int[n];
        for (int i = 0; i < n; i++)
            indices[i] = i + i + 1;
        addFace(indices);
    }

    private void addVertex(float x, float y, float z) {
        mesh.addVertex(x, y, z);
    }

    private void addFace(int... indices) {
        mesh.addFace(indices);
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

}
