package engine.scene.camera;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import math.Matrix4f;
import math.Vector3f;

class FrustumPlaneExtractionTest {

  @Test
  void testPlaneExtractionFromIdentityMatrix() {

    Matrix4f vp = new Matrix4f(Matrix4f.IDENTITY);

    Frustum frustum = new Frustum();
    frustum.update(vp);

    // Point inside
    Vector3f inside = new Vector3f(0, 0, 0);

    assertTrue(frustum.left.distanceToPoint(inside) >= 0);
    assertTrue(frustum.right.distanceToPoint(inside) >= 0);
    assertTrue(frustum.top.distanceToPoint(inside) >= 0);
    assertTrue(frustum.bottom.distanceToPoint(inside) >= 0);
    assertTrue(frustum.near.distanceToPoint(inside) >= 0);
    assertTrue(frustum.far.distanceToPoint(inside) >= 0);
  }
}
