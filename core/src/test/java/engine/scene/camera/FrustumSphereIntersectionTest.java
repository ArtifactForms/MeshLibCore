package engine.scene.camera;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import math.Vector3f;

class FrustumSphereIntersectionTest {

  private Frustum frustum;

  @BeforeEach
  void setup() {

    frustum = new Frustum();

    frustum.left.set(1, 0, 0, 1);
    frustum.right.set(-1, 0, 0, 1);

    frustum.bottom.set(0, 1, 0, 1);
    frustum.top.set(0, -1, 0, 1);

    frustum.near.set(0, 0, 1, 1);
    frustum.far.set(0, 0, -1, 1);
  }

  @Test
  void testSphereInside() {

    assertTrue(frustum.intersectsSphere(new Vector3f(0, 0, 0), 0.5f));
  }

  @Test
  void testSphereOutside() {

    assertFalse(frustum.intersectsSphere(new Vector3f(3, 0, 0), 0.5f));
  }

  @Test
  void testSphereIntersecting() {

    assertTrue(frustum.intersectsSphere(new Vector3f(1.2f, 0, 0), 0.5f));
  }
}
