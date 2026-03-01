package engine.resources;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import mesh.SubMesh;
import mesh.next.surface.SurfaceLayer;

class OBJLoaderTest {

  @Test
  void loadsSurfaceLayerUvAndMaterialSlots() {
    OBJLoader loader = new OBJLoader();

    Model model = loader.load("test/multi_material_uv.obj");

    assertNotNull(model);
    assertNotNull(model.getMesh());
    assertEquals(4, model.getMesh().getVertexCount());

    SurfaceLayer surfaceLayer = model.getMesh().getSurfaceLayer();
    assertEquals(4, surfaceLayer.getUVCount());
    assertArrayEquals(new int[] {0, 1, 2}, surfaceLayer.getFaceUVIndices(0));
    assertArrayEquals(new int[] {0, 2, 3}, surfaceLayer.getFaceUVIndices(1));

    assertEquals(2, model.getSubMeshes().size());

    SubMesh firstSlot = model.getSubMeshes().get(0);
    SubMesh secondSlot = model.getSubMeshes().get(1);

    assertEquals("SlotA", firstSlot.getMaterialName());
    assertEquals("SlotB", secondSlot.getMaterialName());
    assertEquals(1, firstSlot.getFaces().size());
    assertEquals(1, secondSlot.getFaces().size());
  }
}
