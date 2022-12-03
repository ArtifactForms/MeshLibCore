package mesh.creator.primitives;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.modifier.SolidifyModifier;

public class TubeCreator implements IMeshCreator {

	private int vertices;
	private float topOuterRadius;
	private float topInnerRadius;
	private float bottomOuterRadius;
	private float bottomInnerRadius;
	private float height;
	private Mesh3D mesh;

	public TubeCreator() {
		vertices = 32;
		topOuterRadius = 1.0f;
		topInnerRadius = 0.5f;
		bottomOuterRadius = 1.0f;
		bottomInnerRadius = 0.5f;
		height = 2.0f;
	}
	
	@Override
	public Mesh3D create() {
		createBaseCylinder();
		solidifyBaseCylinder();
		transform();
		return mesh;
	}
	
	private void createBaseCylinder() {
		mesh = createCylinderCreator().create();
	}
	
	private CylinderCreator createCylinderCreator() {
		CylinderCreator creator = new CylinderCreator();
		creator.setVertices(vertices);
		creator.setHeight(height);
		creator.setTopCapFillType(FillType.NOTHING);
		creator.setBottomCapFillType(FillType.NOTHING);
		return creator;
	}
	
	private void solidifyBaseCylinder() {
		new SolidifyModifier().modify(mesh);
	}
	
	private void transform() {
		transformTopOuterVertices();
		transformTopInnerVertices();
		transformBottomOuterVertices();
		transformBottomInnerVertices();
	}

	private void transformVertices(int num, float radius, float originY) {
		int startIndex = num * vertices;
		int endIndex = startIndex + vertices;
		
		for (int i = startIndex; i < endIndex; i++) {
			Vector3f v = mesh.getVertexAt(i);
			Vector3f v0 = new Vector3f(v.x, v.y -originY, v.z).normalizeLocal();
			v.set(v0.mult(radius).add(0, v.y, 0));
		}
	}

	private void transformTopOuterVertices() {
		transformVertices(0, topOuterRadius, -height / 2.0f);
	}

	private void transformTopInnerVertices() {
		transformVertices(2, topInnerRadius, -height / 2.0f);
	}

	private void transformBottomOuterVertices() {
		transformVertices(1, bottomOuterRadius, height / 2.0f);
	}

	private void transformBottomInnerVertices() {
		transformVertices(3, bottomInnerRadius, height / 2.0f);
	}

	public int getVertices() {
		return vertices;
	}

	public void setVertices(int vertices) {
		this.vertices = vertices;
	}

	public float getTopOuterRadius() {
		return topOuterRadius;
	}

	public void setTopOuterRadius(float topOuterRadius) {
		this.topOuterRadius = topOuterRadius;
	}

	public float getTopInnerRadius() {
		return topInnerRadius;
	}

	public void setTopInnerRadius(float topInnerRadius) {
		this.topInnerRadius = topInnerRadius;
	}

	public float getBottomOuterRadius() {
		return bottomOuterRadius;
	}

	public void setBottomOuterRadius(float bottomOuterRadius) {
		this.bottomOuterRadius = bottomOuterRadius;
	}

	public float getBottomInnerRadius() {
		return bottomInnerRadius;
	}

	public void setBottomInnerRadius(float bottomInnerRadius) {
		this.bottomInnerRadius = bottomInnerRadius;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

}
