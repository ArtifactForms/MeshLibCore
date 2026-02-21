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
    return new Matrix4f(
        values[0],
        values[4],
        values[8],
        values[12],
        values[1],
        values[5],
        values[9],
        values[13],
        values[2],
        values[6],
        values[10],
        values[14],
        values[3],
        values[7],
        values[11],
        values[15]);
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
        m[row * 4 + col] =
            values[row * 4 + 0] * other.values[0 * 4 + col]
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

  public static Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up) {
    // Forward points from camera to target
    Vector3f forward = target.subtract(eye).normalize();

    // Right-handed system with Y-down
    Vector3f right = up.cross(forward).normalize();
    Vector3f cameraUp = forward.cross(right);

    Matrix4f result = new Matrix4f();

    result.set(0, 0, right.x);
    result.set(0, 1, right.y);
    result.set(0, 2, right.z);
    result.set(0, 3, -right.dot(eye));

    result.set(1, 0, cameraUp.x);
    result.set(1, 1, cameraUp.y);
    result.set(1, 2, cameraUp.z);
    result.set(1, 3, -cameraUp.dot(eye));

    result.set(2, 0, -forward.x);
    result.set(2, 1, -forward.y);
    result.set(2, 2, -forward.z);
    result.set(2, 3, -forward.dot(eye));

    result.set(3, 0, 0);
    result.set(3, 1, 0);
    result.set(3, 2, 0);
    result.set(3, 3, 1);

    return result;
  }

  public static Matrix4f perspective(float fov, float aspect, float near, float far) {
    float f = 1.0f / (float) Math.tan(fov * 0.5f);

    Matrix4f m = new Matrix4f();

    m.set(0, 0, f / aspect);
    m.set(1, 1, -f); // Y-FLIP
    m.set(2, 2, (far + near) / (near - far));
    m.set(2, 3, (2 * far * near) / (near - far));
    m.set(3, 2, -1);
    m.set(3, 3, 0);

    return m;
  }

  /**
   * Constructs a right-handed view matrix for an FPS (First-Person Shooter) style camera.
   *
   * <p>The view matrix is computed based on the camera's position (`eye`), pitch, and yaw angles.
   * It assumes a right-handed coordinate system where:
   *
   * <ul>
   *   <li>+X points to the right
   *   <li>+Y points up
   *   <li>+Z points backward (into the screen)
   * </ul>
   *
   * This method is particularly useful for creating a camera that can move and rotate freely in a
   * 3D scene, such as in games or visualization applications.
   *
   * @param eye the position of the camera in world space, represented as a {@link Vector3f}.
   * @param pitch the pitch angle (rotation around the X-axis), in radians. A positive value tilts
   *     the camera upward.
   * @param yaw the yaw angle (rotation around the Y-axis), in radians. A positive value rotates the
   *     camera to the right.
   * @return a {@link Matrix4f} representing the view matrix for the specified position and
   *     orientation.
   * @see <a href="https://www.3dgep.com/understanding-the-view-matrix/">Understanding the View
   *     Matrix</a>
   */
  public static Matrix4f fpsViewRH(Vector3f eye, float pitch, float yaw) {
    float cosPitch = Mathf.cos(pitch);
    float sinPitch = Mathf.sin(pitch);
    float cosYaw = Mathf.cos(yaw);
    float sinYaw = Mathf.sin(yaw);

    Vector3f right = new Vector3f(cosYaw, 0, -sinYaw);
    Vector3f up = new Vector3f(sinYaw * sinPitch, cosPitch, cosYaw * sinPitch);
    Vector3f forward = new Vector3f(sinYaw * cosPitch, -sinPitch, cosPitch * cosYaw);

    Matrix4f viewMatrix =
        new Matrix4f(
            right.x,
            up.x,
            forward.x,
            0,
            right.y,
            up.y,
            forward.y,
            0,
            right.z,
            up.z,
            forward.z,
            0,
            -right.dot(eye),
            -up.dot(eye),
            -forward.dot(eye),
            1);

    return viewMatrix;
  }

  public Vector4f mult(Vector4f v) {
    float x =
        get(0, 0) * v.getX() + get(0, 1) * v.getY() + get(0, 2) * v.getZ() + get(0, 3) * v.getW();

    float y =
        get(1, 0) * v.getX() + get(1, 1) * v.getY() + get(1, 2) * v.getZ() + get(1, 3) * v.getW();

    float z =
        get(2, 0) * v.getX() + get(2, 1) * v.getY() + get(2, 2) * v.getZ() + get(2, 3) * v.getW();

    float w =
        get(3, 0) * v.getX() + get(3, 1) * v.getY() + get(3, 2) * v.getZ() + get(3, 3) * v.getW();

    return new Vector4f(x, y, z, w);
  }

  public Matrix4f rotateX(float angle) {
    float cos = (float) Math.cos(angle);
    float sin = (float) Math.sin(angle);
    Matrix4f rotation = new Matrix4f(1, 0, 0, 0, 0, cos, -sin, 0, 0, sin, cos, 0, 0, 0, 0, 1);
    return this.multiply(rotation);
  }

  public Matrix4f rotateY(float angle) {
    float cos = (float) Math.cos(angle);
    float sin = (float) Math.sin(angle);
    Matrix4f rotation = new Matrix4f(cos, 0, sin, 0, 0, 1, 0, 0, -sin, 0, cos, 0, 0, 0, 0, 1);
    return this.multiply(rotation);
  }

  public Matrix4f rotateZ(float angle) {
    float cos = (float) Math.cos(angle);
    float sin = (float) Math.sin(angle);
    Matrix4f rotation = new Matrix4f(cos, -sin, 0, 0, sin, cos, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
    return this.multiply(rotation);
  }

  public Matrix4f translate(float x, float y, float z) {
    Matrix4f translation = new Matrix4f(1, 0, 0, x, 0, 1, 0, y, 0, 0, 1, z, 0, 0, 0, 1);
    return this.multiply(translation);
  }

  public Matrix4f setViewMatrix(Vector3f rotation, Vector3f position) {
    return this.identity()
        .rotateX(-rotation.x)
        .rotateY(-rotation.y)
        .rotateZ(-rotation.z)
        .translate(-position.x, -position.y, -position.z);
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
