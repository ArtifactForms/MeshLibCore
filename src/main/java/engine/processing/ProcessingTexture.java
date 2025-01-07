package engine.processing;

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
    image.loadPixels();
    image.pixels = pixels;
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
