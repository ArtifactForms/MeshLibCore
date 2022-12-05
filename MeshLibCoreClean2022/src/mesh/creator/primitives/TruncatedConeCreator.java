package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;

public class TruncatedConeCreator implements IMeshCreator {

	private int vertices;
	private float topRadius;
	private float bottomRadius;
	private float height;

	public TruncatedConeCreator() {
		this.vertices = 32;
		this.topRadius = 0.5f;
		this.bottomRadius = 1f;
		this.height = 2f;
	}

	public TruncatedConeCreator(int vertices, float topRadius, float bottomRadius, float height) {
		this.vertices = vertices;
		this.topRadius = topRadius;
		this.bottomRadius = bottomRadius;
		this.height = height;
	}

	@Override
	public Mesh3D create() {
		CylinderCreator creator = new CylinderCreator();
		creator.setVertices(vertices);
		creator.setTopRadius(topRadius);
		creator.setBottomRadius(bottomRadius);
		creator.setHeight(height);
		creator.setTopCapFillType(FillType.N_GON);
		creator.setBottomCapFillType(FillType.N_GON);
		return creator.create();
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

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

}
