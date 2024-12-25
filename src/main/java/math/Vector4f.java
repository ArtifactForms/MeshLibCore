package math;

/**
 * A 4D vector represented by four floating-point values: x, y, z, and w. This class provides a set
 * of methods for performing various mathematical operations such as addition, subtraction, dot
 * product, normalization, etc., on 4D vectors.
 */
public class Vector4f {

  private float x;

  private float y;

  private float z;

  private float w;

  /** Default constructor. Initializes the vector to (0, 0, 0, 0). */
  public Vector4f() {
    this(0, 0, 0, 0);
  }

  /**
   * Constructs a vector with the specified x, y, z, and w components.
   *
   * @param x The x component of the vector.
   * @param y The y component of the vector.
   * @param z The z component of the vector.
   * @param w The w component of the vector.
   */
  public Vector4f(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  /**
   * Copy constructor. Creates a new vector that is a copy of the given vector.
   *
   * @param other The vector to copy.
   */
  public Vector4f(Vector4f other) {
    this.x = other.x;
    this.y = other.y;
    this.z = other.z;
    this.w = other.w;
  }

  /**
   * Adds the specified vector to this vector and returns the result.
   *
   * @param other The vector to add.
   * @return A new vector representing the sum of the two vectors.
   * @throws IllegalArgumentException If the given vector is null.
   */
  public Vector4f add(Vector4f other) {
    return new Vector4f(this).addLocal(other);
  }

  /**
   * Adds the specified vector to this vector in-place and returns the updated vector.
   *
   * @param other The vector to add.
   * @return The current vector after addition.
   * @throws IllegalArgumentException If the given vector is null.
   */
  public Vector4f addLocal(Vector4f other) {
    if (other == null) {
      throw new IllegalArgumentException("Other vector cannot be null.");
    }
    x += other.x;
    y += other.y;
    z += other.z;
    w += other.w;
    return this;
  }

  /**
   * Subtracts the specified vector from this vector and returns the result.
   *
   * @param other The vector to subtract.
   * @return A new vector representing the difference of the two vectors.
   * @throws IllegalArgumentException If the given vector is null.
   */
  public Vector4f subtract(Vector4f other) {
    return new Vector4f(this).subtractLocal(other);
  }

  /**
   * Subtracts the specified vector from this vector in-place and returns the updated vector.
   *
   * @param other The vector to subtract.
   * @return The current vector after subtraction.
   * @throws IllegalArgumentException If the given vector is null.
   */
  public Vector4f subtractLocal(Vector4f other) {
    if (other == null) {
      throw new IllegalArgumentException("Other vector cannot be null.");
    }
    x -= other.x;
    y -= other.y;
    z -= other.z;
    w -= other.w;
    return this;
  }

  /**
   * Multiplies this vector by a scalar and returns the result.
   *
   * @param scalar The scalar to multiply the vector by.
   * @return A new vector representing the result of the multiplication.
   */
  public Vector4f multiply(float scalar) {
    return new Vector4f(this).multiplyLocal(scalar);
  }

  /**
   * Multiplies this vector by a scalar in-place and returns the updated vector.
   *
   * @param scalar The scalar to multiply the vector by.
   * @return The current vector after multiplication.
   */
  public Vector4f multiplyLocal(float scalar) {
    x *= scalar;
    y *= scalar;
    z *= scalar;
    w *= scalar;
    return this;
  }

  /**
   * Divides this vector by a scalar and returns the result.
   *
   * @param scalar The scalar to divide the vector by.
   * @return A new vector representing the result of the division.
   * @throws ArithmeticException If the scalar is 0.
   */
  public Vector4f divide(float scalar) {
    return new Vector4f(this).divideLocal(scalar);
  }

  /**
   * Divides this vector by a scalar in-place and returns the updated vector.
   *
   * @param scalar The scalar to divide the vector by.
   * @return The current vector after division.
   * @throws ArithmeticException If the scalar is 0.
   */
  public Vector4f divideLocal(float scalar) {
    if (scalar == 0) {
      throw new ArithmeticException("Scalar cannot be 0.");
    }
    x /= scalar;
    y /= scalar;
    z /= scalar;
    w /= scalar;
    return this;
  }

  /**
   * Negates this vector and returns the result.
   *
   * @return A new vector representing the negated vector.
   */
  public Vector4f negate() {
    return new Vector4f(x, y, z, w).negateLocal();
  }

  /**
   * Negates this vector in-place and returns the updated vector.
   *
   * @return The current vector after negation.
   */
  public Vector4f negateLocal() {
    x = x == 0 ? 0 : -x;
    y = y == 0 ? 0 : -y;
    z = z == 0 ? 0 : -z;
    w = w == 0 ? 0 : -w;
    return this;
  }

  /**
   * Returns the length (magnitude) of this vector.
   *
   * @return The length of the vector.
   */
  public float length() {
    return Mathf.sqrt(lengthSquared());
  }

  /**
   * Returns the squared length (magnitude) of this vector.
   *
   * @return The squared length of the vector.
   */
  public float lengthSquared() {
    return x * x + y * y + z * z + w * w;
  }

  /**
   * Normalizes this vector and returns the result.
   *
   * @return A new vector representing the normalized vector.
   */
  public Vector4f normalize() {
    return new Vector4f(this).normalizeLocal();
  }

  /**
   * Normalizes this vector in-place and returns the updated vector.
   *
   * @return The current vector after normalization.
   */
  public Vector4f normalizeLocal() {
    float length = length();
    if (length == 0) {
      set(0, 0, 0, 0);
    } else {
      divideLocal(length);
    }
    return this;
  }

  /**
   * Checks if the vector is a zero vector (x, y, z, w all equal to 0).
   *
   * @return True if the vector is zero, false otherwise.
   */
  public boolean isZero() {
    return x == 0 && y == 0 && z == 0 && w == 0;
  }

  /**
   * Returns the dot product of this vector and another vector.
   *
   * @param other The other vector.
   * @return The dot product of the two vectors.
   * @throws IllegalArgumentException If the given vector is null.
   */
  public float dot(Vector4f other) {
    if (other == null) {
      throw new IllegalArgumentException("Other vector cannot be null.");
    }
    return x * other.x + y * other.y + z * other.z + w * other.w;
  }

  /**
   * Checks if this vector is approximately equal to another vector within a given tolerance.
   *
   * @param other The other vector.
   * @param tolerance The tolerance for comparison.
   * @return True if the vectors are equal within the tolerance, false otherwise.
   */
  public boolean isEqual(Vector4f other, float tolerance) {
    if (other == null) return false;
    if (other == this) return true;

    return Mathf.abs(x - other.x) <= tolerance
        && Mathf.abs(y - other.y) <= tolerance
        && Mathf.abs(z - other.z) <= tolerance
        && Mathf.abs(w - other.w) <= tolerance;
  }

  /**
   * Linearly interpolates between this vector and another vector by a factor t. The parameter t is
   * clamped between [0...1].
   *
   * @param other The other vector.
   * @param t The interpolation factor (0.0 <= t <= 1.0).
   * @return A new vector representing the result of the interpolation.
   * @throws IllegalArgumentException If the given vector is null.
   */
  public Vector4f lerp(Vector4f other, float t) {
    if (other == null) {
      throw new IllegalArgumentException("Other vector cannot be null.");
    }
    float lerpedX = Mathf.lerp(x, other.x, t);
    float lerpedY = Mathf.lerp(y, other.y, t);
    float lerpedZ = Mathf.lerp(z, other.z, t);
    float lerpedW = Mathf.lerp(w, other.w, t);
    return new Vector4f(lerpedX, lerpedY, lerpedZ, lerpedW);
  }

  /**
   * Performs linear interpolation between this vector and the specified vector using the given
   * factor {@code t}. Unlike clamped interpolation, {@code t} is not restricted to the range [0,
   * 1], allowing extrapolation.
   *
   * <ul>
   *   <li>When {@code t = 0}, the method returns this vector.
   *   <li>When {@code t = 1}, the method returns the other vector.
   *   <li>Values of {@code t} outside [0, 1] produce extrapolated results.
   * </ul>
   *
   * @param other the target vector to interpolate towards.
   * @param t the interpolation factor, which is not clamped.
   * @return a new vector representing the interpolated or extrapolated result.
   * @throws IllegalArgumentException if the {@code other} vector is {@code null}.
   */
  public Vector4f lerpUnclamped(Vector4f other, float t) {
    if (other == null) {
      throw new IllegalArgumentException("Other vector cannot be null.");
    }
    float lerpedX = Mathf.lerpUnclamped(x, other.x, t);
    float lerpedY = Mathf.lerpUnclamped(y, other.y, t);
    float lerpedZ = Mathf.lerpUnclamped(z, other.z, t);
    float lerpedW = Mathf.lerpUnclamped(w, other.w, t);
    return new Vector4f(lerpedX, lerpedY, lerpedZ, lerpedW);
  }

  /**
   * Returns the distance between this vector and another vector.
   *
   * @param other The other vector.
   * @return The distance between the two vectors.
   * @throws IllegalArgumentException If the given vector is null.
   */
  public float distanceTo(Vector4f other) {
    return Mathf.sqrt(distanceSquaredTo(other));
  }

  /**
   * Returns the squared distance between this vector and another vector.
   *
   * @param other The other vector.
   * @return The squared distance between the two vectors.
   * @throws IllegalArgumentException If the given vector is null.
   */
  public float distanceSquaredTo(Vector4f other) {
    if (other == null) {
      throw new IllegalArgumentException("Other vector cannot be null.");
    }
    return (x - other.x) * (x - other.x)
        + (y - other.y) * (y - other.y)
        + (z - other.z) * (z - other.z)
        + (w - other.w) * (w - other.w);
  }

  /**
   * Returns the angle (in radians) between this vector and another vector.
   *
   * @param other The other vector.
   * @return The angle between the two vectors in radians.
   * @throws IllegalArgumentException If the given vector is null or if either vector has zero
   *     magnitude.
   */
  public float angleBetween(Vector4f other) {
    // For angleBetween might be alternative mathematical approaches
    // that could be explored for efficiency or numerical stability.

    if (other == null) {
      throw new IllegalArgumentException("Cannot calculate angle with null vector.");
    }

    float magnitude1 = this.length();
    float magnitude2 = other.length();

    if (magnitude1 == 0 || magnitude2 == 0) {
      throw new IllegalArgumentException("Cannot calculate angle with magnitude equals to zero.");
    }

    float dotProduct = this.dot(other);
    float cosTheta = dotProduct / (magnitude1 * magnitude2);

    return Mathf.acos(Mathf.clamp(cosTheta, -1.0f, 1.0f));
  }

  /**
   * Projects this vector onto another vector. The projection of vector A onto vector B is
   * calculated using the formula: (A . B) / (B . B) * B, where A is the current vector and B is the
   * vector onto which the projection is being made. This method returns a new vector that
   * represents the result of the projection.
   *
   * @param other The vector onto which the current vector will be projected.
   * @return A new vector representing the projection of this vector onto the other vector.
   * @throws IllegalArgumentException If the given vector is null or if the other vector has zero
   *     length.
   */
  public Vector4f projectOnto(Vector4f other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot project onto a null vector.");
    }

    float dotProduct = this.dot(other);
    float magnitudeSquared = other.lengthSquared();

    if (magnitudeSquared == 0) {
      throw new IllegalArgumentException("Cannot project onto a zero vector.");
    }

    float scalar = dotProduct / magnitudeSquared;
    return new Vector4f(other).multiplyLocal(scalar);
  }

  /**
   * Reflects this vector around the given normal vector.
   *
   * <p>Reflection is the process of "bouncing" a vector off a surface defined by the normal vector,
   * such that the angle of incidence equals the angle of reflection. This is commonly used in
   * physics simulations, computer graphics, and geometric calculations.
   *
   * <p>The reflection formula is:
   *
   * <pre>
   * R = V - 2 * (V ⋅ N) * N
   * </pre>
   *
   * where:
   *
   * <ul>
   *   <li><b>R</b>: Reflected vector
   *   <li><b>V</b>: Original vector (this vector)
   *   <li><b>N</b>: Normal vector (should be normalized to ensure accurate results)
   * </ul>
   *
   * This method computes the reflection of the current vector based on the specified normal and
   * returns the resulting vector.
   *
   * @param normal the normal vector around which this vector will be reflected. Must not be {@code
   *     null} or a zero vector.
   * @return a new {@code Vector4f} instance representing the reflected vector.
   * @throws IllegalArgumentException if the {@code normal} vector is {@code null} or a zero vector.
   */
  public Vector4f reflect(Vector4f normal) {
    if (normal == null) {
      throw new IllegalArgumentException("Normal cannot be null.");
    }
    if (normal.isZero()) {
      throw new IllegalArgumentException("Normal cannot be zero vertor.");
    }
    return this.subtract(normal.multiply(2 * this.dot(normal)));
  }

  /**
   * Returns the x component of the vector.
   *
   * @return The x component of the vector.
   */
  public float getX() {
    return x;
  }

  /**
   * Sets the x component of the vector.
   *
   * @param x The new value for the x component.
   */
  public void setX(float x) {
    this.x = x;
  }

  /**
   * Returns the y component of the vector.
   *
   * @return The y component of the vector.
   */
  public float getY() {
    return y;
  }

  /**
   * Sets the y component of the vector.
   *
   * @param y The new value for the y component.
   */
  public void setY(float y) {
    this.y = y;
  }

  /**
   * Returns the z component of the vector.
   *
   * @return The z component of the vector.
   */
  public float getZ() {
    return z;
  }

  /**
   * Sets the z component of the vector.
   *
   * @param z The new value for the z component.
   */
  public void setZ(float z) {
    this.z = z;
  }

  /**
   * Returns the w component of the vector.
   *
   * @return The w component of the vector.
   */
  public float getW() {
    return w;
  }

  /**
   * Sets the w component of the vector.
   *
   * @param w The new value for the w component.
   */
  public void setW(float w) {
    this.w = w;
  }

  /**
   * Sets the components of this vector to the specified values.
   *
   * @param x The new x component.
   * @param y The new y component.
   * @param z The new z component.
   * @param w The new w component.
   */
  public void set(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  /**
   * Creates and returns a new copy of this vector. The clone method creates a new instance of the
   * vector, with the same component values. This allows for independent modification of the cloned
   * vector without affecting the original.
   *
   * @return A new instance of the vector with the same component values as the current vector.
   */
  @Override
  public Vector4f clone() {
    return new Vector4f(this);
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
    int result = Float.floatToIntBits(x);
    result = 31 * result + Float.floatToIntBits(y);
    result = 31 * result + Float.floatToIntBits(z);
    result = 31 * result + Float.floatToIntBits(w);
    return result;
  }

  /**
   * Compares this vector to another object for equality. Two vectors are considered equal if they
   * have the same class type and their corresponding components (x, y, z, and w) are equal, taking
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
    Vector4f other = (Vector4f) obj;
    return Float.floatToIntBits(x) == Float.floatToIntBits(other.x)
        && Float.floatToIntBits(y) == Float.floatToIntBits(other.y)
        && Float.floatToIntBits(z) == Float.floatToIntBits(other.z)
        && Float.floatToIntBits(w) == Float.floatToIntBits(other.w);
  }

  /**
   * Returns a string representation of this vector.
   *
   * @return A string representing the vector.".
   */
  @Override
  public String toString() {
    return "Vector4f [x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
  }
}
