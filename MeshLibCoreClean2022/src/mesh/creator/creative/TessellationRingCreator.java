package mesh.creator.creative;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.modifier.HolesModifier;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.subdivision.PlanarMidEdgeCenterModifier;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;
import mesh.util.Mesh3DUtil;

public class TessellationRingCreator implements IMeshCreator {

	private int vertices;
	
	private float topRadius;
	
	private float bottomRadius;
	
	private float thickness;
	
	private float extrudeScale;
	
	private Mesh3D topCircle;
	
	private Mesh3D bottomCircle;
	
	private Mesh3D mesh;

	public TessellationRingCreator() {
		vertices = 12;
		topRadius = 1f;
		bottomRadius = 1f;
		thickness = 0.1f;
		extrudeScale = 0.6f;
	}

	private void createFaces() {
		int n = vertices;
		for (int i = 0; i < n; i++) {
			Vector3f v0 = bottomCircle.getVertexAt(i % n);
			Vector3f v1 = bottomCircle.getVertexAt((i + 1) % n);
			Vector3f v3 = topCircle.getVertexAt((i + 1) % n);
			Vector3f v2 = topCircle.getVertexAt(i % n);
			Mesh3DUtil.bridge(mesh, v0, v1, v2, v3);
		}
	}

	private void createVertices() {
		topCircle = new CircleCreator(vertices, topRadius).create();
		bottomCircle = new CircleCreator(vertices, bottomRadius).create();
		topCircle.translateY(-0.25f);
		bottomCircle.translateY(0.25f);
		mesh.append(topCircle, bottomCircle);
	}

	private void tessellate() {
		new PlanarVertexCenterModifier().modify(mesh);
		new PlanarMidEdgeCenterModifier().modify(mesh);
	}

	private void createHoles() {
		new HolesModifier(extrudeScale).modify(mesh);
	}

	private void solidify() {
		Mesh3DUtil.flipDirection(mesh);
		new SolidifyModifier(thickness).modify(mesh);
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		tessellate();
		createHoles();
		solidify();
		return mesh;
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	public int getVertices() {
		return vertices;
	}

	public void setVertices(int vertices) {
		this.vertices = vertices;
	}

	public float getTopRadius() {
		return topRadius;
	}

	public void setTopRadius(float topRadius) {
		this.topRadius = topRadius;
	}

	public float getBottomRadius() {
		return bottomRadius;
	}

	public void setBottomRadius(float bottomRadius) {
		this.bottomRadius = bottomRadius;
	}

	public float getThickness() {
		return thickness;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
	}

	public float getExtrudeScale() {
		return extrudeScale;
	}

	public void setExtrudeScale(float extrudeScale) {
		this.extrudeScale = extrudeScale;
	}

}
