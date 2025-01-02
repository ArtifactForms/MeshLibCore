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
   * @param unit the texture unit to bind this texture to.
   */
  @Override
  public void bind(int unit) {
    texture.bind(unit);
  }

  /** Unbinds the texture from its currently bound texture unit. */
  @Override
  public void unbind() {
    texture.unbind();
  }

  /** Deletes the texture and releases any associated resources. */
  @Override
  public void delete() {
    texture.delete();
  }

  /**
   * Updates the pixel data of the texture.
   *
   * @param pixels an array of pixel data to set for this texture.
   */
  @Override
  public void setPixels(int[] pixels) {
    texture.setPixels(pixels);
  }

  /**
   * Gets the filter mode currently applied to the texture.
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
   * @param filterMode the desired filter mode to apply to the texture.
   * @see FilterMode
   */
  @Override
  public void setFilterMode(FilterMode filterMode) {
    texture.setFilterMode(filterMode);
  }

  /**
   * Retrieves the backend texture instance used by this facade.
   *
   * @return the underlying {@link Texture} instance.
   */
  @Override
  public Texture getBackendTexture() {
    return texture;
  }
}
