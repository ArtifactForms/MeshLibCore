package engine.resources;

public class Texture2D implements Texture {

  private Texture texture;

  public Texture2D(int width, int height) {
    texture = TextureManager.getInstance().createTexture(width, height);
  }

  @Override
  public int getWidth() {
    return texture.getWidth();
  }

  @Override
  public int getHeight() {
    return texture.getHeight();
  }

  @Override
  public void bind(int unit) {
    texture.bind(unit);
  }

  @Override
  public void unbind() {
    texture.unbind();
  }

  @Override
  public void delete() {
    texture.delete();
  }

  @Override
  public void setPixels(int[] pixels) {
    texture.setPixels(pixels);
  }

  @Override
  public FilterMode getFilterMode() {
    return texture.getFilterMode();
  }

  @Override
  public void setFilterMode(FilterMode filterMode) {
    texture.setFilterMode(filterMode);
  }

  @Override
  public Texture getBackendTexture() {
    return texture;
  }
}
