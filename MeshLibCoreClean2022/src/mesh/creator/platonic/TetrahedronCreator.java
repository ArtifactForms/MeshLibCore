package mesh.creator.platonic;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TetrahedronCreator implements IMeshCreator {

	private float radius;
	private Mesh3D mesh;
	
	public TetrahedronCreator() {
		radius = 1;
	}
		
	private void createVertices() {
		addVertex(-radius, radius, radius);
		addVertex(radius, radius, -radius);
		addVertex(radius, -radius, radius);
		addVertex(-radius, -radius, -radius);
	}
	
	private void createFaces() {
		addFace(0, 3, 2);
		addFace(2, 3, 1);
		addFace(0, 2, 1);
		addFace(1, 3, 0);
	}
	
	private void addVertex(float x, float y, float z) {
		mesh.addVertex(x, y, z);
	}
	
	private void addFace(int... indices) {
		mesh.addFace(indices);
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

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
}
