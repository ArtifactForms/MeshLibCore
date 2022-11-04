package mesh.creator.catalan;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class RhombicDodecahedronCreator implements IMeshCreator {

	private Mesh3D mesh;
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}
	
	private void createVertices() {
		mesh.addVertex(-1, 1, 1);
		mesh.addVertex(-1, 1, -1);
		mesh.addVertex(1, 1, -1);
		mesh.addVertex(1, 1, 1);
		mesh.addVertex(-1, -1, 1);
		mesh.addVertex(-1, -1, -1);
		mesh.addVertex(1, -1, -1);
		mesh.addVertex(1, -1, 1);
		mesh.addVertex(0, 2, 0);
		mesh.addVertex(0, -2, 0);
		mesh.addVertex(2, 0, 0);
		mesh.addVertex(-2, 0, 0); 
		mesh.addVertex(0, 0, 2);
		mesh.addVertex(0, 0, -2);
	}
	
	private void createFaces() {
		mesh.addFace(8, 3, 10, 2);
		mesh.addFace(8, 0, 12, 3);
		mesh.addFace(8, 1, 11, 0);
		mesh.addFace(8, 2, 13, 1);
		mesh.addFace(9, 7, 12, 4);
		mesh.addFace(9, 6, 10, 7);
		mesh.addFace(9, 5, 13, 6);
		mesh.addFace(9, 4, 11, 5);
		mesh.addFace(4, 12, 0, 11);
		mesh.addFace(7, 10, 3, 12);
		mesh.addFace(6, 13, 2, 10);
		mesh.addFace(5, 11, 1, 13);
	}
	
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		return mesh;
	}
	
}
