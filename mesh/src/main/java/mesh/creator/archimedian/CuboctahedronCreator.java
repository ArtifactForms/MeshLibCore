package mesh.creator.archimedian;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class CuboctahedronCreator implements IMeshCreator {

    private float a;

    private Mesh3D mesh;

    public CuboctahedronCreator() {
        a = 1;
    }

    @Override
    public Mesh3D create() {
        initializeMesh();
        createVertices();
        createFaces();
        return mesh;
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    private void createVertices() {
        addVertex(0, -a, +a);
        addVertex(+a, 0, +a);
        addVertex(0, +a, +a);
        addVertex(-a, 0, +a);
        addVertex(+a, -a, 0);
        addVertex(+a, +a, 0);
        addVertex(-a, +a, 0);
        addVertex(-a, -a, 0);
        addVertex(0, -a, -a);
        addVertex(+a, 0, -a);
        addVertex(0, +a, -a);
        addVertex(-a, 0, -a);
    }

    private void createFaces() {
        addFace(0, 1, 2, 3);
        addFace(0, 4, 1);
        addFace(1, 5, 2);
        addFace(2, 6, 3);
        addFace(3, 7, 0);
        addFace(4, 9, 5, 1);
        addFace(2, 5, 10, 6);
        addFace(3, 6, 11, 7);
        addFace(0, 7, 8, 4);
        addFace(4, 8, 9);
        addFace(5, 9, 10);
        addFace(6, 10, 11);
        addFace(7, 11, 8);
        addFace(8, 11, 10, 9);
    }

    private void addVertex(float x, float y, float z) {
        mesh.addVertex(x, y, z);
    }

    private void addFace(int... indices) {
        mesh.addFace(indices);
    }

}