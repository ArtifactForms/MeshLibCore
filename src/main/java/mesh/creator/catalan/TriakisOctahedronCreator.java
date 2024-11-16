package mesh.creator.catalan;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TriakisOctahedronCreator implements IMeshCreator {

    private float a = Mathf.sqrt(2) - 1;

    private Mesh3D mesh;

    @Override
    public Mesh3D create() {
        initializeMesh();
        createVertices();
        createFaces();
        return mesh;
    }

    private void createVertices() {
        createCornerVertices();
        createMidEdgeVertices();
    }

    private void createCornerVertices() {
        addVertex(a, a, a);
        addVertex(a, a, -a);
        addVertex(a, -a, a);
        addVertex(-a, a, a);
        addVertex(a, -a, -a);
        addVertex(-a, a, -a);
        addVertex(-a, -a, a);
        addVertex(-a, -a, -a);
    }

    private void createMidEdgeVertices() {
        addVertex(0, 1, 0);
        addVertex(1, 0, 0);
        addVertex(0, -1, 0);
        addVertex(-1, 0, 0);
        addVertex(0, 0, 1);
        addVertex(0, 0, -1);
    }

    private void createFaces() {
        addFace(8, 0, 9);
        addFace(1, 8, 9);
        addFace(9, 2, 10);
        addFace(3, 8, 11);
        addFace(4, 9, 10);
        addFace(8, 5, 11);
        addFace(10, 6, 11);
        addFace(7, 10, 11);
        addFace(9, 0, 12);
        addFace(1, 9, 13);
        addFace(2, 9, 12);
        addFace(3, 11, 12);
        addFace(9, 4, 13);
        addFace(11, 5, 13);
        addFace(11, 6, 12);
        addFace(7, 11, 13);
        addFace(0, 8, 12);
        addFace(8, 1, 13);
        addFace(10, 2, 12);
        addFace(8, 3, 12);
        addFace(4, 10, 13);
        addFace(5, 8, 13);
        addFace(6, 10, 12);
        addFace(10, 7, 13);
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
