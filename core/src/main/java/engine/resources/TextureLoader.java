package engine.resources;

import java.awt.Image;

/**
 * The {@code TextureLoader} interface provides methods for creating and loading textures in the
 * rendering engine. It abstracts the process of loading textures from file paths or creating them
 * dynamically from images or specific dimensions.
 *
 * <p>This interface allows flexibility in managing texture creation from various sources, ensuring
 * that texture resources are efficiently loaded and handled within the engine.
 */
public interface TextureLoader {

  /**
   * Loads a texture from the specified file path.
   *
   * @param filePath the path to the texture file (e.g., PNG, JPG) on the local file system.
   * @return a {@link Texture} instance representing the loaded texture.
   */
  Texture loadTexture(String filePath);

  /**
   * Creates a texture from an {@link Image} instance.
   *
   * <p>This method is useful for dynamically generating textures from in-memory images, such as
   * those created at runtime or loaded from external sources.
   *
   * @param image the {@link Image} instance to create the texture from.
   * @return a {@link Texture} instance representing the created texture.
   */
  Texture createTexture(Image image);

  /**
   * Creates an empty texture with the specified width and height.
   *
   * <p>This method is typically used for generating blank textures that can later be updated with
   * pixel data.
   *
   * @param width the width of the texture in pixels.
   * @param height the height of the texture in pixels.
   * @return a {@link Texture} instance representing the created empty texture.
   */
  Texture createTexture(int width, int height);
}
