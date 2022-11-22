package mesh.creator.beam;

import math.Mathf;
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
		float thicknessTaper = this.thickness * (1f - taper);
		mesh.addVertex(-thickness / 2f, -thickness, 0);
		mesh.addVertex(thickness / 2f, -thickness, 0);
		mesh.addVertex(-thickness / 2f, -height + thickness, 0);
		mesh.addVertex(thickness / 2f, -height + thickness, 0);
		mesh.addVertex(-thickness / 2f, -height, 0);
		mesh.addVertex(thickness / 2f, -height, 0);
		mesh.addVertex(-width / 2f, -height + thicknessTaper, 0);
		mesh.addVertex(width / 2f, -height + thicknessTaper, 0);
		mesh.addVertex(-width / 2f, -height, 0);
		mesh.addVertex(width / 2f, -height, 0);
		mesh.addVertex(-thicknessTaper / 2f, 0, 0);
		mesh.addVertex(thicknessTaper / 2, 0, 0);
		mesh.addVertex(-width / 2f, 0, 0);
		mesh.addVertex(width / 2f, 0, 0);
		mesh.addVertex(-width / 2f, -thicknessTaper, 0);
		mesh.addVertex(width / 2f, -thicknessTaper, 0);
	}
	
	private void createFaces() {
		mesh.addFace(0, 2, 3, 1);
		mesh.addFace(2, 4, 5, 3);
		mesh.addFace(6, 8, 4, 2);
		mesh.addFace(3, 5, 9, 7);
		mesh.addFace(10, 0, 1, 11);
		mesh.addFace(12, 14, 0, 10);
		mesh.addFace(11, 1, 15, 13);
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
