package mesh.creator.beam;

import math.Mathf;
import mesh.Mesh3D;
import mesh.modifier.SolidifyModifier;

public class BeamUProfileCreator implements IBeamCreator {
	
	private float width;
	private float height;
	private float depth;
	private float thickness;
	private float taper;
	private Mesh3D mesh;
	
	public BeamUProfileCreator() {
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
		mesh.addVertex(-width / 2f, 0, 0);
		mesh.addVertex(width / 2f, 0, 0);
		mesh.addVertex(-width / 2f, -height, 0);
		mesh.addVertex(width / 2f, -height, 0);
		mesh.addVertex(-width / 2f + thickness, -thickness, 0);
		mesh.addVertex(+width / 2f - thickness, -thickness, 0);
		mesh.addVertex(-width / 2f + (thickness * (1f - taper)), -height, 0);
		mesh.addVertex(+width / 2f - (thickness * (1f - taper)), -height, 0);
	}
	
	private void createFaces() {
		mesh.addFace(0, 4, 5, 1);
		mesh.addFace(0, 2, 6, 4);
		mesh.addFace(5, 7, 3, 1);
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
