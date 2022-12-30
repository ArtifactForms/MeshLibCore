package mesh.creator.archimedian;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TruncatedIcosidodecahedronCreator implements IMeshCreator {

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new Mesh3D();

		float a = 2.0f;
		float b = (1.0f + Mathf.sqrt(5.0f)) / 2.0f;
		float c = 1.0f / b;
		float d = 3.0f + b;
		float e = 2.0f / b;
		float f = 1 + 2.0f * b;
		float g = Mathf.pow(b, 2.0f);
		float h = -1.0f + 3.0f * b;
		float i = -1.0f + 2.0f * b;
		float j = 2.0f + b;
		float k = 3.0f;
		float l = 2.0f * b;

		mesh.addVertex(+c, +c, +d);
		mesh.addVertex(+c, +c, -d);
		mesh.addVertex(+c, -c, +d);
		mesh.addVertex(-c, +c, +d);
		mesh.addVertex(+c, -c, -d);
		mesh.addVertex(-c, +c, -d);
		mesh.addVertex(-c, -c, +d);
		mesh.addVertex(-c, -c, -d);

		mesh.addVertex(+c, +d, +c);
		mesh.addVertex(+c, -d, +c);
		mesh.addVertex(-c, +d, +c);
		mesh.addVertex(+c, +d, -c);
		mesh.addVertex(-c, -d, +c);
		mesh.addVertex(+c, -d, -c);
		mesh.addVertex(-c, +d, -c);
		mesh.addVertex(-c, -d, -c);

		mesh.addVertex(+d, +c, +c);
		mesh.addVertex(-d, +c, +c);
		mesh.addVertex(+d, +c, -c);
		mesh.addVertex(+d, -c, +c);
		mesh.addVertex(-d, +c, -c);
		mesh.addVertex(-d, -c, +c);
		mesh.addVertex(+d, -c, -c);
		mesh.addVertex(-d, -c, -c);

		mesh.addVertex(+e, +b, +f);
		mesh.addVertex(+e, +b, -f);
		mesh.addVertex(+e, -b, +f);
		mesh.addVertex(-e, +b, +f);
		mesh.addVertex(+e, -b, -f);
		mesh.addVertex(-e, +b, -f);
		mesh.addVertex(-e, -b, +f);
		mesh.addVertex(-e, -b, -f);

		mesh.addVertex(+b, +f, +e);
		mesh.addVertex(+b, -f, +e);
		mesh.addVertex(-b, +f, +e);
		mesh.addVertex(+b, +f, -e);
		mesh.addVertex(-b, -f, +e);
		mesh.addVertex(+b, -f, -e);
		mesh.addVertex(-b, +f, -e);
		mesh.addVertex(-b, -f, -e);

		mesh.addVertex(+f, +e, +b);
		mesh.addVertex(-f, +e, +b);
		mesh.addVertex(+f, +e, -b);
		mesh.addVertex(+f, -e, +b);
		mesh.addVertex(-f, +e, -b);
		mesh.addVertex(-f, -e, +b);
		mesh.addVertex(+f, -e, -b);
		mesh.addVertex(-f, -e, -b);

		mesh.addVertex(+c, +g, +h);
		mesh.addVertex(+c, +g, -h);
		mesh.addVertex(+c, -g, +h);
		mesh.addVertex(-c, +g, +h);
		mesh.addVertex(+c, -g, -h);
		mesh.addVertex(-c, +g, -h);
		mesh.addVertex(-c, -g, +h);
		mesh.addVertex(-c, -g, -h);

		mesh.addVertex(+g, +h, +c);
		mesh.addVertex(+g, -h, +c);
		mesh.addVertex(-g, +h, +c);
		mesh.addVertex(+g, +h, -c);
		mesh.addVertex(-g, -h, +c);
		mesh.addVertex(+g, -h, -c);
		mesh.addVertex(-g, +h, -c);
		mesh.addVertex(-g, -h, -c);

		mesh.addVertex(+h, +c, +g);
		mesh.addVertex(-h, +c, +g);
		mesh.addVertex(+h, +c, -g);
		mesh.addVertex(+h, -c, +g);
		mesh.addVertex(-h, +c, -g);
		mesh.addVertex(-h, -c, +g);
		mesh.addVertex(+h, -c, -g);
		mesh.addVertex(-h, -c, -g);

		mesh.addVertex(+i, +a, +j);
		mesh.addVertex(+i, +a, -j);
		mesh.addVertex(+i, -a, +j);
		mesh.addVertex(-i, +a, +j);
		mesh.addVertex(+i, -a, -j);
		mesh.addVertex(-i, +a, -j);
		mesh.addVertex(-i, -a, +j);
		mesh.addVertex(-i, -a, -j);

		mesh.addVertex(+a, +j, +i);
		mesh.addVertex(+a, -j, +i);
		mesh.addVertex(-a, +j, +i);
		mesh.addVertex(+a, +j, -i);
		mesh.addVertex(-a, -j, +i);
		mesh.addVertex(+a, -j, -i);
		mesh.addVertex(-a, +j, -i);
		mesh.addVertex(-a, -j, -i);

		mesh.addVertex(+j, +i, +a);
		mesh.addVertex(-j, +i, +a);
		mesh.addVertex(+j, +i, -a);
		mesh.addVertex(+j, -i, +a);
		mesh.addVertex(-j, +i, -a);
		mesh.addVertex(-j, -i, +a);
		mesh.addVertex(+j, -i, -a);
		mesh.addVertex(-j, -i, -a);

		mesh.addVertex(+b, +k, +l);
		mesh.addVertex(+b, +k, -l);
		mesh.addVertex(+b, -k, +l);
		mesh.addVertex(-b, +k, +l);
		mesh.addVertex(+b, -k, -l);
		mesh.addVertex(-b, +k, -l);
		mesh.addVertex(-b, -k, +l);
		mesh.addVertex(-b, -k, -l);

		mesh.addVertex(+k, +l, +b);
		mesh.addVertex(+k, -l, +b);
		mesh.addVertex(-k, +l, +b);
		mesh.addVertex(+k, +l, -b);
		mesh.addVertex(-k, -l, +b);
		mesh.addVertex(+k, -l, -b);
		mesh.addVertex(-k, +l, -b);
		mesh.addVertex(-k, -l, -b);

		mesh.addVertex(+l, +b, +k);
		mesh.addVertex(-l, +b, +k);
		mesh.addVertex(+l, +b, -k);
		mesh.addVertex(+l, -b, +k);
		mesh.addVertex(-l, +b, -k);
		mesh.addVertex(-l, -b, +k);
		mesh.addVertex(+l, -b, -k);
		mesh.addVertex(-l, -b, -k);

		mesh.addFace(0, 3, 6, 2);
		mesh.addFace(1, 4, 7, 5);
		mesh.addFace(8, 11, 14, 10);
		mesh.addFace(9, 12, 15, 13);
		mesh.addFace(16, 19, 22, 18);
		mesh.addFace(17, 20, 23, 21);
		mesh.addFace(24, 72, 96, 48);
		mesh.addFace(25, 49, 97, 73);
		mesh.addFace(26, 50, 98, 74);
		mesh.addFace(27, 51, 99, 75);
		mesh.addFace(28, 76, 100, 52);
		mesh.addFace(29, 77, 101, 53);
		mesh.addFace(30, 78, 102, 54);
		mesh.addFace(31, 55, 103, 79);
		mesh.addFace(32, 80, 104, 56);
		mesh.addFace(33, 57, 105, 81);
		mesh.addFace(34, 58, 106, 82);
		mesh.addFace(35, 59, 107, 83);
		mesh.addFace(36, 84, 108, 60);
		mesh.addFace(37, 85, 109, 61);
		mesh.addFace(38, 86, 110, 62);
		mesh.addFace(39, 63, 111, 87);
		mesh.addFace(40, 88, 112, 64);
		mesh.addFace(41, 65, 113, 89);
		mesh.addFace(42, 66, 114, 90);
		mesh.addFace(43, 67, 115, 91);
		mesh.addFace(44, 92, 116, 68);
		mesh.addFace(45, 93, 117, 69);
		mesh.addFace(46, 94, 118, 70);
		mesh.addFace(47, 71, 119, 95);

		mesh.addFace(0, 24, 48, 51, 27, 3);
		mesh.addFace(1, 5, 29, 53, 49, 25);
		mesh.addFace(2, 6, 30, 54, 50, 26);
		mesh.addFace(4, 28, 52, 55, 31, 7);
		mesh.addFace(8, 32, 56, 59, 35, 11);
		mesh.addFace(9, 13, 37, 61, 57, 33);
		mesh.addFace(10, 14, 38, 62, 58, 34);
		mesh.addFace(12, 36, 60, 63, 39, 15);
		mesh.addFace(16, 40, 64, 67, 43, 19);
		mesh.addFace(17, 21, 45, 69, 65, 41);
		mesh.addFace(18, 22, 46, 70, 66, 42);
		mesh.addFace(20, 44, 68, 71, 47, 23);
		mesh.addFace(72, 112, 88, 104, 80, 96);
		mesh.addFace(73, 97, 83, 107, 90, 114);
		mesh.addFace(74, 98, 81, 105, 91, 115);
		mesh.addFace(75, 99, 82, 106, 89, 113);
		mesh.addFace(76, 118, 94, 109, 85, 100);
		mesh.addFace(78, 117, 93, 108, 84, 102);
		mesh.addFace(79, 103, 87, 111, 95, 119);
		mesh.addFace(86, 101, 77, 116, 92, 110);

		mesh.addFace(0, 2, 26, 74, 115, 67, 64, 112, 72, 24);
		mesh.addFace(1, 25, 73, 114, 66, 70, 118, 76, 28, 4);
		mesh.addFace(3, 27, 75, 113, 65, 69, 117, 78, 30, 6);
		mesh.addFace(5, 7, 31, 79, 119, 71, 68, 116, 77, 29);
		mesh.addFace(8, 10, 34, 82, 99, 51, 48, 96, 80, 32);
		mesh.addFace(9, 33, 81, 98, 50, 54, 102, 84, 36, 12);
		mesh.addFace(11, 35, 83, 97, 49, 53, 101, 86, 38, 14);
		mesh.addFace(13, 15, 39, 87, 103, 55, 52, 100, 85, 37);
		mesh.addFace(16, 18, 42, 90, 107, 59, 56, 104, 88, 40);
		mesh.addFace(17, 41, 89, 106, 58, 62, 110, 92, 44, 20);
		mesh.addFace(19, 43, 91, 105, 57, 61, 109, 94, 46, 22);
		mesh.addFace(21, 23, 47, 95, 111, 63, 60, 108, 93, 45);

		return mesh;
	}

}
