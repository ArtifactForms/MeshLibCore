package mesh.creator.archimedian;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class SnubCubeCreator implements IMeshCreator {

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new Mesh3D();

		float a = 1.0f;
		float b = (1.0f / 3.0f) * (Mathf.pow(17 + 3.0f * Mathf.sqrt(33.0f), 1.0f / 3.0f) - Mathf.pow(-17 + 3.0f * Mathf.sqrt(33.0f), 1.0f / 3.0f) - 1.0f);
		float c = 1.0f / b;

		mesh.addVertex(+a, +b, -c);
		mesh.addVertex(+a, -b, +c);
		mesh.addVertex(-a, +b, +c);
		mesh.addVertex(-a, -b, -c);

		mesh.addVertex(+b, -c, +a);
		mesh.addVertex(-b, +c, +a);
		mesh.addVertex(+b, +c, -a);
		mesh.addVertex(-b, -c, -a);

		mesh.addVertex(-c, +a, +b);
		mesh.addVertex(+c, +a, -b);
		mesh.addVertex(+c, -a, +b);
		mesh.addVertex(-c, -a, -b);

		mesh.addVertex(+a, +c, +b);
		mesh.addVertex(+a, -c, -b);
		mesh.addVertex(-a, +c, -b);
		mesh.addVertex(-a, -c, +b);

		mesh.addVertex(+b, +a, +c);
		mesh.addVertex(-b, +a, -c);
		mesh.addVertex(-b, -a, +c);
		mesh.addVertex(+b, -a, -c);

		mesh.addVertex(+c, +b, +a);
		mesh.addVertex(-c, -b, +a);
		mesh.addVertex(+c, -b, -a);
		mesh.addVertex(-c, +b, -a);

		mesh.addFace(0, 6, 9);
		mesh.addFace(0, 9, 22);
		mesh.addFace(0, 17, 6);
		mesh.addFace(0, 22, 19);
		mesh.addFace(1, 4, 10);
		mesh.addFace(1, 10, 20);
		mesh.addFace(1, 18, 4);
		mesh.addFace(1, 20, 16);
		mesh.addFace(2, 5, 8);
		mesh.addFace(2, 8, 21);
		mesh.addFace(2, 16, 5);
		mesh.addFace(2, 21, 18);
		mesh.addFace(3, 7, 11);
		mesh.addFace(3, 11, 23);
		mesh.addFace(3, 19, 7);
		mesh.addFace(3, 23, 17);
		mesh.addFace(4, 13, 10);
		mesh.addFace(4, 18, 15);
		mesh.addFace(5, 14, 8);
		mesh.addFace(5, 16, 12);
		mesh.addFace(6, 12, 9);
		mesh.addFace(6, 17, 14);
		mesh.addFace(7, 15, 11);
		mesh.addFace(7, 19, 13);
		mesh.addFace(8, 14, 23);
		mesh.addFace(9, 12, 20);
		mesh.addFace(10, 13, 22);
		mesh.addFace(11, 15, 21);
		mesh.addFace(12, 16, 20);
		mesh.addFace(13, 19, 22);
		mesh.addFace(14, 17, 23);
		mesh.addFace(15, 18, 21);

		mesh.addFace(0, 19, 3, 17);
		mesh.addFace(1, 16, 2, 18);
		mesh.addFace(4, 15, 7, 13);
		mesh.addFace(5, 12, 6, 14);
		mesh.addFace(8, 23, 11, 21);
		mesh.addFace(9, 20, 10, 22);

		return mesh;
	}

}
