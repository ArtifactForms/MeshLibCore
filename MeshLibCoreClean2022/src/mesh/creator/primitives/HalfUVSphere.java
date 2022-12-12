package mesh.creator.primitives;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.modifier.subdivision.PlanarVertexCenterModifier;

public class HalfUVSphere implements IMeshCreator {

	private int rings;
	private int segments;
	private float radius;
	private Mesh3D mesh;
	private FillType fillType;

	public HalfUVSphere() {
		this(16, 32, 1);
	}

	private HalfUVSphere(int rings, int segments, float radius) {
		this.rings = rings * 2;
		this.segments = segments;
		this.radius = radius;
		this.fillType = FillType.N_GON;
	}

	private void createVertices() {
		float stepTheta = Mathf.PI / (float) rings;
		float stepPhi = Mathf.TWO_PI / (float) segments;
		for (int row = 1; row < rings / 2 + 1; row++) {
			float theta = row * stepTheta;
			for (int col = 0; col < segments; col++) {
				float phi = col * stepPhi;
				float x = radius * Mathf.cos(phi) * Mathf.sin(theta);
				float y = radius * Mathf.cos(theta);
				float z = radius * Mathf.sin(phi) * Mathf.sin(theta);
				mesh.addVertex(x, y, z);
			}
		}
		mesh.add(new Vector3f(0, radius, 0));
	}

	private int getIndex(int row, int col) {
		int idx = segments * row + col;
		return idx % mesh.vertices.size();
	}

	private void createFaces() {
		for (int row = 0; row < (rings - 2) / 2; row++) {
			for (int col = 0; col < segments; col++) {
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
		}
	}

	@Override
	public Mesh3D create() {
		initializeMesh();

		if (!shouldCreate())
			return mesh;

		createVertices();
		createFaces();
		createCap();
		translate();

		return mesh;
	}

	private void createCap() {
		switch (fillType) {
		case NOTHING:
			break;
		case N_GON:
			capNGon();
			break;
		case TRIANGLE_FAN:
			capNGon();
			splitCapIntoTriangleFan();
			break;
		}
	}
	
	private void splitCapIntoTriangleFan() {
		Face3D faceToSplit =  mesh.getFaceAt(mesh.faces.size() - 1);
		new PlanarVertexCenterModifier().modify(mesh, faceToSplit);
	}

	private void capNGon() {
		int[] indices = new int[segments];
		for (int i = 0; i < segments; i++) {
			indices[i] = (mesh.getFaceCount() - (segments)) + i;
		}
		mesh.addFace(indices);
	}

	private void translate() {
		mesh.translateY(-radius / 2f);
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private boolean shouldCreate() {
		return (rings != 0 && segments != 0);
	}

	public int getRings() {
		return rings;
	}

	public void setRings(int rings) {
		this.rings = rings * 2;
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

	public FillType getFillType() {
		return fillType;
	}

	public void setFillType(FillType fillType) {
		this.fillType = fillType;
	}

}
