package mesh.creator.archimedian;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TruncatedTetrahedronCreator implements IMeshCreator {

    @Override
    public Mesh3D create() {
	Mesh3D mesh = new Mesh3D();

	float a = 1.0f;
	float b = 3.0f;

	mesh.addVertex(+a, +a, +b);
	mesh.addVertex(+a, -a, -b);
	mesh.addVertex(-a, -a, +b);
	mesh.addVertex(-a, +a, -b);

	mesh.addVertex(+a, +b, +a);
	mesh.addVertex(+a, -b, -a);
	mesh.addVertex(-a, -b, +a);
	mesh.addVertex(-a, +b, -a);

	mesh.addVertex(+b, +a, +a);
	mesh.addVertex(+b, -a, -a);
	mesh.addVertex(-b, -a, +a);
	mesh.addVertex(-b, +a, -a);

	mesh.addFace(0, 8, 4);
	mesh.addFace(1, 9, 5);
	mesh.addFace(2, 10, 6);
	mesh.addFace(3, 11, 7);

	mesh.addFace(0, 2, 6, 5, 9, 8);
	mesh.addFace(0, 4, 7, 11, 10, 2);
	mesh.addFace(1, 3, 7, 4, 8, 9);
	mesh.addFace(1, 5, 6, 10, 11, 3);

	return mesh;
    }

}
