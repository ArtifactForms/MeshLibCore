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

  /**
   * Updates the texture's pixel data.
   *
   * @param pixels an array of pixel data, typically in ARGB or RGBA format, depending on the
   *     engine's requirements.
   */
  void setPixels(int[] pixels);

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
   * Gets the underlying backend texture implementation. This is useful for accessing
   * engine-specific or platform-specific features.
   *
   * @return the backend texture object.
   */
  Texture getBackendTexture();
}
