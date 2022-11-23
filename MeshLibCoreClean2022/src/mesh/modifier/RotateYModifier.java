package mesh.modifier;

import math.Mathf;
import math.Matrix3f;
import math.Vector3f;
import mesh.Mesh3D;

public class RotateYModifier implements IMeshModifier {

	private float angle;

	public RotateYModifier() {
		this(0);
	}

	public RotateYModifier(float angle) {
		this.angle = angle;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		float a = angle;
		Matrix3f m = new Matrix3f(Mathf.cos(a), 0, Mathf.sin(a), 0, 1, 0,
				-Mathf.sin(a), 0, Mathf.cos(a));

		for (Vector3f v : mesh.vertices) {
			Vector3f v0 = v.mult(m);
			v.set(v0.x, v.y, v0.z);
		}

		return mesh;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

}
