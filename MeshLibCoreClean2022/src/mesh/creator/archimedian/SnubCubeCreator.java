package mesh.creator.archimedian;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class SnubCubeCreator implements IMeshCreator {

    private Mesh3D mesh;

    @Override
    public Mesh3D create() {
        initializeMesh();
        createVertices();
        createFaces();
        return mesh;
    }

    private void createVertices() {
        float a = 1.0f;
        float b = (1.0f / 3.0f)
                * (Mathf.pow(17 + 3.0f * Mathf.sqrt(33.0f), 1.0f / 3.0f)
                        - Mathf.pow(-17 + 3.0f * Mathf.sqrt(33.0f), 1.0f / 3.0f)
                        - 1.0f);
        float c = 1.0f / b;

        addVertex(+a, +b, -c);
        addVertex(+a, -b, +c);
        addVertex(-a, +b, +c);
        addVertex(-a, -b, -c);

        addVertex(+b, -c, +a);
        addVertex(-b, +c, +a);
        addVertex(+b, +c, -a);
        addVertex(-b, -c, -a);

        addVertex(-c, +a, +b);
        addVertex(+c, +a, -b);
        addVertex(+c, -a, +b);
        addVertex(-c, -a, -b);

        addVertex(+a, +c, +b);
        addVertex(+a, -c, -b);
        addVertex(-a, +c, -b);
        addVertex(-a, -c, +b);

        addVertex(+b, +a, +c);
        addVertex(-b, +a, -c);
        addVertex(-b, -a, +c);
        addVertex(+b, -a, -c);

        addVertex(+c, +b, +a);
        addVertex(-c, -b, +a);
        addVertex(+c, -b, -a);
        addVertex(-c, +b, -a);
    }

    private void createFaces() {
        createTriangles();
        createQuads();
    }

    private void createTriangles() {
        addFace(0, 6, 9);
        addFace(0, 9, 22);
        addFace(0, 17, 6);
        addFace(0, 22, 19);
        addFace(1, 4, 10);
        addFace(1, 10, 20);
        addFace(1, 18, 4);
        addFace(1, 20, 16);
        addFace(2, 5, 8);
        addFace(2, 8, 21);
        addFace(2, 16, 5);
        addFace(2, 21, 18);
        addFace(3, 7, 11);
        addFace(3, 11, 23);
        addFace(3, 19, 7);
        addFace(3, 23, 17);
        addFace(4, 13, 10);
        addFace(4, 18, 15);
        addFace(5, 14, 8);
        addFace(5, 16, 12);
        addFace(6, 12, 9);
        addFace(6, 17, 14);
        addFace(7, 15, 11);
        addFace(7, 19, 13);
        addFace(8, 14, 23);
        addFace(9, 12, 20);
        addFace(10, 13, 22);
        addFace(11, 15, 21);
        addFace(12, 16, 20);
        addFace(13, 19, 22);
        addFace(14, 17, 23);
        addFace(15, 18, 21);
    }

    private void createQuads() {
        addFace(0, 19, 3, 17);
        addFace(1, 16, 2, 18);
        addFace(4, 15, 7, 13);
        addFace(5, 12, 6, 14);
        addFace(8, 23, 11, 21);
        addFace(9, 20, 10, 22);
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
