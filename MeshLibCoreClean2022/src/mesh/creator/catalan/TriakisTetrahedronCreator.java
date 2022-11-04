package mesh.creator.catalan;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TriakisTetrahedronCreator implements IMeshCreator {

	private float a = 3f / 5f;
	private float b = 1 + a;
	private Mesh3D mesh;
	
	private void createVertices() {
		mesh.addVertex(b, b, b);
		mesh.addVertex(b, -b, -b);
		mesh.addVertex(-b, -b, b);
		mesh.addVertex(-b, b, -b);
		mesh.addVertex(1, -1, 1);
		mesh.addVertex(-1, 1, 1);
		mesh.addVertex(1, 1, -1);
		mesh.addVertex(-1, -1, -1);
	}
	
	private void createFaces() {
		mesh.addFace(4,0,5);
		mesh.addFace(6,1,7);
		mesh.addFace(2,4,5);
		mesh.addFace(3,6,7);
		mesh.addFace(5,0,6);
		mesh.addFace(1,4,7);
		mesh.addFace(4,2,7);
		mesh.addFace(3,5,6);
		mesh.addFace(0,4,6);
		mesh.addFace(4,1,6);
		mesh.addFace(2,5,7);
		mesh.addFace(5,3,7);
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}
	
	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		return mesh;
	}

}
