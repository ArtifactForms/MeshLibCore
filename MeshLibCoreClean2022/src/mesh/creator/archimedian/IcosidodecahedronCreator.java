package mesh.creator.archimedian;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class IcosidodecahedronCreator implements IMeshCreator {

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new Mesh3D();

		float a = 0.0f;
		float b = (1.0f + Mathf.sqrt(5.0f)) / 2.0f;
		float c = 1.0f / 2.0f;
		float d = b / 2.0f;
		float e = (1.0f + b) / 2.0f;

		mesh.addVertex(+a, +a, +b);
		mesh.addVertex(+a, +a, -b);
		mesh.addVertex(+a, +b, +a);
		mesh.addVertex(+a, -b, +a);
		mesh.addVertex(+b, +a, +a);
		mesh.addVertex(-b, +a, +a);

		mesh.addVertex(+c, +d, +e);
		mesh.addVertex(+c, +d, -e);
		mesh.addVertex(+c, -d, +e);
		mesh.addVertex(-c, +d, +e);
		mesh.addVertex(+c, -d, -e);
		mesh.addVertex(-c, +d, -e);
		mesh.addVertex(-c, -d, +e);
		mesh.addVertex(-c, -d, -e);

		mesh.addVertex(+d, +e, +c);
		mesh.addVertex(+d, -e, +c);
		mesh.addVertex(-d, +e, +c);
		mesh.addVertex(+d, +e, -c);
		mesh.addVertex(-d, -e, +c);
		mesh.addVertex(+d, -e, -c);
		mesh.addVertex(-d, +e, -c);
		mesh.addVertex(-d, -e, -c);

		mesh.addVertex(+e, +c, +d);
		mesh.addVertex(-e, +c, +d);
		mesh.addVertex(+e, +c, -d);
		mesh.addVertex(+e, -c, +d);
		mesh.addVertex(-e, +c, -d);
		mesh.addVertex(-e, -c, +d);
		mesh.addVertex(+e, -c, -d);
		mesh.addVertex(-e, -c, -d);

		mesh.addFace(0, 6, 9);
		mesh.addFace(0, 12, 8);
		mesh.addFace(1, 10, 13);
		mesh.addFace(1, 11, 7);
		mesh.addFace(2, 14, 17);
		mesh.addFace(2, 20, 16);
		mesh.addFace(3, 18, 21);
		mesh.addFace(3, 19, 15);
		mesh.addFace(4, 22, 25);
		mesh.addFace(4, 28, 24);
		mesh.addFace(5, 26, 29);
		mesh.addFace(5, 27, 23);
		mesh.addFace(6, 22, 14);
		mesh.addFace(7, 17, 24);
		mesh.addFace(8, 15, 25);
		mesh.addFace(9, 16, 23);
		mesh.addFace(10, 28, 19);
		mesh.addFace(11, 26, 20);
		mesh.addFace(12, 27, 18);
		mesh.addFace(13, 21, 29);

		mesh.addFace(0, 8, 25, 22, 6);
		mesh.addFace(0, 9, 23, 27, 12);
		mesh.addFace(1, 7, 24, 28, 10);
		mesh.addFace(1, 13, 29, 26, 11);
		mesh.addFace(2, 16, 9, 6, 14);
		mesh.addFace(2, 17, 7, 11, 20);
		mesh.addFace(3, 15, 8, 12, 18);
		mesh.addFace(3, 21, 13, 10, 19);
		mesh.addFace(4, 24, 17, 14, 22);
		mesh.addFace(4, 25, 15, 19, 28);
		mesh.addFace(5, 23, 16, 20, 26);
		mesh.addFace(5, 29, 21, 18, 27);

		return mesh;
	}

}
