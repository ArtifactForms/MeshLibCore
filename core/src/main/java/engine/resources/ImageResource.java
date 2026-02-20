package engine.resources;

public class ImageResource implements Resource {
    
  private Object image;
  
  private String path;

  @Override
  public void load(String path) {
    this.image = ResourceManager.getInstance().loadImage(path);
    this.path = path;
  }

  @Override
  public void unload() {
    ResourceManager.getInstance().unloadImage(path);
    this.image = null;
  }

  @Override
  public boolean isLoaded() {
    return image != null;
  }

  public Object getImage() {
    return image;
  }
}
