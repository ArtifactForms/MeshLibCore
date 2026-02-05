package engine.collision.narrowphase;

import engine.collision.collider.*;
import engine.collision.component.ColliderComponent;
import engine.collision.contact.Contact;
import math.Vector3f;

public final class CollisionTests {

  public static Contact test(ColliderComponent a, ColliderComponent b) {

    Collider ca = a.getCollider();
    Collider cb = b.getCollider();

    // Sphere / Sphere
    if (ca.getType() == ColliderType.SPHERE && cb.getType() == ColliderType.SPHERE) {
      return sphereSphere(
          (SphereCollider) ca, a,
          (SphereCollider) cb, b);
    }

    // AABB / AABB
    if (ca.getType() == ColliderType.AABB && cb.getType() == ColliderType.AABB) {
      return aabbAabb(
          (AABBCollider) ca, a,
          (AABBCollider) cb, b);
    }

    // Sphere / AABB
    if (ca.getType() == ColliderType.SPHERE && cb.getType() == ColliderType.AABB) {
      return sphereAabb(
          (SphereCollider) ca, a,
          (AABBCollider) cb, b);
    }

    if (ca.getType() == ColliderType.AABB && cb.getType() == ColliderType.SPHERE) {
      Contact c =
          sphereAabb(
              (SphereCollider) cb, b,
              (AABBCollider) ca, a);
      return c != null ? c.inverted() : null;
    }

    // Capsule / Sphere
    if (ca.getType() == ColliderType.CAPSULE && cb.getType() == ColliderType.SPHERE) {
      return capsuleSphere(
          (CapsuleCollider) ca, a,
          (SphereCollider) cb, b);
    }

    if (ca.getType() == ColliderType.SPHERE && cb.getType() == ColliderType.CAPSULE) {
      Contact c =
          capsuleSphere(
              (CapsuleCollider) cb, b,
              (SphereCollider) ca, a);
      return c != null ? c.inverted() : null;
    }

    // Capsule / AABB (Character Controller!)
    if (ca.getType() == ColliderType.CAPSULE && cb.getType() == ColliderType.AABB) {
      return capsuleAabb(
          (CapsuleCollider) ca, a,
          (AABBCollider) cb, b);
    }

    if (ca.getType() == ColliderType.AABB && cb.getType() == ColliderType.CAPSULE) {
      Contact c =
          capsuleAabb(
              (CapsuleCollider) cb, b,
              (AABBCollider) ca, a);
      return c != null ? c.inverted() : null;
    }

    return null;
  }

  /* =======================
  Sphere / Sphere
  ======================= */

  private static Contact sphereSphere(
      SphereCollider a, ColliderComponent ca, SphereCollider b, ColliderComponent cb) {

    Vector3f pa = ca.getWorldPosition();
    Vector3f pb = cb.getWorldPosition();

    // Delta from b to a (points towards a)
    Vector3f delta = pa.subtract(pb);
    float distSq = delta.lengthSquared();
    float r = a.getRadius() + b.getRadius();

    if (distSq > r * r) return null;

    float dist = (float) Math.sqrt(distSq);
    Vector3f normal = dist > 0.00001f ? delta.divide(dist) : new Vector3f(1, 0, 0);

    return new Contact(normal, r - dist);
  }

  /* =======================
  AABB / AABB
  ======================= */

  private static Contact aabbAabb(
      AABBCollider a, ColliderComponent ca, AABBCollider b, ColliderComponent cb) {

    Vector3f pa = ca.getWorldPosition();
    Vector3f pb = cb.getWorldPosition();
    Vector3f ha = a.getHalfExtents();
    Vector3f hb = b.getHalfExtents();

    float dx = pb.x - pa.x;
    float px = (ha.x + hb.x) - Math.abs(dx);
    if (px <= 0) return null;

    float dy = pb.y - pa.y;
    float py = (ha.y + hb.y) - Math.abs(dy);
    if (py <= 0) return null;

    float dz = pb.z - pa.z;
    float pz = (ha.z + hb.z) - Math.abs(dz);
    if (pz <= 0) return null;

    if (px < py && px < pz) return new Contact(new Vector3f(Math.signum(dx), 0, 0), px);
    if (py < pz) return new Contact(new Vector3f(0, Math.signum(dy), 0), py);

    return new Contact(new Vector3f(0, 0, Math.signum(dz)), pz);
  }

  /* =======================
  Sphere / AABB
  ======================= */

  private static Contact sphereAabb(
      SphereCollider sphere, ColliderComponent cs, AABBCollider box, ColliderComponent cb) {

    Vector3f sc = cs.getWorldPosition();
    Vector3f bc = cb.getWorldPosition();
    Vector3f h = box.getHalfExtents();

    float x = clamp(sc.x, bc.x - h.x, bc.x + h.x);
    float y = clamp(sc.y, bc.y - h.y, bc.y + h.y);
    float z = clamp(sc.z, bc.z - h.z, bc.z + h.z);

    Vector3f closest = new Vector3f(x, y, z);
    Vector3f delta = closest.subtract(sc);
    float distSq = delta.lengthSquared();
    float r = sphere.getRadius();

    if (distSq > r * r) return null;

    float dist = (float) Math.sqrt(distSq);
    Vector3f normal = dist > 0.00001f ? delta.divide(dist) : new Vector3f(0, 1, 0);

    return new Contact(normal, r - dist);
  }

  /* =======================
  Capsule / Sphere
  ======================= */

  private static Contact capsuleSphere(
      CapsuleCollider cap, ColliderComponent cc, SphereCollider sph, ColliderComponent cs) {

    Vector3f capCenter = cc.getWorldPosition();
    Vector3f sphereCenter = cs.getWorldPosition();

    Vector3f a = new Vector3f();
    Vector3f b = new Vector3f();
    cap.getSegment(capCenter, a, b);

    Vector3f closestOnCap = closestPointOnSegment(a, b, sphereCenter);

    // IMPORTANT: delta must point from the obstacle (sphere) to the player (cap)
    Vector3f delta = closestOnCap.subtract(sphereCenter);

    float r = cap.getRadius() + sph.getRadius();
    float distSq = delta.lengthSquared();

    if (distSq > r * r) return null;

    float dist = (float) Math.sqrt(distSq);
    // Normal now points towards the player
    Vector3f normal = dist > 0.00001f ? delta.divide(dist) : new Vector3f(0, 1, 0);

    return new Contact(normal, r - dist);
  }

  /* =======================
  Capsule / AABB (PLAYER!)
  ======================= */

  private static Contact capsuleAabb(
      CapsuleCollider cap, ColliderComponent cc, AABBCollider box, ColliderComponent cb) {

    Vector3f p = cc.getWorldPosition();
    Vector3f bc = cb.getWorldPosition();
    Vector3f h = box.getHalfExtents();

    // 1. Check Y-overlap
    float capTop = p.y + cap.getHalfHeight();
    float capBottom = p.y - cap.getHalfHeight();
    float boxTop = bc.y + h.y;
    float boxBottom = bc.y - h.y;

    if (capTop < boxBottom || capBottom > boxTop) return null;

    // 2. Find closest point on AABB (XZ only)
    float cx = clamp(p.x, bc.x - h.x, bc.x + h.x);
    float cz = clamp(p.z, bc.z - h.z, bc.z + h.z);

    float dx = p.x - cx;
    float dz = p.z - cz;

    float distSq = dx * dx + dz * dz;
    float r = cap.getRadius();

    // If center point is outside the radius -> no collision
    if (distSq > r * r && distSq > 0.00001f) return null;

    float dist = (float) Math.sqrt(distSq);
    Vector3f normal;

    if (dist < 0.0001f) {
      // SPECIAL CASE: Player is exactly in the center of the pillar
      // Push them forward (or another fixed direction)
      normal = new Vector3f(1, 0, 0);
      return new Contact(normal, r);
    } else {
      // Calculate normal (points from obstacle away towards player)
      normal = new Vector3f(dx / dist, 0, dz / dist);
    }

    float penetration = r - dist;
    return new Contact(normal, penetration);
  }

  /* =======================
  Helpers
  ======================= */

  private static Vector3f closestPointOnSegment(Vector3f a, Vector3f b, Vector3f p) {
    Vector3f ab = b.subtract(a);
    float t = p.subtract(a).dot(ab) / ab.dot(ab);
    t = clamp(t, 0f, 1f);
    return a.add(ab.mult(t));
  }

  private static float clamp(float v, float min, float max) {
    return Math.max(min, Math.min(max, v));
  }
}
