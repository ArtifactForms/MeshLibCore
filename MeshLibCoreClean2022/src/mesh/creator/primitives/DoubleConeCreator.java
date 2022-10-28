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
		super();
		this.vertices = 32;
		this.radius = 1f;
		this.height = 2f;
	}

	public DoubleConeCreator(int vertices, float radius, float height) {
		super();
		this.vertices = vertices;
		this.radius = radius;
		this.height = height;
	}
	
	private void initializeMeshAndCreateVertices() {
		mesh = new CircleCreator(vertices, radius).create();
		mesh.add(new Vector3f(0, -height / 2f, 0));
		mesh.add(new Vector3f(0, height / 2f, 0));
	}
	
	private void createTopFaces() {
		for (int i = 0; i < vertices; i++)
			mesh.add(new Face3D(vertices, i, (i + 1) % vertices));
	}
	
	private void createBottomFaces() {
		for (int i = 0; i < vertices; i++)
			mesh.add(new Face3D(vertices + 1, (i + 1) % vertices, i));
	}
	
	private void createFaces() {
		createTopFaces();
		createBottomFaces();
	}
	
	@Override
	public Mesh3D create() {
		initializeMeshAndCreateVertices();
		createFaces();
		return mesh;
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
