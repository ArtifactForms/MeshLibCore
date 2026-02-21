package engine.resources;

/**
 * A facade class representing a 2D texture, providing a simplified interface for creating and
 * manipulating textures in the rendering engine.
 *
 * <p>This class delegates functionality to an underlying {@link Texture} instance managed by the
 * {@link TextureManager}. It provides an abstraction to easily work with 2D textures without
 * directly handling the backend implementation.
 */
public class Texture2D implements Texture {

  private Texture texture;

  /**
   * Constructs a 2D texture with the specified width and height.
   *
   * <p>The texture is created using the {@link TextureManager} singleton instance. The width and
   * height must be positive integers; otherwise, an exception is thrown.
   *
   * @param width the width of the texture in pixels. Must be greater than 0.
   * @param height the height of the texture in pixels. Must be greater than 0.
   * @throws IllegalArgumentException if the width or height is less than or equal to zero.
   */
  public Texture2D(int width, int height) {
    if (width <= 0) {
      throw new IllegalArgumentException("Width cannot be negative or zero.");
    }
    if (height <= 0) {
      throw new IllegalArgumentException("Height cannot be negative or zero.");
    }
    texture = TextureManager.getInstance().createTexture(width, height);
  }

  /**
   * Gets the width of the texture in pixels.
   *
   * @return the width of the texture.
   */
  @Override
  public int getWidth() {
    return texture.getWidth();
  }

  /**
   * Gets the height of the texture in pixels.
   *
   * @return the height of the texture.
   */
  @Override
  public int getHeight() {
    return texture.getHeight();
  }

  /**
   * Binds the texture to the specified texture unit for rendering.
   *
   * <p>Binding a texture makes it available for use in subsequent rendering operations. The texture
   * unit represents a specific slot in the graphics pipeline to which the texture is assigned.
   *
   * @param unit the texture unit to bind this texture to.
   * @see #unbind()
   */
  @Override
  public void bind(int unit) {
    texture.bind(unit);
  }

  /**
   * Unbinds the texture from its currently bound texture unit.
   *
   * <p>Once a texture is unbound, it is no longer available for rendering until it is bound again.
   */
  @Override
  public void unbind() {
    texture.unbind();
  }

  /**
   * Deletes the texture and releases any associated resources.
   *
   * <p>This method should be called when the texture is no longer needed to free up GPU memory.
   * After a texture is deleted, it cannot be used for rendering unless it is recreated.
   */
  @Override
  public void delete() {
    texture.delete();
  }

  /**
   * Updates the pixel data of the texture.
   *
   * <p>This method replaces the existing texture data with new pixel values. The pixel data must
   * match the dimensions of the texture.
   *
   * @param pixels an array of pixel data to set for this texture.
   * @throws IllegalArgumentException if the pixel array size does not match the texture dimensions.
   */
  @Override
  public void setPixels(int[] pixels) {
    texture.setPixels(pixels);
  }

  /**
   * Gets the filter mode currently applied to the texture.
   *
   * <p>The filter mode determines how the texture is sampled when it is magnified or minified
   * during rendering. Common filter modes include nearest-neighbor and linear filtering.
   *
   * @return the filter mode of the texture.
   * @see FilterMode
   */
  @Override
  public FilterMode getFilterMode() {
    return texture.getFilterMode();
  }

  /**
   * Sets the filter mode for the texture.
   *
   * <p>The filter mode controls how the texture is sampled when rendered at different sizes. For
   * example, linear filtering smooths the texture when scaled, while nearest-neighbor filtering
   * preserves sharp edges.
   *
   * @param filterMode the desired filter mode to apply to the texture.
   * @see FilterMode
   */
  @Override
  public void setFilterMode(FilterMode filterMode) {
    texture.setFilterMode(filterMode);
  }

  /**
   * Gets the texture wrapping mode currently applied to this texture.
   *
   * <p>The texture wrap mode controls how the texture is applied when texture coordinates exceed
   * the [0, 1] range. For example, in {@link TextureWrapMode#REPEAT}, the texture will tile
   * infinitely, whereas in {@link TextureWrapMode#CLAMP}, the texture's edge pixels are stretched.
   *
   * @return the {@link TextureWrapMode} applied to the texture.
   */
  @Override
  public TextureWrapMode getTextureWrapMode() {
    return texture.getTextureWrapMode();
  }

  /**
   * Sets the texture wrapping mode for this texture.
   *
   * <p>This method controls how the texture is mapped outside the standard [0, 1] texture
   * coordinate range. Use {@link TextureWrapMode#REPEAT} to tile the texture across a surface, or
   * {@link TextureWrapMode#CLAMP} to stretch the texture's edge pixels when coordinates exceed the
   * valid range.
   *
   * @param textureWrapMode the new {@link TextureWrapMode} to apply to the texture.
   */
  @Override
  public void setTextureWrapMode(TextureWrapMode textureWrapMode) {
    this.texture.setTextureWrapMode(textureWrapMode);
  }

  /**
   * Retrieves the backend texture instance used by this facade.
   *
   * <p>This method provides access to the underlying texture object managed by the {@link
   * TextureManager}. It is useful for accessing low-level or engine-specific features not exposed
   * by the {@link Texture2D} class.
   *
   * @return the underlying {@link Texture} instance.
   */
  @Override
  public Texture getBackendTexture() {
    return texture;
  }
}
