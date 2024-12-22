package math;

import java.util.Arrays;

public class Matrix4f {

	public static final Matrix4f ZERO = new Matrix4f();

	public static final Matrix4f IDENTITY = new Matrix4f().identity();

	private final float[] values;

	public Matrix4f() {
		this.values = new float[16];
	}

	public Matrix4f(float... elements) {
		if (elements.length != 16) {
			throw new IllegalArgumentException("Matrix4f requires 16 elements.");
		}
		this.values = Arrays.copyOf(elements, 16);
	}

	public Matrix4f(Matrix4f other) {
		this.values = Arrays.copyOf(other.values, 16);
	}

	public Matrix4f identity() {
		Arrays.fill(values, 0);
		values[0] = values[5] = values[10] = values[15] = 1;
		return this;
	}

	public Matrix4f transpose() {
		return new Matrix4f(values[0], values[4], values[8], values[12], values[1],
		    values[5], values[9], values[13], values[2], values[6], values[10],
		    values[14], values[3], values[7], values[11], values[15]);
	}

	public Matrix4f add(Matrix4f other) {
		float[] result = new float[16];
		for (int i = 0; i < 16; i++) {
			result[i] = this.values[i] + other.values[i];
		}
		return new Matrix4f(result);
	}

	public Matrix4f addLocal(Matrix4f other) {
		for (int i = 0; i < 16; i++) {
			this.values[i] += other.values[i];
		}
		return this;
	}

	public Matrix4f multiply(Matrix4f other) {
		float[] m = new float[16];
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				m[row * 4 + col] = values[row * 4 + 0] * other.values[0 * 4 + col]
				    + values[row * 4 + 1] * other.values[1 * 4 + col]
				    + values[row * 4 + 2] * other.values[2 * 4 + col]
				    + values[row * 4 + 3] * other.values[3 * 4 + col];
			}
		}
		return new Matrix4f(m);
	}

	public float[] getValues() {
		return Arrays.copyOf(values, values.length);
	}

	public float get(int row, int col) {
		return values[row * 4 + col];
	}

	public void set(int row, int col, float value) {
		values[row * 4 + col] = value;
	}

	public static Matrix4f createTranslation(float x, float y, float z) {
		return new Matrix4f(1, 0, 0, x, 0, 1, 0, y, 0, 0, 1, z, 0, 0, 0, 1);
	}

	/**
	 * Sets the matrix to represent a perspective projection matrix.
	 * 
	 * <p>
	 * This method computes a standard perspective projection matrix based on the
	 * provided field of view, aspect ratio, near clipping plane, and far clipping
	 * plane. The resulting matrix transforms 3D points into normalized device
	 * coordinates for rendering with perspective projection.
	 * </p>
	 * 
	 * @param fov       Field of view in radians (vertical field of view). Must be
	 *                  between 0 and π radians.
	 * @param aspect    Aspect ratio of the viewport (width / height). Must be
	 *                  positive.
	 * @param nearPlane Distance to the near clipping plane. Must be less than
	 *                  `farPlane`.
	 * @param farPlane  Distance to the far clipping plane. Must be greater than
	 *                  `nearPlane`.
	 * @return This matrix for chaining calls.
	 * @throws IllegalArgumentException if the input parameters are invalid.
	 */
	public Matrix4f setPerspective(float fov, float aspect, float nearPlane,
	    float farPlane) {
		if (nearPlane > farPlane) {
			throw new IllegalArgumentException(String.format(
			    "Near plane (%.2f) cannot be greater than far plane (%.2f).",
			    nearPlane, farPlane));
		}
		if (aspect <= 0) {
			throw new IllegalArgumentException(
			    "Aspect ratio must be a positive number.");
		}
		if (fov <= 0.0 || fov >= Math.PI) {
			throw new IllegalArgumentException(
			    "Field of view must be between 0 and π radians.");
		}

		float f = (float) (1.0 / Math.tan(fov / 2.0));
		Arrays.fill(values, 0);
		values[0] = f / aspect;
		values[5] = f;
		values[10] = (farPlane + nearPlane) / (nearPlane - farPlane);
		values[11] = -1;
		values[14] = (2 * farPlane * nearPlane) / (nearPlane - farPlane);

		return this;
	}

	/**
	 * Constructs a right-handed view matrix for an FPS (First-Person Shooter)
	 * style camera.
	 * <p>
	 * The view matrix is computed based on the camera's position (`eye`), pitch,
	 * and yaw angles. It assumes a right-handed coordinate system where:
	 * <ul>
	 * <li>+X points to the right</li>
	 * <li>+Y points up</li>
	 * <li>+Z points backward (into the screen)</li>
	 * </ul>
	 * This method is particularly useful for creating a camera that can move and
	 * rotate freely in a 3D scene, such as in games or visualization
	 * applications.
	 * </p>
	 * 
	 * @param eye   the position of the camera in world space, represented as a
	 *              {@link Vector3f}.
	 * @param pitch the pitch angle (rotation around the X-axis), in radians. A
	 *              positive value tilts the camera upward.
	 * @param yaw   the yaw angle (rotation around the Y-axis), in radians. A
	 *              positive value rotates the camera to the right.
	 * @return a {@link Matrix4f} representing the view matrix for the specified
	 *         position and orientation.
	 * 
	 * @see <a href="https://www.3dgep.com/understanding-the-view-matrix/">
	 *      Understanding the View Matrix</a>
	 */
	public static Matrix4f fpsViewRH(Vector3f eye, float pitch, float yaw) {
		float cosPitch = Mathf.cos(pitch);
		float sinPitch = Mathf.sin(pitch);
		float cosYaw = Mathf.cos(yaw);
		float sinYaw = Mathf.sin(yaw);

		Vector3f right = new Vector3f(cosYaw, 0, -sinYaw);
		Vector3f up = new Vector3f(sinYaw * sinPitch, cosPitch, cosYaw * sinPitch);
		Vector3f forward = new Vector3f(sinYaw * cosPitch, -sinPitch,
		    cosPitch * cosYaw);

		Matrix4f viewMatrix = new Matrix4f(right.x, up.x, forward.x, 0, right.y,
		    up.y, forward.y, 0, right.z, up.z, forward.z, 0, -right.dot(eye),
		    -up.dot(eye), -forward.dot(eye), 1);

		return viewMatrix;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Matrix4f:\n");
		for (int row = 0; row < 4; row++) {
			sb.append("[ ");
			for (int col = 0; col < 4; col++) {
				sb.append(get(row, col)).append(" ");
			}
			sb.append("]\n");
		}
		return sb.toString();
	}

}