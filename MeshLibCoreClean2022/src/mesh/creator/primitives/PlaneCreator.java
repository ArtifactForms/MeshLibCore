package mesh.creator.primitives;

import math.Vector3f;
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
		createFaces();
		return mesh;
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}
	
	private void createVertices() {
		mesh.add(new Vector3f(radius, 0, -radius));
		mesh.add(new Vector3f(radius, 0, radius));
		mesh.add(new Vector3f(-radius, 0, radius));
		mesh.add(new Vector3f(-radius, 0, -radius));
	}
	
	private void createFaces() {
		mesh.add(new Face3D(0, 1, 2, 3));
	}
	
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public void setSize(float size) {
		setRadius(size * 0.5f);
	}

}
