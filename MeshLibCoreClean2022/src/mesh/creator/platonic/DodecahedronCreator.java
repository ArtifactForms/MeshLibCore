package mesh.creator.platonic;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class DodecahedronCreator implements IMeshCreator {

	private static final float GOLDEN_RATIO = 1.618034f;
	private static final float TWO_FIFTHS_PI = (float) Mathf.PI * 0.4f;
	
	private float radius;
	private Mesh3D mesh;

	public DodecahedronCreator() {
		this.radius = Mathf.sqrt(3f) / 4f * (1f + Mathf.sqrt(5f));
	}

	public DodecahedronCreator(float radius) {
		this.radius = radius;
	}
	
	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		createVertices();
		createFaces();
		return mesh;
	}

	private void createVertices() {
		float scalar = 3.2945564f;
		for (int i = 0; i < 5; i++) {
			Vector3f v0 = new Vector3f(2 * Mathf.cos(TWO_FIFTHS_PI * i),
					2 * Mathf.sin(TWO_FIFTHS_PI * i), GOLDEN_RATIO + 1);
			Vector3f v1 = new Vector3f(2 * GOLDEN_RATIO
					* Mathf.cos(TWO_FIFTHS_PI * i), 2 * GOLDEN_RATIO
					* Mathf.sin(TWO_FIFTHS_PI * i), GOLDEN_RATIO - 1);
			mesh.add(v0.divide(scalar).mult(radius));
			mesh.add(v0.negate().divide(scalar).mult(radius));
			mesh.add(v1.divide(scalar).mult(radius));
			mesh.add(v1.negate().divide(scalar).mult(radius));
		}
	}

	private void createFaces() {
		mesh.addFace(4, 8, 12, 16, 0);
		mesh.addFace(6, 19, 10, 8, 4);
		mesh.addFace(10, 3, 14, 12, 8);
		mesh.addFace(14, 7, 18, 16, 12);
		mesh.addFace(18, 11, 2, 0, 16);
		mesh.addFace(2, 15, 6, 4, 0);
		mesh.addFace(17, 1, 3, 10, 19);
		mesh.addFace(1, 5, 7, 14, 3);
		mesh.addFace(5, 9, 11, 18, 7);
		mesh.addFace(9, 13, 15, 2, 11);
		mesh.addFace(13, 17, 19, 6, 15);
		mesh.addFace(13, 9, 5, 1, 17);
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

}
