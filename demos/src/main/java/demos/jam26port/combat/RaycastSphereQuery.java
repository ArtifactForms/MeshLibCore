package demos.jam26port.combat;

import demos.jam26port.world.SphereCollider;
import demos.ray.RaycastHit;
import engine.scene.SceneNode;
import engine.scene.SceneNodeVisitor;
import math.Ray3f;

public final class RaycastSphereQuery implements SceneNodeVisitor {

  private final Ray3f ray;
  private RaycastHit closestHit;

  public RaycastSphereQuery(Ray3f ray) {
    this.ray = ray;
  }

  @Override
  public void visit(SceneNode node) {
    for (SphereCollider rc : node.getComponents(SphereCollider.class)) {
      RaycastHit hit = rc.raycast(ray);
      if (hit != null) {
        if (closestHit == null || hit.getDistance() < closestHit.getDistance()) {
          closestHit = hit;
        }
      }
    }
  }

  public RaycastHit getResult() {
    return closestHit;
  }
}
