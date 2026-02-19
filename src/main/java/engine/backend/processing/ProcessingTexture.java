package engine.backend.processing;

import engine.resources.FilterMode;
import engine.resources.Texture;
import engine.resources.TextureWrapMode;
import processing.core.PImage;

public class ProcessingTexture implements Texture {

  private final PImage image;

  private FilterMode filterMode;

  private TextureWrapMode textureWrapMode;

  public ProcessingTexture(PImage image) {
    this.image = image;
    this.filterMode = FilterMode.BILINEAR;
    this.textureWrapMode = TextureWrapMode.CLAMP;
  }

  @Override
  public int getWidth() {
    return image.width;
  }

  @Override
  public int getHeight() {
    return image.height;
  }

  @Override
  public void bind(int unit) {
    // Processing doesn't use texture units in the same way, just bind globally
  }

  @Override
  public void unbind() {
    // No specific unbind operation for Processing
  }

  @Override
  public void delete() {
    // Processing handles memory management automatically
  }

  @Override
  public void setPixels(int[] pixels) {
    if (pixels.length != image.width * image.height) {
      throw new IllegalArgumentException("Pixel array size mismatch");
    }

    image.loadPixels();
    System.arraycopy(pixels, 0, image.pixels, 0, pixels.length);
    image.updatePixels();
  }

  @Override
  public void setPixels(int x, int y, int w, int h, int[] pixels) {
    if (pixels.length != w * h) {
      throw new IllegalArgumentException("Pixel array size mismatch");
    }

    image.loadPixels();

    for (int row = 0; row < h; row++) {
      int srcOffset = row * w;
      int dstOffset = (y + row) * image.width + x;
      System.arraycopy(pixels, srcOffset, image.pixels, dstOffset, w);
    }

    image.updatePixels();
  }

  public PImage getImage() {
    return image;
  }

  @Override
  public FilterMode getFilterMode() {
    return filterMode;
  }

  @Override
  public void setFilterMode(FilterMode filterMode) {
    this.filterMode = filterMode;
  }

  @Override
  public TextureWrapMode getTextureWrapMode() {
    return textureWrapMode;
  }

  @Override
  public void setTextureWrapMode(TextureWrapMode textureWrapMode) {
    this.textureWrapMode = textureWrapMode;
  }

  @Override
  public Texture getBackendTexture() {
    return this;
  }
}
