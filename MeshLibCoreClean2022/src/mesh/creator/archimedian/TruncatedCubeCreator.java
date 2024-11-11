package mesh.creator.archimedian;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TruncatedCubeCreator implements IMeshCreator {

    @Override
    public Mesh3D create() {
        Mesh3D mesh = new Mesh3D();

        float a = 1.0f;
        float b = Mathf.sqrt(2.0f) - 1.0f;

        mesh.addVertex(+a, +a, +b);
        mesh.addVertex(+a, +a, -b);
        mesh.addVertex(+a, -a, +b);
        mesh.addVertex(-a, +a, +b);
        mesh.addVertex(+a, -a, -b);
        mesh.addVertex(-a, +a, -b);
        mesh.addVertex(-a, -a, +b);
        mesh.addVertex(-a, -a, -b);

        mesh.addVertex(+a, +b, +a);
        mesh.addVertex(+a, +b, -a);
        mesh.addVertex(+a, -b, +a);
        mesh.addVertex(-a, +b, +a);
        mesh.addVertex(+a, -b, -a);
        mesh.addVertex(-a, +b, -a);
        mesh.addVertex(-a, -b, +a);
        mesh.addVertex(-a, -b, -a);

        mesh.addVertex(+b, +a, +a);
        mesh.addVertex(+b, +a, -a);
        mesh.addVertex(+b, -a, +a);
        mesh.addVertex(-b, +a, +a);
        mesh.addVertex(+b, -a, -a);
        mesh.addVertex(-b, +a, -a);
        mesh.addVertex(-b, -a, +a);
        mesh.addVertex(-b, -a, -a);

        mesh.addFace(0, 16, 8);
        mesh.addFace(1, 9, 17);
        mesh.addFace(2, 10, 18);
        mesh.addFace(3, 11, 19);
        mesh.addFace(4, 20, 12);
        mesh.addFace(5, 21, 13);
        mesh.addFace(6, 22, 14);
        mesh.addFace(7, 15, 23);

        mesh.addFace(0, 1, 17, 21, 5, 3, 19, 16);
        mesh.addFace(0, 8, 10, 2, 4, 12, 9, 1);
        mesh.addFace(2, 18, 22, 6, 7, 23, 20, 4);
        mesh.addFace(3, 5, 13, 15, 7, 6, 14, 11);
        mesh.addFace(8, 16, 19, 11, 14, 22, 18, 10);
        mesh.addFace(9, 12, 20, 23, 15, 13, 21, 17);

        return mesh;
    }

}
