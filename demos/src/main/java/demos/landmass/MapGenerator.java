package demos.landmass;

import math.Color;
import math.Mathf;

/**
 * A procedural landmass generator, ported from Sebastian Lague's Unity script. This class generates
 * a height map and a color map based on Perlin noise and terrain types. It supports adjustable
 * parameters for map size, noise generation, and biome regions.
 *
 * <p>The generated maps can be used to create terrains with realistic variations in height and
 * color.
 */
public class MapGenerator {

  private int chunkSize;
  //  private int chunkSize = 481;
  //  private int chunkSize = 961;
  private int mapWidth;
  private int mapHeight;
  private int seed = 221;
  private int octaves = 4;
  private float scale = 50;
  private float persistance = 0.5f;
  private float lacunarity = 2f;
  private float[][] heightMap;
  private TerrainType[] regions;

  /** Constructs a new {@code MapGenerator} and initializes the height map and terrain regions. */
  public MapGenerator(int chunkSize) {
    this.chunkSize = chunkSize + 1;
    this.mapWidth = this.chunkSize;
    this.mapHeight = this.chunkSize;
    initializeRegions();
    heightMap =
        Noise.createHeightMap(mapWidth, mapHeight, seed, scale, octaves, persistance, lacunarity);
  }

  /**
   * Initializes the predefined terrain regions for the map. Each region is associated with a
   * specific height threshold, name, and color.
   */
  private void initializeRegions() {
    regions = new TerrainType[8];

    regions[0] = new TerrainType(0.3f, "Water Deep", Color.getColorFromInt(52, 99, 196));
    regions[1] = new TerrainType(0.4f, "Water Shallow", Color.getColorFromInt(54, 102, 199));
    regions[2] = new TerrainType(0.45f, "Sand", Color.getColorFromInt(206, 209, 125));
    regions[3] = new TerrainType(0.55f, "Grass", Color.getColorFromInt(86, 151, 24));
    regions[4] = new TerrainType(0.6f, "Grass 2", Color.getColorFromInt(62, 107, 18));
    regions[5] = new TerrainType(0.7f, "Rock", Color.getColorFromInt(90, 69, 62));
    regions[6] = new TerrainType(0.9f, "Rock 2", Color.getColorFromInt(77, 69, 56));
    regions[7] = new TerrainType(1.0f, "Snow", Color.getColorFromInt(253, 253, 253));
  }

  /**
   * Generates a grayscale noise map representing the terrain height. The noise values are mapped to
   * a color gradient ranging from black (low) to white (high).
   *
   * @return an array of RGBA color values representing the noise map
   */
  public int[] createNoiseMap() {
    int[] colorMap = new int[heightMap.length * heightMap[9].length];

    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        float lerpedValue = Mathf.lerp(0, 1, heightMap[x][y]);
        colorMap[y * mapWidth + x] = new Color(lerpedValue, lerpedValue, lerpedValue).getRGBA();
      }
    }
    return colorMap;
  }

  /**
   * Generates a color map based on predefined terrain regions. The height values in the height map
   * are compared against the thresholds of each terrain type to determine the color.
   *
   * @return an array of RGBA color values representing the color map
   */
  public int[] createColorMap() {
    int[] colorMap = new int[mapWidth * mapHeight];

    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        float currentHeight = heightMap[x][y];
        for (int i = 0; i < regions.length; i++) {
          if (currentHeight <= regions[i].height) {
            colorMap[y * mapWidth + x] = regions[i].color.getRGBA();
            break;
          }
        }
      }
    }
    return colorMap;
  }

  /**
   * Returns the generated height map. The height map is a 2D array of floats, where each value
   * represents the elevation at a specific point.
   *
   * @return a 2D float array representing the height map
   */
  public float[][] getHeightMap() {
    return heightMap;
  }

  /**
   * Represents a terrain type, which includes a height threshold, a name, and a color. Terrain
   * types are used to categorize different regions of the generated map.
   */
  public class TerrainType {

    public float height;
    public String name;
    public Color color;

    /**
     * Constructs a new {@code TerrainType} with the specified height threshold, name, and color.
     *
     * @param height the height threshold for this terrain type
     * @param name the name of the terrain type
     * @param color the color associated with this terrain type
     */
    public TerrainType(float height, String name, Color color) {
      this.height = height;
      this.name = name;
      this.color = color;
    }
  }
}
