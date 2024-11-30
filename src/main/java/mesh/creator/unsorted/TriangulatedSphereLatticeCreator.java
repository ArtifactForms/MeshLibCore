package mesh.creator.unsorted;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.FlipFacesModifier;
import mesh.modifier.HolesModifier;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.SpherifyModifier;
import mesh.modifier.subdivision.PlanarMidEdgeCenterModifier;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;

public class TriangulatedSphereLatticeCreator implements IMeshCreator {

	private int tessellations;

	private float radius;

	private float thickness;

	private float scaleExtrude;

	private Mesh3D mesh;

	public TriangulatedSphereLatticeCreator() {
		tessellations = 3;
		radius = 3f;
		thickness = 0.05f;
		scaleExtrude = 0.8f;
	}

	private void tessellate() {
		for (int i = 0; i < tessellations; i++)
			new PlanarMidEdgeCenterModifier().modify(mesh);
		new PlanarVertexCenterModifier().modify(mesh);
	}

	private void pushToSphere() {
		mesh.apply(new SpherifyModifier(radius));
	}

	private void createHoles() {
		new HolesModifier(scaleExtrude).modify(mesh);
	}

	private void solidify() {
		new SolidifyModifier(thickness).modify(mesh);
	}

	private void flipFaceNormals() {
		mesh.apply(new FlipFacesModifier());
	}

	private void initializeMesh() {
		mesh = new CubeCreator().create();
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		tessellate();
		pushToSphere();
		createHoles();
		flipFaceNormals();
		solidify();
		return mesh;
	}

}
