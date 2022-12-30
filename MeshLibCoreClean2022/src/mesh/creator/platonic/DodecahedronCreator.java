package mesh.creator.platonic;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class DodecahedronCreator implements IMeshCreator {

	private static final float SCALAR = 3.2945564f;
	private static final float GOLDEN_RATIO = 1.618034f;
	private static final float TWO_FIFTHS_PI = (float) Mathf.PI * 0.4f;

	private float radius;
	private Mesh3D mesh;

	public DodecahedronCreator() {
		this.radius = Mathf.sqrt(3f) / 4f * (1f + Mathf.sqrt(5f));
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
		for (int i = 0; i < 5; i++)
			createVerticesAt(i);
	}

	private void createVerticesAt(int i) {
		float a = TWO_FIFTHS_PI * i;
		float sinA2 = Mathf.sin(a) * 2;
		float cosA2 = Mathf.cos(a) * 2;
		Vector3f v0 = new Vector3f(cosA2, sinA2, GOLDEN_RATIO + 1);
		Vector3f v1 = new Vector3f(cosA2 * GOLDEN_RATIO, sinA2 * GOLDEN_RATIO, GOLDEN_RATIO - 1);
		addVertices(v0, v1);
	}

	private void addVertices(Vector3f v0, Vector3f v1) {
		addVertex(v0.divide(SCALAR).mult(radius));
		addVertex(v0.negate().divide(SCALAR).mult(radius));
		addVertex(v1.divide(SCALAR).mult(radius));
		addVertex(v1.negate().divide(SCALAR).mult(radius));
	}

	private void addVertex(Vector3f v) {
		mesh.add(v);
	}

	private void createFaces() {
		addFace(4, 8, 12, 16, 0);
		addFace(6, 19, 10, 8, 4);
		addFace(10, 3, 14, 12, 8);
		addFace(14, 7, 18, 16, 12);
		addFace(18, 11, 2, 0, 16);
		addFace(2, 15, 6, 4, 0);
		addFace(17, 1, 3, 10, 19);
		addFace(1, 5, 7, 14, 3);
		addFace(5, 9, 11, 18, 7);
		addFace(9, 13, 15, 2, 11);
		addFace(13, 17, 19, 6, 15);
		addFace(13, 9, 5, 1, 17);
	}

	private void addFace(int... indices) {
		mesh.add(new Face3D(indices));
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

}
