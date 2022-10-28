package mesh.creator.platonic;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TetrahedronCreator implements IMeshCreator {

	private float radius;
	private Mesh3D mesh;
	
	public TetrahedronCreator() {
		this(1);
	}
	
	public TetrahedronCreator(float radius) {
		super();
		this.radius = radius;
	}
	
	private void createVertices() {
		mesh.addVertex(-radius, radius, radius);
		mesh.addVertex(radius, radius, -radius);
		mesh.addVertex(radius, -radius, radius);
		mesh.addVertex(-radius, -radius, -radius);
	}
	
	private void createFaces() {
		mesh.addFace(0, 3, 2);
		mesh.addFace(2, 3, 1);
		mesh.addFace(0, 2, 1);
		mesh.addFace(1, 3, 0);
	}
	
	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		createVertices();
		createFaces();
		return mesh;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
}
