package engine.physics;

import engine.physics.collision.component.ColliderComponent;

public class RaycastResult {

  private final boolean hit;
  private final Float distance;
  private final ColliderComponent collider;

  public RaycastResult(boolean hit, ColliderComponent collider, Float distance) {
    this.hit = hit;
    this.collider = collider;
    this.distance = distance;
  }

  public boolean hasHit() {
    return hit;
  }

  public Float distance() {
    return distance;
  }
  
  public ColliderComponent getCollider() {
	  return collider;
  }
}
