package engine.render;

/**
 * Defines how surface normals are interpreted during lighting calculations.
 *
 * <p>Shading modes control the visual appearance of a rendered surface by determining whether
 * lighting is evaluated per face or smoothly interpolated across vertices.
 *
 * <p>This enum is typically used by materials or render states and does not modify mesh geometry
 * directly.
 */
public enum Shading {

  /**
   * Flat shading.
   *
   * <p>Lighting is evaluated per face using a single normal for the entire primitive, resulting in
   * a faceted appearance.
   */
  FLAT,

  /**
   * Smooth shading.
   *
   * <p>Lighting is evaluated per vertex and interpolated across the surface, producing a smooth
   * appearance between adjacent faces.
   */
  SMOOTH
}
