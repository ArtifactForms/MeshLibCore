package mesh.creator.archimedian;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class RhombicuboctahedronCreator implements IMeshCreator {

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new Mesh3D();

		float a = 1.0f;
		float b = 1.0f + Mathf.sqrt(2.0f);

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

		mesh.addFace(0, 2, 18, 16);
		mesh.addFace(0, 3, 6, 2);
		mesh.addFace(0, 8, 11, 3);
		mesh.addFace(1, 4, 7, 5);
		mesh.addFace(1, 5, 13, 9);
		mesh.addFace(1, 17, 20, 4);
		mesh.addFace(2, 6, 14, 10);
		mesh.addFace(3, 19, 22, 6);
		mesh.addFace(4, 12, 15, 7);
		mesh.addFace(5, 7, 23, 21);
		mesh.addFace(8, 9, 13, 11);
		mesh.addFace(8, 16, 17, 9);
		mesh.addFace(10, 12, 20, 18);
		mesh.addFace(10, 14, 15, 12);
		mesh.addFace(11, 13, 21, 19);
		mesh.addFace(14, 22, 23, 15);
		mesh.addFace(16, 18, 20, 17);
		mesh.addFace(19, 21, 23, 22);

		return mesh;
	}

}
