package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;

public class TriangleFanCreator implements IMeshCreator {

	private float radius;
	private float centerY;
	private int vertices;

	public TriangleFanCreator() {
		radius = 1;
		centerY = 0;
		vertices = 32;
	}

	@Override
	public Mesh3D create() {
		return new CircleCreator(vertices, radius, centerY, FillType.TRIANGLE_FAN).create();
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getCenterY() {
		return centerY;
	}

	public void setCenterY(float centerY) {
		this.centerY = centerY;
	}

	public int getVertices() {
		return vertices;
	}

	public void setVertices(int vertices) {
		this.vertices = vertices;
	}

}
