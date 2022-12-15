package mesh.creator.catalan;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TriakisTetrahedronCreator implements IMeshCreator {

    private float a;

    private float b;

    private Mesh3D mesh;

    public TriakisTetrahedronCreator() {
	a = 3f / 5f;
	b = 1 + a;
    }

    @Override
    public Mesh3D create() {
	initializeMesh();
	createVertices();
	createFaces();
	return mesh;
    }

    private void createVertices() {
	addVertex(b, b, b);
	addVertex(b, -b, -b);
	addVertex(-b, -b, b);
	addVertex(-b, b, -b);
	addVertex(1, -1, 1);
	addVertex(-1, 1, 1);
	addVertex(1, 1, -1);
	addVertex(-1, -1, -1);
    }

    private void createFaces() {
	addFace(4, 0, 5);
	addFace(6, 1, 7);
	addFace(2, 4, 5);
	addFace(3, 6, 7);
	addFace(5, 0, 6);
	addFace(1, 4, 7);
	addFace(4, 2, 7);
	addFace(3, 5, 6);
	addFace(0, 4, 6);
	addFace(4, 1, 6);
	addFace(2, 5, 7);
	addFace(5, 3, 7);
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
