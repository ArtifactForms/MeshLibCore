package math;

public class Vector3f {

  public static final Vector3f ZERO = new Vector3f(0, 0, 0);

  public float x;

  public float y;

  public float z;

  /** Default constructor. Initializes the vector to (0, 0, 0). */
  public Vector3f() {
    this(0, 0, 0);
  }

  /**
   * Constructs a vector with the specified x, y, and z components.
   *
   * @param x The x component of the vector.
   * @param y The y component of the vector.
   * @param z The z component of the vector.
   */
  public Vector3f(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Copy constructor. Creates a new vector that is a copy of the given vector.
   *
   * @param other The vector to copy.
   */
  public Vector3f(Vector3f other) {
    this.x = other.x;
    this.y = other.y;
    this.z = other.z;
  }

  public Vector3f addLocal(float x, float y, float z) {
    this.x += x;
    this.y += y;
    this.z += z;
    return this;
  }

  /**
   * Adds the specified vector to this vector and returns the result.
   *
   * @param other The vector to add.
   * @return A new vector representing the sum of the two vectors.
   * @throws IllegalArgumentException If the given vector is null.
   */
  public Vector3f add(Vector3f other) {
    if (other == null) {
      throw new IllegalArgumentException("Other vector cannot be null.");
    }
    return new Vector3f(x + other.x, y + other.y, z + other.z);
  }

  public Vector3f addLocal(Vector3f other) {
    x += other.x;
    y += other.y;
    z += other.z;
    return this;
  }

  public Vector3f subtract(Vector3f v) {
    return new Vector3f(x - v.x, y - v.y, z - v.z);
  }

  public Vector3f subtractLocal(Vector3f v) {
    x -= v.x;
    y -= v.y;
    z -= v.z;
    return this;
  }

  //  public Vector3f(float value) {
  //    x = y = z = value;
  //  }

  //  public Vector3f(float x, float y) {
  //    this(x, y, 0);
  //  }

  //  public Vector3f(float[] values) {
  //    x = values[0];
  //    y = values[1];
  //    z = values[2];
  //  }

  /**
   * Rounds the x, y, and z components of this vector to the specified number of decimal places.
   *
   * @param decimalPlaces The number of decimal places to round to.
   */
  public void roundLocalDecimalPlaces(int decimalPlaces) {
    float factor = Mathf.pow(10, decimalPlaces);
    x = Mathf.round(x * factor) / factor;
    y = Mathf.round(y * factor) / factor;
    z = Mathf.round(z * factor) / factor;
  }

  public boolean approximatelyEquals(Vector3f v, float threshold) {
    if (threshold < 0.0f)
      throw new IllegalArgumentException("Threshold must be greater or equal to 0.0f.");

    float diffX = Math.abs(x - v.x);
    float diffY = Math.abs(y - v.y);
    float diffZ = Math.abs(z - v.z);

    return (diffX <= threshold && diffY <= threshold && diffZ <= threshold);
  }

  /**
   * Computes the angle in radians between this vector and {@code v}.
   *
   * @param v The other vector.
   * @return The unsigned angle in radians in the range [0, PI].
   * @throws IllegalArgumentException If one of the vectors has zero length.
   */
  public float angle(Vector3f v) {
    float denominator = length() * v.length();
    if (denominator == 0.0f) {
      throw new IllegalArgumentException("Angle is undefined for zero-length vectors.");
    }
    float cosine = dot(v) / denominator;
    cosine = Math.max(-1.0f, Math.min(1.0f, cosine));
    return (float) Math.acos(cosine);
  }

  /**
   * Computes the signed angle between this vector and {@code v} around a plane normal.
   *
   * @param v The target vector.
   * @param normal The normal that defines the orientation of the signed angle.
   * @return Signed angle in radians.
   */
  public float signedAngle(Vector3f v, Vector3f normal) {
    float unsignedAngle = angle(v);
    return unsignedAngle * Math.signum(cross(v).dot(normal));
  }

  /**
   * Projects this vector onto {@code v}.
   *
   * @param v The vector to project onto.
   * @return Projected vector.
   */
  public Vector3f project(Vector3f v) {
    float scalar = dot(v) / v.lengthSquared();
    return v.mult(scalar);
  }

  /**
   * Projects this vector onto {@code v} and stores the result in this vector.
   *
   * @param v The vector to project onto.
   * @return This vector after projection.
   */
  public Vector3f projectLocal(Vector3f v) {
    float scalar = dot(v) / v.lengthSquared();
    set(v.mult(scalar));
    return this;
  }

  /**
   * Projects this vector onto the plane defined by {@code planeNormal}.
   *
   * @param planeNormal The plane normal.
   * @return Projected vector on the plane.
   */
  public Vector3f projectOnPlane(Vector3f planeNormal) {
    float scalar = dot(planeNormal) / planeNormal.lengthSquared();
    return subtract(planeNormal.mult(scalar));
  }

  /**
   * Projects this vector onto the plane defined by {@code planeNormal} and stores it locally.
   *
   * @param planeNormal The plane normal.
   * @return This vector after projection on the plane.
   */
  public Vector3f projectOnPlaneLocal(Vector3f planeNormal) {
    float scalar = dot(planeNormal) / planeNormal.lengthSquared();
    subtractLocal(planeNormal.mult(scalar));
    return this;
  }

  /**
   * Returns a vector whose length is at most {@code maxLength}.
   *
   * @param maxLength Maximum allowed length.
   * @return New vector clamped to the requested maximum length.
   */
  public Vector3f clampLength(float maxLength) {
    float currentLength = length();
    if (currentLength > maxLength && currentLength != 0.0f) {
      return divide(currentLength).mult(maxLength);
    }
    return new Vector3f(this);
  }

  /**
   * Clamps this vector's length so it is at most {@code maxLength}.
   *
   * @param maxLength Maximum allowed length.
   * @return This vector.
   */
  public Vector3f clampLengthLocal(float maxLength) {
    float currentLength = length();
    if (currentLength > maxLength && currentLength != 0.0f) {
      divideLocal(currentLength);
      multLocal(maxLength);
    }
    return this;
  }

  public float lengthSquared() {
    return (x * x) + (y * y) + (z * z);
  }

  public float length() {
    return (float) Math.sqrt(lengthSquared());
  }

  public float distanceSquared(float x, float y, float z) {
    float dx = this.x - x;
    float dy = this.y - y;
    float dz = this.z - z;
    return (dx * dx) + (dy * dy) + (dz * dz);
  }

  public float distance(float x, float y, float z) {
    return (float) Math.sqrt(distanceSquared(x, y, z));
  }

  public float distanceSquared(Vector3f v) {
    return distanceSquared(v.x, v.y, v.z);
  }

  public float distance(Vector3f v) {
    return (float) Math.sqrt(distanceSquared(v));
  }

  public Vector3f normalize() {
    float length = length();
    if (length != 0) {
      return divide(length);
    }
    return divide(1);
  }

  public Vector3f normalizeLocal() {
    float length = length();
    if (length != 0) {
      return divideLocal(length);
    }
    return divideLocal(1);
  }

  public float dot(Vector3f v) {
    return (x * v.x) + (y * v.y) + z * (v.z);
  }

  public Vector3f cross(float x, float y, float z) {
    return new Vector3f(
        (this.y * z) - (this.z * y), (this.z * x) - (this.x * z), (this.x * y) - (this.y * x));
  }

  public Vector3f crossLocal(float x, float y, float z) {
    float oldX = this.x;
    float oldY = this.y;
    float oldZ = this.z;
    this.x = (oldY * z) - (oldZ * y);
    this.y = (oldZ * x) - (oldX * z);
    this.z = (oldX * y) - (oldY * x);
    return this;
  }

  public Vector3f cross(Vector3f v) {
    return new Vector3f((y * v.z) - (z * v.y), (z * v.x) - (x * v.z), (x * v.y) - (y * v.x));
  }

  public Vector3f crossLocal(Vector3f v) {
    float oldX = x;
    float oldY = y;
    float oldZ = z;
    x = (oldY * v.z) - (oldZ * v.y);
    y = (oldZ * v.x) - (oldX * v.z);
    z = (oldX * v.y) - (oldY * v.x);
    return this;
  }

  public Vector3f cross(Vector3f v, Vector3f result) {
    if (result == null) result = new Vector3f();
    return result.set(cross(v));
  }

  public Vector3f negate() {
    return new Vector3f(x, y, z).negateLocal();
  }

  public Vector3f negateLocal() {
    x = x == 0 ? 0 : -x;
    y = y == 0 ? 0 : -y;
    z = z == 0 ? 0 : -z;
    return this;
  }

  public Vector3f add(float x, float y, float z) {
    return new Vector3f(this.x + x, this.y + y, this.z + z);
  }

  public Vector3f add(Vector3f v, Vector3f result) {
    if (result == null) result = new Vector3f();
    return result.set(add(v));
  }

  public Vector3f subtract(float x, float y, float z) {
    return new Vector3f(this.x - x, this.y - y, this.z - z);
  }

  public Vector3f subtractLocal(float x, float y, float z) {
    this.x -= x;
    this.y -= y;
    this.z -= z;
    return this;
  }

  public Vector3f subtract(Vector3f v, Vector3f result) {
    if (result == null) result = new Vector3f();
    return result.set(subtract(v));
  }

  public Vector3f mult(float x, float y, float z) {
    return new Vector3f(this.x * x, this.y * y, this.z * z);
  }

  public Vector3f multLocal(float x, float y, float z) {
    this.x *= x;
    this.y *= y;
    this.z *= z;
    return this;
  }

  public Vector3f mult(Vector3f v) {
    return new Vector3f(x * v.x, y * v.y, z * v.z);
  }

  public Vector3f multLocal(Vector3f v) {
    x *= v.x;
    y *= v.y;
    z *= v.z;
    return this;
  }

  public Vector3f mult(Vector3f v, Vector3f result) {
    if (result == null) result = new Vector3f();
    return result.set(mult(v));
  }

  public Vector3f divide(float x, float y, float z) {
    return new Vector3f(this.x / x, this.y / y, this.z / z);
  }

  public Vector3f divideLocal(float x, float y, float z) {
    this.x /= x;
    this.y /= y;
    this.z /= z;
    return this;
  }

  public Vector3f divide(Vector3f v) {
    return new Vector3f(x / v.x, y / v.y, z / v.z);
  }

  public Vector3f divideLocal(Vector3f v) {
    x /= v.x;
    y /= v.y;
    z /= v.z;
    return this;
  }

  public Vector3f mult(float scalar) {
    return new Vector3f(x * scalar, y * scalar, z * scalar);
  }

  public Vector3f multLocal(float scalar) {
    x *= scalar;
    y *= scalar;
    z *= scalar;
    return this;
  }

  public Vector3f mult(Matrix3f m) {
    float x0 = m.values[0] * x + m.values[1] * y + m.values[2] * z;
    float y0 = m.values[3] * x + m.values[4] * y + m.values[5] * z;
    float z0 = m.values[6] * x + m.values[7] * y + m.values[8] * z;
    return new Vector3f(x0, y0, z0);
  }

  public Vector3f multLocal(Matrix3f m) {
    float x0 = m.values[0] * x + m.values[1] * y + m.values[2] * z;
    float y0 = m.values[3] * x + m.values[4] * y + m.values[5] * z;
    float z0 = m.values[6] * x + m.values[7] * y + m.values[8] * z;
    set(x0, y0, z0);
    return this;
  }

  public Vector3f divide(float scalar) {
    return new Vector3f(x / scalar, y / scalar, z / scalar);
  }

  public Vector3f divideLocal(float scalar) {
    x /= scalar;
    y /= scalar;
    z /= scalar;
    return this;
  }

  public Vector3f divide(Vector3f v, Vector3f result) {
    if (result == null) result = new Vector3f();
    return result.set(divide(v));
  }

  public Vector3f abs() {
    return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
  }

  public Vector3f absLocal() {
    x = Math.abs(x);
    y = Math.abs(y);
    z = Math.abs(z);
    return this;
  }

  public Vector3f min(Vector3f v) {
    return new Vector3f(Math.min(x, v.x), Math.min(y, v.y), Math.min(z, v.z));
  }

  public Vector3f minLocal(Vector3f v) {
    x = Math.min(x, v.x);
    y = Math.min(y, v.y);
    z = Math.min(z, v.z);
    return this;
  }

  public Vector3f minLocal(Vector3f a, Vector3f b) {
    x = Math.min(a.x, b.x);
    y = Math.min(a.y, b.y);
    z = Math.min(a.z, b.z);
    return this;
  }

  public Vector3f max(Vector3f v) {
    return new Vector3f(Math.max(x, v.x), Math.max(y, v.y), Math.max(z, v.z));
  }

  public Vector3f maxLocal(Vector3f v) {
    x = Math.max(x, v.x);
    y = Math.max(y, v.y);
    z = Math.max(z, v.z);
    return this;
  }

  public Vector3f maxLocal(Vector3f a, Vector3f b) {
    x = Math.max(a.x, b.x);
    y = Math.max(a.y, b.y);
    z = Math.max(a.z, b.z);
    return this;
  }

  public Vector3f reciprocal() {
    return new Vector3f(
        x != 0.0f ? 1.0f / x : Math.copySign(Float.POSITIVE_INFINITY, x),
        y != 0.0f ? 1.0f / y : Math.copySign(Float.POSITIVE_INFINITY, y),
        z != 0.0f ? 1.0f / z : Math.copySign(Float.POSITIVE_INFINITY, z));
  }

  public Vector3f lerpLocal(Vector3f finalVec, float changeAmnt) {
    if (changeAmnt == 0) {
      return this;
    }
    if (changeAmnt == 1) {
      this.x = finalVec.x;
      this.y = finalVec.y;
      this.z = finalVec.z;
      return this;
    }
    this.x = (1 - changeAmnt) * this.x + changeAmnt * finalVec.x;
    this.y = (1 - changeAmnt) * this.y + changeAmnt * finalVec.y;
    this.z = (1 - changeAmnt) * this.z + changeAmnt * finalVec.z;
    return this;
  }

  public Vector3f lerpLocal(Vector3f beginVec, Vector3f finalVec, float changeAmnt) {
    if (changeAmnt == 0) {
      this.x = beginVec.x;
      this.y = beginVec.y;
      this.z = beginVec.z;
      return this;
    }
    if (changeAmnt == 1) {
      this.x = finalVec.x;
      this.y = finalVec.y;
      this.z = finalVec.z;
      return this;
    }
    this.x = (1 - changeAmnt) * beginVec.x + changeAmnt * finalVec.x;
    this.y = (1 - changeAmnt) * beginVec.y + changeAmnt * finalVec.y;
    this.z = (1 - changeAmnt) * beginVec.z + changeAmnt * finalVec.z;
    return this;
  }

  public static boolean isValid(Vector3f v) {
    if (v == null) return false;
    if (Float.isNaN(v.x) || Float.isNaN(v.y) || Float.isNaN(v.z)) return false;
    if (Float.isInfinite(v.x) || Float.isInfinite(v.y) || Float.isInfinite(v.z)) return false;
    return true;
  }

  public boolean isZero() {
    float threshold = 1e-6f;
    return Math.abs(x) < threshold && Math.abs(y) < threshold && Math.abs(z) < threshold;
  }

  public Vector3f set(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
    return this;
  }

  public Vector3f set(Vector3f v) {
    x = v.x;
    y = v.y;
    z = v.z;
    return this;
  }

  //  public Vector3f set(float[] values) {
  //    x = values[0];
  //    y = values[1];
  //    z = values[2];
  //    return this;
  //  }

  public float getX() {
    return x;
  }

  public Vector3f setX(float x) {
    this.x = x;
    return this;
  }

  public float getY() {
    return y;
  }

  public Vector3f setY(float y) {
    this.y = y;
    return this;
  }

  public float getZ() {
    return z;
  }

  public Vector3f setZ(float z) {
    this.z = z;
    return this;
  }

  // Used
  public float get(int i) {
    switch (i) {
      case 0:
        return x;
      case 1:
        return y;
      case 2:
        return z;
      default:
        throw new IndexOutOfBoundsException("Index must be 0, 1, or 2.");
    }
  }

  /**
   * Computes and returns the hash code for this vector. The hash code is calculated based on the
   * vector's components using the `Float.floatToIntBits()` method to avoid precision issues with
   * floating-point numbers. This ensures that vectors with the same values produce the same hash
   * code.
   *
   * @return The hash code for this vector.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Float.floatToIntBits(x);
    result = prime * result + Float.floatToIntBits(y);
    result = prime * result + Float.floatToIntBits(z);
    return result;
  }

  /**
   * Compares this vector to another object for equality. Two vectors are considered equal if they
   * have the same class type and their corresponding components (x, y, and z) are equal, taking
   * into account floating-point precision. This method uses `Float.floatToIntBits()` to compare the
   * components to avoid issues with floating-point comparison.
   *
   * @param obj The object to compare this vector with.
   * @return {@code true} if the object is equal to this vector; {@code false} otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Vector3f other = (Vector3f) obj;
    if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) return false;
    if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) return false;
    if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) return false;
    return true;
  }

  /**
   * Returns a string representation of this vector.
   *
   * @return A string representing the vector.".
   */
  @Override
  public String toString() {
    return "Vector3f [x=" + x + ", y=" + y + ", z=" + z + "]";
  }
}
