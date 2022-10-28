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
	
	private void createVertices() {
		mesh.addVertex(0, 0, radius);
		mesh.addVertex(0, 0, -radius);
		mesh.addVertex(radius, 0, 0);
		mesh.addVertex(-radius, 0, 0);
		mesh.addVertex(0, radius, 0);
		mesh.addVertex(0, -radius, 0);
	}
	
	private void createFaces() {
		mesh.addFace(2, 5, 1);	
		mesh.addFace(1, 5, 3);
		mesh.addFace(3, 5, 0);
		mesh.addFace(0, 5, 2);
		mesh.addFace(2, 1, 4);
		mesh.addFace(1, 3, 4);
		mesh.addFace(3, 0, 4);
		mesh.addFace(0, 2, 4);
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
