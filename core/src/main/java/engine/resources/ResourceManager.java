package engine.resources;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager {

  private static ResourceManager instance;

  private ImageLoader imageLoader;

  private final Map<String, Image> resourceCache = new HashMap<>();

  private ResourceManager() {}

  public static ResourceManager getInstance() {
    if (instance == null) {
      instance = new ResourceManager();
    }
    return instance;
  }

  public void setImageLoader(ImageLoader loader) {
    this.imageLoader = loader;
  }

  public Image loadImage(String path) {
    if (resourceCache.containsKey(path)) {
      return resourceCache.get(path); // Return cached resource
    }

    if (imageLoader == null) {
      throw new IllegalStateException("ImageLoader is not set!");
    }

    Object obj = imageLoader.loadImage(path);
    Image image = new Image(obj);

    resourceCache.put(path, image);

    return image;
  }

  public void unloadImage(String path) {
    resourceCache.remove(path); // Optionally handle cleanup for backend-specific resources
  }
}
