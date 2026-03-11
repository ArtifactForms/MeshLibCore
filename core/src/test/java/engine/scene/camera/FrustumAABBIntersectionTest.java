package engine.scene.camera;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import math.Bounds;
import math.Vector3f;

class FrustumAABBIntersectionTest {

  private Frustum frustum;

  @BeforeEach
  void setup() {

    frustum = new Frustum();

    frustum.left.set(1,0,0,1);
    frustum.right.set(-1,0,0,1);

    frustum.bottom.set(0,1,0,1);
    frustum.top.set(0,-1,0,1);

    frustum.near.set(0,0,1,1);
    frustum.far.set(0,0,-1,1);
  }

  @Test
  void testAABBInsideFrustum() {

    Bounds box = new Bounds(
        new Vector3f(-0.5f,-0.5f,-0.5f),
        new Vector3f(0.5f,0.5f,0.5f));

    assertTrue(frustum.intersectsAABB(box));
  }

  @Test
  void testAABBOutsideFrustum() {

    Bounds box = new Bounds(
        new Vector3f(2,2,2),
        new Vector3f(3,3,3));

    assertFalse(frustum.intersectsAABB(box));
  }

  @Test
  void testAABBIntersectingFrustum() {

    Bounds box = new Bounds(
        new Vector3f(0.5f,0.5f,0.5f),
        new Vector3f(2,2,2));

    assertTrue(frustum.intersectsAABB(box));
  }
}