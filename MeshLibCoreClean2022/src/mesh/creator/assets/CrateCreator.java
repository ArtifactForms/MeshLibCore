package mesh.creator.assets;

import java.util.List;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.wip.Mesh3DUtil;

public class CrateCreator implements IMeshCreator {

	private float radius;
	private float crossBeamRadius;
	private float inset;
	private float insetDepth;
	private CrossBeamType crossBeamType;
	private Mesh3D mesh;

	public CrateCreator() {
		this(1.0f, 0.2f, 0.1f);
	}

	public CrateCreator(float radius) {
		this(radius, radius * 0.1f, 0.1f);
	}

	public CrateCreator(float radius, float inset, float insetDepth) {
		this.radius = radius;
		this.inset = inset;
		this.insetDepth = insetDepth;
	}

	@Override
	public Mesh3D create() {
		updateCrossBeamRadius();
		createBaseCube();
		scaleBaseCube();
		extrudeFaces();
		createCrossBeams();
		snapToGround();
		return mesh;
	}

	private void updateCrossBeamRadius() {
		crossBeamRadius = radius - inset;
	}

	private void snapToGround() {
		mesh.translateY(-radius);
	}

	private void createBaseCube() {
		mesh = new CubeCreator(1).create();
	}

	private void scaleBaseCube() {
		mesh.scale(radius);
	}

	private void extrudeFaces() {
		List<Face3D> faces = mesh.getFaces(0, mesh.getFaceCount());
		for (Face3D face : faces) {
			Mesh3DUtil.extrudeFace(mesh, face, 1.0f - (inset / radius), 0.0f);
			Mesh3DUtil.extrudeFace(mesh, face, 1.0f, -insetDepth);
		}
	}

	private void createCrossBeams() {
		if (crossBeamType == CrossBeamType.NOTHING)
			return;
		createTopCrossBeam();
		createBottomCrossBeam();
		createFrontCrossBeam();
		createBackCrossBeam();
		createLeftCrossBeam();
		createRightCrossBeam();
	}

	private void createTopCrossBeam() {
		Mesh3D mesh = createCrossBeam();
		mesh.translateY(-this.radius);
		this.mesh.append(mesh);
	}

	private void createBottomCrossBeam() {
		Mesh3D mesh = createCrossBeam();
		mesh.rotateX(Mathf.PI);
		mesh.translateY(this.radius);
		this.mesh.append(mesh);
	}

	private void createFrontCrossBeam() {
		Mesh3D mesh = createCrossBeam();
		mesh.rotateX(-Mathf.HALF_PI);
		mesh.translateZ(this.radius);
		this.mesh.append(mesh);
	}

	private void createBackCrossBeam() {
		Mesh3D mesh = createCrossBeam();
		mesh.rotateX(Mathf.HALF_PI);
		mesh.translateZ(-this.radius);
		this.mesh.append(mesh);
	}

	private void createLeftCrossBeam() {
		Mesh3D mesh = createCrossBeam();
		mesh.rotateX(-Mathf.HALF_PI);
		mesh.rotateY(-Mathf.HALF_PI);
		mesh.translateX(-this.radius);
		this.mesh.append(mesh);
	}

	private void createRightCrossBeam() {
		Mesh3D mesh = createCrossBeam();
		mesh.rotateX(-Mathf.HALF_PI);
		mesh.rotateY(Mathf.HALF_PI);
		mesh.translateX(this.radius);
		this.mesh.append(mesh);
	}

	private Mesh3D createCrossBeam() {
		float inset = Mathf.sqrt(this.inset * this.inset * 0.5f);
		float radius = crossBeamRadius;
		Mesh3D mesh = new Mesh3D();

		mesh.addVertex(-radius, 0, -radius);
		mesh.addVertex(-radius + inset, 0, -radius);
		mesh.addVertex(radius, 0, radius - inset);
		mesh.addVertex(radius, 0, radius);
		mesh.addVertex(radius - inset, 0, radius);
		mesh.addVertex(-radius, 0, -radius + inset);

		mesh.addVertex(-radius + inset, insetDepth, -radius);
		mesh.addVertex(radius, insetDepth, radius - inset);
		mesh.addVertex(radius - inset, insetDepth, radius);
		mesh.addVertex(-radius, insetDepth, -radius + inset);

		mesh.addFace(0, 1, 2, 3);
		mesh.addFace(0, 3, 4, 5);

		mesh.addFace(2, 1, 6, 7);
		mesh.addFace(5, 4, 8, 9);

		if (crossBeamType == CrossBeamType.CROSS) {
			float a = radius;
			Mesh3D mesh1 = mesh.copy();

			mesh1.getVertexAt(2).subtractLocal(a, 0, a);
			mesh1.getVertexAt(3).subtractLocal(a + (inset / 2), 0, a + (inset / 2));
			mesh1.getVertexAt(4).subtractLocal(a, 0, a);
			mesh1.getVertexAt(7).subtractLocal(a, 0, a);
			mesh1.getVertexAt(8).subtractLocal(a, 0, a);
			mesh1.rotateY(Mathf.HALF_PI);

			Mesh3D mesh2 = mesh1.copy();
			mesh2.rotateY(Mathf.PI);

			mesh = Mesh3DUtil.append(mesh, mesh1, mesh2);
		}

		return mesh;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getInset() {
		return inset;
	}

	public void setInset(float inset) {
		this.inset = inset;
	}

	public float getInsetDepth() {
		return insetDepth;
	}

	public void setInsetDepth(float insetDepth) {
		this.insetDepth = insetDepth;
	}

	public CrossBeamType getCroosBeamType() {
		return crossBeamType;
	}

	public void setCroosBeamType(CrossBeamType croosBeamType) {
		this.crossBeamType = croosBeamType;
	}

}
