package mesh.creator.beam;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.topology.SolidifyModifier;
import mesh.modifier.transform.TranslateModifier;

public class BeamOProfileCreator implements IBeamCreator {

	private float width;

	private float height;

	private float depth;

	private float thickness;

	private float taper;

	private Mesh3D mesh;

	public BeamOProfileCreator() {
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

		addVertex(-halfWidth, 0, 0);
		addVertex(+halfWidth, 0, 0);
		addVertex(-halfWidth, -height, 0);
		addVertex(+halfWidth, -height, 0);
		addVertex(-halfWidth + thickness, -thickness, 0);
		addVertex(+halfWidth - thickness, -thickness, 0);
		addVertex(-halfWidth + thickness, -height + thickness, 0);
		addVertex(+halfWidth - thickness, -height + thickness, 0);
	}

	private void createFaces() {
		addFace(0, 4, 5, 1);
		addFace(0, 2, 6, 4);
		addFace(6, 2, 3, 7);
		addFace(5, 7, 3, 1);
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
		mesh.apply(new TranslateModifier(0, height / 2f, depth / 2f));
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
