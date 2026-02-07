package mesh.uv;

/**
 * Represents a rectangular region in normalized UV texture space.
 *
 * <p>UV coordinates are expected to be in the range {@code [0..1]} where:
 *
 * <ul>
 *   <li>{@code (0,0)} is the top-left of the texture
 *   <li>{@code (1,1)} is the bottom-right of the texture
 * </ul>
 *
 * <p>This class is immutable and safe to share across the engine.
 *
 * <p>Typical use cases:
 *
 * <ul>
 *   <li>Texture atlases / sprite sheets
 *   <li>Billboards and particles
 *   <li>UI rendering
 * </ul>
 */
public final class UVRect {

  /** UV rectangle covering the entire texture */
  public static final UVRect FULL = new UVRect(0f, 0f, 1f, 1f);

  /** Minimum U coordinate (left edge) */
  public final float uMin;

  /** Minimum V coordinate (top edge) */
  public final float vMin;

  /** Maximum U coordinate (right edge) */
  public final float uMax;

  /** Maximum V coordinate (bottom edge) */
  public final float vMax;

  /**
   * Creates a new UV rectangle.
   *
   * @param uMin left edge (inclusive)
   * @param vMin top edge (inclusive)
   * @param uMax right edge (exclusive)
   * @param vMax bottom edge (exclusive)
   * @throws IllegalArgumentException if the rectangle is invalid
   */
  public UVRect(float uMin, float vMin, float uMax, float vMax) {
    this.uMin = uMin;
    this.vMin = vMin;
    this.uMax = uMax;
    this.vMax = vMax;
    validate();
  }

  private void validate() {
    if (uMin >= uMax || vMin >= vMax) {
      throw new IllegalArgumentException(
          "Invalid UVRect: min values must be smaller than max values");
    }

    if (uMin < 0f || vMin < 0f || uMax > 1f || vMax > 1f) {
      throw new IllegalArgumentException("UVRect coordinates must be in normalized range [0..1]");
    }
  }

  /**
   * Returns a {@link UVRect} that covers the entire texture.
   *
   * <p>This is equivalent to {@link #FULL} and is provided for readability.
   *
   * @return the full-texture {@code UVRect}
   */
  public static UVRect full() {
    return FULL;
  }

  /**
   * Returns a new {@link UVRect} inset by the given amount on all sides.
   *
   * <p>This is commonly used to avoid texture bleeding when sampling from texture atlases,
   * especially with linear filtering or mipmapping.
   *
   * <p>The inset value is applied in normalized UV space:
   *
   * <pre>
   * uMin' = uMin + eps
   * vMin' = vMin + eps
   * uMax' = uMax - eps
   * vMax' = vMax - eps
   * </pre>
   *
   * @param eps inset amount in normalized UV units (typically a small value such as {@code 0.001f}
   *     or {@code 0.25 / textureSize})
   * @return a new inset {@code UVRect}
   * @throws IllegalArgumentException if the inset results in an invalid rectangle
   */
  public UVRect inset(float eps) {
    return new UVRect(uMin + eps, vMin + eps, uMax - eps, vMax - eps);
  }

  /** @return width of the rectangle in UV space */
  public float getWidth() {
    return uMax - uMin;
  }

  /** @return height of the rectangle in UV space */
  public float getHeight() {
    return vMax - vMin;
  }

  @Override
  public String toString() {
    return "UVRect[" + "uMin=" + uMin + ", vMin=" + vMin + ", uMax=" + uMax + ", vMax=" + vMax
        + ']';
  }
}
