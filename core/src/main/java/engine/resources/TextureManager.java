package engine.resources;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

public class TextureManager {

  private static TextureManager instance;

  private TextureLoader textureLoader;

  private final Map<String, Texture> resourceCache = new HashMap<>();

  private TextureManager() {}

  public static TextureManager getInstance() {
    if (instance == null) {
      instance = new TextureManager();
    }
    return instance;
  }

  public void setTextureLoader(TextureLoader loader) {
    this.textureLoader = loader;
  }

  public Texture loadTexture(String path) {
    if (resourceCache.containsKey(path)) {
      return resourceCache.get(path); // Return cached resource
    }

    if (textureLoader == null) {
      throw new IllegalStateException("TextureLoader is not set.");
    }

    Texture texture = textureLoader.loadTexture(path);
    resourceCache.put(path, texture);

    return texture;
  }

  public void unloadImage(String path) {
    resourceCache.remove(path); // Optionally handle cleanup for backend-specific resources
  }

  public Texture createTexture(Image image) {
    return textureLoader.createTexture(image);
  }

  public Texture createTexture(int width, int height) {
    return textureLoader.createTexture(width, height);
  }
}
