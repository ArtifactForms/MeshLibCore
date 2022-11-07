package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class SquareBasedPyramidCreator implements IMeshCreator {

	private float size;
	private float height;
	private Mesh3D mesh;
	
	public SquareBasedPyramidCreator() {
		this.size = 1f;
		this.height = 2f;
	}
	
	public SquareBasedPyramidCreator(float size, float height) {
		this.size = size;
		this.height = height;
	}
	
	private void createVertices() {		
		mesh.addVertex(-1, 0, -1);
		mesh.addVertex(-1, 0, 1);
		mesh.addVertex(1, 0, 1);
		mesh.addVertex(1, 0, -1);
		mesh.addVertex(0, -1, 0);
	}
	
	private void createFaces() {
		mesh.addFace(0, 1, 2, 3);
		mesh.addFace(1, 0, 4);
		mesh.addFace(2, 1, 4);
		mesh.addFace(3, 2, 4);
		mesh.addFace(0, 3, 4);
	}
	
	private void transform() {
		mesh.scale(size, height, size);
		mesh.translate(0, height / 2f, 0);
	}

	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		createVertices();
		createFaces();
		transform();
		return mesh;
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
