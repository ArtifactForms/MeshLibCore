package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class WedgeCreator implements IMeshCreator {

	private float radius;
	private Mesh3D mesh;

	public WedgeCreator() {
		setRadius(1);
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		addVertices();
		addFaces();
		return mesh;
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void addVertices() {
		addBottomVertices();
		addTopVertices();
	}

	private void addBottomVertices() {
		addVertex(-radius, +radius, +radius);
		addVertex(+radius, +radius, +radius);
		addVertex(+radius, +radius, -radius);
		addVertex(-radius, +radius, -radius);
	}

	private void addTopVertices() {
		addVertex(+radius, -radius, -radius);
		addVertex(-radius, -radius, -radius);
	}

	private void addFaces() {
		addBottomFace();
		addLeftFace();
		addRightFace();
		addTopFace();
		addBackFace();
	}

	private void addBottomFace() {
		addFace(0, 1, 2, 3);
	}

	private void addLeftFace() {
		addFace(0, 3, 5);
	}

	private void addRightFace() {
		addFace(4, 2, 1);
	}

	private void addTopFace() {
		addFace(1, 0, 5, 4);
	}

	private void addBackFace() {
		addFace(4, 5, 3, 2);
	}

	private void addFace(int... indices) {
		mesh.addFace(indices);
	}

	private void addVertex(float x, float y, float z) {
		mesh.addVertex(x, y, z);
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

}
