package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class DoubleConeCreator implements IMeshCreator {

    private int vertices;

    private float radius;

    private float height;

    private Mesh3D mesh;

    public DoubleConeCreator() {
        vertices = 32;
        radius = 1f;
        height = 2f;
    }

    @Override
    public Mesh3D create() {
        createVertices();
        createFaces();
        return mesh;
    }

    private void createVertices() {
        createVerticesAroundOrigin();
        createTopCenterVertex();
        createBottomCenterVertex();
    }

    private void createFaces() {
        for (int i = 0; i < vertices; i++) {
            createTopFaceAt(i);
            createBottomFaceAt(i);
        }
    }

    private void createVerticesAroundOrigin() {
        mesh = new CircleCreator(vertices, radius).create();
    }

    private void createBottomCenterVertex() {
        addVertex(0, height / 2f, 0);
    }

    private void createTopCenterVertex() {
        addVertex(0, -height / 2f, 0);
    }

    private void createTopFaceAt(int i) {
        addFace(vertices, i, (i + 1) % vertices);
    }

    private void createBottomFaceAt(int i) {
        addFace(vertices + 1, (i + 1) % vertices, i);
    }

    private void addVertex(float x, float y, float z) {
        mesh.addVertex(x, y, z);
    }

    private void addFace(int... indices) {
        mesh.addFace(indices);
    }

    public int getVertices() {
        return vertices;
    }

    public void setVertices(int vertices) {
        this.vertices = vertices;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

}
