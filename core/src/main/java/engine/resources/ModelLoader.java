package engine.resources;

public class ModelLoader {

  // Core method to load models
  public static Model load(String filePath, String extension) {
    //    String extension = getFileExtension(filePath);
    ModelLoaderStrategy loader = getLoaderForExtension(extension);
    return loader.load(filePath);
  }

  // Helper to get appropriate loader
  private static ModelLoaderStrategy getLoaderForExtension(String extension) {
    switch (extension.toLowerCase()) {
      case "obj":
        return new OBJLoader();
      case "gltf":
        return new GLTFLoader();
        //      case "fbx":
        //        return new FBXLoader();
      default:
        throw new UnsupportedOperationException("Unsupported format: " + extension);
    }
  }
}
