package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.wip.Mesh3DUtil;

public class QuadSphereCreator implements IMeshCreator {

	private float radius;
	private int subdivisions;
	private Mesh3D mesh;

	public QuadSphereCreator() {
		this(1, 3);
	}

	public QuadSphereCreator(float radius, int subdivisions) {
		this.radius = radius;
		this.subdivisions = subdivisions;
	}

	@Override
	public Mesh3D create() {
		createCube();
		subdivide();
		scale();
		pushToSphere();
		return mesh;
	}

	private void createCube() {
		mesh = new CubeCreator().create();
	}

	private void subdivide() {
		for (int i = 0; i < subdivisions; i++)
			Mesh3DUtil.subdivide(mesh);
	}

	private void scale() {
		Mesh3DUtil.scale(mesh, radius);
	}

	private void pushToSphere() {
		Mesh3DUtil.pushToSphere(mesh, radius);
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
