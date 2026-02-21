package mesh.creator.archimedian;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TruncatedTetrahedronCreator implements IMeshCreator {

    private float a = 1.0f;
    
    private float b = 3.0f;

    private Mesh3D mesh;

    @Override
    public Mesh3D create() {
        initializeMesh();
        createVertices();
        createFaces();
        return mesh;
    }

    private void createVertices() {
        addVertex(+a, +a, +b);
        addVertex(+a, -a, -b);
        addVertex(-a, -a, +b);
        addVertex(-a, +a, -b);
        addVertex(+a, +b, +a);
        addVertex(+a, -b, -a);
        addVertex(-a, -b, +a);
        addVertex(-a, +b, -a);
        addVertex(+b, +a, +a);
        addVertex(+b, -a, -a);
        addVertex(-b, -a, +a);
        addVertex(-b, +a, -a);
    }

    private void createFaces() {
        createTriangularFaces();
        createHexagonalFaces();
    }

    private void createTriangularFaces() {
        addFace(0, 8, 4);
        addFace(1, 9, 5);
        addFace(2, 10, 6);
        addFace(3, 11, 7);
    }

    private void createHexagonalFaces() {
        addFace(0, 2, 6, 5, 9, 8);
        addFace(0, 4, 7, 11, 10, 2);
        addFace(1, 3, 7, 4, 8, 9);
        addFace(1, 5, 6, 10, 11, 3);
    }

    private void addVertex(float x, float y, float z) {
        mesh.addVertex(x, y, z);
    }

    private void addFace(int... indices) {
        mesh.addFace(indices);
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

}
