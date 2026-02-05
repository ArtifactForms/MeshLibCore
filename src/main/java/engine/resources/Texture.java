package engine.resources;

/**
 * Represents a generic texture in the engine. Provides methods for querying dimensions, managing
 * pixels, binding/unbinding the texture, and controlling texture sampling and filtering options.
 */
public interface Texture {

  /**
   * Gets the width of the texture in pixels.
   *
   * @return the width of the texture.
   */
  int getWidth();

  /**
   * Gets the height of the texture in pixels.
   *
   * @return the height of the texture.
   */
  int getHeight();

  /** Replaces the entire pixel buffer */
  void setPixels(int[] pixels);

  /** Updates a sub-region */
  default void setPixels(int x, int y, int width, int height, int[] pixels) {
    throw new UnsupportedOperationException("Partial update not supported");
  }

  /**
   * Binds the texture to a specific texture unit for rendering.
   *
   * @param unit the texture unit to bind to (e.g., 0 for GL_TEXTURE0).
   */
  void bind(int unit);

  /** Unbinds the texture from the current texture unit. */
  void unbind();

  /** Deletes the texture and releases any resources associated with it. */
  void delete();

  /**
   * Gets the current filter mode of the texture.
   *
   * @return the {@link FilterMode} applied to the texture.
   */
  FilterMode getFilterMode();

  /**
   * Sets the filter mode for the texture, determining how it is sampled.
   *
   * @param filterMode the new {@link FilterMode} to apply.
   */
  void setFilterMode(FilterMode filterMode);

  /**
   * Gets the current texture wrapping mode.
   *
   * <p>The texture wrap mode determines how the texture is applied when texture coordinates exceed
   * the [0, 1] range. For example, in {@link TextureWrapMode#REPEAT}, the texture will repeat,
   * whereas in {@link TextureWrapMode#CLAMP}, the texture will extend the edge pixels.
   *
   * @return the {@link TextureWrapMode} currently applied to the texture.
   */
  TextureWrapMode getTextureWrapMode();

  /**
   * Sets the texture wrapping mode.
   *
   * <p>This method controls how the texture is mapped outside the standard [0, 1] texture
   * coordinate range. Use {@link TextureWrapMode#REPEAT} to tile the texture across a surface, or
   * {@link TextureWrapMode#CLAMP} to stretch the texture's edge pixels when coordinates exceed the
   * valid range.
   *
   * @param textureWrapMode the new {@link TextureWrapMode} to apply to the texture.
   */
  void setTextureWrapMode(TextureWrapMode textureWrapMode);

  /**
   * Gets the underlying backend texture implementation. This is useful for accessing
   * engine-specific or platform-specific features.
   *
   * @return the backend texture object.
   */
  Texture getBackendTexture();
}
