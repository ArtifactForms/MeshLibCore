package engine.resources;

public class Image {
    
  private final Object backendImage;

  public Image(Object backendImage) {
    this.backendImage = backendImage;
  }

  public Object getBackendImage() {
    return backendImage;
  }

  // Add methods to abstract common operations like `getWidth()`, `getHeight()`, etc.
}
