package mesh.creator.platonic;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class OctahedronCreator implements IMeshCreator {

    private float radius;
    private Mesh3D mesh;

    public OctahedronCreator() {
	this(1f);
    }

    public OctahedronCreator(float radius) {
	this.radius = radius;
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
	addVertex(0, 0, radius);
	addVertex(0, 0, -radius);
	addVertex(radius, 0, 0);
	addVertex(-radius, 0, 0);
	addVertex(0, radius, 0);
	addVertex(0, -radius, 0);
    }

    private void createFaces() {
	addFace(2, 5, 1);
	addFace(1, 5, 3);
	addFace(3, 5, 0);
	addFace(0, 5, 2);
	addFace(2, 1, 4);
	addFace(1, 3, 4);
	addFace(3, 0, 4);
	addFace(0, 2, 4);
    }

    private void addVertex(float x, float y, float z) {
	mesh.addVertex(x, y, z);
    }

    private void addFace(int... indices) {
	mesh.addFace(indices);
    }

    public float getRadius() {
	return radius;
    }

    public void setRadius(float radius) {
	this.radius = radius;
    }

}
