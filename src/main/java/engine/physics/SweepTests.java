package engine.physics;

import engine.collision.collider.AABBCollider;
import engine.collision.collider.CapsuleCollider;
import engine.collision.collider.ColliderType;
import engine.collision.component.ColliderComponent;
import math.Vector3f;

public final class SweepTests {

  private static final float EPSILON = 0.000001f;

  public static SweepResult sweepCapsuleVsCollider(
      ColliderComponent capsuleCol, ColliderComponent other, Vector3f delta) {

    if (capsuleCol.getCollider().getType() != ColliderType.CAPSULE) return SweepResult.noHit();

    if (other.getCollider().getType() != ColliderType.AABB) return SweepResult.noHit();

    CapsuleCollider capsule = (CapsuleCollider) capsuleCol.getCollider();
    AABBCollider box = (AABBCollider) other.getCollider();

    return sweepCapsuleVsAABB(
        capsuleCol.getWorldPosition(), capsule, other, other.getWorldPosition(), box, delta);
  }

  public static SweepResult sweepCapsuleVsColliderAtPosition(
      ColliderComponent capsuleCol,
      Vector3f capsuleStartPos,
      ColliderComponent other,
      Vector3f delta) {

    if (capsuleCol.getCollider().getType() != ColliderType.CAPSULE) return SweepResult.noHit();

    if (other.getCollider().getType() != ColliderType.AABB) return SweepResult.noHit();

    CapsuleCollider capsule = (CapsuleCollider) capsuleCol.getCollider();

    AABBCollider box = (AABBCollider) other.getCollider();

    // ⚠ WICHTIG:
    // Hier benutzen wir NICHT capsuleCol.getWorldPosition()
    // sondern die explizite Startposition!

    return sweepCapsuleVsAABB(
        capsuleStartPos, capsule, other, other.getWorldPosition(), box, delta);
  }
  /* ========================================================= */

  private static SweepResult sweepCapsuleVsAABB(
      Vector3f capsuleCenter,
      CapsuleCollider capsule,
      ColliderComponent otherComponent,
      Vector3f boxCenter,
      AABBCollider box,
      Vector3f delta) {

    float radius = capsule.getRadius();
    float halfHeight = capsule.getHalfHeight();

    Vector3f bottomCenter = capsuleCenter.add(new Vector3f(0, -halfHeight, 0));
    Vector3f topCenter = capsuleCenter.add(new Vector3f(0, halfHeight, 0));

    SweepResult bottomHit =
        sweepSphereVsAABB(bottomCenter, radius, otherComponent, boxCenter, box, delta);

    SweepResult topHit =
        sweepSphereVsAABB(topCenter, radius, otherComponent, boxCenter, box, delta);

    SweepResult cylinderHit =
        sweepCylinderVsAABB(capsuleCenter, capsule, otherComponent, boxCenter, box, delta);

    SweepResult best = SweepResult.noHit();
    best = chooseBetter(best, bottomHit);
    best = chooseBetter(best, topHit);
    best = chooseBetter(best, cylinderHit);

    return best;
  }

  /* ========================================================= */

  private static SweepResult chooseBetter(SweepResult a, SweepResult b) {

    if (!b.hasHit()) return a;
    if (!a.hasHit()) return b;

    return b.getTOI() < a.getTOI() ? b : a;
  }

  /* ========================================================= */
  /* Sphere Sweep */

  private static SweepResult sweepSphereVsAABB(
      Vector3f sphereCenter,
      float radius,
      ColliderComponent otherComponent,
      Vector3f boxCenter,
      AABBCollider box,
      Vector3f delta) {

    Vector3f halfExt = box.getHalfExtents();

    Vector3f expandedHalf =
        new Vector3f(halfExt.x + radius, halfExt.y + radius, halfExt.z + radius);

    Vector3f min = boxCenter.subtract(expandedHalf);
    Vector3f max = boxCenter.add(expandedHalf);

    Vector3f origin = new Vector3f(sphereCenter);

    float tMin = 0f;
    float tMax = 1f;

    for (int i = 0; i < 3; i++) {

      float o = origin.get(i);
      float d = delta.get(i);
      float minA = min.get(i);
      float maxA = max.get(i);

      if (Math.abs(d) < EPSILON) {

        if (o < minA || o > maxA) return SweepResult.noHit();

      } else {

        float invD = 1f / d;

        float t1 = (minA - o) * invD;
        float t2 = (maxA - o) * invD;

        if (t1 > t2) {
          float tmp = t1;
          t1 = t2;
          t2 = tmp;
        }

        tMin = Math.max(tMin, t1);
        tMax = Math.min(tMax, t2);

        if (tMin > tMax) return SweepResult.noHit();
      }
    }

    if (tMin < 0f || tMin > 1f) return SweepResult.noHit();

    Vector3f hitPoint = origin.add(delta.mult(tMin));
    Vector3f normal = computeNormal(hitPoint, boxCenter, halfExt);

    return new SweepResult(true, tMin, hitPoint, normal, otherComponent);
  }

  /* ========================================================= */
  /* Cylinder Sweep (Capsule Body) */

  private static SweepResult sweepCylinderVsAABB(
      Vector3f capsuleCenter,
      CapsuleCollider capsule,
      ColliderComponent otherComponent,
      Vector3f boxCenter,
      AABBCollider box,
      Vector3f delta) {

    float radius = capsule.getRadius();
    float halfHeight = capsule.getHalfHeight();
    Vector3f halfExt = box.getHalfExtents();

    float capsuleMinY = Math.min(
            capsuleCenter.y - halfHeight,
            capsuleCenter.y + delta.y - halfHeight);

    float capsuleMaxY = Math.max(
            capsuleCenter.y + halfHeight,
            capsuleCenter.y + delta.y + halfHeight);

    float boxMinY = boxCenter.y - halfExt.y;
    float boxMaxY = boxCenter.y + halfExt.y;

    if (capsuleMaxY < boxMinY || capsuleMinY > boxMaxY)
        return SweepResult.noHit();

    Vector3f expandedHalf = new Vector3f(halfExt.x + radius, halfExt.y, halfExt.z + radius);

    Vector3f min = boxCenter.subtract(expandedHalf);
    Vector3f max = boxCenter.add(expandedHalf);

    Vector3f origin = capsuleCenter;

    float tMin = 0f;
    float tMax = 1f;

    int[] axes = {0, 2};

    for (int axis : axes) {

      float o = origin.get(axis);
      float d = delta.get(axis);
      float minA = min.get(axis);
      float maxA = max.get(axis);

      if (Math.abs(d) < EPSILON) {

        if (o < minA || o > maxA) return SweepResult.noHit();

      } else {

        float invD = 1f / d;

        float t1 = (minA - o) * invD;
        float t2 = (maxA - o) * invD;

        if (t1 > t2) {
          float tmp = t1;
          t1 = t2;
          t2 = tmp;
        }

        tMin = Math.max(tMin, t1);
        tMax = Math.min(tMax, t2);

        if (tMin > tMax) return SweepResult.noHit();
      }
    }

    if (tMin < 0f || tMin > 1f) return SweepResult.noHit();

    Vector3f hitPoint = origin.add(delta.mult(tMin));
    Vector3f normal = computeCylinderNormal(hitPoint, boxCenter, halfExt);

    return new SweepResult(true, tMin, hitPoint, normal, otherComponent);
  }

  /* ========================================================= */
  /* Normals */

  private static Vector3f computeCylinderNormal(Vector3f point, Vector3f center, Vector3f halfExt) {

    Vector3f min = center.subtract(halfExt);
    Vector3f max = center.add(halfExt);

    float bias = 0.001f;

    if (Math.abs(point.x - min.x) < bias) return new Vector3f(-1, 0, 0);
    if (Math.abs(point.x - max.x) < bias) return new Vector3f(1, 0, 0);
    if (Math.abs(point.z - min.z) < bias) return new Vector3f(0, 0, -1);
    if (Math.abs(point.z - max.z) < bias) return new Vector3f(0, 0, 1);

    return new Vector3f(0, 0, 0);
  }

  private static Vector3f computeNormal(Vector3f point, Vector3f center, Vector3f halfExt) {

    Vector3f min = center.subtract(halfExt);
    Vector3f max = center.add(halfExt);

    float bias = 0.001f;

    if (Math.abs(point.x - min.x) < bias) return new Vector3f(-1, 0, 0);
    if (Math.abs(point.x - max.x) < bias) return new Vector3f(1, 0, 0);
    if (Math.abs(point.y - min.y) < bias) return new Vector3f(0, -1, 0);
    if (Math.abs(point.y - max.y) < bias) return new Vector3f(0, 1, 0);
    if (Math.abs(point.z - min.z) < bias) return new Vector3f(0, 0, -1);
    if (Math.abs(point.z - max.z) < bias) return new Vector3f(0, 0, 1);

    return new Vector3f(0, -1, 0);
  }
}
