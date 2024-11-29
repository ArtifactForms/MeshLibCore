package mesh.creator.beam;

import math.Mathf;
import mesh.Mesh3D;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.TranslateModifier;

public class BeamCProfileCreator implements IBeamCreator {

	private float width;

	private float height;

	private float depth;

	private float thickness;

	private float taper;

	private Mesh3D mesh;

	public BeamCProfileCreator() {
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
		rotate();
		solidify();
		center();
		return mesh;
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void createVertices() {
		float halfHeight = height / 2f;

		addVertex(-halfHeight, 0, 0);
		addVertex(+halfHeight, 0, 0);
		addVertex(-halfHeight, -width, 0);
		addVertex(+halfHeight, -width, 0);
		addVertex(-halfHeight + thickness, -thickness, 0);
		addVertex(+halfHeight - thickness, -thickness, 0);
		addVertex(-halfHeight + (thickness * (1f - taper)), -width, 0);
		addVertex(+halfHeight - (thickness * (1f - taper)), -width, 0);
	}

	private void createFaces() {
		addFace(0, 4, 5, 1);
		addFace(0, 2, 6, 4);
		addFace(5, 7, 3, 1);
	}

	private void addVertex(float x, float y, float z) {
		mesh.addVertex(x, y, z);
	}

	private void addFace(int... indices) {
		mesh.addFace(indices);
	}

	private void rotate() {
		mesh.rotateZ(Mathf.HALF_PI);
	}

	private void solidify() {
		new SolidifyModifier(depth).modify(mesh);
	}

	private void center() {
		mesh.apply(new TranslateModifier(-width / 2f, 0, depth / 2f));
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
