package client.world;

import java.util.Arrays;
import client.resources.TextureAtlas;
import client.ui.GameTextures;
import common.game.block.BlockRegistry;
import common.game.block.BlockShape;
import common.game.block.Blocks;
import engine.backend.processing.BufferedShape;
import engine.components.StaticGeometry;
import engine.rendering.Material;
import engine.resources.FilterMode;
import engine.resources.Texture;
import engine.resources.TextureWrapMode;
import math.Vector2f;

public class ChunkMesher {

  private static final int TOP = 0;

  private static final int BOTTOM = 1;

  private static final int FRONT = 2;

  private static final int BACK = 3;

  private static final int RIGHT = 4;

  private static final int LEFT = 5;

  private static final float radius = 0.5f;

  private final Chunk chunk;

  private final ChunkManager chunkManager;

  private BufferedShape opaqueShape;

  private BufferedShape waterShape;

  private BufferedShape decorShape;

  private BufferedShape currentShape;

  public static final Material sharedMaterial;

  private static final TextureAtlas textureAtlas;

  private short[] localCache;

  private static final int P_WIDTH = Chunk.WIDTH + 2;

  private static final int P_DEPTH = Chunk.DEPTH + 2;

  private static final ThreadLocal<short[]> CACHE_HOLDER =
      new ThreadLocal<short[]>() {
        @Override
        protected short[] initialValue() {
          return new short[P_WIDTH * Chunk.HEIGHT * P_DEPTH];
        }
      };

  static {
    sharedMaterial = new Material();
    textureAtlas = GameTextures.TEXTURE_ATLAS;
    Texture texture = textureAtlas.getTexture();
    texture.setFilterMode(FilterMode.POINT);
    texture.setTextureWrapMode(TextureWrapMode.CLAMP);
    sharedMaterial.setDiffuseTexture(texture);
  }

  public ChunkMesher(Chunk chunk, ChunkManager chunkManager) {
    this.chunk = chunk;
    this.chunkManager = chunkManager;
  }

  public static class MeshResult {

    public final StaticGeometry opaque;

    public final StaticGeometry water;

    public final StaticGeometry decor;

    public MeshResult(StaticGeometry o, StaticGeometry w, StaticGeometry d) {
      this.opaque = o;
      this.water = w;
      this.decor = d;
    }
  }

  public MeshResult createMesh() {
    long startTime = System.nanoTime();

    fillCache();

    opaqueShape = new BufferedShape(sharedMaterial);
    waterShape = new BufferedShape(sharedMaterial);
    decorShape = new BufferedShape(sharedMaterial);

    opaqueShape.begin(BufferedShape.QUADS);
    waterShape.begin(BufferedShape.QUADS);
    decorShape.begin(BufferedShape.QUADS);

    for (int x = 0; x < Chunk.WIDTH; x++) {
      for (int y = 0; y < Chunk.HEIGHT; y++) {
        for (int z = 0; z < Chunk.DEPTH; z++) {
          short blockId = chunk.getBlockId(x, y, z);
          if (blockId == Blocks.AIR.getId()) continue;
          if (BlockRegistry.getTypeUnsafe(blockId).getShape() == BlockShape.CROSS) continue;

          currentShape = (blockId == Blocks.WATER.getId()) ? waterShape : opaqueShape;

          for (int face = 0; face < 6; face++) {
            if (shouldRender(blockId, x, y, z, face)) {
              addSingleFace(x, y, z, blockId, face);
            }
          }
        }
      }
    }

    generateDecor();
    opaqueShape.end();
    waterShape.end();
    decorShape.end();

    long endTime = System.nanoTime();
    double durationMs = (endTime - startTime) / 1_000_000.0;

    System.out.format(
        "Chunk [%d, %d] meshed in: %.3f ms%n", chunk.getChunkX(), chunk.getChunkZ(), durationMs);

    return new MeshResult(
        new StaticGeometry(opaqueShape.getVBO(), sharedMaterial),
        new StaticGeometry(waterShape.getVBO(), sharedMaterial),
        new StaticGeometry(decorShape.getVBO(), sharedMaterial));
  }

  /**
   * AO calculation taking into account the inverted Y-axis in rendering. If rendering uses -Y as
   * up, we need to adjust how we query world data (+Y as up).
   */
  private float getAO(
      int sx1, int sy1, int sz1, int sx2, int sy2, int sz2, int cx, int cy, int cz) {
    int s1 = isSolidFast(sx1, sy1, sz1) ? 1 : 0;
    int s2 = isSolidFast(sx2, sy2, sz2) ? 1 : 0;
    int c = isSolidFast(cx, cy, cz) ? 1 : 0;

    if (s1 == 1 && s2 == 1) return 0.45f;
    return 1.0f - (s1 + s2 + c) * 0.18f;
  }

  private void addSingleFace(int x, int y, int z, short blockId, int face) {
    Vector2f[] uv = textureAtlas.getUVCoordinates(blockId, face);
    float baseLight;
    switch (face) {
      case FRONT:
      case BACK:
        baseLight = 0.85f;
        break;
      case LEFT:
      case RIGHT:
        baseLight = 0.7f;
        break;
      case BOTTOM:
        baseLight = 0.5f;
        break;
      default:
        baseLight = 1.0f;
        break;
    }

    float xf = x - radius, yf = y - radius, zf = z - radius;
    float a0, a1, a2, a3;

    switch (face) {
      case TOP:
        yf += 1.0f;
        a0 = getAO(x - 1, y + 1, z, x, y + 1, z - 1, x - 1, y + 1, z - 1) * baseLight;
        a1 = getAO(x - 1, y + 1, z, x, y + 1, z + 1, x - 1, y + 1, z + 1) * baseLight;
        a2 = getAO(x + 1, y + 1, z, x, y + 1, z + 1, x + 1, y + 1, z + 1) * baseLight;
        a3 = getAO(x + 1, y + 1, z, x, y + 1, z - 1, x + 1, y + 1, z - 1) * baseLight;
        drawFace(
            xf, -yf, zf, xf, -yf, zf + 1, xf + 1, -yf, zf + 1, xf + 1, -yf, zf, a0, a1, a2, a3, uv);
        break;

      case BOTTOM:
        a0 = getAO(x - 1, y - 1, z, x, y - 1, z - 1, x - 1, y - 1, z - 1) * baseLight;
        a1 = getAO(x + 1, y - 1, z, x, y - 1, z - 1, x + 1, y - 1, z - 1) * baseLight;
        a2 = getAO(x + 1, y - 1, z, x, y - 1, z + 1, x + 1, y - 1, z + 1) * baseLight;
        a3 = getAO(x - 1, y - 1, z, x, y - 1, z + 1, x - 1, y - 1, z + 1) * baseLight;
        drawFace(
            xf, -yf, zf, xf + 1, -yf, zf, xf + 1, -yf, zf + 1, xf, -yf, zf + 1, a0, a1, a2, a3, uv);
        break;

      case FRONT:
        zf += 1.0f;
        a0 = getAO(x - 1, y, z + 1, x, y - 1, z + 1, x - 1, y - 1, z + 1) * baseLight;
        a1 = getAO(x + 1, y, z + 1, x, y - 1, z + 1, x + 1, y - 1, z + 1) * baseLight;
        a2 = getAO(x + 1, y, z + 1, x, y + 1, z + 1, x + 1, y + 1, z + 1) * baseLight;
        a3 = getAO(x - 1, y, z + 1, x, y + 1, z + 1, x - 1, y + 1, z + 1) * baseLight;
        drawFace(
            xf, -yf, zf, xf + 1, -yf, zf, xf + 1, -yf - 1, zf, xf, -yf - 1, zf, a0, a1, a2, a3, uv);
        break;

      case BACK:
        a0 = getAO(x + 1, y, z - 1, x, y - 1, z - 1, x + 1, y - 1, z - 1) * baseLight;
        a1 = getAO(x - 1, y, z - 1, x, y - 1, z - 1, x - 1, y - 1, z - 1) * baseLight;
        a2 = getAO(x - 1, y, z - 1, x, y + 1, z - 1, x - 1, y + 1, z - 1) * baseLight;
        a3 = getAO(x + 1, y, z - 1, x, y + 1, z - 1, x + 1, y + 1, z - 1) * baseLight;
        drawFace(
            xf + 1, -yf, zf, xf, -yf, zf, xf, -yf - 1, zf, xf + 1, -yf - 1, zf, a0, a1, a2, a3, uv);
        break;

      case RIGHT:
        xf += 1.0f;
        a0 = getAO(x + 1, y, z + 1, x + 1, y - 1, z, x + 1, y - 1, z + 1) * baseLight;
        a1 = getAO(x + 1, y, z - 1, x + 1, y - 1, z, x + 1, y - 1, z - 1) * baseLight;
        a2 = getAO(x + 1, y, z - 1, x + 1, y + 1, z, x + 1, y + 1, z - 1) * baseLight;
        a3 = getAO(x + 1, y, z + 1, x + 1, y + 1, z, x + 1, y + 1, z + 1) * baseLight;
        drawFace(
            xf, -yf, zf + 1, xf, -yf, zf, xf, -yf - 1, zf, xf, -yf - 1, zf + 1, a0, a1, a2, a3, uv);
        break;

      case LEFT:
        a0 = getAO(x - 1, y, z - 1, x - 1, y - 1, z, x - 1, y - 1, z - 1) * baseLight;
        a1 = getAO(x - 1, y, z + 1, x - 1, y - 1, z, x - 1, y - 1, z + 1) * baseLight;
        a2 = getAO(x - 1, y, z + 1, x - 1, y + 1, z, x - 1, y + 1, z + 1) * baseLight;
        a3 = getAO(x - 1, y, z - 1, x - 1, y + 1, z, x - 1, y + 1, z - 1) * baseLight;
        drawFace(
            xf, -yf, zf, xf, -yf, zf + 1, xf, -yf - 1, zf + 1, xf, -yf - 1, zf, a0, a1, a2, a3, uv);
        break;
    }
  }

  private void drawFace(
      float x1,
      float y1,
      float z1,
      float x2,
      float y2,
      float z2,
      float x3,
      float y3,
      float z3,
      float x4,
      float y4,
      float z4,
      float a0,
      float a1,
      float a2,
      float a3,
      Vector2f[] uv) {

    if (a0 + a2 < a1 + a3) {
      currentShape.color(a1, a1, a1);
      currentShape.vertex(x2, y2, z2, uv[0].x, uv[1].y);
      currentShape.color(a2, a2, a2);
      currentShape.vertex(x3, y3, z3, uv[0].x, uv[2].y);
      currentShape.color(a3, a3, a3);
      currentShape.vertex(x4, y4, z4, uv[1].x, uv[2].y);
      currentShape.color(a0, a0, a0);
      currentShape.vertex(x1, y1, z1, uv[1].x, uv[1].y);
    } else {
      currentShape.color(a0, a0, a0);
      currentShape.vertex(x1, y1, z1, uv[1].x, uv[1].y);
      currentShape.color(a1, a1, a1);
      currentShape.vertex(x2, y2, z2, uv[0].x, uv[1].y);
      currentShape.color(a2, a2, a2);
      currentShape.vertex(x3, y3, z3, uv[0].x, uv[2].y);
      currentShape.color(a3, a3, a3);
      currentShape.vertex(x4, y4, z4, uv[1].x, uv[2].y);
    }
  }

  public boolean shouldRender(int myId, int x, int y, int z, int face) {
    int nx = x, ny = y, nz = z;
    switch (face) {
      case TOP:
        ny++;
        break;
      case BOTTOM:
        ny--;
        break;
      case FRONT:
        nz++;
        break;
      case BACK:
        nz--;
        break;
      case RIGHT:
        nx++;
        break;
      case LEFT:
        nx--;
        break;
    }
    if (ny < 0) return false;
    if (ny >= Chunk.HEIGHT) return true;
    int neighborId = getBlockDataFast(nx, ny, nz);
    if (myId == Blocks.WATER.getId()) return neighborId == Blocks.AIR.getId();
    if (neighborId == Blocks.WATER.getId()) return true;
    return !isSolidFast(nx, ny, nz);
  }

  private void fillCache() {
    this.localCache = CACHE_HOLDER.get();
    Arrays.fill(localCache, (short) 0);
    for (int ox = -1; ox <= 1; ox++) {
      for (int oz = -1; oz <= 1; oz++) {
        Chunk neighbor = chunkManager.getChunk(chunk.getChunkX() + ox, chunk.getChunkZ() + oz);
        if (neighbor == null || !neighbor.isDataReady()) continue;
        for (int x = 0; x < Chunk.WIDTH; x++) {
          for (int z = 0; z < Chunk.DEPTH; z++) {
            for (int y = 0; y < Chunk.HEIGHT; y++) {
              int cx = x + 1 + (ox * Chunk.WIDTH);
              int cz = z + 1 + (oz * Chunk.DEPTH);
              if (cx >= 0 && cx < P_WIDTH && cz >= 0 && cz < P_DEPTH) {
                localCache[(cx * Chunk.HEIGHT * P_DEPTH) + (y * P_DEPTH) + cz] =
                    neighbor.getBlockId(x, y, z);
              }
            }
          }
        }
      }
    }
  }

  private int getBlockDataFast(int x, int y, int z) {
    if (y < 0 || y >= Chunk.HEIGHT) return 0;
    int idx = ((x + 1) * Chunk.HEIGHT * P_DEPTH) + (y * P_DEPTH) + (z + 1);
    return (idx < 0 || idx >= localCache.length) ? 0 : localCache[idx];
  }

  private boolean isSolidFast(int x, int y, int z) {
    int id = getBlockDataFast(x, y, z);
    return id != 0 && BlockRegistry.getTypeUnsafe((short) id).isSolid();
  }

  private void generateDecor() {
    for (int x = 0; x < Chunk.WIDTH; x++) {
      for (int z = 0; z < Chunk.DEPTH; z++) {
        for (int y = 0; y < Chunk.HEIGHT; y++) {
          short id = chunk.getBlockId(x, y, z);
          if (id != 0 && BlockRegistry.getTypeUnsafe(id).getShape() == BlockShape.CROSS)
            createBillBoard(x, y, z);
        }
      }
    }
  }

  private void createBillBoard(int x, int y, int z) {
    currentShape = decorShape;
    short blockId = chunk.getBlockId(x, y, z);
    Vector2f[] uv = textureAtlas.getUVCoordinates(blockId, 0);
    float cx = x, cy = y, cz = z, h = 1.0f, off = 0.5f;
    currentShape.color(1, 1, 1);
    currentShape.vertex(cx - off, -cy + radius, cz - off, uv[1].x, uv[1].y);
    currentShape.vertex(cx + off, -cy + radius, cz + off, uv[0].x, uv[1].y);
    currentShape.vertex(cx + off, -cy - h + radius, cz + off, uv[0].x, uv[2].y);
    currentShape.vertex(cx - off, -cy - h + radius, cz - off, uv[1].x, uv[2].y);
    currentShape.vertex(cx - off, -cy + radius, cz + off, uv[1].x, uv[1].y);
    currentShape.vertex(cx + off, -cy + radius, cz - off, uv[0].x, uv[1].y);
    currentShape.vertex(cx + off, -cy - h + radius, cz - off, uv[0].x, uv[2].y);
    currentShape.vertex(cx - off, -cy - h + radius, cz + off, uv[1].x, uv[2].y);
  }
}
