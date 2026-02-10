package mesh.modifier.transform;

import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * A mesh modifier that applies a rotation to a {@link Mesh3D} around a given {@link TransformAxis}.
 *
 * <p>This modifier acts as a convenience wrapper that delegates the actual rotation logic to
 * axis-specific rotation modifiers (e.g. {@code RotateXModifier}, {@code RotateYModifier}, {@code
 * RotateZModifier}).
 *
 * <p>The rotation angle is interpreted in radians unless stated otherwise by the underlying
 * rotation modifier implementations.
 */
public class RotateModifier implements IMeshModifier {

  /** Rotation angle to apply. */
  private float angle;

  /** Axis around which the mesh will be rotated. */
  private TransformAxis axis;

  /**
   * Creates a new {@code RotateModifier}.
   *
   * @param angle the rotation angle
   * @param axis the axis around which to rotate the mesh
   * @throws IllegalArgumentException if {@code axis} is {@code null}
   */
  public RotateModifier(float angle, TransformAxis axis) {
    setAngle(angle);
    setAxis(axis);
  }

  /**
   * Applies the rotation to the given mesh.
   *
   * <p>Internally, this method delegates to an axis-specific rotation modifier based on the
   * configured {@link TransformAxis}.
   *
   * @param mesh the mesh to modify
   * @return the rotated mesh
   */
  @Override
  public Mesh3D modify(Mesh3D mesh) {
    if (axis == TransformAxis.X) return new RotateXModifier(angle).modify(mesh);
    if (axis == TransformAxis.Y) return new RotateYModifier(angle).modify(mesh);
    if (axis == TransformAxis.Z) return new RotateYModifier(angle).modify(mesh);

    return mesh;
  }

  /**
   * Returns the rotation angle.
   *
   * @return the rotation angle
   */
  public float getAngle() {
    return angle;
  }

  /**
   * Sets the rotation angle.
   *
   * @param angle the rotation angle to use
   */
  public void setAngle(float angle) {
    this.angle = angle;
  }

  /**
   * Returns the axis around which the mesh will be rotated.
   *
   * @return the rotation axis
   */
  public TransformAxis getAxis() {
    return axis;
  }

  /**
   * Sets the rotation axis.
   *
   * @param axis the axis to rotate around
   * @throws IllegalArgumentException if {@code axis} is {@code null}
   */
  public void setAxis(TransformAxis axis) {
    if (axis == null) {
      throw new IllegalArgumentException("Axis cannot be null.");
    }
    this.axis = axis;
  }
}
