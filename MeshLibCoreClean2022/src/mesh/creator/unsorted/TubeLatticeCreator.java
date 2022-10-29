package mesh.creator.unsorted;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.HolesModifier;
import mesh.modifier.SolidifyModifier;

public class TubeLatticeCreator implements IMeshCreator {

	private int segments;
	private int vertices;
	private float outerRadius;
	private float innerRadius;
	private float height;
	private float scaleExtrude;
	private Mesh3D mesh;

	public TubeLatticeCreator() {
		super();
		this.segments = 10;
		this.vertices = 32;
		this.outerRadius = 1f;
		this.innerRadius = 0.9f;
		this.height = 2f;
		this.scaleExtrude = 0.5f;
	}

	public TubeLatticeCreator(int segments, int vertices, float outerRadius,
			float innerRadius, float height, float scaleExtrude, Mesh3D mesh) {
		super();
		this.segments = segments;
		this.vertices = vertices;
		this.outerRadius = outerRadius;
		this.innerRadius = innerRadius;
		this.height = height;
		this.scaleExtrude = scaleExtrude;
		this.mesh = mesh;
	}

	@Override
	public Mesh3D create() {
		mesh = new SegmentedTubeCreator(segments, vertices, outerRadius,
				outerRadius, height).create();
		new HolesModifier(scaleExtrude).modify(mesh);
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

	public float getScaleExtrude() {
		return scaleExtrude;
	}

	public void setScaleExtrude(float scaleExtrude) {
		this.scaleExtrude = scaleExtrude;
	}

}
