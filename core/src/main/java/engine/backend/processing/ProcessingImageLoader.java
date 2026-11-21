package engine.backend.processing;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

import engine.resources.ImageLoader;
import processing.core.PApplet;
import processing.core.PImage;

public class ProcessingImageLoader implements ImageLoader {

  private final PApplet parent;

  public ProcessingImageLoader(PApplet parent) {
    this.parent = parent;
  }

  @Override
  public Object loadImage(String path) {
    String resourcePath = "/resources/images/" + path;

    try (InputStream stream = ProcessingImageLoader.class.getResourceAsStream(resourcePath)) {

      if (stream == null) {
        throw new IllegalArgumentException("Image resource not found: " + resourcePath);
      }

      BufferedImage bufferedImage = ImageIO.read(stream);

      if (bufferedImage == null) {
        throw new IllegalArgumentException("Could not decode image: " + resourcePath);
      }

      PImage image = new PImage(bufferedImage.getWidth(), bufferedImage.getHeight(), PApplet.ARGB);

      bufferedImage.getRGB(0, 0, image.width, image.height, image.pixels, 0, image.width);

      image.updatePixels();

      return image;

    } catch (Exception e) {
      throw new RuntimeException("Failed to load image: " + resourcePath, e);
    }
  }
}
