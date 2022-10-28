package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.modifier.subdivision.OneToFourTriangleSplitModifier;
import mesh.wip.Mesh3DUtil;

public class IcoSphereCreator implements IMeshCreator {

	private float radius;
	private int subdivisions;

	public IcoSphereCreator() {
		this.radius = 1f;
		this.subdivisions = 0;
	}

	public IcoSphereCreator(float radius, int subdivisions) {
		this.radius = radius;
		this.subdivisions = subdivisions;
	}

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new IcosahedronCreator().create();
		OneToFourTriangleSplitModifier modifier = new OneToFourTriangleSplitModifier();
		for (int i = 0; i < subdivisions; i++) {
			modifier.modify(mesh);
		}
		Mesh3DUtil.pushToSphere(mesh, radius);
		return mesh;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

}
