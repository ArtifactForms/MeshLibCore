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
		this(0, Mathf.TWO_PI, 1, 32);
	}

	public ArcCreator(float startAngle, float endAngle, float radius, int vertices) {
		this.startAngle = startAngle;
		this.endAngle = endAngle;
		this.radius = radius;
		this.vertices = vertices;
	}

	private void createVertices() {
		float a = (endAngle - startAngle) / ((float) vertices - 1);

		for (int i = 0; i < vertices; i++) {
			float x = radius * Mathf.cos(startAngle + (a * i));
			float y = 0;
			float z = radius * Mathf.sin(startAngle + (a * i));
			mesh.addVertex(x, y, z);
		}
	}

	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		createVertices();
		return mesh;
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
