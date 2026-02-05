package math;

import java.util.Random;

import math.noise.Noise2D;

/**
 * The {@code PerlinNoise} class generates smooth procedural noise using Perlin Noise, a popular
 * algorithm in procedural terrain generation and other computer graphics applications.
 *
 * <p>This implementation supports 2D noise generation, allowing for realistic terrain heightmaps,
 * textures, and other procedurally generated elements.
 *
 * <p>The noise output is normalized to the range [0, 1].
 */
public class PerlinNoise implements Noise2D {

  /** Permutation table used to generate pseudo-random gradients for the noise function. */
  private final int[] permutationTable;

  /** Random number generator for shuffling the permutation table based on a seed. */
  private final Random random;

  /**
   * Constructs a new {@code PerlinNoise} instance with a specified seed.
   *
   * @param seed The seed value to initialize the random number generator, ensuring repeatable noise
   *     patterns for the same seed.
   */
  public PerlinNoise(long seed) {
    random = new Random(seed);
    permutationTable = new int[512];
    int[] p = new int[256];

    // Initialize the permutation table with values 0 to 255
    for (int i = 0; i < 256; i++) {
      p[i] = i;
    }

    // Shuffle the values in the permutation table using the seed
    for (int i = 255; i > 0; i--) {
      int swap = random.nextInt(i + 1);
      int temp = p[i];
      p[i] = p[swap];
      p[swap] = temp;
    }

    // Duplicate the permutation table to handle wrap-around
    for (int i = 0; i < 512; i++) {
      permutationTable[i] = p[i % 256];
    }
  }

  /**
   * Computes the Perlin noise value at the specified 2D coordinates.
   *
   * @param x The x-coordinate in noise space.
   * @param y The y-coordinate in noise space.
   * @return A noise value in the range [0, 1].
   */
  public double noise(double x, double y) {
    // Calculate the integer part of the coordinates
    int x0 = (int) Math.floor(x) & 255;
    int y0 = (int) Math.floor(y) & 255;

    // Calculate the fractional part of the coordinates
    double xf = x - Math.floor(x);
    double yf = y - Math.floor(y);

    // Compute fade curves for smoothing
    double u = fade(xf);
    double v = fade(yf);

    // Hash coordinates of the square corners
    int aa = permutationTable[permutationTable[x0] + y0];
    int ab = permutationTable[permutationTable[x0] + y0 + 1];
    int ba = permutationTable[permutationTable[x0 + 1] + y0];
    int bb = permutationTable[permutationTable[x0 + 1] + y0 + 1];

    // Linearly interpolate the noise values at the corners
    double x1 = lerp(u, grad(aa, xf, yf), grad(ba, xf - 1, yf));
    double x2 = lerp(u, grad(ab, xf, yf - 1), grad(bb, xf - 1, yf - 1));
    double result = lerp(v, x1, x2);

    // Normalize the result to the range [0, 1]
    return (result + 1) / 2;
  }

  /**
   * Applies a fade curve to the input value, smoothing the transitions between points.
   *
   * <p>The fade function is defined as {@code 6t^5 - 15t^4 + 10t^3}, which provides a smoother
   * interpolation.
   *
   * @param t The input value to apply the fade curve to.
   * @return The smoothed value.
   */
  private double fade(double t) {
    return t * t * t * (t * (t * 6 - 15) + 10);
  }

  /**
   * Performs linear interpolation between two values based on a specified factor.
   *
   * @param t The interpolation factor, typically in the range [0, 1].
   * @param a The start value.
   * @param b The end value.
   * @return The interpolated value.
   */
  private double lerp(double t, double a, double b) {
    return a + t * (b - a);
  }

  /**
   * Computes the gradient function for a given hash value and coordinates.
   *
   * <p>The gradient function determines the influence of each corner of a grid cell on the final
   * noise value.
   *
   * @param hash The hash value used to select a gradient.
   * @param x The x-offset from the grid point.
   * @param y The y-offset from the grid point.
   * @return The gradient value.
   */
  private double grad(int hash, double x, double y) {
    // Use the last 2 bits of the hash to determine the gradient direction
    int h = hash & 3;
    double u = h < 2 ? x : -x;
    double v = h < 1 || h == 2 ? y : -y;
    return u + v;
  }

  @Override
  public float sample(float x, float y) {
    return (float) noise(x, y);
  }
}
