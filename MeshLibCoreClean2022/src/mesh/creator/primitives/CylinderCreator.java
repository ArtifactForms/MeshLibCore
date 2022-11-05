package mesh.creator.primitives;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.util.Mesh3DUtil;

public class CylinderCreator implements IMeshCreator {

	private int vertices;
	private float topRadius;
	private float bottomRadius;
	private float height;
	private FillType topCapFillType;
	private FillType bottomCapFillType;
	private Mesh3D mesh;

	public CylinderCreator() {
		this(32, 1, 1, 2, FillType.N_GON, FillType.N_GON);
	}
	
	public CylinderCreator(int vertices, float radius, float height, FillType fillType) {
		this(vertices, radius, radius, height, fillType, fillType);
	}
	
	public CylinderCreator(int vertices, float radius, float height) {
		this(vertices, radius, radius, height, FillType.N_GON, FillType.N_GON);
	}

	public CylinderCreator(int vertices, float topRadius, float bottomRadius, float height, FillType topCapFillType,
			FillType bottomCapFillType) {
		this.vertices = vertices;
		this.topRadius = topRadius;
		this.bottomRadius = bottomRadius;
		this.height = height;
		this.topCapFillType = topCapFillType;
		this.bottomCapFillType = bottomCapFillType;
	}

	private void flipDirectionOfFaces(Mesh3D mesh) {
		for (Face3D face : mesh.faces) {
			Mesh3DUtil.flipDirection(mesh, face);
		}
	}

	private void bridge(Mesh3D m0, Mesh3D m1) {
		for (int i = 0; i < vertices; i++) {
			Mesh3DUtil.bridge(mesh, m1.getVertexAt(i), m1.getVertexAt((i + 1) % vertices), m0.getVertexAt(i),
					m0.getVertexAt((i + 1) % vertices));
		}
	}

	private Mesh3D createTopCap() {
		return new CircleCreator(vertices, topRadius, -height / 2f, topCapFillType).create();
	}

	private Mesh3D createBottomCap() {
		Mesh3D mesh = new CircleCreator(vertices, bottomRadius, height / 2f, bottomCapFillType).create();
		flipDirectionOfFaces(mesh);
		return mesh;
	}

	@Override
	public Mesh3D create() {
		Mesh3D topCap = createTopCap();
		Mesh3D bottomCap = createBottomCap();
		mesh = new Mesh3D();
		mesh.append(topCap, bottomCap);
		bridge(topCap, bottomCap);
		return mesh;
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

	public FillType getTopCapFillType() {
		return topCapFillType;
	}

	public void setTopCapFillType(FillType topCapFillType) {
		this.topCapFillType = topCapFillType;
	}

	public FillType getBottomCapFillType() {
		return bottomCapFillType;
	}

	public void setBottomCapFillType(FillType bottomCapFillType) {
		this.bottomCapFillType = bottomCapFillType;
	}

}
