package engine.resources;

public interface Texture {

  int getWidth();

  int getHeight();

  void bind(int unit); // Bind to a specific texture unit

  void unbind();

  void delete();

  void setPixels(int[] pixels);
  
  FilterMode getFilterMode();
  
  void setFilterMode(FilterMode filterMode);
}
