package mesh.creator.archimedian;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TruncatedIcosahedronCreator implements IMeshCreator {

    @Override
    public Mesh3D create() {
        Mesh3D mesh = new Mesh3D();

        float a = 0.0f;
        float b = 1.0f;
        float c = 2.0f;
        float d = (1.0f + Mathf.sqrt(5.0f)) / 2.0f;
        float e = 3.0f * d;
        float f = 1.0f + 2.0f * d;
        float g = 2.0f + d;
        float h = 2.0f * d;

        mesh.addVertex(+a, +b, +e);
        mesh.addVertex(+a, +b, -e);
        mesh.addVertex(+a, -b, +e);
        mesh.addVertex(+a, -b, -e);

        mesh.addVertex(+b, +e, +a);
        mesh.addVertex(+b, -e, +a);
        mesh.addVertex(-b, +e, +a);
        mesh.addVertex(-b, -e, +a);

        mesh.addVertex(+e, +a, +b);
        mesh.addVertex(-e, +a, +b);
        mesh.addVertex(+e, +a, -b);
        mesh.addVertex(-e, +a, -b);

        mesh.addVertex(+c, +f, +d);
        mesh.addVertex(+c, +f, -d);
        mesh.addVertex(+c, -f, +d);
        mesh.addVertex(-c, +f, +d);
        mesh.addVertex(+c, -f, -d);
        mesh.addVertex(-c, +f, -d);
        mesh.addVertex(-c, -f, +d);
        mesh.addVertex(-c, -f, -d);

        mesh.addVertex(+f, +d, +c);
        mesh.addVertex(+f, -d, +c);
        mesh.addVertex(-f, +d, +c);
        mesh.addVertex(+f, +d, -c);
        mesh.addVertex(-f, -d, +c);
        mesh.addVertex(+f, -d, -c);
        mesh.addVertex(-f, +d, -c);
        mesh.addVertex(-f, -d, -c);

        mesh.addVertex(+d, +c, +f);
        mesh.addVertex(-d, +c, +f);
        mesh.addVertex(+d, +c, -f);
        mesh.addVertex(+d, -c, +f);
        mesh.addVertex(-d, +c, -f);
        mesh.addVertex(-d, -c, +f);
        mesh.addVertex(+d, -c, -f);
        mesh.addVertex(-d, -c, -f);

        mesh.addVertex(+b, +g, +h);
        mesh.addVertex(+b, +g, -h);
        mesh.addVertex(+b, -g, +h);
        mesh.addVertex(-b, +g, +h);
        mesh.addVertex(+b, -g, -h);
        mesh.addVertex(-b, +g, -h);
        mesh.addVertex(-b, -g, +h);
        mesh.addVertex(-b, -g, -h);

        mesh.addVertex(+g, +h, +b);
        mesh.addVertex(+g, -h, +b);
        mesh.addVertex(-g, +h, +b);
        mesh.addVertex(+g, +h, -b);
        mesh.addVertex(-g, -h, +b);
        mesh.addVertex(+g, -h, -b);
        mesh.addVertex(-g, +h, -b);
        mesh.addVertex(-g, -h, -b);

        mesh.addVertex(+h, +b, +g);
        mesh.addVertex(-h, +b, +g);
        mesh.addVertex(+h, +b, -g);
        mesh.addVertex(+h, -b, +g);
        mesh.addVertex(-h, +b, -g);
        mesh.addVertex(-h, -b, +g);
        mesh.addVertex(+h, -b, -g);
        mesh.addVertex(-h, -b, -g);

        mesh.addFace(0, 28, 36, 39, 29);
        mesh.addFace(1, 32, 41, 37, 30);
        mesh.addFace(2, 33, 42, 38, 31);
        mesh.addFace(3, 34, 40, 43, 35);
        mesh.addFace(4, 12, 44, 47, 13);
        mesh.addFace(5, 16, 49, 45, 14);
        mesh.addFace(6, 17, 50, 46, 15);
        mesh.addFace(7, 18, 48, 51, 19);
        mesh.addFace(8, 20, 52, 55, 21);
        mesh.addFace(9, 24, 57, 53, 22);
        mesh.addFace(10, 25, 58, 54, 23);
        mesh.addFace(11, 26, 56, 59, 27);

        mesh.addFace(0, 2, 31, 55, 52, 28);
        mesh.addFace(0, 29, 53, 57, 33, 2);
        mesh.addFace(1, 3, 35, 59, 56, 32);
        mesh.addFace(1, 30, 54, 58, 34, 3);
        mesh.addFace(4, 6, 15, 39, 36, 12);
        mesh.addFace(4, 13, 37, 41, 17, 6);
        mesh.addFace(5, 7, 19, 43, 40, 16);
        mesh.addFace(5, 14, 38, 42, 18, 7);
        mesh.addFace(8, 10, 23, 47, 44, 20);
        mesh.addFace(8, 21, 45, 49, 25, 10);
        mesh.addFace(9, 11, 27, 51, 48, 24);
        mesh.addFace(9, 22, 46, 50, 26, 11);
        mesh.addFace(12, 36, 28, 52, 20, 44);
        mesh.addFace(13, 47, 23, 54, 30, 37);
        mesh.addFace(14, 45, 21, 55, 31, 38);
        mesh.addFace(15, 46, 22, 53, 29, 39);
        mesh.addFace(16, 40, 34, 58, 25, 49);
        mesh.addFace(17, 41, 32, 56, 26, 50);
        mesh.addFace(18, 42, 33, 57, 24, 48);
        mesh.addFace(19, 51, 27, 59, 35, 43);

        return mesh;
    }

}
