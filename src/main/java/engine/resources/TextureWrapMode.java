package engine.resources;

/**
 * Represents the texture wrapping mode used to determine how textures are applied when texture
 * coordinates exceed the standard [0, 1] range.
 *
 * <p>This enum provides two wrapping modes:
 *
 * <ul>
 *   <li>{@link #CLAMP} - Clamps the texture coordinates to the edges of the texture. This prevents
 *       repetition and ensures that the texture borders extend outward.
 *   <li>{@link #REPEAT} - Repeats the texture in both directions when the texture coordinates
 *       exceed the [0, 1] range. This is useful for creating seamless, tiled patterns.
 * </ul>
 *
 * <p>Depending on the rendering backend, more wrapping modes may be supported. This abstraction
 * allows for easy integration with different graphics libraries while keeping the codebase flexible
 * and maintainable.
 *
 * @see engine.texture.Texture
 */
public enum TextureWrapMode {

  /**
   * Clamps texture coordinates to the edges of the texture.
   *
   * <p>This mode ensures that any texture coordinate outside the [0, 1] range will be mapped to the
   * nearest edge of the texture, effectively stretching the border pixels. It is commonly used to
   * avoid visible seams on models when the texture should not repeat.
   *
   * <p><b>Example Use Case:</b>
   *
   * <ul>
   *   <li>Skyboxes
   *   <li>UI elements
   *   <li>Billboards
   * </ul>
   */
  CLAMP,

  /**
   * Repeats the texture in both directions when the texture coordinates exceed the [0, 1] range.
   * This mode is useful for creating seamless, tiled patterns such as terrain, walls, or floors in
   * games and 3D environments.
   *
   * <p><b>Example Use Case:</b>
   *
   * <ul>
   *   <li>Terrain textures
   *   <li>Repeating patterns on walls, floors, or ceilings
   *   <li>Cloth or fabric simulations
   * </ul>
   */
  REPEAT
}
