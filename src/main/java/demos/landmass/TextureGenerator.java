package demos.landmass;

import engine.resources.FilterMode;
import engine.resources.Texture2D;
import math.Color;
import math.Mathf;

public class TextureGenerator {

  public static Texture2D textureFromColorMap(int colorMap[], int width, int height) {
    Texture2D texture = new Texture2D(width, height);
    texture.setFilterMode(FilterMode.POINT);
    texture.setPixels(colorMap);
    return texture;
  }

  public static Texture2D textureFromHeightMap(float[][] heightMap) {
    int width = heightMap.length;
    int height = heightMap[0].length;

    int[] colorMap = new int[heightMap.length * heightMap[9].length];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        float lerpedValue = Mathf.lerp(0, 1, heightMap[x][y]);
        colorMap[y * width + x] = new Color(lerpedValue, lerpedValue, lerpedValue).getRGBA();
      }
    }
    return textureFromColorMap(colorMap, width, height);
  }
}
