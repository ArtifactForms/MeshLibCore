package math;

import java.util.Random;

public class PerlinNoise3 {

  private final int[] permutationTable;

  public PerlinNoise3(long seed) {
    Random random = new Random(seed);
    permutationTable = new int[512];
    int[] p = new int[256];

    for (int i = 0; i < 256; i++) {
      p[i] = i;
    }

    for (int i = 255; i > 0; i--) {
      int swap = random.nextInt(i + 1);
      int temp = p[i];
      p[i] = p[swap];
      p[swap] = temp;
    }

    for (int i = 0; i < 512; i++) {
      permutationTable[i] = p[i % 256];
    }
  }

  /**
   * Computes the Perlin noise value at the specified 3D coordinates.
   *
   * @param x The x-coordinate in noise space.
   * @param y The y-coordinate in noise space.
   * @param z The z-coordinate in noise space.
   * @return A noise value in the range [0, 1].
   */
  public double noise(double x, double y, double z) {
    // Calculate the integer parts of the coordinates
    int x0 = (int) Math.floor(x) & 255;
    int y0 = (int) Math.floor(y) & 255;
    int z0 = (int) Math.floor(z) & 255;

    // Calculate the fractional parts of the coordinates
    double xf = x - Math.floor(x);
    double yf = y - Math.floor(y);
    double zf = z - Math.floor(z);

    // Compute fade curves for smoothing
    double u = fade(xf);
    double v = fade(yf);
    double w = fade(zf);

    // Hash coordinates of the cube corners
    int aaa = permutationTable[permutationTable[permutationTable[x0] + y0] + z0];
    int aab = permutationTable[permutationTable[permutationTable[x0] + y0] + z0 + 1];
    int aba = permutationTable[permutationTable[permutationTable[x0] + y0 + 1] + z0];
    int abb = permutationTable[permutationTable[permutationTable[x0] + y0 + 1] + z0 + 1];
    int baa = permutationTable[permutationTable[permutationTable[x0 + 1] + y0] + z0];
    int bab = permutationTable[permutationTable[permutationTable[x0 + 1] + y0] + z0 + 1];
    int bba = permutationTable[permutationTable[permutationTable[x0 + 1] + y0 + 1] + z0];
    int bbb = permutationTable[permutationTable[permutationTable[x0 + 1] + y0 + 1] + z0 + 1];

    // Linearly interpolate the noise values at the cube corners
    double x1, x2, y1, y2;
    x1 = lerp(u, grad(aaa, xf, yf, zf), grad(baa, xf - 1, yf, zf));
    x2 = lerp(u, grad(aba, xf, yf - 1, zf), grad(bba, xf - 1, yf - 1, zf));
    y1 = lerp(v, x1, x2);

    x1 = lerp(u, grad(aab, xf, yf, zf - 1), grad(bab, xf - 1, yf, zf - 1));
    x2 = lerp(u, grad(abb, xf, yf - 1, zf - 1), grad(bbb, xf - 1, yf - 1, zf - 1));
    y2 = lerp(v, x1, x2);

    double result = lerp(w, y1, y2);

    // Normalize the result to the range [0, 1]
    return (result + 1) / 2;
  }

  private double fade(double t) {
    return t * t * t * (t * (t * 6 - 15) + 10);
  }

  private double lerp(double t, double a, double b) {
    return a + t * (b - a);
  }

  private double grad(int hash, double x, double y, double z) {
    int h = hash & 15;
    double u = h < 8 ? x : y;
    double v = h < 4 ? y : (h == 12 || h == 14 ? x : z);
    return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
  }
}
