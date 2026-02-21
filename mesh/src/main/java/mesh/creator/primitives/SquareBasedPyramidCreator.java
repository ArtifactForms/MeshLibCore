package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.transform.ScaleModifier;
import mesh.modifier.transform.TranslateModifier;

public class SquareBasedPyramidCreator implements IMeshCreator {

	private float size;

	private float height;

	private Mesh3D mesh;

	public SquareBasedPyramidCreator() {
		size = 1f;
		height = 2f;
	}

	private void createVertices() {
		addVertex(-1, 0, -1);
		addVertex(-1, 0, 1);
		addVertex(1, 0, 1);
		addVertex(1, 0, -1);
		addVertex(0, -1, 0);
	}

	private void createFaces() {
		createQuadFace();
		createTriangularFaces();
	}

	private void createQuadFace() {
		addFace(0, 1, 2, 3);
	}

	private void createTriangularFaces() {
		addFace(1, 0, 4);
		addFace(2, 1, 4);
		addFace(3, 2, 4);
		addFace(0, 3, 4);
	}

	private void scale() {
		new ScaleModifier(size, height, size).modify(mesh);
	}

	private void centerAlongAxisY() {
		new TranslateModifier(0, height / 2f, 0).modify(mesh);
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		scale();
		centerAlongAxisY();
		return mesh;
	}

	private void addFace(int... indices) {
		mesh.addFace(indices);
	}

	private void addVertex(float x, float y, float z) {
		mesh.addVertex(x, y, z);
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

}
