package engine.resources;

public interface ImageLoader {

  Object loadImage(String path); // Returns the backend-specific image object
}
