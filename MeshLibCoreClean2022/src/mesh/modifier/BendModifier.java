package mesh.modifier;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;

/**
 * https://www.reddit.com/r/blender/comments/1x10ba/help_with_understanding_the_simple_deform_bend/
 */
public class BendModifier implements IMeshModifier {

	private float factor;

	public BendModifier(float factor) {
		this.factor = factor;
	}

	private void simpleDeformBend(float factor, Vector3f v) {
		float x = v.getX();
		float y = v.getY();
		float z = v.getZ();
		float theta;
		float sint;
		float cost;

		theta = x * factor;
		sint = Mathf.sin(theta);
		cost = Mathf.cos(theta);

		if (Mathf.abs(factor) > 1e-7f) {
			float bx = -(y - 1.0f / factor) * sint;
			float by = (y - 1.0f / factor) * cost + 1.0f / factor;
			float bz = z;
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
