package engine.backend.processing;

import java.awt.Image;

import engine.resources.Texture;
import engine.resources.TextureLoader;
import processing.core.PApplet;
import processing.core.PImage;

public class ProcessingTextureLoader implements TextureLoader {

  @Override
  public Texture loadTexture(String filePath) {
    String fullPath = "/images/" + filePath;
    java.io.InputStream is = ProcessingTextureLoader.class.getResourceAsStream(fullPath);

    if (is == null) {
      fullPath = "/resources/images/" + filePath;
      is = ProcessingTextureLoader.class.getResourceAsStream(fullPath);
    }

    if (is == null) {
      System.err.println("File not found in either /images/ or /resources/images/: " + filePath);
      return null;
    }

    try {
      java.awt.image.BufferedImage bufferedImage = javax.imageio.ImageIO.read(is);
      is.close();

      if (bufferedImage == null) return null;

      PImage pImage = new PImage(bufferedImage.getWidth(), bufferedImage.getHeight(), PApplet.ARGB);
      bufferedImage.getRGB(0, 0, pImage.width, pImage.height, pImage.pixels, 0, pImage.width);
      pImage.updatePixels();

      return new ProcessingTexture(pImage);
    } catch (java.io.IOException e) {
      e.printStackTrace();
      return null;
    }
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
