package engine.resources;

public interface Resource {

  void load(String path);

  void unload();

  boolean isLoaded();
}
