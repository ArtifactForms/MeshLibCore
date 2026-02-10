package engine.gfx;

import engine.resources.Texture;

/**
 * A {@code TextureAtlas} represents a grid-based subdivision of a texture into equally sized tiles
 * and provides UV coordinates for individual tiles.
 *
 * <p>The atlas assumes a fixed tile size in pixels and computes normalized UV rectangles that can
 * be applied directly to mesh geometry.
 *
 * <h2>Coordinate System</h2>
 *
 * <ul>
 *   <li>Tile indexing is <strong>row / column based</strong>
 *   <li>{@code (row=0, col=0)} refers to the <strong>bottom-left tile</strong>
 *   <li>UV coordinates follow the conventional OpenGL-style <strong>bottom-left origin</strong>
 * </ul>
 *
 * <p>If your asset pipeline or mental model uses a top-left origin (common in 2D tools and pixel
 * art), use {@link #getUVTopLeft(int, int)} instead.
 *
 * <h2>UV Padding (Epsilon)</h2>
 *
 * <p>An optional pixel-based epsilon is applied to all UV rectangles to prevent texture bleeding
 * caused by filtering or floating point precision.
 *
 * <p>The epsilon is specified in <strong>pixels</strong> and converted to normalized UV space
 * internally.
 *
 * <h2>Typical Usage</h2>
 *
 * <pre>{@code
 * TextureAtlas atlas = new TextureAtlas(texture, 32);
 * UVRect uv = atlas.getUV(0, 0);        // bottom-left tile
 * UVRect ui = atlas.getUVTopLeft(0, 0); // top-left tile
 * }</pre>
 *
 * <p>This class is intentionally minimal and engine-level:
 *
 * <ul>
 *   <li>No rendering logic
 *   <li>No scene or component dependencies
 *   <li>No assumptions about sprites or animation
 * </ul>
 *
 * <p>Higher-level constructs such as sprites, tile layers, or animations should be built on top of
 * this class.
 */
public class TextureAtlas {

  /** Default UV padding in pixels to avoid texture bleeding. */
  private static final float DEFAULT_EPSILON_PX = 0.25f;

  private final Texture texture;
  private final int rows;
  private final int cols;
  private final int tileSizePx;
  private final float widthPx;
  private final float heightPx;
  private final float epsPx;

  /**
   * Creates a texture atlas by inferring the number of rows and columns from the texture size and
   * tile size.
   *
   * @param texture the source texture
   * @param tileSizePx the size of one tile in pixels (assumes square tiles)
   */
  public TextureAtlas(Texture texture, int tileSizePx) {
    this(
        texture,
        (int) (texture.getHeight() / tileSizePx),
        (int) (texture.getWidth() / tileSizePx),
        tileSizePx,
        DEFAULT_EPSILON_PX);
  }

  /**
   * Creates a texture atlas with explicit row and column counts.
   *
   * @param texture the source texture
   * @param rows number of tile rows
   * @param cols number of tile columns
   * @param tileSizePx the size of one tile in pixels
   */
  public TextureAtlas(Texture texture, int rows, int cols, int tileSizePx) {
    this(texture, rows, cols, tileSizePx, DEFAULT_EPSILON_PX);
  }

  /**
   * Creates a texture atlas with full control over layout and UV padding.
   *
   * @param texture the source texture
   * @param rows number of tile rows
   * @param cols number of tile columns
   * @param tileSizePx the size of one tile in pixels
   * @param epsPx UV padding in pixels to avoid texture bleeding
   */
  public TextureAtlas(Texture texture, int rows, int cols, int tileSizePx, float epsPx) {
    if (texture == null) throw new IllegalArgumentException("texture must not be null");
    if (rows <= 0 || cols <= 0) throw new IllegalArgumentException("rows/cols must be > 0");
    if (tileSizePx <= 0) throw new IllegalArgumentException("tileSizePx must be > 0");

    this.texture = texture;
    this.rows = rows;
    this.cols = cols;
    this.tileSizePx = tileSizePx;
    this.widthPx = texture.getWidth();
    this.heightPx = texture.getHeight();
    this.epsPx = epsPx;
  }

  /** @return the underlying atlas texture */
  public Texture getTexture() {
    return texture;
  }

  /**
   * Checks whether the given tile coordinates are within the atlas bounds.
   *
   * @param row tile row (bottom-left origin)
   * @param col tile column
   * @return {@code true} if the tile exists
   */
  public boolean isInBounds(int row, int col) {
    return row >= 0 && col >= 0 && row < rows && col < cols;
  }

  /**
   * Returns the UV rectangle for a tile using a bottom-left origin.
   *
   * <p>{@code row = 0} refers to the bottom-most row of the texture.
   *
   * @param row tile row (bottom-left origin)
   * @param col tile column
   * @return normalized UV rectangle for the tile
   * @throws IndexOutOfBoundsException if the tile is outside the atlas
   */
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

  /**
   * Returns the UV rectangle for a tile using a top-left origin.
   *
   * <p>{@code row = 0} refers to the top-most row of the texture, which is common in 2D editors and
   * sprite sheets.
   *
   * @param row tile row (top-left origin)
   * @param col tile column
   * @return normalized UV rectangle for the tile
   */
  public UVRect getUVTopLeft(int row, int col) {
    return getUV(rows - 1 - row, col);
  }
}
