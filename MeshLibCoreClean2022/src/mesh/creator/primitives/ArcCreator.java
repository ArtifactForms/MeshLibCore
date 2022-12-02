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
		this.startAngle = 0;
		this.endAngle = Mathf.TWO_PI;
		this.radius = 1;
		this.vertices = 32;
	}

	private void createVertices() {
		float angleStep = calculateAngleStep();

		for (int i = 0; i < vertices; i++) {
			float x = radius * Mathf.cos(startAngle + (angleStep * i));
			float y = 0;
			float z = radius * Mathf.sin(startAngle + (angleStep * i));
			mesh.addVertex(x, y, z);
		}
	}
	
	private float calculateAngleStep() {
		 return (endAngle - startAngle) / ((float) vertices - 1);
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
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
