package mesh.creator.archimedian;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TruncatedOctahedronCreator implements IMeshCreator {

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new Mesh3D();

		float a = 1.0f;
		float b = 0.0f;
		float c = 2.0f;

		mesh.addVertex(+b, +a, +c);
		mesh.addVertex(+b, +a, -c);
		mesh.addVertex(+b, -a, +c);
		mesh.addVertex(+b, -a, -c);

		mesh.addVertex(+b, +c, +a);
		mesh.addVertex(+b, -c, +a);
		mesh.addVertex(+b, +c, -a);
		mesh.addVertex(+b, -c, -a);

		mesh.addVertex(+a, +b, +c);
		mesh.addVertex(+a, +b, -c);
		mesh.addVertex(-a, +b, +c);
		mesh.addVertex(-a, +b, -c);

		mesh.addVertex(+a, +c, +b);
		mesh.addVertex(+a, -c, +b);
		mesh.addVertex(-a, +c, +b);
		mesh.addVertex(-a, -c, +b);

		mesh.addVertex(+c, +b, +a);
		mesh.addVertex(-c, +b, +a);
		mesh.addVertex(+c, +b, -a);
		mesh.addVertex(-c, +b, -a);

		mesh.addVertex(+c, +a, +b);
		mesh.addVertex(-c, +a, +b);
		mesh.addVertex(+c, -a, +b);
		mesh.addVertex(-c, -a, +b);

		mesh.addFace(0, 10, 2, 8);
		mesh.addFace(1, 9, 3, 11);
		mesh.addFace(4, 12, 6, 14);
		mesh.addFace(5, 15, 7, 13);
		mesh.addFace(16, 22, 18, 20);
		mesh.addFace(17, 21, 19, 23);

		mesh.addFace(0, 4, 14, 21, 17, 10);
		mesh.addFace(0, 8, 16, 20, 12, 4);
		mesh.addFace(1, 6, 12, 20, 18, 9);
		mesh.addFace(1, 11, 19, 21, 14, 6);
		mesh.addFace(2, 5, 13, 22, 16, 8);
		mesh.addFace(2, 10, 17, 23, 15, 5);
		mesh.addFace(3, 7, 15, 23, 19, 11);
		mesh.addFace(3, 9, 18, 22, 13, 7);

		return mesh;
	}

}
