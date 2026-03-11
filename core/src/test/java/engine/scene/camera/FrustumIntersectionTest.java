package engine.scene.camera;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import math.Bounds;
import math.Vector3f;

class FrustumIntersectionTest {

  private Frustum frustum;

  @BeforeEach
  void setup() {
    frustum = new Frustum();

    // Cube frustum from -1..1 in all axes

    frustum.left.set(1, 0, 0, 1); // x >= -1
    frustum.right.set(-1, 0, 0, 1); // x <= 1

    frustum.bottom.set(0, 1, 0, 1); // y >= -1
    frustum.top.set(0, -1, 0, 1); // y <= 1

    frustum.near.set(0, 0, 1, 1); // z >= -1
    frustum.far.set(0, 0, -1, 1); // z <= 1
  }

  @Test
  void testAABBInsideFrustum() {

    Bounds box = new Bounds(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0.5f, 0.5f, 0.5f));

    assertTrue(frustum.intersectsAABB(box));
  }

  @Test
  void testAABBOutsideFrustum() {

    Bounds box = new Bounds(new Vector3f(2f, 2f, 2f), new Vector3f(3f, 3f, 3f));

    assertFalse(frustum.intersectsAABB(box));
  }

  @Test
  void testAABBIntersectingFrustum() {

    Bounds box = new Bounds(new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(2f, 2f, 2f));

    assertTrue(frustum.intersectsAABB(box));
  }

  @Test
  void testAABBOnFrustumPlane() {

    Bounds box = new Bounds(new Vector3f(1f, -0.2f, -0.2f), new Vector3f(1.5f, 0.2f, 0.2f));

    assertTrue(frustum.intersectsAABB(box));
  }

  @Test
  void testSphereInside() {

    Vector3f center = new Vector3f(0, 0, 0);

    assertTrue(frustum.intersectsSphere(center, 0.5f));
  }

  @Test
  void testSphereOutside() {

    Vector3f center = new Vector3f(5, 5, 5);

    assertFalse(frustum.intersectsSphere(center, 1f));
  }

  @Test
  void testChunkBoundsCulling() {

    Bounds chunk = new Bounds(new Vector3f(-8, -8, -8), new Vector3f(8, 8, 8));

    assertTrue(frustum.intersectsAABB(chunk));
  }
}
