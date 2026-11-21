package engine.physics.collision.collider;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import math.Vector3f;

class AABBColliderTest {

  @Test
  void constructorStoresHalfExtents() {
    Vector3f halfExtents = new Vector3f(1.0f, 2.0f, 3.0f);

    AABBCollider collider = new AABBCollider(halfExtents);

    assertVectorEquals(halfExtents, collider.getHalfExtents());
  }

  @Test
  void constructorCreatesDefensiveCopy() {
    Vector3f halfExtents = new Vector3f(1.0f, 2.0f, 3.0f);

    AABBCollider collider = new AABBCollider(halfExtents);

    halfExtents.set(5.0f, 5.0f, 5.0f);

    assertVectorEquals(new Vector3f(1.0f, 2.0f, 3.0f), collider.getHalfExtents());
  }

  @Test
  void getHalfExtentsReturnsDefensiveCopy() {
    AABBCollider collider = new AABBCollider(new Vector3f(1.0f, 2.0f, 3.0f));

    Vector3f result = collider.getHalfExtents();
    result.set(5.0f, 5.0f, 5.0f);

    assertVectorEquals(new Vector3f(1.0f, 2.0f, 3.0f), collider.getHalfExtents());
  }

  @Test
  void getHalfExtentsStoresValuesInProvidedVector() {
    AABBCollider collider = new AABBCollider(new Vector3f(1.0f, 2.0f, 3.0f));

    Vector3f store = new Vector3f();
    Vector3f result = collider.getHalfExtents(store);

    assertSame(store, result);
    assertVectorEquals(new Vector3f(1.0f, 2.0f, 3.0f), store);
  }

  @Test
  void getHalfExtentsRejectsNullStore() {
    AABBCollider collider = new AABBCollider(new Vector3f(1.0f, 2.0f, 3.0f));

    assertThrows(IllegalArgumentException.class, () -> collider.getHalfExtents(null));
  }

  @Test
  void constructorRejectsNullHalfExtents() {
    assertThrows(IllegalArgumentException.class, () -> new AABBCollider(null));
  }

  @Test
  void constructorRejectsZeroHalfExtents() {
    assertThrows(
        IllegalArgumentException.class, () -> new AABBCollider(new Vector3f(0.0f, 1.0f, 1.0f)));

    assertThrows(
        IllegalArgumentException.class, () -> new AABBCollider(new Vector3f(1.0f, 0.0f, 1.0f)));

    assertThrows(
        IllegalArgumentException.class, () -> new AABBCollider(new Vector3f(1.0f, 1.0f, 0.0f)));
  }

  @Test
  void constructorRejectsNegativeHalfExtents() {
    assertThrows(
        IllegalArgumentException.class, () -> new AABBCollider(new Vector3f(-1.0f, 1.0f, 1.0f)));

    assertThrows(
        IllegalArgumentException.class, () -> new AABBCollider(new Vector3f(1.0f, -1.0f, 1.0f)));

    assertThrows(
        IllegalArgumentException.class, () -> new AABBCollider(new Vector3f(1.0f, 1.0f, -1.0f)));
  }

  @Test
  void getTypeReturnsAABB() {
    AABBCollider collider = new AABBCollider(new Vector3f(1.0f, 2.0f, 3.0f));

    assertEquals(ColliderType.AABB, collider.getType());
  }

  private void assertVectorEquals(Vector3f expected, Vector3f actual) {
    assertEquals(expected.x, actual.x, 0.0001f);
    assertEquals(expected.y, actual.y, 0.0001f);
    assertEquals(expected.z, actual.z, 0.0001f);
  }
}
