package mesh.creator.primitives;

import math.Vector3f;
import mesh.Face3D;
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
	mesh.add(new Vector3f(0, height / 2f, 0));
    }

    private void createTopCenterVertex() {
	mesh.add(new Vector3f(0, -height / 2f, 0));
    }

    private void createTopFaceAt(int i) {
	mesh.add(new Face3D(vertices, i, (i + 1) % vertices));
    }

    private void createBottomFaceAt(int i) {
	mesh.add(new Face3D(vertices + 1, (i + 1) % vertices, i));
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
