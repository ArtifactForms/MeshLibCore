package mesh.creator.archimedian;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class SnubDodecahedronCreator implements IMeshCreator {

    @Override
    public Mesh3D create() {
	Mesh3D mesh = new Mesh3D();

	float a = 2.0f;
	float b = (1.0f + Mathf.sqrt(5.0f)) / 2.0f;
	float c = Mathf.pow(b / 2.0f + (1.0f / 2.0f) * Mathf.sqrt(b - (5.0f / 27.0f)), 1.0f / 3.0f)
		+ Mathf.pow(b / 2.0f - (1.0f / 2.0f) * Mathf.sqrt(b - (5.0f / 27.0f)), 1.0f / 3.0f);
	float d = c - (1.0f / c);
	float e = c * b + Mathf.pow(b, 2.0f) + b / c;
	float f = 2.0f * d;
	float g = 2.0f * e;
	float h = d + (e / b) + b;
	float i = -(d * b) + e + (1.0f / b);
	float j = (d / b) + (e * b) - 1.0f;
	float k = -(d / b) + (e * b) + 1.0f;
	float l = -d + (e / b) - b;
	float m = (d * b) + e - (1.0f / b);
	float n = -(d / b) + (e * b) - 1.0f;
	float o = d - (e / b) - b;
	float p = (d * b) + e + (1.0f / b);
	float q = d + (e / b) - b;
	float r = (d * b) - e + (1.0f / b);
	float s = (d / b) + (e * b) + 1.0f;

	mesh.addVertex(+f, +a, -g);
	mesh.addVertex(+f, -a, +g);
	mesh.addVertex(-f, +a, +g);
	mesh.addVertex(-f, -a, -g);

	mesh.addVertex(+a, -g, +f);
	mesh.addVertex(-a, +g, +f);
	mesh.addVertex(+a, +g, -f);
	mesh.addVertex(-a, -g, -f);

	mesh.addVertex(-g, +f, +a);
	mesh.addVertex(+g, +f, -a);
	mesh.addVertex(+g, -f, +a);
	mesh.addVertex(-g, -f, -a);

	mesh.addVertex(+h, +i, -j);
	mesh.addVertex(+h, -i, +j);
	mesh.addVertex(-h, +i, +j);
	mesh.addVertex(-h, -i, -j);

	mesh.addVertex(+i, -j, +h);
	mesh.addVertex(-i, +j, +h);
	mesh.addVertex(+i, +j, -h);
	mesh.addVertex(-i, -j, -h);

	mesh.addVertex(-j, +h, +i);
	mesh.addVertex(+j, +h, -i);
	mesh.addVertex(+j, -h, +i);
	mesh.addVertex(-j, -h, -i);

	mesh.addVertex(+k, +l, -m);
	mesh.addVertex(+k, -l, +m);
	mesh.addVertex(-k, +l, +m);
	mesh.addVertex(-k, -l, -m);

	mesh.addVertex(+l, -m, +k);
	mesh.addVertex(-l, +m, +k);
	mesh.addVertex(+l, +m, -k);
	mesh.addVertex(-l, -m, -k);

	mesh.addVertex(-m, +k, +l);
	mesh.addVertex(+m, +k, -l);
	mesh.addVertex(+m, -k, +l);
	mesh.addVertex(-m, -k, -l);

	mesh.addVertex(+n, +o, -p);
	mesh.addVertex(+n, -o, +p);
	mesh.addVertex(-n, +o, +p);
	mesh.addVertex(-n, -o, -p);

	mesh.addVertex(+o, -p, +n);
	mesh.addVertex(-o, +p, +n);
	mesh.addVertex(+o, +p, -n);
	mesh.addVertex(-o, -p, -n);

	mesh.addVertex(-p, +n, +o);
	mesh.addVertex(+p, +n, -o);
	mesh.addVertex(+p, -n, +o);
	mesh.addVertex(-p, -n, -o);

	mesh.addVertex(+q, +r, -s);
	mesh.addVertex(+q, -r, +s);
	mesh.addVertex(-q, +r, +s);
	mesh.addVertex(-q, -r, -s);

	mesh.addVertex(+r, -s, +q);
	mesh.addVertex(-r, +s, +q);
	mesh.addVertex(+r, +s, -q);
	mesh.addVertex(-r, -s, -q);

	mesh.addVertex(-s, +q, +r);
	mesh.addVertex(+s, +q, -r);
	mesh.addVertex(+s, -q, +r);
	mesh.addVertex(-s, -q, -r);

	mesh.addFace(0, 3, 51);
	mesh.addFace(0, 30, 12);
	mesh.addFace(0, 48, 3);
	mesh.addFace(0, 51, 30);
	mesh.addFace(1, 2, 50);
	mesh.addFace(1, 28, 13);
	mesh.addFace(1, 49, 2);
	mesh.addFace(1, 50, 28);
	mesh.addFace(2, 29, 14);
	mesh.addFace(2, 49, 29);
	mesh.addFace(3, 31, 15);
	mesh.addFace(3, 48, 31);
	mesh.addFace(4, 7, 55);
	mesh.addFace(4, 34, 16);
	mesh.addFace(4, 52, 7);
	mesh.addFace(4, 55, 34);
	mesh.addFace(5, 6, 54);
	mesh.addFace(5, 32, 17);
	mesh.addFace(5, 53, 6);
	mesh.addFace(5, 54, 32);
	mesh.addFace(6, 33, 18);
	mesh.addFace(6, 53, 33);
	mesh.addFace(7, 35, 19);
	mesh.addFace(7, 52, 35);
	mesh.addFace(8, 11, 59);
	mesh.addFace(8, 26, 20);
	mesh.addFace(8, 56, 11);
	mesh.addFace(8, 59, 26);
	mesh.addFace(9, 10, 58);
	mesh.addFace(9, 24, 21);
	mesh.addFace(9, 57, 10);
	mesh.addFace(9, 58, 24);
	mesh.addFace(10, 25, 22);
	mesh.addFace(10, 57, 25);
	mesh.addFace(11, 27, 23);
	mesh.addFace(11, 56, 27);
	mesh.addFace(12, 18, 21);
	mesh.addFace(12, 21, 24);
	mesh.addFace(12, 30, 18);
	mesh.addFace(13, 16, 22);
	mesh.addFace(13, 22, 25);
	mesh.addFace(13, 28, 16);
	mesh.addFace(14, 17, 20);
	mesh.addFace(14, 20, 26);
	mesh.addFace(14, 29, 17);
	mesh.addFace(15, 19, 23);
	mesh.addFace(15, 23, 27);
	mesh.addFace(15, 31, 19);
	mesh.addFace(16, 34, 22);
	mesh.addFace(17, 32, 20);
	mesh.addFace(18, 33, 21);
	mesh.addFace(19, 35, 23);
	mesh.addFace(24, 58, 36);
	mesh.addFace(25, 57, 37);
	mesh.addFace(26, 59, 38);
	mesh.addFace(27, 56, 39);
	mesh.addFace(28, 50, 40);
	mesh.addFace(29, 49, 41);
	mesh.addFace(30, 51, 42);
	mesh.addFace(31, 48, 43);
	mesh.addFace(32, 54, 44);
	mesh.addFace(33, 53, 45);
	mesh.addFace(34, 55, 46);
	mesh.addFace(35, 52, 47);
	mesh.addFace(36, 43, 48);
	mesh.addFace(36, 46, 43);
	mesh.addFace(36, 58, 46);
	mesh.addFace(37, 41, 49);
	mesh.addFace(37, 45, 41);
	mesh.addFace(37, 57, 45);
	mesh.addFace(38, 40, 50);
	mesh.addFace(38, 47, 40);
	mesh.addFace(38, 59, 47);
	mesh.addFace(39, 42, 51);
	mesh.addFace(39, 44, 42);
	mesh.addFace(39, 56, 44);
	mesh.addFace(40, 47, 52);
	mesh.addFace(41, 45, 53);
	mesh.addFace(42, 44, 54);
	mesh.addFace(43, 46, 55);

	mesh.addFace(0, 12, 24, 36, 48);
	mesh.addFace(1, 13, 25, 37, 49);
	mesh.addFace(2, 14, 26, 38, 50);
	mesh.addFace(3, 15, 27, 39, 51);
	mesh.addFace(4, 16, 28, 40, 52);
	mesh.addFace(5, 17, 29, 41, 53);
	mesh.addFace(6, 18, 30, 42, 54);
	mesh.addFace(7, 19, 31, 43, 55);
	mesh.addFace(8, 20, 32, 44, 56);
	mesh.addFace(9, 21, 33, 45, 57);
	mesh.addFace(10, 22, 34, 46, 58);
	mesh.addFace(11, 23, 35, 47, 59);

	return mesh;
    }

}
