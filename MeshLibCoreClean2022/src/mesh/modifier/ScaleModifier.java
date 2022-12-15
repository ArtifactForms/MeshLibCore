package mesh.modifier;

import mesh.Mesh3D;

public class ScaleModifier implements IMeshModifier {

    private float scaleX;

    private float scaleY;

    private float scaleZ;

    public ScaleModifier() {
	this(1, 1, 1);
    }

    public ScaleModifier(float scale) {
	this(scale, scale, scale);
    }

    public ScaleModifier(float scaleX, float scaleY, float scaleZ) {
	this.scaleX = scaleX;
	this.scaleY = scaleY;
	this.scaleZ = scaleZ;
    }

    @Override
    public Mesh3D modify(Mesh3D mesh) {
	mesh.scale(scaleX, scaleZ, scaleZ);
	return mesh;
    }

    public float getScaleX() {
	return scaleX;
    }

    public void setScaleX(float scaleX) {
	this.scaleX = scaleX;
    }

    public float getScaleY() {
	return scaleY;
    }

    public void setScaleY(float scaleY) {
	this.scaleY = scaleY;
    }

    public float getScaleZ() {
	return scaleZ;
    }

    public void setScaleZ(float scaleZ) {
	this.scaleZ = scaleZ;
    }

}
