package mesh.creator.primitives;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.SolidifyModifier;

public class SegmentedTubeCreator implements IMeshCreator {

	private int segments;
	private int vertices;
	private float outerRadius;
	private float innerRadius;
	private float height;
	private Mesh3D mesh;

	public SegmentedTubeCreator() {
		this(2, 32, 1, 0.5f, 2);
	}
	
	public SegmentedTubeCreator(int segments, int vertices, float outerRadius,
			float innerRadius, float height) {
		this.segments = segments;
		this.vertices = vertices;
		this.outerRadius = outerRadius;
		this.innerRadius = innerRadius;
		this.height = height;
	}
	
	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createQuadFaces();
		centerOnAxisY();
		solidify();
		return mesh;
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}
	
	private void centerOnAxisY() {
		mesh.translateY(-height / 2);
	}
	
	private void solidify() {
		if (outerRadius == innerRadius)
			return;
		new SolidifyModifier(outerRadius - innerRadius).modify(mesh);
	}

	private void createVertices() {
		float segmentHeight = height / (float) segments;
		for (int i = 0; i <= segments; i++) {
			Mesh3D mesh = new CircleCreator(vertices, outerRadius).create();
			mesh.translateY(i * segmentHeight);
			this.mesh.append(mesh);
		}
	}
	
	private void createQuadFaces() {
		for (int i = 0; i < segments - 1; i++) {
			for (int j = 0; j < vertices; j++) {
				addFace(i, j);
			}
		}
	}
	
	private void addFace(int i, int j)  {
		int idx0 = Mathf.toOneDimensionalIndex(i, j, vertices);
		int idx1 = Mathf.toOneDimensionalIndex(i + 1, j, vertices);
		int idx2 = Mathf.toOneDimensionalIndex(i + 1, (j + 1) % vertices, vertices);
		int idx3 = Mathf.toOneDimensionalIndex(i, (j + 1) % vertices, vertices);
		mesh.addFace(idx0, idx1, idx2, idx3);
	}

	public int getSegments() {
		return segments;
	}

	public void setSegments(int segments) {
		this.segments = segments;
	}

	public int getVertices() {
		return vertices;
	}

	public void setVertices(int vertices) {
		this.vertices = vertices;
	}

	public float getOuterRadius() {
		return outerRadius;
	}

	public void setOuterRadius(float outerRadius) {
		this.outerRadius = outerRadius;
	}

	public float getInnerRadius() {
		return innerRadius;
	}

	public void setInnerRadius(float innerRadius) {
		this.innerRadius = innerRadius;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

}