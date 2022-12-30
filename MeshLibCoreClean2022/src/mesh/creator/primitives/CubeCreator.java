package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class CubeCreator implements IMeshCreator {

	private float radius;
	private Mesh3D mesh;

	public CubeCreator() {
		this(1);
	}

	public CubeCreator(float radius) {
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
		addVertex(+radius, -radius, -radius);
		addVertex(+radius, -radius, +radius);
		addVertex(-radius, -radius, +radius);
		addVertex(-radius, -radius, -radius);
		addVertex(+radius, +radius, -radius);
		addVertex(+radius, +radius, +radius);
		addVertex(-radius, +radius, +radius);
		addVertex(-radius, +radius, -radius);
	}

	private void createFaces() {
		addFace(3, 0, 1, 2);
		addFace(6, 5, 4, 7);
		addFace(1, 0, 4, 5);
		addFace(1, 5, 6, 2);
		addFace(6, 7, 3, 2);
		addFace(3, 7, 4, 0);
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
