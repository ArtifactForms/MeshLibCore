package mesh.creator.archimedian;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TruncatedDodecahedronCreator implements IMeshCreator {

    @Override
    public Mesh3D create() {
	Mesh3D mesh = new Mesh3D();

	float a = 0.0f;
	float b = 2.0f;
	float c = (1.0f + Mathf.sqrt(5.0f)) / 2.0f;
	float d = 1.0f / c;
	float e = 2.0f + c;
	float f = 2.0f * c;
	float g = Mathf.pow(c, 2.0f);

	mesh.addVertex(+a, +d, +e);
	mesh.addVertex(+a, +d, -e);
	mesh.addVertex(+a, -d, +e);
	mesh.addVertex(+a, -d, -e);

	mesh.addVertex(+e, +a, +d);
	mesh.addVertex(-e, +a, +d);
	mesh.addVertex(+e, +a, -d);
	mesh.addVertex(-e, +a, -d);

	mesh.addVertex(+d, +e, +a);
	mesh.addVertex(+d, -e, +a);
	mesh.addVertex(-d, +e, +a);
	mesh.addVertex(-d, -e, +a);

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

	mesh.addVertex(+c, +b, +g);
	mesh.addVertex(+c, +b, -g);
	mesh.addVertex(+c, -b, +g);
	mesh.addVertex(-c, +b, +g);
	mesh.addVertex(+c, -b, -g);
	mesh.addVertex(-c, +b, -g);
	mesh.addVertex(-c, -b, +g);
	mesh.addVertex(-c, -b, -g);

	mesh.addVertex(+g, +c, +b);
	mesh.addVertex(+g, +c, -b);
	mesh.addVertex(+g, -c, +b);
	mesh.addVertex(-g, +c, +b);
	mesh.addVertex(+g, -c, -b);
	mesh.addVertex(-g, +c, -b);
	mesh.addVertex(-g, -c, +b);
	mesh.addVertex(-g, -c, -b);

	mesh.addVertex(+b, +g, +c);
	mesh.addVertex(+b, +g, -c);
	mesh.addVertex(+b, -g, +c);
	mesh.addVertex(-b, +g, +c);
	mesh.addVertex(+b, -g, -c);
	mesh.addVertex(-b, +g, -c);
	mesh.addVertex(-b, -g, +c);
	mesh.addVertex(-b, -g, -c);

	mesh.addFace(0, 12, 15);
	mesh.addFace(1, 17, 13);
	mesh.addFace(2, 18, 14);
	mesh.addFace(3, 16, 19);
	mesh.addFace(4, 20, 22);
	mesh.addFace(5, 26, 23);
	mesh.addFace(6, 24, 21);
	mesh.addFace(7, 25, 27);
	mesh.addFace(8, 28, 29);
	mesh.addFace(9, 32, 30);
	mesh.addFace(10, 33, 31);
	mesh.addFace(11, 34, 35);
	mesh.addFace(36, 44, 52);
	mesh.addFace(37, 53, 45);
	mesh.addFace(38, 54, 46);
	mesh.addFace(39, 55, 47);
	mesh.addFace(40, 48, 56);
	mesh.addFace(41, 49, 57);
	mesh.addFace(42, 50, 58);
	mesh.addFace(43, 59, 51);

	mesh.addFace(0, 2, 14, 38, 46, 22, 20, 44, 36, 12);
	mesh.addFace(0, 15, 39, 47, 23, 26, 50, 42, 18, 2);
	mesh.addFace(1, 3, 19, 43, 51, 27, 25, 49, 41, 17);
	mesh.addFace(1, 13, 37, 45, 21, 24, 48, 40, 16, 3);
	mesh.addFace(4, 6, 21, 45, 53, 29, 28, 52, 44, 20);
	mesh.addFace(4, 22, 46, 54, 30, 32, 56, 48, 24, 6);
	mesh.addFace(5, 7, 27, 51, 59, 35, 34, 58, 50, 26);
	mesh.addFace(5, 23, 47, 55, 31, 33, 57, 49, 25, 7);
	mesh.addFace(8, 10, 31, 55, 39, 15, 12, 36, 52, 28);
	mesh.addFace(8, 29, 53, 37, 13, 17, 41, 57, 33, 10);
	mesh.addFace(9, 11, 35, 59, 43, 19, 16, 40, 56, 32);
	mesh.addFace(9, 30, 54, 38, 14, 18, 42, 58, 34, 11);

	return mesh;
    }

}
