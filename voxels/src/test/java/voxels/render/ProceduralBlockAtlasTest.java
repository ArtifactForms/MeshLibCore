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
}
