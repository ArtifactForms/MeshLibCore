package mesh.creator.primitives;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class SquareBasedPyramidCreator implements IMeshCreator {

	private float size;
	private float height;
	private Mesh3D mesh;
	
	public SquareBasedPyramidCreator() {
		size = 1f;
		height = 2f;
	}
	
	public SquareBasedPyramidCreator(float size, float height) {
		this.size = size;
		this.height = height;
	}
	
	private void createVertices() {		
		addVertex(-1, 0, -1);
		addVertex(-1, 0, 1);
		addVertex(1, 0, 1);
		addVertex(1, 0, -1);
		addVertex(0, -1, 0);
	}
	
	private void createFaces() {
		addFace(0, 1, 2, 3);
		addFace(1, 0, 4);
		addFace(2, 1, 4);
		addFace(3, 2, 4);
		addFace(0, 3, 4);
	}
	
	private void transform() {
		mesh.scale(size, height, size);
		mesh.translate(0, height / 2f, 0);
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		transform();
		return mesh;
	}
	
	private void addFace(int... indices) {
		mesh.add(new Face3D(indices));
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
