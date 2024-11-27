package mesh.modifier;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;

/**
 * A modifier that bends a mesh along the X-axis.
 * 
 * This modifier applies a simple bending deformation to each vertex of the
 * mesh. The degree of bending is controlled by the `factor` parameter. A higher
 * factor results in a more pronounced bend.
 */
public class BendModifier implements IMeshModifier {

	private static final float EPSILON = 1e-7f;

	private float factor;

	public BendModifier(float factor) {
		this.factor = factor;
	}

	private void simpleDeformBend(float factor, Vector3f v) {
		float theta = v.x * factor;
		float sinTheta = Mathf.sin(theta);
		float cosTheta = Mathf.cos(theta);

		if (Mathf.abs(factor) > EPSILON) {
			float bx = -(v.y - 1.0f / factor) * sinTheta;
			float by = (v.y - 1.0f / factor) * cosTheta + 1.0f / factor;
			float bz = v.z;
			v.set(bx, by, bz);
		}
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		for (Vector3f v : mesh.vertices)
			simpleDeformBend(factor, v);
		return mesh;
	}

	public float getFactor() {
		return factor;
	}

	public void setFactor(float factor) {
		this.factor = factor;
	}

}
