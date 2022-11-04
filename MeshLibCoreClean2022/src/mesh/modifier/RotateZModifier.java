package mesh.modifier;

import math.Mathf;
import math.Matrix3f;
import math.Vector3f;
import mesh.Mesh3D;

public class RotateZModifier implements IMeshModifier {

	private float angle;

	public RotateZModifier() {
		super();
		this.angle = 0;
	}

	public RotateZModifier(float angle) {
		super();
		this.angle = angle;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		float a = angle;
		Matrix3f m = new Matrix3f(Mathf.cos(a), -Mathf.sin(a), 0,
				Mathf.sin(a), Mathf.cos(a), 0, 0, 0, 1);

		for (Vector3f v : mesh.vertices) {
			Vector3f v0 = v.mult(m);
			v.set(v0.x, v0.y, v.z);
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
