package mesh.creator.unsorted;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.HolesModifier;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.subdivision.PlanarMidEdgeCenterModifier;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;
import mesh.util.Mesh3DUtil;

public class TriangulatedSphereLatticeCreator implements IMeshCreator {

	private int tessellations;
	private float radius;
	private float thickness;
	private float scaleExtrude;
	private Mesh3D mesh;
	
	public TriangulatedSphereLatticeCreator() {
		super();
		this.tessellations = 3;
		this.radius = 3f;
		this.thickness = 0.05f;
		this.scaleExtrude = 0.8f;
	}
	
	private void tessellate() {
		for (int i = 0; i < tessellations; i++)
			new PlanarMidEdgeCenterModifier().modify(mesh);
		new PlanarVertexCenterModifier().modify(mesh);
	}
	
	private void pushToSphere() {
		Mesh3DUtil.pushToSphere(mesh, radius);
	}
	
	private void createHoles() {
		new HolesModifier(scaleExtrude).modify(mesh);
	}
	
	private void solidify() {
		Mesh3DUtil.flipDirection(mesh);
		new SolidifyModifier(thickness).modify(mesh);
	}
	
	@Override
	public Mesh3D create() {
		mesh = new CubeCreator().create();
		tessellate();
		pushToSphere();
		createHoles();
		solidify();
		return mesh;
	}

}
