package engine.physics.ray;

import engine.scene.SceneNode;
import engine.scene.SceneNodeVisitor;
import math.Ray3f;

public final class RaycastQuery implements SceneNodeVisitor {

  private final Ray3f ray;
  private RaycastHit closestHit;

  public RaycastQuery(Ray3f ray) {
    this.ray = ray;
  }

  @Override
  public void visit(SceneNode node) {
    for (RaycastComponent rc : node.getComponents(RaycastComponent.class)) {
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
