package demos.jam26.assets;

import engine.resources.Texture;
import mesh.UVRect;

public class TextureAtlas {

  private static final float DEFAULT_EPSILON_PX = 0.25f;

  private final Texture texture;
  private final int rows;
  private final int cols;
  private final int tileSizePx;
  private final float widthPx;
  private final float heightPx;
  private final float epsPx;

  public TextureAtlas(Texture texture, int tileSizePx) {
    this(
        texture,
        (int) (texture.getHeight() / tileSizePx),
        (int) (texture.getWidth() / tileSizePx),
        tileSizePx,
        DEFAULT_EPSILON_PX);
  }

  public TextureAtlas(Texture texture, int rows, int cols, int tileSizePx) {
    this(texture, rows, cols, tileSizePx, DEFAULT_EPSILON_PX);
  }

  public TextureAtlas(Texture texture, int rows, int cols, int tileSizePx, float epsPx) {
    this.texture = texture;
    this.rows = rows;
    this.cols = cols;
    this.tileSizePx = tileSizePx;
    this.widthPx = texture.getWidth();
    this.heightPx = texture.getHeight();
    this.epsPx = epsPx;
  }

  public Texture getTexture() {
    return texture;
  }

  public boolean isInBounds(int row, int col) {
    return row >= 0 && col >= 0 && row < rows && col < cols;
  }

  public UVRect getUV(int row, int col) {
    if (!isInBounds(row, col)) {
      throw new IndexOutOfBoundsException("UV out of bounds: row=" + row + ", col=" + col);
    }

    float px1 = col * tileSizePx + epsPx;
    float py1 = row * tileSizePx + epsPx;
    float px2 = (col + 1) * tileSizePx - epsPx;
    float py2 = (row + 1) * tileSizePx - epsPx;

    return new UVRect(px1 / widthPx, py1 / heightPx, px2 / widthPx, py2 / heightPx);
  }
}
