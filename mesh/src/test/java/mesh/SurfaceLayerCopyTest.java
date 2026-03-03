package mesh;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import mesh.next.surface.SurfaceLayer;

public class SurfaceLayerCopyTest {

  @Test
  void copy_shouldDeepCopyUVs() {
    SurfaceLayer surface = new SurfaceLayer();
    surface.addUV(0.5f, 0.5f);

    SurfaceLayer copy = surface.copy();

    copy.getUvAt(0).x = 999f;

    assertNotEquals(surface.getUvAt(0).x, copy.getUvAt(0).x);
  }

  @Test
  void copy_shouldDeepCopyFaceUVIndices() {
    SurfaceLayer surface = new SurfaceLayer();
    surface.addUV(0, 0);
    surface.setFaceUVIndices(0, new int[] {0});

    SurfaceLayer copy = surface.copy();

    copy.getFaceUVIndices(0)[0] = 999;

    assertNotEquals(surface.getFaceUVIndices(0)[0], copy.getFaceUVIndices(0)[0]);
  }
}
