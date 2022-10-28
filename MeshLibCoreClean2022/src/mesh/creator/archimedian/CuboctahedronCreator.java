package mesh.creator.archimedian;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class CuboctahedronCreator implements IMeshCreator {

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new Mesh3D();
		
		float a = 1;
		
		mesh.addVertex(0, -a, a);
		mesh.addVertex(a, 0, a);
		mesh.addVertex(0, a, a);
		mesh.addVertex(-a, 0, a);
		mesh.addVertex(a, -a, 0);
		mesh.addVertex(a, a, 0);
		mesh.addVertex(-a, a, 0);
		mesh.addVertex(-a, -a, 0);
		mesh.addVertex(0, -a, -a);
		mesh.addVertex(a, 0, -a);
		mesh.addVertex(0, a, -a);
		mesh.addVertex(-a, 0, -a);

		mesh.addFace(0, 1, 2, 3);
		mesh.addFace(0, 4, 1);
		mesh.addFace(1, 5, 2);
		mesh.addFace(2, 6, 3);
		mesh.addFace(3, 7, 0);
		mesh.addFace(4, 9, 5, 1);
		mesh.addFace(2, 5, 10, 6);
		mesh.addFace(3, 6, 11, 7);
		mesh.addFace(0, 7, 8, 4);
		mesh.addFace(4, 8, 9);
		mesh.addFace(5, 9, 10);
		mesh.addFace(6, 10, 11);
		mesh.addFace(7, 11, 8);
		mesh.addFace(8, 11, 10, 9);
		
		return mesh;
	}

}
