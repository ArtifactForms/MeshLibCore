package engine.processing;

import engine.resources.Texture;
import engine.resources.TextureLoader;
import processing.core.PApplet;
import processing.core.PImage;

public class ProcessingTextureLoader implements TextureLoader {

  private final PApplet parent;

  public ProcessingTextureLoader(PApplet parent) {
    this.parent = parent;
  }

  @Override
  public Texture loadTexture(String filePath) {
    PImage image =
        parent.loadImage(
            ProcessingTextureLoader.class
                .getClassLoader()
                .getResource("images/" + filePath)
                .getPath());
    ProcessingTexture texture = new ProcessingTexture(image);
    return texture;
  }
}
