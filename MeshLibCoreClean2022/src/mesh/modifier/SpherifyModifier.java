package mesh.modifier;

import math.Vector3f;
import mesh.Mesh3D;

public class SpherifyModifier implements IMeshModifier {

    private float factor;

    private float radius;

    private Vector3f center;

    public SpherifyModifier() {
	this(1f, new Vector3f());
    }

    public SpherifyModifier(float radius) {
	this(radius, new Vector3f());
    }

    public SpherifyModifier(float radius, Vector3f center) {
	this.radius = radius;
	this.center = center;
	this.factor = 1.0f;
    }

    @Override
    public Mesh3D modify(Mesh3D mesh) {
	Vector3f origin = new Vector3f(center);
	for (Vector3f v0 : mesh.vertices) {
	    Vector3f v1 = new Vector3f(v0.subtract(origin)).normalizeLocal().mult(radius).add(origin);
	    v0.lerpLocal(v1, factor);
	}
	return mesh;
    }

    public void setRadius(float radius) {
	this.radius = radius;
    }

    public void setCenter(float x, float y, float z) {
	center = new Vector3f(x, y, z);
    }

    public void setCenter(Vector3f center) {
	this.center.set(center);
    }

    public float getFactor() {
	return factor;
    }

    public void setFactor(float factor) {
	this.factor = factor;
    }

}
