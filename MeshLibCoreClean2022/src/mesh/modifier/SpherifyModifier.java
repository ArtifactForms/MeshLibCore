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
		for (Vector3f v : mesh.vertices) {
			Vector3f v1 = new Vector3f(v.x - origin.x, v.y - origin.y, v.z - origin.z).normalizeLocal();
			Vector3f a = new Vector3f(v);
			Vector3f b = (v1.mult(radius).add(origin));
			v.set(lerp(a, b, factor));
		}
		return mesh;
	}
	
	private Vector3f lerp(Vector3f p, Vector3f q, float f) {
		p.lerpLocal(p, q, f);
		return p;
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
