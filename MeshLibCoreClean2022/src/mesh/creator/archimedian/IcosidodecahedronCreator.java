package mesh.creator.archimedian;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class IcosidodecahedronCreator implements IMeshCreator {

	private float a = 0.0f;
	private float b = (1.0f + Mathf.sqrt(5.0f)) / 2.0f;
	private float c = 1.0f / 2.0f;
	private float d = b / 2.0f;
	private float e = (1.0f + b) / 2.0f;
	private Mesh3D mesh;

	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		return mesh;
	}

	private void createFaces() {
		createTriangularFaces();
		createPentagonalFaces();
	}

	private void createTriangularFaces() {
		addFace(0, 6, 9);
		addFace(0, 12, 8);
		addFace(1, 10, 13);
		addFace(1, 11, 7);
		addFace(2, 14, 17);
		addFace(2, 20, 16);
		addFace(3, 18, 21);
		addFace(3, 19, 15);
		addFace(4, 22, 25);
		addFace(4, 28, 24);
		addFace(5, 26, 29);
		addFace(5, 27, 23);
		addFace(6, 22, 14);
		addFace(7, 17, 24);
		addFace(8, 15, 25);
		addFace(9, 16, 23);
		addFace(10, 28, 19);
		addFace(11, 26, 20);
		addFace(12, 27, 18);
		addFace(13, 21, 29);
	}

	private void createPentagonalFaces() {
		addFace(0, 8, 25, 22, 6);
		addFace(0, 9, 23, 27, 12);
		addFace(1, 7, 24, 28, 10);
		addFace(1, 13, 29, 26, 11);
		addFace(2, 16, 9, 6, 14);
		addFace(2, 17, 7, 11, 20);
		addFace(3, 15, 8, 12, 18);
		addFace(3, 21, 13, 10, 19);
		addFace(4, 24, 17, 14, 22);
		addFace(4, 25, 15, 19, 28);
		addFace(5, 23, 16, 20, 26);
		addFace(5, 29, 21, 18, 27);
	}

	private void createVertices() {
		addVertex(+a, +a, +b);
		addVertex(+a, +a, -b);
		addVertex(+a, +b, +a);
		addVertex(+a, -b, +a);
		addVertex(+b, +a, +a);
		addVertex(-b, +a, +a);

		addVertex(+c, +d, +e);
		addVertex(+c, +d, -e);
		addVertex(+c, -d, +e);
		addVertex(-c, +d, +e);
		addVertex(+c, -d, -e);
		addVertex(-c, +d, -e);
		addVertex(-c, -d, +e);
		addVertex(-c, -d, -e);

		addVertex(+d, +e, +c);
		addVertex(+d, -e, +c);
		addVertex(-d, +e, +c);
		addVertex(+d, +e, -c);
		addVertex(-d, -e, +c);
		addVertex(+d, -e, -c);
		addVertex(-d, +e, -c);
		addVertex(-d, -e, -c);

		addVertex(+e, +c, +d);
		addVertex(-e, +c, +d);
		addVertex(+e, +c, -d);
		addVertex(+e, -c, +d);
		addVertex(-e, +c, -d);
		addVertex(-e, -c, +d);
		addVertex(+e, -c, -d);
		addVertex(-e, -c, -d);
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void addVertex(float x, float y, float z) {
		mesh.addVertex(x, y, z);
	}

	private void addFace(int... indices) {
		mesh.addFace(indices);
	}

}
