package mesh.creator.primitives;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class UVSphereCreator implements IMeshCreator {

	private int rings;
	private int segments;
	private float radius;
	private Mesh3D mesh;

	public UVSphereCreator() {
		this(16, 32, 1);
	}

	public UVSphereCreator(int rings, int segments, float radius) {
		this.rings = rings;
		this.segments = segments;
		this.radius = radius;
	}

	@Override
	public Mesh3D create() {
		if (rings == 0 && segments == 0)
			return new Mesh3D();

		initializeMesh();
		createVertices();
		createFaces();

		return mesh;
	}

	private void createVertices() {
		float stepTheta = Mathf.PI / (float) rings;
		float stepPhi = Mathf.TWO_PI / (float) segments;
		for (int row = 1; row < rings; row++) {
			float theta = row * stepTheta;
			for (int col = 0; col < segments; col++) {
				float phi = col * stepPhi;
				float x = radius * Mathf.cos(phi) * Mathf.sin(theta);
				float y = radius * Mathf.cos(theta);
				float z = radius * Mathf.sin(phi) * Mathf.sin(theta);
				addVertex(x, y, z);
			}
		}
		addTopCenterVertex();
		addBottomCenterVertex();
	}

	private void addTopCenterVertex() {
		addVertex(0, -radius, 0);
	}

	private void addBottomCenterVertex() {
		addVertex(0, radius, 0);
	}

	private void createFaces() {
		for (int row = 0; row < rings - 2; row++)
			for (int col = 0; col < segments; col++)
				createFaceAt(row, col);
	}

	private void createFaceAt(int row, int col) {
		int a = getIndex(row, (col + 1) % segments);
		int b = getIndex(row + 1, (col + 1) % segments);
		int c = getIndex(row + 1, col);
		int d = getIndex(row, col);
		mesh.add(new Face3D(a, b, c, d));
		if (row == 0)
			mesh.add(new Face3D(d, mesh.vertices.size() - 1, a));
		if (row == rings - 3)
			mesh.add(new Face3D(c, b, mesh.vertices.size() - 2));
	}

	private int getIndex(int row, int col) {
		int idx = segments * row + col;
		return idx % mesh.vertices.size();
	}

	private void addVertex(float x, float y, float z) {
		mesh.addVertex(x, y, z);
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	public int getRings() {
		return rings;
	}

	public void setRings(int rings) {
		this.rings = rings;
	}

	public int getSegments() {
		return segments;
	}

	public void setSegments(int segments) {
		this.segments = segments;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float size) {
		this.radius = size;
	}

}
