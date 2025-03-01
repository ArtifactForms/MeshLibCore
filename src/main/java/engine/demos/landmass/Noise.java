package engine.demos.landmass;

import math.Mathf;
import math.PerlinNoise;

/**
 * Utility class for generating procedural height maps using Perlin noise.
 *
 * <p>This class is a Java adaptation of Sebastian Lague's Unity script for procedural landmass
 * creation. It generates a two-dimensional array representing height values for a terrain based on
 * various noise parameters.
 */
public class Noise {

  /**
   * Generates a height map using Perlin noise.
   *
   * <p>The height map is generated by layering multiple octaves of Perlin noise, each with varying
   * amplitude and frequency, allowing for detailed and natural-looking terrain generation.
   *
   * @param mapWidth the width of the height map to be generated
   * @param mapHeight the height of the height map to be generated
   * @param seed the seed value for the noise generation, ensuring reproducibility
   * @param scale the scale of the noise; smaller values zoom in on noise patterns
   * @param octaves the number of noise layers (octaves) to combine for more complex patterns
   * @param persistance the rate at which the amplitude of each octave decreases
   * @param lacunarity the rate at which the frequency of each octave increases
   * @return a two-dimensional float array representing the generated height map
   */
  public static float[][] createHeightMap(
      int mapWidth,
      int mapHeight,
      int seed,
      float scale,
      int octaves,
      float persistance,
      float lacunarity) {

    // Initialize Perlin noise with the provided seed
    PerlinNoise perlinNoise = new PerlinNoise(seed);
    float[][] noiseMap = new float[mapWidth][mapHeight];

    // Prevent division by zero by ensuring a minimum scale value
    if (scale <= 0) {
      scale = 0.0001f;
    }

    // Variables to track the min and max noise values for normalization
    float maxNoiseHeight = Float.MIN_VALUE;
    float minNoiseHeight = Float.MAX_VALUE;

    // Generate noise values for each point in the map
    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {

        float amplitude = 1;
        float frequency = 1;
        float noiseHeight = 0;

        // Generate noise using multiple octaves
        for (int i = 0; i < octaves; i++) {
          // Sample points in the Perlin noise space
          float sampleX = x / scale * frequency;
          float sampleY = y / scale * frequency;

          // Calculate the Perlin noise value and adjust it to allow negative values
          float perlinValue = (float) perlinNoise.noise(sampleX, sampleY) * 2 - 1;
          noiseHeight += perlinValue * amplitude;

          // Decrease amplitude and increase frequency for the next octave
          amplitude *= persistance;
          frequency *= lacunarity;
        }

        // Update min and max noise height values
        if (noiseHeight > maxNoiseHeight) {
          maxNoiseHeight = noiseHeight;
        } else if (noiseHeight < minNoiseHeight) {
          minNoiseHeight = noiseHeight;
        }

        // Assign the calculated noise height to the map
        noiseMap[x][y] = noiseHeight;
      }
    }

    // Normalize the noise map values to a range between 0 and 1
    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        noiseMap[x][y] = Mathf.inverseLerp(minNoiseHeight, maxNoiseHeight, noiseMap[x][y]);
      }
    }

    return noiseMap;
  }
}
