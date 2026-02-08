package mesh.modifier.transform;

/**
 * Defines a principal axis in 3D space used by transform modifiers.
 *
 * <p>This enum is typically used to specify the axis along which a transformation operates, such
 * as:
 *
 * <ul>
 *   <li>Rotation around an axis
 *   <li>Mirroring across a plane perpendicular to an axis
 *   <li>Scaling or shearing along a specific direction
 * </ul>
 *
 * <p>The interpretation of the axis depends on the modifier using it. For example:
 *
 * <ul>
 *   <li>{@code MirrorModifier}: mirrors the mesh across the plane orthogonal to the axis
 *   <li>{@code RotateX/Y/ZModifier}: rotates around the corresponding axis
 * </ul>
 *
 * <p>All axes are defined in object (local) space.
 */
public enum TransformAxis {

  /** The X axis (left–right direction). */
  X,

  /** The Y axis (up–down direction). */
  Y,

  /** The Z axis (forward–backward direction). */
  Z;
}
