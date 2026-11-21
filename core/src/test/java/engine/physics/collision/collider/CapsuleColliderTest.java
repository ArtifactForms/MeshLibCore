package engine.physics.collision.collider;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import math.Vector3f;

class CapsuleColliderTest {

  @Test
  void constructorStoresRadiusAndHalfHeight() {
    CapsuleCollider collider = new CapsuleCollider(0.5f, 1.0f);

    assertEquals(0.5f, collider.getRadius());
    assertEquals(1.0f, collider.getHalfHeight());
  }

  @Test
  void constructorAllowsZeroValues() {
    CapsuleCollider collider = new CapsuleCollider(0.0f, 0.0f);

    assertEquals(0.0f, collider.getRadius());
    assertEquals(0.0f, collider.getHalfHeight());
  }

  @Test
  void constructorRejectsNegativeRadius() {
    assertThrows(IllegalArgumentException.class, () -> new CapsuleCollider(-0.5f, 1.0f));
  }

  @Test
  void constructorRejectsNegativeHalfHeight() {
    assertThrows(IllegalArgumentException.class, () -> new CapsuleCollider(0.5f, -1.0f));
  }

  @Test
  void getTypeReturnsCapsule() {
    CapsuleCollider collider = new CapsuleCollider(0.5f, 1.0f);

    assertEquals(ColliderType.CAPSULE, collider.getType());
  }

  @Test
  void getSegmentReturnsCorrectSphereCenters() {
    CapsuleCollider collider = new CapsuleCollider(0.5f, 1.0f);

    Vector3f center = new Vector3f(2.0f, 3.0f, 4.0f);
    Vector3f outA = new Vector3f();
    Vector3f outB = new Vector3f();

    collider.getSegment(center, outA, outB);

    // -Y is up in this engine.
    assertVectorEquals(new Vector3f(2.0f, 2.0f, 4.0f), outA);
    assertVectorEquals(new Vector3f(2.0f, 4.0f, 4.0f), outB);
  }

  @Test
  void getSegmentDoesNotModifyCenter() {
    CapsuleCollider collider = new CapsuleCollider(0.5f, 1.0f);

    Vector3f center = new Vector3f(2.0f, 3.0f, 4.0f);
    Vector3f originalCenter = new Vector3f(center);

    collider.getSegment(center, new Vector3f(), new Vector3f());

    assertVectorEquals(originalCenter, center);
  }

  private void assertVectorEquals(Vector3f expected, Vector3f actual) {
    assertEquals(expected.x, actual.x, 0.0001f);
    assertEquals(expected.y, actual.y, 0.0001f);
    assertEquals(expected.z, actual.z, 0.0001f);
  }
}
