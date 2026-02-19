package engine.processing;

import java.awt.Image;

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

  @Override
  public Texture createTexture(Image image) {
    PImage pImage = new PImage(image);
    return new ProcessingTexture(pImage);
  }

  @Override
  public Texture createTexture(int width, int height) {
    PImage pImage = new PImage(width, height);
    return new ProcessingTexture(pImage);
  }
}
