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
		mesh.addVertex(-radius, radius, radius);
		mesh.addVertex(radius, radius, radius);
		mesh.addVertex(radius, radius, -radius);
		mesh.addVertex(-radius, radius, -radius);
	}
	
	private void addTopVertices() {
		mesh.addVertex(radius, -radius, -radius);
		mesh.addVertex(-radius, -radius, -radius);
	}
	
	private void addFaces() {
		addBottomFace();
		addLeftFace();
		addRightFace();
		addTopFace();
		addBackFace();
	}
	
	private void addBottomFace() {
		mesh.addFace(0, 1, 2, 3);
	}
	
	private void addLeftFace() {
		mesh.addFace(0, 3, 5);
	}
	
	private void addRightFace() {
		mesh.addFace(4, 2, 1);
	}
	
	public void addTopFace() {
		mesh.addFace(1, 0, 5, 4);
	}
	
	public void addBackFace() {
		mesh.addFace(4, 5, 3, 2);
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
}
