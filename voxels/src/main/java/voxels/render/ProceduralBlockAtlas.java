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
 * <p>Layout: rows = block IDs, columns = cube face ordinals.
 */
public class ProceduralBlockAtlas {

  public static final int FACE_TOP = 0;
  public static final int FACE_FRONT = 1;
  public static final int FACE_RIGHT = 2;
  public static final int FACE_LEFT = 3;
  public static final int FACE_BACK = 4;
  public static final int FACE_BOTTOM = 5;

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

  private final int rows;
  private final Texture texture;
  private final float[] uvData;

  public ProceduralBlockAtlas() {
    rows = computeRowCount();
    BufferedImage image = createAtlasImage();
    texture = TextureManager.getInstance().createTexture(image);
    texture.setFilterMode(FilterMode.POINT);
    uvData = buildUVData();
  }

  public Texture getTexture() {
    return texture;
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
    int clampedBlockId = Math.max(0, Math.min(blockId, rows - 1));
    int clampedFace = Math.max(0, Math.min(faceOrdinal, COLUMNS - 1));

    int base = (clampedBlockId * COLUMNS + clampedFace) * 4;
    return new int[] {base + 3, base + 2, base + 1, base};
  }

  private int computeRowCount() {
    return Math.max(
            Blocks.AIR,
            Math.max(
                Blocks.STONE,
                Math.max(Blocks.DIRT, Math.max(Blocks.GRASS, Math.max(Blocks.LOG, Blocks.LEAF)))))
        + 1;
  }

  private BufferedImage createAtlasImage() {
    int width = TILE_SIZE * COLUMNS;
    int height = TILE_SIZE * rows;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    PerlinNoise noise = new PerlinNoise(1337L);

    for (int blockId = 0; blockId < rows; blockId++) {
      Color base = baseColorForBlock((short) blockId);

      for (int face = 0; face < COLUMNS; face++) {
        int startX = face * TILE_SIZE;
        int startY = blockId * TILE_SIZE;
        float faceShade = FACE_SHADE[face];

        for (int localY = 0; localY < TILE_SIZE; localY++) {
          for (int localX = 0; localX < TILE_SIZE; localX++) {
            int px = startX + localX;
            int py = startY + localY;

            float n = noise.sample((px + blockId * 17) * 0.24f, (py + face * 29) * 0.24f);
            float noiseMul = 0.92f + (n * 0.08f);
            float shade = clamp01(faceShade * noiseMul);

            int r = (int) (base.getRed() * shade);
            int g = (int) (base.getGreen() * shade);
            int b = (int) (base.getBlue() * shade);

            image.setRGB(px, py, rgba(r, g, b, base.getAlpha()));
          }
        }
      }
    }

    return image;
  }

  private float[] buildUVData() {
    int totalTiles = rows * COLUMNS;
    float[] out = new float[totalTiles * 8];

    float uStep = 1f / COLUMNS;
    float vStep = 1f / rows;

    int cursor = 0;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < COLUMNS; col++) {
        float u0 = col * uStep + EPSILON;
        float u1 = (col + 1) * uStep - EPSILON;
        float v0 = row * vStep + EPSILON;
        float v1 = (row + 1) * vStep - EPSILON;

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

  private Color baseColorForBlock(short blockId) {
    if (blockId == Blocks.STONE) {
      return new Color(124, 124, 124, 255);
    }
    if (blockId == Blocks.DIRT) {
      return new Color(124, 88, 56, 255);
    }
    if (blockId == Blocks.GRASS) {
      return new Color(92, 140, 58, 255);
    }
    if (blockId == Blocks.LOG) {
      return new Color(122, 90, 53, 255);
    }
    if (blockId == Blocks.LEAF) {
      return new Color(74, 132, 52, 255);
    }
    return new Color(0, 0, 0, 0);
  }

  private int rgba(int r, int g, int b, int a) {
    return ((a & 0xFF) << 24) | ((clampByte(r) & 0xFF) << 16) | ((clampByte(g) & 0xFF) << 8) | (clampByte(b) & 0xFF);
  }

  private int clampByte(int value) {
    return Math.max(0, Math.min(255, value));
  }

  private float clamp01(float value) {
    return Math.max(0f, Math.min(1f, value));
  }
}
