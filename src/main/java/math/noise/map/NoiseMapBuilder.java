package math.noise.map;

import java.util.Objects;

import math.Mathf;
import math.noise.Noise2D;

/**
 * Builder for baking a {@link Noise2D} function into a {@link NoiseMap}.
 *
 * <p>This class samples a continuous noise function into a reusable buffer and optionally applies
 * normalization, remapping, and clamping.
 *
 * <p>All operations are performed in-place. No allocations occur during {@code buildInto()}.
 */
public final class NoiseMapBuilder {

  private final Noise2D noise;

  private float scale = 1f;
  private float offsetX = 0f;
  private float offsetY = 0f;

  private boolean normalize = false;
  private boolean clamp01 = false;

  // --- Remap configuration ---
  private boolean remap = false;
  private float remapInMin;
  private float remapInMax;
  private float remapOutMin;
  private float remapOutMax;

  public NoiseMapBuilder(Noise2D noise) {
    this.noise = Objects.requireNonNull(noise);
  }

  public static NoiseMapBuilder create(Noise2D noise) {
    return new NoiseMapBuilder(noise);
  }

  public NoiseMapBuilder scale(float scale) {
    if (scale <= 0f) {
      throw new IllegalArgumentException("Scale must be > 0");
    }
    this.scale = scale;
    return this;
  }

  public NoiseMapBuilder offset(float x, float y) {
    this.offsetX = x;
    this.offsetY = y;
    return this;
  }

  public NoiseMapBuilder normalize() {
    this.normalize = true;
    return this;
  }

  /**
   * Remaps values from a defined input range into a target output range.
   *
   * <p>Applied after normalization (if enabled) and before clamping.
   */
  public NoiseMapBuilder remap(float inMin, float inMax, float outMin, float outMax) {
    if (inMax == inMin) {
      throw new IllegalArgumentException("inMax must not equal inMin");
    }

    this.remap = true;
    this.remapInMin = inMin;
    this.remapInMax = inMax;
    this.remapOutMin = outMin;
    this.remapOutMax = outMax;
    return this;
  }

  public NoiseMapBuilder clamp01() {
    this.clamp01 = true;
    return this;
  }

  /**
   * Samples noise and writes results into the provided {@link NoiseMap}.
   *
   * <p>This method performs no allocations and may be called every frame.
   */
  public void buildInto(NoiseMap map) {

    float[] data = map.data;
    int width = map.width;
    int height = map.height;

    float min = Float.POSITIVE_INFINITY;
    float max = Float.NEGATIVE_INFINITY;

    // --- Sample noise ---
    int i = 0;
    for (int y = 0; y < height; y++) {
      float ny = (y + offsetY) * scale;
      for (int x = 0; x < width; x++) {
        float nx = (x + offsetX) * scale;

        float v = noise.sample(nx, ny);
        data[i++] = v;

        if (normalize) {
          min = Math.min(min, v);
          max = Math.max(max, v);
        }
      }
    }

    // --- Normalize ---
    if (normalize && max > min) {
      float invRange = 1f / (max - min);
      for (i = 0; i < data.length; i++) {
        data[i] = (data[i] - min) * invRange;
      }
    }

    // --- Remap ---
    if (remap) {
      float inRange = remapInMax - remapInMin;
      float outRange = remapOutMax - remapOutMin;
      float invInRange = 1f / inRange;

      for (i = 0; i < data.length; i++) {
        float t = (data[i] - remapInMin) * invInRange;
        data[i] = remapOutMin + t * outRange;
      }
    }

    // --- Clamp ---
    if (clamp01) {
      for (i = 0; i < data.length; i++) {
        data[i] = Mathf.clamp01(data[i]);
      }
    }
  }
}
