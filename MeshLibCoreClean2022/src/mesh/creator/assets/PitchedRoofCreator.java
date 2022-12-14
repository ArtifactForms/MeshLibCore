package mesh.creator.assets;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.subdivision.QuadsToTrianglesModifier;

public class PitchedRoofCreator implements IMeshCreator {

	private float width;
	private float height;
	private float depth;
	private boolean triangulate;
	private boolean capBottom;
	private boolean snapToGround;
	private Mesh3D mesh;
	
	public PitchedRoofCreator() {
		width = 4;
		height = 2;
		depth = 6;
	}
	
	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		triangulate();
		snapToGround();
		return mesh;
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}
	
	private void createVertices() {
		createBottomVertices();
		createTopVertices();
	}
	
	private void createTopVertices() {
		float halfHeight = height / 2.0f;
		float halfDepth = depth / 2.0f;
		addVertex(0, -halfHeight, -halfDepth);
		addVertex(0, -halfHeight, +halfDepth);
	}
	
	private void createBottomVertices() {
		float halfWidth = width / 2.0f;
		float halfHeight = height / 2.0f;
		float halfDepth = depth / 2.0f;
		addVertex(-halfWidth, +halfHeight, -halfDepth);
		addVertex(+halfWidth, +halfHeight, -halfDepth);
		addVertex(+halfWidth, +halfHeight, +halfDepth);
		addVertex(-halfWidth, +halfHeight, +halfDepth);
	}
	
	private void createFaces() {
		createEastFace();
		createWestFace();
		createNorthFace();
		createSouthFace();
		createBottomFace();
	}
	
	private void createEastFace() {
		addFace(5, 4, 1, 2);
	}
	
	private void createWestFace() {
		addFace(4, 5, 3, 0);
	}
	
	private void createNorthFace() {
		addFace(5, 2, 3);
	}
	
	private void createSouthFace() {
		addFace(4, 0, 1);
	}
	
	private void createBottomFace() {
		if (isCapBottom())
			addFace(3, 2, 1, 0);
	}
	
	private void triangulate() {
		if (isTriangulate())
			new QuadsToTrianglesModifier().modify(mesh);
	}
	
	private void snapToGround() {
		if (isSnapToGround())
			mesh.translateY(-height / 2.0f);
	}
	
	private void addVertex(float x, float y, float z) {
		mesh.addVertex(x, y, z);
	}
	
	private void addFace(int... indices) {
		mesh.addFace(indices);
	}
	
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getDepth() {
		return depth;
	}

	public void setDepth(float depth) {
		this.depth = depth;
	}

	public boolean isTriangulate() {
		return triangulate;
	}

	public void setTriangulate(boolean triangulate) {
		this.triangulate = triangulate;
	}

	public boolean isCapBottom() {
		return capBottom;
	}

	public void setCapBottom(boolean capBottom) {
		this.capBottom = capBottom;
	}

	public boolean isSnapToGround() {
		return snapToGround;
	}

	public void setSnapToGround(boolean snapToGround) {
		this.snapToGround = snapToGround;
	}

}
