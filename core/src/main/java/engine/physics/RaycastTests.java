package engine.physics;

import engine.physics.collision.collider.AABBCollider;
import engine.physics.collision.collider.ColliderType;
import engine.physics.collision.component.ColliderComponent;
import math.Bounds;
import math.Ray3f;
import math.Vector3f;

public class RaycastTests {

  public static Float raycast(Ray3f ray, ColliderComponent c) {
    if (c.getCollider().getType() == ColliderType.AABB) {
      AABBCollider aabb = (AABBCollider) c.getCollider();
      Vector3f center = c.getWorldPosition();
      Vector3f size = aabb.getHalfExtents().mult(2);
      Bounds bounds = Bounds.fromCenterSize(center, size);
      Float distance = bounds.intersectRayDistance(ray);
      return distance;
    }
    return null;
  }
}
