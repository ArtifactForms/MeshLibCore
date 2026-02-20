package engine.resources;

/**
 * Enum representing the various filter modes available for texture sampling. These filter modes
 * determine how textures are sampled and filtered when applied to 3D models or surfaces, affecting
 * the appearance of textures when viewed at different distances or angles.
 *
 * <p>The available filter modes are:
 *
 * <ul>
 *   <li><strong>POINT:</strong> A basic, nearest-neighbor sampling method where the texture pixel
 *       closest to the screen pixel is selected.
 *   <li><strong>LINEAR:</strong> A linear interpolation method that smooths between the two nearest
 *       texture pixels to create a blend.
 *   <li><strong>BILINEAR:</strong> A more advanced version of linear interpolation that considers
 *       the four nearest texture pixels, interpolating in both x and y directions.
 *   <li><strong>TRILINEAR:</strong> An extension of bilinear filtering that interpolates between
 *       multiple mipmap levels, providing smoother transitions between textures at different
 *       distances from the viewer.
 * </ul>
 *
 * Each mode offers a different trade-off between performance and visual quality.
 *
 * @see <a href="https://www.opengl.org/wiki/Texture_Filtering">OpenGL Wiki on Texture Filtering</a>
 */
public enum FilterMode {
  /**
   * Nearest-neighbor filtering, where the closest texel (texture pixel) is chosen. Produces a
   * blocky appearance when viewed from a distance.
   */
  POINT,

  /**
   * Linear interpolation between two nearest texels, offering smoother transitions compared to
   * POINT.
   */
  LINEAR,

  /**
   * Bilinear interpolation that considers the four nearest texels, interpolating in both x and y
   * directions. Provides smoother results than LINEAR.
   */
  BILINEAR,

  /**
   * Trilinear interpolation that blends between multiple mipmap levels in addition to performing
   * bilinear interpolation on each level. It smooths transitions between textures at varying
   * distances from the camera.
   */
  TRILINEAR
}
