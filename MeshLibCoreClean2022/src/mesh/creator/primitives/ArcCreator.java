package mesh.creator.primitives;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class ArcCreator implements IMeshCreator {

	private float startAngle;
	private float endAngle;
	private float radius;
	private int vertices;
	private Mesh3D mesh;

	public ArcCreator() {
		startAngle = 0;
		endAngle = Mathf.TWO_PI;
		radius = 1;
		vertices = 32;
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		return mesh;
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void createVertices() {
		float angleBetweenPoints = calculateAngleBetweenPoints();
		for (int i = 0; i < vertices; i++) {
			float currentAngle = angleBetweenPoints * i;
			float x = radius * Mathf.cos(currentAngle);
			float z = radius * Mathf.sin(currentAngle);
			addVertex(x, 0, z);
		}
	}

	private void addVertex(float x, float y, float z) {
		mesh.addVertex(x, y, z);
	}

	private float calculateAngleBetweenPoints() {
		return startAngle + ((endAngle - startAngle) / ((float) vertices - 1));
	}

	public float getStartAngle() {
		return startAngle;
	}

	public void setStartAngle(float startAngle) {
		this.startAngle = startAngle;
	}

	public float getEndAngle() {
		return endAngle;
	}

	public void setEndAngle(float endAngle) {
		this.endAngle = endAngle;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public int getVertices() {
		return vertices;
	}

	public void setVertices(int vertices) {
		this.vertices = vertices;
	}

}
