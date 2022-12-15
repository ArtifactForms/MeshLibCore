package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class BoxCreator implements IMeshCreator {

    private float width;

    private float height;

    private float depth;

    public BoxCreator() {
	this(1, 1, 1);
    }

    public BoxCreator(float width, float height, float depth) {
	this.width = width;
	this.height = height;
	this.depth = depth;
    }

    @Override
    public Mesh3D create() {
	Mesh3D mesh = new CubeCreator(0.5f).create();
	mesh.scale(width, height, depth);
	return mesh;
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

}
