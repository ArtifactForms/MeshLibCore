package mesh.creator.unsorted;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.deform.SpherifyModifier;
import mesh.modifier.subdivision.PlanarMidEdgeCenterModifier;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;
import mesh.modifier.topology.FlipFacesModifier;
import mesh.modifier.topology.HolesModifier;
import mesh.modifier.topology.SolidifyModifier;

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
		new SpherifyModifier(radius).modify(mesh);
	}

	private void createHoles() {
		new HolesModifier(scaleExtrude).modify(mesh);
	}

	private void solidify() {
		new SolidifyModifier(thickness).modify(mesh);
	}

	private void flipFaceNormals() {
		new FlipFacesModifier().modify(mesh);
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
