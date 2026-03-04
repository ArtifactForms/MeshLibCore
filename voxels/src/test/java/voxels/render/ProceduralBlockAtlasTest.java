package voxels.render;

import mesh.next.surface.SurfaceLayer;
import org.junit.jupiter.api.Test;
import voxels.world.Blocks;

import static org.junit.jupiter.api.Assertions.*;

class ProceduralBlockAtlasTest {

  @Test
  void appendUVsAddsExpectedAmount() {
    ProceduralBlockAtlas atlas = new ProceduralBlockAtlas();
    SurfaceLayer surfaceLayer = new SurfaceLayer();

    atlas.appendUVs(surfaceLayer);

    int expectedUvCount = atlas.getTileTypeCount() * 6 * 4;
    assertEquals(expectedUvCount, surfaceLayer.getUVCount());
  }

  @Test
  void getFaceUVIndicesClampsOutOfRangeValues() {
    ProceduralBlockAtlas atlas = new ProceduralBlockAtlas();

    int[] uvIndices = atlas.getFaceUVIndices((short) 999, 999);

    assertEquals(4, uvIndices.length);
    assertTrue(uvIndices[0] >= 0);
    assertTrue(uvIndices[0] > uvIndices[1]);
    assertTrue(uvIndices[1] > uvIndices[2]);
    assertTrue(uvIndices[2] > uvIndices[3]);
  }

  @Test
  void grassUsesDifferentTilesForTopBottomAndSides() {
    ProceduralBlockAtlas atlas = new ProceduralBlockAtlas();

    int[] top = atlas.getFaceUVIndices(Blocks.GRASS, ProceduralBlockAtlas.FACE_TOP);
    int[] side = atlas.getFaceUVIndices(Blocks.GRASS, ProceduralBlockAtlas.FACE_FRONT);
    int[] bottom = atlas.getFaceUVIndices(Blocks.GRASS, ProceduralBlockAtlas.FACE_BOTTOM);

    assertNotEquals(top[3], side[3]);
    assertNotEquals(side[3], bottom[3]);
  }


  @Test
  void firstAtlasRowUsesHighVCoordinatesForProcessingBackendFlip() {
    ProceduralBlockAtlas atlas = new ProceduralBlockAtlas();
    SurfaceLayer surfaceLayer = new SurfaceLayer();

    atlas.appendUVs(surfaceLayer);

    float firstTileMinV = Float.MAX_VALUE;
    for (int i = 0; i < 4; i++) {
      firstTileMinV = Math.min(firstTileMinV, surfaceLayer.getUvAt(i).y);
    }

    assertTrue(firstTileMinV > 0.8f);
  }

  @Test
  void lastAtlasRowUsesLowVCoordinatesForProcessingBackendFlip() {
    ProceduralBlockAtlas atlas = new ProceduralBlockAtlas();
    SurfaceLayer surfaceLayer = new SurfaceLayer();

    atlas.appendUVs(surfaceLayer);

    int rowCount = atlas.getTileTypeCount();
    int uvPerTile = 4;
    int faceCount = 6;
    int lastRowStart = (rowCount - 1) * faceCount * uvPerTile;

    float lastRowMaxV = Float.MIN_VALUE;
    for (int i = 0; i < uvPerTile; i++) {
      lastRowMaxV = Math.max(lastRowMaxV, surfaceLayer.getUvAt(lastRowStart + i).y);
    }

    assertTrue(lastRowMaxV < 0.2f);
  }


  @Test
  void vFlipCanBeDisabledForNonProcessingBackends() {
    ProceduralBlockAtlas atlas = new ProceduralBlockAtlas(false);
    SurfaceLayer surfaceLayer = new SurfaceLayer();

    atlas.appendUVs(surfaceLayer);

    float firstTileMaxV = Float.MIN_VALUE;
    for (int i = 0; i < 4; i++) {
      firstTileMaxV = Math.max(firstTileMaxV, surfaceLayer.getUvAt(i).y);
    }

    assertTrue(firstTileMaxV < 0.2f);
  }

  @Test
  void stoneAndGrassTopUseDifferentTiles() {
    ProceduralBlockAtlas atlas = new ProceduralBlockAtlas();

    int[] stoneTop = atlas.getFaceUVIndices(Blocks.STONE, ProceduralBlockAtlas.FACE_TOP);
    int[] grassTop = atlas.getFaceUVIndices(Blocks.GRASS, ProceduralBlockAtlas.FACE_TOP);

    assertNotEquals(stoneTop[3], grassTop[3]);
  }

}