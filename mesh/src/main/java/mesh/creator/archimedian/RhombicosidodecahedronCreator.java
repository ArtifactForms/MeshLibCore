package mesh.creator.archimedian;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class RhombicosidodecahedronCreator implements IMeshCreator {

    private float a = 0.0f;

    private float b = 1.0f;

    private float c = (1.0f + Mathf.sqrt(5.0f)) / 2.0f;

    private float d = Mathf.pow(c, 2.0f);

    private float e = Mathf.pow(c, 3.0f);

    private float f = 2.0f * c;

    private float g = 2.0f + c;

    private Mesh3D mesh;

    @Override
    public Mesh3D create() {
        initializeMesh();
        addFaces();
        createVertices();
        return mesh;
    }
    
    private void initializeMesh() {
        mesh = new Mesh3D();
    }

    private void createVertices() {
        mesh.addVertex(+b, +b, +e);
        mesh.addVertex(+b, +b, -e);
        mesh.addVertex(+b, -b, +e);
        mesh.addVertex(-b, +b, +e);
        mesh.addVertex(+b, -b, -e);
        mesh.addVertex(-b, +b, -e);
        mesh.addVertex(-b, -b, +e);
        mesh.addVertex(-b, -b, -e);

        mesh.addVertex(+e, +b, +b);
        mesh.addVertex(+e, +b, -b);
        mesh.addVertex(+e, -b, +b);
        mesh.addVertex(-e, +b, +b);
        mesh.addVertex(+e, -b, -b);
        mesh.addVertex(-e, +b, -b);
        mesh.addVertex(-e, -b, +b);
        mesh.addVertex(-e, -b, -b);

        mesh.addVertex(+b, +e, +b);
        mesh.addVertex(+b, +e, -b);
        mesh.addVertex(+b, -e, +b);
        mesh.addVertex(-b, +e, +b);
        mesh.addVertex(+b, -e, -b);
        mesh.addVertex(-b, +e, -b);
        mesh.addVertex(-b, -e, +b);
        mesh.addVertex(-b, -e, -b);

        mesh.addVertex(+d, +c, +f);
        mesh.addVertex(+d, +c, -f);
        mesh.addVertex(+d, -c, +f);
        mesh.addVertex(-d, +c, +f);
        mesh.addVertex(+d, -c, -f);
        mesh.addVertex(-d, +c, -f);
        mesh.addVertex(-d, -c, +f);
        mesh.addVertex(-d, -c, -f);

        mesh.addVertex(+f, +d, +c);
        mesh.addVertex(+f, +d, -c);
        mesh.addVertex(+f, -d, +c);
        mesh.addVertex(-f, +d, +c);
        mesh.addVertex(+f, -d, -c);
        mesh.addVertex(-f, +d, -c);
        mesh.addVertex(-f, -d, +c);
        mesh.addVertex(-f, -d, -c);

        mesh.addVertex(+c, +f, +d);
        mesh.addVertex(+c, +f, -d);
        mesh.addVertex(+c, -f, +d);
        mesh.addVertex(-c, +f, +d);
        mesh.addVertex(+c, -f, -d);
        mesh.addVertex(-c, +f, -d);
        mesh.addVertex(-c, -f, +d);
        mesh.addVertex(-c, -f, -d);

        mesh.addVertex(+g, +a, +d);
        mesh.addVertex(+g, +a, -d);
        mesh.addVertex(-g, +a, +d);
        mesh.addVertex(-g, +a, -d);

        mesh.addVertex(+d, +g, +a);
        mesh.addVertex(-d, +g, +a);
        mesh.addVertex(+d, -g, +a);
        mesh.addVertex(-d, -g, +a);

        mesh.addVertex(+a, +d, +g);
        mesh.addVertex(+a, -d, +g);
        mesh.addVertex(+a, +d, -g);
        mesh.addVertex(+a, -d, -g);
    }

    private void addFaces() {
        mesh.addFace(0, 56, 3);
        mesh.addFace(1, 5, 58);
        mesh.addFace(2, 6, 57);
        mesh.addFace(4, 59, 7);
        mesh.addFace(8, 48, 10);
        mesh.addFace(9, 12, 49);
        mesh.addFace(11, 14, 50);
        mesh.addFace(13, 51, 15);
        mesh.addFace(16, 52, 17);
        mesh.addFace(18, 20, 54);
        mesh.addFace(19, 21, 53);
        mesh.addFace(22, 55, 23);
        mesh.addFace(24, 32, 40);
        mesh.addFace(25, 41, 33);
        mesh.addFace(26, 42, 34);
        mesh.addFace(27, 43, 35);
        mesh.addFace(28, 36, 44);
        mesh.addFace(29, 37, 45);
        mesh.addFace(30, 38, 46);
        mesh.addFace(31, 47, 39);

        mesh.addFace(0, 3, 6, 2);
        mesh.addFace(0, 24, 40, 56);
        mesh.addFace(1, 4, 7, 5);
        mesh.addFace(1, 58, 41, 25);
        mesh.addFace(2, 57, 42, 26);
        mesh.addFace(3, 56, 43, 27);
        mesh.addFace(4, 28, 44, 59);
        mesh.addFace(5, 29, 45, 58);
        mesh.addFace(6, 30, 46, 57);
        mesh.addFace(7, 59, 47, 31);
        mesh.addFace(8, 10, 12, 9);
        mesh.addFace(8, 32, 24, 48);
        mesh.addFace(9, 49, 25, 33);
        mesh.addFace(10, 48, 26, 34);
        mesh.addFace(11, 13, 15, 14);
        mesh.addFace(11, 50, 27, 35);
        mesh.addFace(12, 36, 28, 49);
        mesh.addFace(13, 37, 29, 51);
        mesh.addFace(14, 38, 30, 50);
        mesh.addFace(15, 51, 31, 39);
        mesh.addFace(16, 17, 21, 19);
        mesh.addFace(16, 40, 32, 52);
        mesh.addFace(17, 52, 33, 41);
        mesh.addFace(18, 22, 23, 20);
        mesh.addFace(18, 54, 34, 42);
        mesh.addFace(19, 53, 35, 43);
        mesh.addFace(20, 44, 36, 54);
        mesh.addFace(21, 45, 37, 53);
        mesh.addFace(22, 46, 38, 55);
        mesh.addFace(23, 55, 39, 47);

        mesh.addFace(0, 2, 26, 48, 24);
        mesh.addFace(1, 25, 49, 28, 4);
        mesh.addFace(3, 27, 50, 30, 6);
        mesh.addFace(5, 7, 31, 51, 29);
        mesh.addFace(8, 9, 33, 52, 32);
        mesh.addFace(10, 34, 54, 36, 12);
        mesh.addFace(11, 35, 53, 37, 13);
        mesh.addFace(14, 15, 39, 55, 38);
        mesh.addFace(16, 19, 43, 56, 40);
        mesh.addFace(17, 41, 58, 45, 21);
        mesh.addFace(18, 42, 57, 46, 22);
        mesh.addFace(20, 23, 47, 59, 44);
    }

}
