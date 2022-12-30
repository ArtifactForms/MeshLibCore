package mesh.creator.special;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;

public class DiamondCreator implements IMeshCreator {

	private int segments;
	private float girdleRadius;
	private float tableRadius;
	private float crownHeight;
	private float pavillionHeight;
	private Mesh3D mesh;

	public DiamondCreator() {
		segments = 32;
		girdleRadius = 1;
		tableRadius = 0.6f;
		crownHeight = 0.35f;
		pavillionHeight = 0.8f;
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		return mesh;
	}

	private void createVertices() {
		createGirdleVertices();
		createTableVertices();
		createPavillionVertex();
	}

	private void createFaces() {
		createTriangles();
		createQuads();
		createTableFace();
	}

	private void createQuads() {
		for (int i = 0; i < segments; i++) {
			int index0 = i;
			int index1 = (i + 1) % segments;
			int index2 = segments + index1;
			int index3 = segments + index0;
			addFace(index0, index1, index2, index3);
		}
	}

	private void createTriangles() {
		int pavillionIndex = segments + segments;
		for (int i = 0; i < segments; i++)
			addFace(i, pavillionIndex, (i + 1) % segments);
	}

	private void createTableFace() {
		int[] indices = new int[segments];
		for (int i = 0; i < segments; i++)
			indices[i] = segments + i;
		addFace(indices);
	}

	private void createPavillionVertex() {
		mesh.addVertex(0, pavillionHeight, 0);
	}

	private void createTableVertices() {
		CircleCreator creator = new CircleCreator();
		creator.setRadius(tableRadius);
		creator.setCenterY(-crownHeight);
		creator.setVertices(segments);
		mesh.addVertices(creator.create().getVertices());
	}

	private void createGirdleVertices() {
		CircleCreator creator = new CircleCreator();
		creator.setRadius(girdleRadius);
		creator.setVertices(segments);
		mesh.addVertices(creator.create().getVertices());
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void addFace(int... indices) {
		mesh.addFace(indices);
	}

	public int getSegments() {
		return segments;
	}

	public void setSegments(int segments) {
		this.segments = segments;
	}

	public float getGirdleRadius() {
		return girdleRadius;
	}

	public void setGirdleRadius(float girdleRadius) {
		this.girdleRadius = girdleRadius;
	}

	public float getTableRadius() {
		return tableRadius;
	}

	public void setTableRadius(float tableRadius) {
		this.tableRadius = tableRadius;
	}

	public float getCrownHeight() {
		return crownHeight;
	}

	public void setCrownHeight(float crownHeight) {
		this.crownHeight = crownHeight;
	}

	public float getPavillionHeight() {
		return pavillionHeight;
	}

	public void setPavillionHeight(float pavillionHeight) {
		this.pavillionHeight = pavillionHeight;
	}

}
