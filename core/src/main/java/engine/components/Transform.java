package engine.components;

import engine.rendering.Graphics;
import math.Vector3f;

/**
 * Represents a transformation in 3D space, encapsulating position, rotation, and scale.
 *
 * <h2>Coordinate System Convention</h2>
 *
 * <p>This engine uses a <b>right-handed coordinate system</b> with a <b>Y-down world
 * convention</b>, aligned with Processing's default screen space.
 *
 * <pre>
 * +X → right
 * -Y → up
 * +Y → down
 * -Z → forward
 * </pre>
 *
 * <p>As a consequence:
 *
 * <ul>
 *   <li>The global world-up vector is {@code (0, -1, 0)}
 *   <li>{@link #getUp()} returns a vector pointing in negative Y direction
 *   <li>Movement, camera logic, and look-at calculations rely exclusively on the orientation
 *       vectors derived from this transform
 * </ul>
 *
 * <p><b>Important:</b> This convention is used consistently throughout the engine. Hard-coded up
 * vectors such as {@code (0, 1, 0)} or {@code (0, -1, 0)} must not be used outside this class. All
 * directional queries must go through {@link #getForward()}, {@link #getRight()}, and {@link
 * #getUp()}.
 *
 * @see Vector3f
 * @see Graphics
 */
public class Transform extends AbstractComponent {

  /**
   * Global world-up vector for the engine.
   *
   * <p>This engine uses a Y-down coordinate system, therefore the world-up direction points along
   * negative Y.
   *
   * <pre>
   * WORLD_UP = (0, -1, 0)
   * </pre>
   *
   * <p>This vector is used as the stable reference for computing the local right and up vectors via
   * cross products.
   */
  private static final Vector3f WORLD_UP = new Vector3f(0, -1, 0);

  /** The position of this transform in 3D space. */
  private Vector3f position;

  /** The rotation (in radians) of this transform around the X, Y, and Z axes. */
  private Vector3f rotation;

  /** The scaling factors along the X, Y, and Z axes. */
  private Vector3f scale;

  /**
   * Constructs a new {@code Transform} with default position, rotation, and scale values.
   *
   * <p>The default position is set to (0, 0, 0), the default rotation is (0, 0, 0), and the default
   * scale is (1, 1, 1).
   */
  public Transform() {
    this.position = new Vector3f();
    this.rotation = new Vector3f();
    this.scale = new Vector3f(1, 1, 1);
  }

  /**
   * Applies this transformation to the given graphics context.
   *
   * <p>This method translates the context to the object's position, applies rotations around the X,
   * Y, and Z axes, and scales the object using the defined scale values.
   *
   * @param g The graphics context to which this transformation is applied.
   */
  public void apply(Graphics g) {
    g.translate(position.x, position.y, position.z);
    g.scale(scale.x, scale.y, scale.z);
    g.rotateX(rotation.x);
    g.rotateY(rotation.y);
    g.rotateZ(rotation.z);
  }

  /**
   * Translates this transformation by the given delta vector.
   *
   * <p>This modifies the position of the transform by adding the provided vector to the current
   * position.
   *
   * @param delta The vector representing the change in position.
   */
  public void translate(Vector3f delta) {
    this.position.addLocal(delta);
  }

  /**
   * Translates this transformation by the given delta vector.
   *
   * <p>This modifies the position of the transform by adding the provided vector to the current
   * position.
   *
   * @param deltaX x component of the delta vector
   * @param deltaY y component of the delta vector
   * @param deltaZ z component of the delta vector
   */
  public void translate(float deltaX, float deltaY, float deltaZ) {
    this.position.addLocal(deltaX, deltaY, deltaZ);
  }

  /**
   * Rotates this transformation by the given delta vector (in radians).
   *
   * <p>This modifies the rotation of the transform by adding the provided vector's values to the
   * current rotation.
   *
   * @param delta The vector representing the change in rotation (in radians).
   */
  public void rotate(Vector3f delta) {
    this.rotation.addLocal(delta);
  }

  /**
   * Rotates this transformation by the given delta values for each axis (in radians).
   *
   * @param x The change in rotation around the X-axis (in radians).
   * @param y The change in rotation around the Y-axis (in radians).
   * @param z The change in rotation around the Z-axis (in radians).
   */
  public void rotate(float x, float y, float z) {
    this.rotation.addLocal(x, y, z);
  }

  /**
   * Scales this transformation by the provided scaling factors.
   *
   * <p>This modifies the scale of the transform by multiplying the current scale values by the
   * provided factors.
   *
   * @param factor The vector representing the scale factors to apply along each axis.
   */
  public void scale(Vector3f factor) {
    this.scale.multLocal(factor);
  }

  /**
   * Retrieves a copy of the position vector.
   *
   * @return A new Vector3f instance representing the current position.
   */
  public Vector3f getPosition() {
    return new Vector3f(position);
  }

  /**
   * Sets the position of this transform to a specific value.
   *
   * @param position The new position vector to set. Must not be null.
   * @throws IllegalArgumentException if the provided vector is null.
   */
  public void setPosition(Vector3f position) {
    if (position == null) {
      throw new IllegalArgumentException("Position cannot be null.");
    }
    this.position.set(position);
  }

  /**
   * Sets the position of this transform to the specified coordinates.
   *
   * <p>This method updates the position vector to the provided (x, y, z) values, effectively moving
   * the object to the given location in 3D space.
   *
   * @param x The X-coordinate of the new position.
   * @param y The Y-coordinate of the new position.
   * @param z The Z-coordinate of the new position.
   */
  public void setPosition(float x, float y, float z) {
    this.position.set(x, y, z);
  }

  /**
   * Retrieves a copy of the rotation vector.
   *
   * @return A new Vector3f instance representing the current rotation.
   */
  public Vector3f getRotation() {
    return new Vector3f(rotation);
  }

  /**
   * Sets the rotation of this transform to a specific value.
   *
   * @param rotation The new rotation vector (in radians) to set. Must not be null.
   * @throws IllegalArgumentException if the provided vector is null.
   */
  public void setRotation(Vector3f rotation) {
    if (rotation == null) {
      throw new IllegalArgumentException("Rotation cannot be null.");
    }
    this.rotation.set(rotation);
  }

  /**
   * Sets the rotation of this transform to the specified angles in radians.
   *
   * <p>This method updates the rotation vector to the provided (rx, ry, rz) values, which represent
   * the rotation of the object around the X, Y, and Z axes, respectively.
   *
   * @param rx The rotation angle around the X-axis, in radians.
   * @param ry The rotation angle around the Y-axis, in radians.
   * @param rz The rotation angle around the Z-axis, in radians.
   */
  public void setRotation(float rx, float ry, float rz) {
    this.rotation.set(rx, ry, rz);
  }

  /**
   * Retrieves a copy of the scale vector.
   *
   * @return A new Vector3f instance representing the current scale.
   */
  public Vector3f getScale() {
    return new Vector3f(scale);
  }

  /**
   * Sets the scale of this transform to a specific value.
   *
   * @param scale The new scale vector to set. Must not be null.
   * @throws IllegalArgumentException if the provided vector is null.
   */
  public void setScale(Vector3f scale) {
    if (scale == null) {
      throw new IllegalArgumentException("Scale cannot be null.");
    }
    this.scale.set(scale);
  }

  /**
   * Sets the scale of this transform to the specified scale factors along each axis.
   *
   * <p>This method updates the scale vector to the provided (sx, sy, sz) values, allowing the
   * object to be scaled uniformly or non-uniformly along the X, Y, and Z axes.
   *
   * @param sx The scale factor along the X-axis.
   * @param sy The scale factor along the Y-axis.
   * @param sz The scale factor along the Z-axis.
   */
  public void setScale(float sx, float sy, float sz) {
    this.scale.set(sx, sy, sz);
  }

  /**
   * Sets a uniform scale for this transform.
   *
   * <p>This method sets the same scale factor on all three axes (X, Y, and Z), resulting in uniform
   * scaling of the object in all directions.
   *
   * @param scale The uniform scale factor to apply.
   */
  public void setScale(float scale) {
    this.scale.set(scale, scale, scale);
  }

  /**
   * Calculates the normalized forward direction vector based on the current rotation. *
   *
   * <p>This implementation is designed for a <b>Y-down coordinate system</b>. To maintain intuitive
   * movement and raycasting, the pitch component (Y) is inverted. This ensures that a positive
   * pitch rotation results in a downward-pointing vector in world space.
   *
   * <p>The vector is derived using spherical coordinates:
   *
   * <ul>
   *   <li><b>X:</b> {@code sin(yaw) * cos(pitch)}
   *   <li><b>Y:</b> {@code -sin(pitch)} (Inverted for Y-down consistency)
   *   <li><b>Z:</b> {@code -cos(yaw) * cos(pitch)} (Standard right-handed forward)
   * </ul>
   *
   * @return A normalized {@link Vector3f} representing the direction the transform is facing.
   */
  public Vector3f getForward() {
    float cosPitch = (float) Math.cos(rotation.x);
    float sinPitch = (float) Math.sin(rotation.x);
    float cosYaw = (float) Math.cos(rotation.y);
    float sinYaw = (float) Math.sin(rotation.y);

    return new Vector3f(sinYaw * cosPitch, -sinPitch, -cosYaw * cosPitch).normalizeLocal();
  }

  /**
   * Returns the right direction vector of this transform.
   *
   * <p>The right vector is computed using the cross product:
   *
   * <pre>
   * Right = WORLD_UP × Forward
   * </pre>
   *
   * <p>This formulation is consistent with a right-handed coordinate system and the engine's Y-down
   * world convention.
   *
   * @return A normalized right direction vector.
   */
  public Vector3f getRight() {
    return WORLD_UP.cross(getForward()).normalizeLocal();
  }

  /**
   * Returns the up direction vector of this transform.
   *
   * <p>The up vector is derived from the orthonormal basis:
   *
   * <pre>
   * Up = Forward × Right
   * </pre>
   *
   * <p>Due to the Y-down world convention, this vector points in the negative Y direction when the
   * transform has no rotation.
   *
   * <p>This method must be used for all vertical movement and camera up-vector queries.
   *
   * @return A normalized up direction vector.
   */
  public Vector3f getUp() {
    return getForward().cross(getRight()).normalizeLocal();
  }

  /**
   * Sets the forward direction of this transform.
   *
   * <p>This method computes the required pitch and yaw rotations to align the transform with the
   * given forward direction, assuming a Y-down coordinate system.
   *
   * <p>The provided vector must be normalized and must not be collinear with the world-up
   * direction.
   *
   * @param forward The desired forward direction.
   * @throws IllegalArgumentException if the vector is null or zero-length.
   */
  public void setForward(Vector3f forward) {
    if (forward == null || forward.lengthSquared() == 0f) {
      throw new IllegalArgumentException("Forward vector cannot be null or zero-length.");
    }

    Vector3f f = new Vector3f(forward).normalizeLocal();

    // y-down: pitch negative
    float yaw = (float) Math.atan2(f.x, -f.z);
    float pitch = (float) -Math.asin(f.y);

    rotation.set(pitch, yaw, 0f);
  }

  /**
   * Rotates this transform so that its forward direction points at the given target.
   *
   * <p>This method respects the engine's coordinate system:
   *
   * <pre>
   * +X → right
   * -Y → up
   * +Y → down
   * -Z → forward
   * </pre>
   *
   * <p>The rotation is applied by computing the forward direction and delegating to {@link
   * #setForward(Vector3f)}.
   *
   * @param target The world-space point to look at.
   * @throws IllegalArgumentException if target is null or coincides with position.
   */
  public void lookAt(Vector3f target) {
    if (target == null) {
      throw new IllegalArgumentException("Target cannot be null.");
    }

    Vector3f forward = target.subtract(position);

    if (forward.lengthSquared() == 0f) {
      throw new IllegalArgumentException("Target must not be equal to position.");
    }

    setForward(forward.normalizeLocal());
  }

  @Override
  public void onUpdate(float tpf) {}

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
