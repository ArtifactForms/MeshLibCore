package engine.resources;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {

  private static TextureManager instance;

  private TextureLoader imageLoader;

  private final Map<String, Texture> resourceCache = new HashMap<>();

  private TextureManager() {}

  public static TextureManager getInstance() {
    if (instance == null) {
      instance = new TextureManager();
    }
    return instance;
  }

  public void setTextureLoader(TextureLoader loader) {
    this.imageLoader = loader;
  }

  public Texture loadTexture(String path) {
    if (resourceCache.containsKey(path)) {
      return resourceCache.get(path); // Return cached resource
    }

    if (imageLoader == null) {
      throw new IllegalStateException("ImageLoader is not set!");
    }

    Texture texture = imageLoader.loadTexture(path);
    resourceCache.put(path, texture);

    return texture;
  }

  public void unloadImage(String path) {
    resourceCache.remove(path); // Optionally handle cleanup for backend-specific resources
  }
}
