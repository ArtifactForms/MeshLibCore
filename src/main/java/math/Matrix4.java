package math;

import java.util.Arrays;

// Potential Optimizations loop unrolling
public class Matrix4 {

  // 0  1  2  3
  // 4  5  6  7
  // 8  9  10 11
  // 12 13 14 15

  // 00 01 02 03
  // 10 11 12 13
  // 20 21 22 23
  // 30 31 32 33

  private static final float[] IDENTITY_VALUES =
      new float[] {
        1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1
      };

  public static final Matrix4 IDENTITY = new Matrix4().setToIdentity();

  public static final Matrix4 ZERO = new Matrix4();

  private float[] values;

  public Matrix4() {
    this.values = new float[16];
  }

  public Matrix4(float... values) {
    setValues(values);
  }

  public Matrix4(Matrix4 other) {
    this.values = Arrays.copyOf(other.values, 16);
  }

  public Matrix4 add(Matrix4 other) {
    Matrix4 resultMatrix = new Matrix4();
    for (int i = 0; i < 16; i++) {
      resultMatrix.values[i] = this.values[i] + other.values[i];
    }
    return resultMatrix;
  }

  public Matrix4 addLocal(Matrix4 other) {
    for (int i = 0; i < 16; i++) {
      this.values[i] += other.values[i];
    }
    return this;
  }

  public Matrix4 multiply(Matrix4 other) {
    Matrix4 result = new Matrix4();
    for (int row = 0; row < 4; row++) {
      for (int col = 0; col < 4; col++) {
        float sum = 0.0f;
        for (int k = 0; k < 4; k++) {
          sum += this.values[row * 4 + k] * other.values[k * 4 + col];
        }
        result.values[row * 4 + col] = sum;
      }
    }
    return result;
  }

  public Matrix4 multiplyLocal(Matrix4 other) {
    setValues(multiply(other).values);
    return this;
  }

  public Vector3f multiply(Vector3f point) {
    float x = values[0] * point.x + values[1] * point.y + values[2] * point.z + values[3];
    float y = values[4] * point.x + values[5] * point.y + values[6] * point.z + values[7];
    float z = values[8] * point.x + values[9] * point.y + values[10] * point.z + values[11];
    float w = values[12] * point.x + values[13] * point.y + values[14] * point.z + values[15];

    // Homogeneous division if using projective transformations
    if (w != 0) {
      x /= w;
      y /= w;
      z /= w;
    }
    return new Vector3f(x, y, z);
  }
  
  public Matrix4 rotateX(float angle) {
      float cos = (float) Math.cos(angle);
      float sin = (float) Math.sin(angle);
      Matrix4 rotation = new Matrix4(
          1, 0, 0, 0,
          0, cos, -sin, 0,
          0, sin, cos, 0,
          0, 0, 0, 1
      );
      return this.multiply(rotation);
  }

  public Matrix4 rotateY(float angle) {
      float cos = (float) Math.cos(angle);
      float sin = (float) Math.sin(angle);
      Matrix4 rotation = new Matrix4(
          cos, 0, sin, 0,
          0, 1, 0, 0,
          -sin, 0, cos, 0,
          0, 0, 0, 1
      );
      return this.multiply(rotation);
  }

  public Matrix4 rotateZ(float angle) {
      float cos = (float) Math.cos(angle);
      float sin = (float) Math.sin(angle);
      Matrix4 rotation = new Matrix4(
          cos, -sin, 0, 0,
          sin, cos, 0, 0,
          0, 0, 1, 0,
          0, 0, 0, 1
      );
      return this.multiply(rotation);
  }

  public Matrix4 translate(float x, float y, float z) {
      Matrix4 translation = new Matrix4(
          1, 0, 0, x,
          0, 1, 0, y,
          0, 0, 1, z,
          0, 0, 0, 1
      );
      return this.multiply(translation);
  }
  
  public Matrix4 identity() {
      Arrays.fill(values, 0);
      values[0] = values[5] = values[10] = values[15] = 1;
      return this;
    }

  public Matrix4 transpose() {
    return new Matrix4(
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

  public Matrix4 transposeLocal() {
    setValues(
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
    return this;
  }

  public Matrix4 invert() {
    float[] result = new float[16];
    float det = determinant();

    if (Math.abs(det) < 1E-6f) { // Check for near-zero determinant
      throw new ArithmeticException("Matrix is singular or nearly singular. Inversion failed.");
    }

    // Calculate cofactors
    float[] cofactors = new float[16];
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        cofactors[j * 4 + i] = cofactor(i, j); // Transpose while calculating cofactors
      }
    }

    // Calculate inverse
    for (int i = 0; i < 16; i++) {
      result[i] = normalize(cofactors[i] / det);
    }

    return new Matrix4(result);
  }

  public float determinant() {
    float det = 0.0f;

    for (int i = 0; i < 4; i++) {
      det += values[i] * cofactor(0, i); // Use row 0 for determinant calculation
    }

    return det;
  }

  private float cofactor(int row, int col) {
    float[] minor = new float[9]; // 3x3 minor matrix
    int minorIndex = 0;

    for (int i = 0; i < 4; i++) {
      if (i != row) {
        for (int j = 0; j < 4; j++) {
          if (j != col) {
            minor[minorIndex++] = values[i * 4 + j]; // Correct indexing for 1D array
          }
        }
      }
    }

    return (float) Math.pow(-1, row + col) * determinant3x3(minor);
  }

  private float determinant3x3(float[] matrix) {
    return matrix[0] * (matrix[4] * matrix[8] - matrix[5] * matrix[7])
        - matrix[1] * (matrix[3] * matrix[8] - matrix[5] * matrix[6])
        + matrix[2] * (matrix[3] * matrix[7] - matrix[4] * matrix[6]);
  }

  // Helper method to normalize values
  private float normalize(float value) {
    return Math.abs(value) < 1e-6 ? 0.0f : value; // Convert -0.0 to 0.0
  }

  public boolean isIdentity() {
    return Arrays.equals(values, IDENTITY_VALUES);
  }

  public static Matrix4 perspective(float fov, float aspect, float zNear, float zFar) {
    validateNearFarPlanes(zNear, zFar);

    float tanHalfFOV = (float) Math.tan(fov / 2);
    Matrix4 matrix = new Matrix4();

    matrix.values[0] = 1 / (tanHalfFOV * aspect);
    matrix.values[5] = -1 / tanHalfFOV; // Note the negative sign to flip the Y-axis.
    matrix.values[10] = -(zFar + zNear) / (zFar - zNear);
    matrix.values[11] = -(2 * zFar * zNear) / (zFar - zNear);
    matrix.values[14] = -1;
    matrix.values[15] = 0;

    return matrix;
  }

  public static Matrix4 lookAt(Vector3f eye, Vector3f center, Vector3f up) {
    Vector3f forward = eye.subtract(center).normalize();
    Vector3f right = up.cross(forward).normalize();
    Vector3f cameraUp = forward.cross(right);

    Matrix4 result = new Matrix4();
    result.values[0] = right.x;
    result.values[1] = right.y;
    result.values[2] = right.z;
    result.values[3] = -right.dot(eye);

    result.values[4] = cameraUp.x;
    result.values[5] = cameraUp.y;
    result.values[6] = cameraUp.z;
    result.values[7] = -cameraUp.dot(eye);

    result.values[8] = -forward.x;
    result.values[9] = -forward.y;
    result.values[10] = -forward.z;
    result.values[11] = -forward.dot(eye);

    result.values[12] = 0;
    result.values[13] = 0;
    result.values[14] = 0;
    result.values[15] = 1;

    return result;
  }

  private static void validateNearFarPlanes(float zNear, float zFar) {
    if (zNear <= 0 || zFar <= 0) {
      throw new IllegalArgumentException("Near and far planes must be positive values.");
    }

    if (zNear > zFar) {
      throw new IllegalArgumentException(
          String.format("Near plane (%.2f) cannot be greater than far plane (%.2f).", zNear, zFar));
    }

    if (zNear == zFar) {
      throw new IllegalArgumentException(
          String.format("Near plane (%.2f) cannot be equal to far plane (%.2f).", zNear, zFar));
    }
  }

  public Matrix4 setToIdentity() {
    Arrays.fill(values, 0);
    values[0] = values[5] = values[10] = values[15] = 1;
    return this;
  }

  public float[] getValues() {
    return Arrays.copyOf(values, values.length);
  }

  public void setValues(float... values) {
    if (values.length != 16) {
      throw new IllegalArgumentException("Matrix4 must have 16 elements.");
    }
    this.values = values;
  }

  public float get(int row, int col) {
    int index = Mathf.toOneDimensionalIndex(row, col, 4);
    return (values[index]);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      sb.append("[");
      for (int j = 0; j < 4; j++) {
        sb.append(values[i * 4 + j]);
        if (j < 3) sb.append(", ");
      }
      sb.append("]\n");
    }
    return sb.toString();
  }
}
