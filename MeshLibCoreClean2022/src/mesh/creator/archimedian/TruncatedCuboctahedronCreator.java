package mesh.creator.archimedian;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TruncatedCuboctahedronCreator implements IMeshCreator {

    @Override
    public Mesh3D create() {
	Mesh3D mesh = new Mesh3D();

	float a = 1.0f;
	float b = 1.0f + Mathf.sqrt(2.0f);
	float c = 1.0f + 2.0f * Mathf.sqrt(2.0f);

	mesh.addVertex(+a, +b, +c);
	mesh.addVertex(+a, +b, -c);
	mesh.addVertex(+a, -b, +c);
	mesh.addVertex(-a, +b, +c);
	mesh.addVertex(+a, -b, -c);
	mesh.addVertex(-a, +b, -c);
	mesh.addVertex(-a, -b, +c);
	mesh.addVertex(-a, -b, -c);

	mesh.addVertex(+a, +c, +b);
	mesh.addVertex(+a, -c, +b);
	mesh.addVertex(+a, +c, -b);
	mesh.addVertex(-a, +c, +b);
	mesh.addVertex(+a, -c, -b);
	mesh.addVertex(-a, -c, +b);
	mesh.addVertex(-a, +c, -b);
	mesh.addVertex(-a, -c, -b);

	mesh.addVertex(+b, +a, +c);
	mesh.addVertex(+b, +a, -c);
	mesh.addVertex(-b, +a, +c);
	mesh.addVertex(+b, -a, +c);
	mesh.addVertex(-b, +a, -c);
	mesh.addVertex(+b, -a, -c);
	mesh.addVertex(-b, -a, +c);
	mesh.addVertex(-b, -a, -c);

	mesh.addVertex(+b, +c, +a);
	mesh.addVertex(+b, -c, +a);
	mesh.addVertex(-b, +c, +a);
	mesh.addVertex(+b, +c, -a);
	mesh.addVertex(-b, -c, +a);
	mesh.addVertex(+b, -c, -a);
	mesh.addVertex(-b, +c, -a);
	mesh.addVertex(-b, -c, -a);

	mesh.addVertex(+c, +a, +b);
	mesh.addVertex(-c, +a, +b);
	mesh.addVertex(+c, +a, -b);
	mesh.addVertex(+c, -a, +b);
	mesh.addVertex(-c, +a, -b);
	mesh.addVertex(-c, -a, +b);
	mesh.addVertex(+c, -a, -b);
	mesh.addVertex(-c, -a, -b);

	mesh.addVertex(+c, +b, +a);
	mesh.addVertex(-c, +b, +a);
	mesh.addVertex(+c, -b, +a);
	mesh.addVertex(+c, +b, -a);
	mesh.addVertex(-c, -b, +a);
	mesh.addVertex(-c, +b, -a);
	mesh.addVertex(+c, -b, -a);
	mesh.addVertex(-c, -b, -a);

	mesh.addFace(0, 8, 11, 3);
	mesh.addFace(1, 5, 14, 10);
	mesh.addFace(2, 6, 13, 9);
	mesh.addFace(4, 12, 15, 7);
	mesh.addFace(16, 19, 35, 32);
	mesh.addFace(17, 34, 38, 21);
	mesh.addFace(18, 33, 37, 22);
	mesh.addFace(23, 39, 36, 20);
	mesh.addFace(24, 40, 43, 27);
	mesh.addFace(25, 29, 46, 42);
	mesh.addFace(26, 30, 45, 41);
	mesh.addFace(28, 44, 47, 31);

	mesh.addFace(0, 16, 32, 40, 24, 8);
	mesh.addFace(1, 10, 27, 43, 34, 17);
	mesh.addFace(2, 9, 25, 42, 35, 19);
	mesh.addFace(3, 11, 26, 41, 33, 18);
	mesh.addFace(4, 21, 38, 46, 29, 12);
	mesh.addFace(5, 20, 36, 45, 30, 14);
	mesh.addFace(6, 22, 37, 44, 28, 13);
	mesh.addFace(7, 15, 31, 47, 39, 23);

	mesh.addFace(0, 3, 18, 22, 6, 2, 19, 16);
	mesh.addFace(1, 17, 21, 4, 7, 23, 20, 5);
	mesh.addFace(8, 24, 27, 10, 14, 30, 26, 11);
	mesh.addFace(9, 13, 28, 31, 15, 12, 29, 25);
	mesh.addFace(32, 35, 42, 46, 38, 34, 43, 40);
	mesh.addFace(33, 41, 45, 36, 39, 47, 44, 37);

	return mesh;
    }

}
