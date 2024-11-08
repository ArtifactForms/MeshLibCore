package mesh.modifier;

import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.util.VertexNormals;

/**
 * A modifier that adds random noise to a 3D mesh.
 * 
 * This modifier displaces the vertex positions along their normal vectors by a
 * random value, creating a more irregular surface appearance.
 */
public class NoiseModifier implements IMeshModifier {

	/**
	 * The minimum value for the random noise.
	 */
	private float minimum;

	/**
	 * The maximum value for the random noise.
	 */
	private float maximum;

	/**
	 * Creates a new NoiseModifier with a default noise range from 0.0 to 1.0.
	 */
	public NoiseModifier() {
		this(0.0f, 1.0f);
	}

	/**
	 * Creates a new NoiseModifier with a custom noise range.
	 *
	 * @param minimum The minimum value for the noise.
	 * @param maximum The maximum value for the noise.
	 */
	public NoiseModifier(float minimum, float maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
	}

	/**
	 * Modifies the given mesh by adding noise.
	 *
	 * @param mesh The mesh to be modified.
	 * @return The modified mesh.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		List<Vector3f> normals = new VertexNormals(mesh).getVertexNormals();
		for (int i = 0; i < mesh.vertices.size(); i++) {
			Vector3f vertex = mesh.getVertexAt(i);
			Vector3f normal = normals.get(i);
			vertex.addLocal(normal.mult(createRandomValue()));
		}
		return mesh;
	}

	/**
	 * Creates a random value within the specified minimum and maximum range.
	 *
	 * @return A random float value between `minimum` and `maximum`, inclusive.
	 */
	private float createRandomValue() {
		return Mathf.random(minimum, maximum);
	}

	/**
	 * Gets the minimum value for the random noise.
	 *
	 * @return The minimum value.
	 */
	public float getMinimum() {
		return minimum;
	}

	/**
	 * Sets the minimum value for the random noise.
	 *
	 * @param minimum The new minimum value.
	 */
	public void setMinimum(float minimum) {
		this.minimum = minimum;
	}

	/**
	 * Gets the maximum value for the random noise.
	 *
	 * @return The maximum value.
	 */
	public float getMaximum() {
		return maximum;
	}

	/**
	 * Sets the maximum value for the random noise.
	 *
	 * @param maximum The new maximum value.
	 */
	public void setMaximum(float maximum) {
		this.maximum = maximum;
	}

}
