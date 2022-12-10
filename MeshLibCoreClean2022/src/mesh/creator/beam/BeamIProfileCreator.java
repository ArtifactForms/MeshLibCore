package mesh.creator.beam;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.SolidifyModifier;

public class BeamIProfileCreator implements IBeamCreator {
	
	private float width;
	private float height;
	private float depth;
	private float thickness;
	private float taper;
	private Mesh3D mesh;
	
	public BeamIProfileCreator() {
		height = 0.85f;
		width = 0.5f;
		depth = 2.0f;
		thickness = 0.1f;
		taper = 0.0f;
	}
	
	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		solidify();
		center();
		return mesh;
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}
	
	private void createVertices() {
		float halfWidth = width / 2f;
		float halfThickness = thickness / 2f;
		float thicknessTaper = thickness * (1f - taper);
		
		addVertex(-halfThickness, -thickness, 0);
		addVertex(+halfThickness, -thickness, 0);
		addVertex(-halfThickness, -height + thickness, 0);
		addVertex(+halfThickness, -height + thickness, 0);
		addVertex(-halfThickness, -height, 0);
		addVertex(+halfThickness, -height, 0);
		addVertex(-halfWidth, -height + thicknessTaper, 0);
		addVertex(+halfWidth, -height + thicknessTaper, 0);
		addVertex(-halfWidth, -height, 0);
		addVertex(+halfWidth, -height, 0);
		addVertex(-thicknessTaper / 2f, 0, 0);
		addVertex(+thicknessTaper / 2, 0, 0);
		addVertex(-halfWidth, 0, 0);
		addVertex(+halfWidth, 0, 0);
		addVertex(-halfWidth, -thicknessTaper, 0);
		addVertex(+halfWidth, -thicknessTaper, 0);
	}
	
	private void createFaces() {
		addFace(0, 2, 3, 1);
		addFace(2, 4, 5, 3);
		addFace(6, 8, 4, 2);
		addFace(3, 5, 9, 7);
		addFace(10, 0, 1, 11);
		addFace(12, 14, 0, 10);
		addFace(11, 1, 15, 13);
	}
	
	private void addVertex(float x, float y, float z) {
		mesh.addVertex(x, y, z);
	}
	
	private void addFace(int... indices) {
		mesh.add(new Face3D(indices));
	}
	
	private void solidify() {
		new SolidifyModifier(depth).modify(mesh);
	}
	
	private void center() {
		mesh.translate(0, height / 2f, depth / 2f);
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

	public float getThickness() {
		return thickness;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
	}

	public float getTaper() {
		return taper;
	}

	public void setTaper(float taper) {
		this.taper = Mathf.clamp(taper, 0.0f, 1.0f);
	}

}
