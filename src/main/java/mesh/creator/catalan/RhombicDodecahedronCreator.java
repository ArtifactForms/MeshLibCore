package mesh.creator.catalan;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class RhombicDodecahedronCreator implements IMeshCreator {

    private Mesh3D mesh;

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
        addVertex(-1, 1, 1);
        addVertex(-1, 1, -1);
        addVertex(1, 1, -1);
        addVertex(1, 1, 1);
        addVertex(-1, -1, 1);
        addVertex(-1, -1, -1);
        addVertex(1, -1, -1);
        addVertex(1, -1, 1);
    }

    private void createOuterVertices() {
        addVertex(0, 2, 0);
        addVertex(0, -2, 0);
        addVertex(2, 0, 0);
        addVertex(-2, 0, 0);
        addVertex(0, 0, 2);
        addVertex(0, 0, -2);
    }

    private void createFaces() {
        addFace(8, 3, 10, 2);
        addFace(8, 0, 12, 3);
        addFace(8, 1, 11, 0);
        addFace(8, 2, 13, 1);
        addFace(9, 7, 12, 4);
        addFace(9, 6, 10, 7);
        addFace(9, 5, 13, 6);
        addFace(9, 4, 11, 5);
        addFace(4, 12, 0, 11);
        addFace(7, 10, 3, 12);
        addFace(6, 13, 2, 10);
        addFace(5, 11, 1, 13);
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