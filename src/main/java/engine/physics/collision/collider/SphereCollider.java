package engine.physics.collision.collider;

public class SphereCollider extends AbstractCollider {

  private float radius;

  public SphereCollider(float radius) {
    this.radius = radius;
  }

  public float getRadius() {
    return radius;
  }

  @Override
  public ColliderType getType() {
    return ColliderType.SPHERE;
  }
}
