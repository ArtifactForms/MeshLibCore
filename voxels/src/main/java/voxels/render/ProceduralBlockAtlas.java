package voxels.render;

import engine.resources.FilterMode;
import engine.resources.Texture;
import engine.resources.TextureManager;
import math.PerlinNoise;
import mesh.next.surface.SurfaceLayer;
import voxels.world.Blocks;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Procedurally generates a block atlas and UV layout for the voxel editor.
 *
 * <p>Layout: rows = texture tile types, columns = cube face ordinals.
 */
public class ProceduralBlockAtlas {

  public static final int FACE_TOP = 0;
  public static final int FACE_FRONT = 1;
  public static final int FACE_RIGHT = 2;
  public static final int FACE_LEFT = 3;
  public static final int FACE_BACK = 4;
  public static final int FACE_BOTTOM = 5;

  private static final int TILE_AIR = 0;
  private static final int TILE_STONE = 1;
  private static final int TILE_DIRT = 2;
  private static final int TILE_GRASS_TOP = 3;
  private static final int TILE_GRASS_SIDE = 4;
  private static final int TILE_LOG_SIDE = 5;
  private static final int TILE_LEAF = 6;
  private static final int TILE_LOG_TOP = 7;

  private static final int TILE_TYPE_COUNT = 8;

  private static final int TILE_SIZE = 32;
  private static final int COLUMNS = 6;
  private static final float EPSILON = 0.0015f;

  private static final float[] FACE_SHADE = {
    1.00f, // top
    0.88f, // front
    0.80f, // right
    0.70f, // left
    0.76f, // back
    0.56f // bottom
  };

  private final Texture texture;
  private final float[] uvData;
  private final boolean flipVForProcessing;

  public ProceduralBlockAtlas() {
    this(true);
  }

  public ProceduralBlockAtlas(boolean flipVForProcessing) {
    this.flipVForProcessing = flipVForProcessing;
    BufferedImage image = createAtlasImage();
    texture = TextureManager.getInstance().createTexture(image);
    texture.setFilterMode(FilterMode.POINT);
    uvData = buildUVData();
  }

  public Texture getTexture() {
    return texture;
  }

  public int getTileTypeCount() {
    return TILE_TYPE_COUNT;
  }

  public void appendUVs(SurfaceLayer surfaceLayer) {
    if (surfaceLayer == null) {
      throw new IllegalArgumentException("surfaceLayer cannot be null");
    }

    for (int i = 0; i < uvData.length; i += 2) {
      surfaceLayer.addUV(uvData[i], uvData[i + 1]);
    }
  }

  public int[] getFaceUVIndices(short blockId, int faceOrdinal) {
    int face = Math.max(0, Math.min(faceOrdinal, COLUMNS - 1));
    int tileType = tileTypeFor(blockId, face);

    int base = (tileType * COLUMNS + face) * 4;
    return new int[] {base + 3, base + 2, base + 1, base};
  }

  private BufferedImage createAtlasImage() {
    int width = TILE_SIZE * COLUMNS;
    int height = TILE_SIZE * TILE_TYPE_COUNT;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    PerlinNoise noise = new PerlinNoise(1337L);

    for (int tileType = 0; tileType < TILE_TYPE_COUNT; tileType++) {
      Color base = baseColorForTile(tileType);

      for (int face = 0; face < COLUMNS; face++) {
        int startX = face * TILE_SIZE;
        int startY = tileType * TILE_SIZE;
        float faceShade = FACE_SHADE[face];

        for (int localY = 0; localY < TILE_SIZE; localY++) {
          for (int localX = 0; localX < TILE_SIZE; localX++) {
            int px = startX + localX;
            int py = startY + localY;

            Color source = base;

            // Two-tone grass side: green top band + dirt below.
            if (tileType == TILE_GRASS_SIDE) {
              source = localY < TILE_SIZE / 4 ? new Color(92, 140, 58, 255) : new Color(124, 88, 56, 255);
            }

            float n = noise.sample((px + tileType * 17) * 0.24f, (py + face * 29) * 0.24f);
            float noiseMul = 0.92f + (n * 0.08f);
            float shade = clamp01(faceShade * noiseMul);

            int r = (int) (source.getRed() * shade);
            int g = (int) (source.getGreen() * shade);
            int b = (int) (source.getBlue() * shade);

            image.setRGB(px, py, rgba(r, g, b, source.getAlpha()));
          }
        }
      }
    }

    return image;
  }

  private float[] buildUVData() {
    int totalTiles = TILE_TYPE_COUNT * COLUMNS;
    float[] out = new float[totalTiles * 8];

    float uStep = 1f / COLUMNS;
    float vStep = 1f / TILE_TYPE_COUNT;

    int cursor = 0;
    for (int row = 0; row < TILE_TYPE_COUNT; row++) {
      int flippedRow = flippedAtlasRow(row);
      for (int col = 0; col < COLUMNS; col++) {
        float u0 = col * uStep + EPSILON;
        float u1 = (col + 1) * uStep - EPSILON;
        float v0 = flippedRow * vStep + EPSILON;
        float v1 = (flippedRow + 1) * vStep - EPSILON;

        out[cursor++] = u1;
        out[cursor++] = v1;

        out[cursor++] = u0;
        out[cursor++] = v1;

        out[cursor++] = u0;
        out[cursor++] = v0;

        out[cursor++] = u1;
        out[cursor++] = v0;
      }
    }
    return out;
  }



  private int flippedAtlasRow(int row) {
    // Processing backend samples V with flipped orientation compared to the generated BufferedImage.
    // Address this by mapping logical atlas rows from top->bottom onto UV rows from bottom->top.
    if (!flipVForProcessing) {
      return row;
    }
    return TILE_TYPE_COUNT - 1 - row;
  }

  private int tileTypeFor(short blockId, int faceOrdinal) {
    if (blockId == Blocks.STONE) {
      return TILE_STONE;
    }
    if (blockId == Blocks.DIRT) {
      return TILE_DIRT;
    }
    if (blockId == Blocks.GRASS) {
      if (faceOrdinal == FACE_TOP) {
        return TILE_GRASS_TOP;
      }
      if (faceOrdinal == FACE_BOTTOM) {
        return TILE_DIRT;
      }
      return TILE_GRASS_SIDE;
    }
    if (blockId == Blocks.LOG) {
      if (faceOrdinal == FACE_TOP || faceOrdinal == FACE_BOTTOM) {
        return TILE_LOG_TOP;
      }
      return TILE_LOG_SIDE;
    }
    if (blockId == Blocks.LEAF) {
      return TILE_LEAF;
    }
    return TILE_AIR;
  }

  private Color baseColorForTile(int tileType) {
    if (tileType == TILE_STONE) {
      return new Color(124, 124, 124, 255);
    }
    if (tileType == TILE_DIRT) {
      return new Color(124, 88, 56, 255);
    }
    if (tileType == TILE_GRASS_TOP) {
      return new Color(92, 140, 58, 255);
    }
    if (tileType == TILE_GRASS_SIDE) {
      return new Color(124, 88, 56, 255);
    }
    if (tileType == TILE_LOG_SIDE) {
      return new Color(122, 90, 53, 255);
    }
    if (tileType == TILE_LEAF) {
      return new Color(74, 132, 52, 255);
    }
    if (tileType == TILE_LOG_TOP) {
      return new Color(150, 118, 80, 255);
    }
    return new Color(0, 0, 0, 0);
  }

  private int rgba(int r, int g, int b, int a) {
    return ((a & 0xFF) << 24)
        | ((clampByte(r) & 0xFF) << 16)
        | ((clampByte(g) & 0xFF) << 8)
        | (clampByte(b) & 0xFF);
  }

  private int clampByte(int value) {
    return Math.max(0, Math.min(255, value));
  }

  private float clamp01(float value) {
    return Math.max(0f, Math.min(1f, value));
  }
}