package mesh.creator.unsorted;

import java.util.ArrayList;
import java.util.List;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.modifier.SolidifyModifier;
import mesh.util.Mesh3DUtil;

public class SegmentedTubeCreator implements IMeshCreator {

	private int segments;
	private int vertices;
	private float outerRadius;
	private float innerRadius;
	private float height;
	private Mesh3D mesh;

	public SegmentedTubeCreator() {
		super();
		this.segments = 3;
		this.vertices = 32;
		this.outerRadius = 1f;
		this.innerRadius = 0.5f;
		this.height = 2f;
	}
	
	public SegmentedTubeCreator(int segments, int vertices, float outerRadius,
			float innerRadius, float height) {
		super();
		this.segments = segments;
		this.vertices = vertices;
		this.outerRadius = outerRadius;
		this.innerRadius = innerRadius;
		this.height = height;
	}

	private void createCircles(List<Mesh3D> meshes) {
		float segmentHeight = height / segments;
		for (int i = 0; i <= segments; i++) {
			Mesh3D mesh = new CircleCreator(vertices, outerRadius).create();
			mesh.translateY(i * segmentHeight);
			meshes.add(mesh);
		}
	}
	
	private void append(List<Mesh3D> meshes) {
		for (Mesh3D mesh : meshes) {
			this.mesh.append(mesh);
		}
	}

	private void bridge(List<Mesh3D> meshes) {
		for (int i = 0; i < meshes.size() - 1; i++) {
			bridge(meshes.get(i), meshes.get(i + 1));
		}
	}

	private void bridge(Mesh3D m0, Mesh3D m1) {
		for (int i = 0; i < vertices; i++) {
			Mesh3DUtil.bridge(mesh, m1.getVertexAt(i),
					m1.getVertexAt((i + 1) % vertices), m0.getVertexAt(i),
					m0.getVertexAt((i + 1) % vertices));
		}
	}

	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		List<Mesh3D> meshes = new ArrayList<Mesh3D>();
		createCircles(meshes);
		append(meshes);
		bridge(meshes);
		mesh.translateY(-height / 2);
		if (outerRadius != innerRadius)
			new SolidifyModifier(outerRadius - innerRadius).modify(mesh);
		return mesh;
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
