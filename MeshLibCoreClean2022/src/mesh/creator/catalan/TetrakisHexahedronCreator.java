package mesh.creator.catalan;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TetrakisHexahedronCreator implements IMeshCreator {

	float a = 3f / 2f;
	private Mesh3D mesh;

	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		return mesh;
	}

	private void createVertices() {
		createInnerVertices();
		createOuterVertices();
	}

	private void createInnerVertices() {
		addVertex(0.0f, 0.0f, a);
		addVertex(0.0f, 0.0f, -a);
		addVertex(0.0f, a, 0.0f);
		addVertex(0.0f, -a, 0.0f);
		addVertex(a, 0.0f, 0.0f);
		addVertex(-a, 0.0f, 0.0f);
	}

	private void createOuterVertices() {
		addVertex(-1.0f, 1.0f, 1.0f);
		addVertex(1.0f, 1.0f, 1.0f);
		addVertex(1.0f, 1.0f, -1.0f);
		addVertex(-1.0f, 1.0f, -1.0f);
		addVertex(1.0f, -1.0f, 1.0f);
		addVertex(-1.0f, -1.0f, 1.0f);
		addVertex(-1.0f, -1.0f, -1.0f);
		addVertex(1.0f, -1.0f, -1.0f);
	}

	public void createFaces() {
		addFace(6, 0, 7);
		addFace(8, 1, 9);
		addFace(10, 0, 11);
		addFace(12, 1, 13);
		addFace(2, 6, 7);
		addFace(3, 10, 11);
		addFace(2, 8, 9);
		addFace(3, 12, 13);
		addFace(7, 0, 10);
		addFace(1, 8, 13);
		addFace(0, 6, 11);
		addFace(9, 1, 12);
		addFace(2, 7, 8);
		addFace(10, 3, 13);
		addFace(6, 2, 9);
		addFace(3, 11, 12);
		addFace(4, 7, 10);
		addFace(6, 5, 11);
		addFace(8, 4, 13);
		addFace(5, 9, 12);
		addFace(7, 4, 8);
		addFace(5, 6, 9);
		addFace(4, 10, 13);
		addFace(11, 5, 12);
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void addVertex(float x, float y, float z) {
		mesh.addVertex(x, y, z);
	}

	private void addFace(int... indices) {
		mesh.add(new Face3D(indices));
	}

}
