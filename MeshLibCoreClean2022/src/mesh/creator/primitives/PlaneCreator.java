package mesh.creator.primitives;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class PlaneCreator implements IMeshCreator {

	private float radius;
	
	private Mesh3D mesh;
	
	public PlaneCreator() {
		this(1);
	}
	
	public PlaneCreator(float size) {
		this.radius = size;
	}
	
	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFace();
		return mesh;
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}
	
	private void createVertices() {
		addVertex(+radius, 0, -radius);
		addVertex(+radius, 0, +radius);
		addVertex(-radius, 0, +radius);
		addVertex(-radius, 0, -radius);
	}
	
	private void addVertex(float x, float y, float z) {
		mesh.addVertex(x, y, z);
	}
	
	private void createFace() {
		mesh.add(new Face3D(0, 1, 2, 3));
	}
	
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public float getSize() {
		return radius * 2;
	}
	
	public void setSize(float size) {
		setRadius(size * 0.5f);
	}

}
