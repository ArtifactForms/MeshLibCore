package engine.resources;

import java.awt.Image;

public interface TextureLoader {

  Texture loadTexture(String filePath);
  
  Texture createTexture(Image image);
  
}
