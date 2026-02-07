package mesh;

/**
 * Defines a 2D plane in 3D space by specifying the two axes it spans.
 *
 * <p>This enum is typically used for operations that work on planar projections of 3D data, such
 * as:
 *
 * <ul>
 *   <li>Mesh slicing
 *   <li>UV generation
 *   <li>2D projections of 3D geometry
 * </ul>
 */
public enum Axis {

  /**
   * Plane spanned by the X and Y axes.
   *
   * <p>The Z axis is treated as the perpendicular (normal) axis.
   */
  XY,

  /**
   * Plane spanned by the X and Z axes.
   *
   * <p>The Y axis is treated as the perpendicular (normal) axis.
   */
  XZ,

  /**
   * Plane spanned by the Y and Z axes.
   *
   * <p>The X axis is treated as the perpendicular (normal) axis.
   */
  YZ
}
