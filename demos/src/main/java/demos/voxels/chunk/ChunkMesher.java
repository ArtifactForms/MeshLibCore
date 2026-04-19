package demos.voxels.chunk;

import java.util.ArrayList;

import demos.voxels.TextureAtlas2;
import demos.voxels.world.BlockType;
import engine.backend.processing.BufferedShape;
import engine.components.StaticGeometry;
import engine.rendering.Material;
import engine.resources.FilterMode;
import engine.resources.Texture;
import engine.resources.TextureWrapMode;
import math.Vector2f;

public class ChunkMesher {

  private static final int TOP = 0;

  private static final int BOTTOM = 5;

  private static final int FRONT = 1;

  private static final int BACK = 4;

  private static final int RIGHT = 2;

  private static final int LEFT = 3;

  private static float radius = 0.5f;

  private Chunk chunk;
  private ChunkManager chunkManager;
  private BufferedShape shape;

  private static ArrayList<Vector2f> uvs;
  public static Material sharedMaterial;
  private static TextureAtlas2 textureAtlas;

  static {
    sharedMaterial = new Material();
    textureAtlas = new TextureAtlas2();
    Texture texture = textureAtlas.getTexture();

    texture.setFilterMode(FilterMode.POINT);
    texture.setTextureWrapMode(TextureWrapMode.REPEAT);

    sharedMaterial.setDiffuseTexture(texture);
    uvs = textureAtlas.getUVCoordinates();
  }

  public ChunkMesher(Chunk chunk, ChunkManager chunkManager) {
    this.chunk = chunk;
    this.chunkManager = chunkManager;
  }

  public StaticGeometry createMesh() {
    shape = new BufferedShape(sharedMaterial);
    shape.begin(BufferedShape.QUADS);

    greedyTopFaces();
    generateSideFaces();

    shape.end();
    return new StaticGeometry(shape.getVBO(), sharedMaterial);
  }

  private void greedyTopFaces() {
    for (int y = 0; y < Chunk.HEIGHT; y++) {
      boolean[][] visited = new boolean[Chunk.WIDTH][Chunk.DEPTH];

      for (int x = 0; x < Chunk.WIDTH; x++) {
        for (int z = 0; z < Chunk.DEPTH; z++) {

          if (visited[x][z]) continue;

          int blockId = chunk.getBlockData(x, y, z);

          if (blockId == BlockType.AIR.getId() || !shouldRender(blockId, x, y + 1, z)) continue;

          int width = 1;
          while (x + width < Chunk.WIDTH
              && !visited[x + width][z]
              && chunk.getBlockData(x + width, y, z) == blockId
              && shouldRender(blockId, x + width, y + 1, z)) {
            width++;
          }

          int depth = 1;
          boolean done = false;

          while (z + depth < Chunk.DEPTH && !done) {
            for (int k = 0; k < width; k++) {
              if (visited[x + k][z + depth]
                  || chunk.getBlockData(x + k, y, z + depth) != blockId
                  || !shouldRender(blockId, x + k, y + 1, z + depth)) {
                done = true;
                break;
              }
            }
            if (!done) depth++;
          }

          for (int dx = 0; dx < width; dx++) {
            for (int dz = 0; dz < depth; dz++) {
              visited[x + dx][z + dz] = true;
            }
          }

          addTopQuad(x, y, z, width, depth, blockId);
        }
      }
    }
  }

  private void addTopQuad(int x, int y, int z, int width, int depth, int blockId) {

    Vector2f[] uv = textureAtlas.getUVCoordinates(blockId, TOP);

    float minX = x - radius;
    float minZ = z - radius;
    float yPos = -radius - y;

    for (int dx = 0; dx < width; dx++) {
      for (int dz = 0; dz < depth; dz++) {

        int bx = x + dx;
        int bz = z + dz;

        float a0 =
            aoBrightness(
                vertexAO(
                    isSolid(bx - 1, y + 1, bz),
                    isSolid(bx, y + 1, bz - 1),
                    isSolid(bx - 1, y + 1, bz - 1)));

        float a1 =
            aoBrightness(
                vertexAO(
                    isSolid(bx + 1, y + 1, bz),
                    isSolid(bx, y + 1, bz - 1),
                    isSolid(bx + 1, y + 1, bz - 1)));

        float a2 =
            aoBrightness(
                vertexAO(
                    isSolid(bx - 1, y + 1, bz + 1),
                    isSolid(bx, y + 1, bz + 1),
                    isSolid(bx - 1, y + 1, bz + 1)));

        float a3 =
            aoBrightness(
                vertexAO(
                    isSolid(bx + 1, y + 1, bz + 1),
                    isSolid(bx, y + 1, bz + 1),
                    isSolid(bx + 1, y + 1, bz + 1)));

        drawCorrectedQuad(minX + dx, yPos, minZ + dz, 1, 1, uv, 1.0f, a0, a1, a2, a3, TOP);
      }
    }
  }

  private void generateSideFaces() {
    for (int x = 0; x < Chunk.WIDTH; x++) {
      for (int z = 0; z < Chunk.DEPTH; z++) {
        for (int y = 0; y < Chunk.HEIGHT; y++) {

          int blockId = chunk.getBlockData(x, y, z);
          if (blockId == BlockType.AIR.getId()) continue;

          if (blockId == BlockType.GRASS.getId()) {
            createBillBoard(x, y, z);
            continue;
          }

          if (shouldRender(blockId, x, y - 1, z)) addFace(BOTTOM, x, y, z);
          if (shouldRender(blockId, x, y, z + 1)) addFace(FRONT, x, y, z);
          if (shouldRender(blockId, x, y, z - 1)) addFace(BACK, x, y, z);
          if (shouldRender(blockId, x + 1, y, z)) addFace(RIGHT, x, y, z);
          if (shouldRender(blockId, x - 1, y, z)) addFace(LEFT, x, y, z);
        }
      }
    }
  }

  private void addFace(int face, int x, int y, int z) {

    int blockId = chunk.getBlockData(x, y, z);
    Vector2f[] uv = textureAtlas.getUVCoordinates(blockId, face);

    if (face != TOP && face != BOTTOM) {
      uv =
          new Vector2f[] {
            new Vector2f(uv[0].x, uv[2].y),
            new Vector2f(uv[1].x, uv[3].y),
            new Vector2f(uv[2].x, uv[0].y),
            new Vector2f(uv[3].x, uv[1].y)
          };
    }

    float light;
    switch (face) {
      case FRONT:
      case BACK:
        light = 0.85f;
        break;
      case LEFT:
      case RIGHT:
        light = 0.7f;
        break;
      case BOTTOM:
        light = 0.5f;
        break;
      default:
        light = 1.0f;
        break;
    }

    float a0;
    float a1;
    float a2;
    float a3;

    switch (face) {
      case FRONT:
        a0 =
            aoBrightness(
                vertexAO(
                    isSolid(x + 1, y, z + 1),
                    isSolid(x, y - 1, z + 1),
                    isSolid(x + 1, y - 1, z + 1)));
        a1 =
            aoBrightness(
                vertexAO(
                    isSolid(x - 1, y, z + 1),
                    isSolid(x, y - 1, z + 1),
                    isSolid(x - 1, y - 1, z + 1)));
        a2 =
            aoBrightness(
                vertexAO(
                    isSolid(x - 1, y, z + 1),
                    isSolid(x, y + 1, z + 1),
                    isSolid(x - 1, y + 1, z + 1)));
        a3 =
            aoBrightness(
                vertexAO(
                    isSolid(x + 1, y, z + 1),
                    isSolid(x, y + 1, z + 1),
                    isSolid(x + 1, y + 1, z + 1)));
        drawCorrectedQuad(
            x - radius, -radius - y, z + radius, 1, 1, uv, light, a0, a1, a2, a3, FRONT);
        break;

      case BACK:
        a0 =
            aoBrightness(
                vertexAO(
                    isSolid(x - 1, y, z - 1),
                    isSolid(x, y - 1, z - 1),
                    isSolid(x - 1, y - 1, z - 1)));
        a1 =
            aoBrightness(
                vertexAO(
                    isSolid(x + 1, y, z - 1),
                    isSolid(x, y - 1, z - 1),
                    isSolid(x + 1, y - 1, z - 1)));
        a2 =
            aoBrightness(
                vertexAO(
                    isSolid(x + 1, y, z - 1),
                    isSolid(x, y + 1, z - 1),
                    isSolid(x + 1, y + 1, z - 1)));
        a3 =
            aoBrightness(
                vertexAO(
                    isSolid(x - 1, y, z - 1),
                    isSolid(x, y + 1, z - 1),
                    isSolid(x - 1, y + 1, z - 1)));
        drawCorrectedQuad(
            x - radius, -radius - y, z - radius, 1, 1, uv, light, a0, a1, a2, a3, BACK);
        break;

      case RIGHT:
        a0 =
            aoBrightness(
                vertexAO(
                    isSolid(x + 1, y, z - 1),
                    isSolid(x + 1, y - 1, z),
                    isSolid(x + 1, y - 1, z - 1)));
        a1 =
            aoBrightness(
                vertexAO(
                    isSolid(x + 1, y, z + 1),
                    isSolid(x + 1, y - 1, z),
                    isSolid(x + 1, y - 1, z + 1)));
        a2 =
            aoBrightness(
                vertexAO(
                    isSolid(x + 1, y, z + 1),
                    isSolid(x + 1, y + 1, z),
                    isSolid(x + 1, y + 1, z + 1)));
        a3 =
            aoBrightness(
                vertexAO(
                    isSolid(x + 1, y, z - 1),
                    isSolid(x + 1, y + 1, z),
                    isSolid(x + 1, y + 1, z - 1)));
        drawCorrectedQuad(
            x + radius, -radius - y, z - radius, 1, 1, uv, light, a0, a1, a2, a3, RIGHT);
        break;

      case LEFT:
        a0 =
            aoBrightness(
                vertexAO(
                    isSolid(x - 1, y, z + 1),
                    isSolid(x - 1, y - 1, z),
                    isSolid(x - 1, y - 1, z + 1)));
        a1 =
            aoBrightness(
                vertexAO(
                    isSolid(x - 1, y, z - 1),
                    isSolid(x - 1, y - 1, z),
                    isSolid(x - 1, y - 1, z - 1)));
        a2 =
            aoBrightness(
                vertexAO(
                    isSolid(x - 1, y, z - 1),
                    isSolid(x - 1, y + 1, z),
                    isSolid(x - 1, y + 1, z - 1)));
        a3 =
            aoBrightness(
                vertexAO(
                    isSolid(x - 1, y, z + 1),
                    isSolid(x - 1, y + 1, z),
                    isSolid(x - 1, y + 1, z + 1)));
        drawCorrectedQuad(
            x - radius, -radius - y, z - radius, 1, 1, uv, light, a0, a1, a2, a3, LEFT);
        break;

      case BOTTOM:
        drawCorrectedQuad(x - radius, radius - y, z - radius, 1, 1, uv, light, 1, 1, 1, 1, BOTTOM);
        break;
    }
  }

  // =========================
  // REMAINING METHODS (UNCHANGED)
  // =========================

  private void drawCorrectedQuad(
      float x,
      float y,
      float z,
      float w,
      float d,
      Vector2f[] uv,
      float light,
      float a0,
      float a1,
      float a2,
      float a3,
      int face) {
    boolean flip = (a0 + a3 > a1 + a2);

    if (face == TOP) {
      if (!flip) {
        drawV(x + w, y, z, uv[0], a1 * light);
        drawV(x, y, z, uv[1], a0 * light);
        drawV(x, y, z + d, uv[2], a2 * light);
        drawV(x + w, y, z + d, uv[3], a3 * light);
      } else {
        drawV(x, y, z, uv[1], a0 * light);
        drawV(x, y, z + d, uv[2], a2 * light);
        drawV(x + w, y, z + d, uv[3], a3 * light);
        drawV(x + w, y, z, uv[0], a1 * light);
      }
    } else if (face == FRONT) {
      if (!flip) {
        drawV(x + w, y, z, uv[0], a0 * light);
        drawV(x, y, z, uv[1], a1 * light);
        drawV(x, y + 1, z, uv[2], a2 * light);
        drawV(x + w, y + 1, z, uv[3], a3 * light);
      } else {
        drawV(x, y, z, uv[1], a1 * light);
        drawV(x, y + 1, z, uv[2], a2 * light);
        drawV(x + w, y + 1, z, uv[3], a3 * light);
        drawV(x + w, y, z, uv[0], a0 * light);
      }
    } else if (face == BACK) {
      if (!flip) {
        drawV(x, y, z, uv[0], a0 * light);
        drawV(x + w, y, z, uv[1], a1 * light);
        drawV(x + w, y + 1, z, uv[2], a2 * light);
        drawV(x, y + 1, z, uv[3], a3 * light);
      } else {
        drawV(x + w, y, z, uv[1], a1 * light);
        drawV(x + w, y + 1, z, uv[2], a2 * light);
        drawV(x, y + 1, z, uv[3], a3 * light);
        drawV(x, y, z, uv[0], a0 * light);
      }
    } else if (face == RIGHT) {
      if (!flip) {
        drawV(x, y, z, uv[0], a0 * light);
        drawV(x, y, z + d, uv[1], a1 * light);
        drawV(x, y + 1, z + d, uv[2], a2 * light);
        drawV(x, y + 1, z, uv[3], a3 * light);
      } else {
        drawV(x, y, z + d, uv[1], a1 * light);
        drawV(x, y + 1, z + d, uv[2], a2 * light);
        drawV(x, y + 1, z, uv[3], a3 * light);
        drawV(x, y, z, uv[0], a0 * light);
      }
    } else if (face == LEFT) {
      if (!flip) {
        drawV(x, y, z + d, uv[0], a0 * light);
        drawV(x, y, z, uv[1], a1 * light);
        drawV(x, y + 1, z, uv[2], a2 * light);
        drawV(x, y + 1, z + d, uv[3], a3 * light);
      } else {
        drawV(x, y, z, uv[1], a1 * light);
        drawV(x, y + 1, z, uv[2], a2 * light);
        drawV(x, y + 1, z + d, uv[3], a3 * light);
        drawV(x, y, z + d, uv[0], a0 * light);
      }
    } else if (face == BOTTOM) {
      drawV(x + w, y, z, uv[0], light);
      drawV(x + w, y, z + d, uv[1], light);
      drawV(x, y, z + d, uv[2], light);
      drawV(x, y, z, uv[3], light);
    }
  }

  private void drawV(float x, float y, float z, Vector2f uv, float c) {
    shape.color(c, c, c);
    shape.vertex(x, y, z, uv.x, uv.y);
  }

  private boolean isSolid(int x, int y, int z) {
    if (y < 0 || y >= Chunk.HEIGHT) return false;

    if (x >= 0 && x < Chunk.WIDTH && z >= 0 && z < Chunk.DEPTH) {
      return chunk.isBlockSolid(x, y, z);
    }

    int cx = chunk.getChunkX();
    int cz = chunk.getChunkZ();

    if (x < 0) cx--;
    else if (x >= Chunk.WIDTH) cx++;

    if (z < 0) cz--;
    else if (z >= Chunk.DEPTH) cz++;

    Chunk neighbor = chunkManager.getChunk(cx, cz);

    if (neighbor == null || !neighbor.isDataReady()) return false;

    int nx = (x + Chunk.WIDTH) % Chunk.WIDTH;
    int nz = (z + Chunk.DEPTH) % Chunk.DEPTH;

    return neighbor.isBlockSolid(nx, y, nz);
  }

  private int getBlockData(int x, int y, int z) {
    if (x >= 0 && x < Chunk.WIDTH && y >= 0 && y < Chunk.HEIGHT && z >= 0 && z < Chunk.DEPTH) {
      return chunk.getBlockData(x, y, z);
    }

    int cx = chunk.getChunkX();
    int cz = chunk.getChunkZ();

    if (x < 0) cx--;
    else if (x >= Chunk.WIDTH) cx++;

    if (z < 0) cz--;
    else if (z >= Chunk.DEPTH) cz++;

    Chunk neighbor = chunkManager.getChunk(cx, cz);

    if (neighbor == null || !neighbor.isDataReady()) {
      return BlockType.AIR.getId();
    }

    int nx = (x + Chunk.WIDTH) % Chunk.WIDTH;
    int nz = (z + Chunk.DEPTH) % Chunk.DEPTH;

    return neighbor.getBlockData(nx, y, nz);
  }

  public boolean shouldRender(int myId, int x, int y, int z) {
    if (y < 0) return false;
    if (y >= Chunk.HEIGHT) return true;

    int neighborId = getBlockData(x, y, z);

    if (myId != BlockType.WATER.getId() && neighborId == BlockType.WATER.getId()) return true;

    if (myId == BlockType.WATER.getId() && neighborId == BlockType.WATER.getId()) return false;

    return !isSolid(x, y, z);
  }

  private int vertexAO(boolean s1, boolean s2, boolean c) {
    if (s1 && s2) return 0;
    return 3 - ((s1 ? 1 : 0) + (s2 ? 1 : 0) + (c ? 1 : 0));
  }

  private float aoBrightness(int ao) {
    return 0.6f + (ao / 3.0f) * 0.4f;
  }

  private void createBillBoard(int x, int y, int z) {
    int[] uvIdx = textureAtlas.getUVIndices(chunk.getBlockData(x, y, z), 0);

    Vector2f[] vuv = {uvs.get(uvIdx[0]), uvs.get(uvIdx[1]), uvs.get(uvIdx[2]), uvs.get(uvIdx[3])};

    shape.color(0.9f, 0.9f, 0.9f);

    shape.vertex(radius + x, -radius - y, radius + z, vuv[0].x, vuv[0].y);
    shape.vertex(-radius + x, -radius - y, -radius + z, vuv[1].x, vuv[1].y);
    shape.vertex(-radius + x, radius - y, -radius + z, vuv[2].x, vuv[2].y);
    shape.vertex(radius + x, radius - y, radius + z, vuv[3].x, vuv[3].y);

    shape.vertex(radius + x, -radius - y, -radius + z, vuv[0].x, vuv[0].y);
    shape.vertex(-radius + x, -radius - y, radius + z, vuv[1].x, vuv[1].y);
    shape.vertex(-radius + x, radius - y, radius + z, vuv[2].x, vuv[2].y);
    shape.vertex(radius + x, radius - y, -radius + z, vuv[3].x, vuv[3].y);
  }
}
