package demos.jam26port.world;

import demos.jam26port.combat.RaySphereIntersection;
import engine.components.AbstractComponent;
import engine.physics.ray.RaycastHit;
import engine.scene.SceneNode;
import math.Ray3f;
import math.Vector3f;

public class SphereCollider extends AbstractComponent implements Collider {

  private final float radius;

  public SphereCollider(float radius) {
    this.radius = radius;
  }

  @Override
  public RaycastHit raycast(Ray3f ray) {

    float distance = RaySphereIntersection.intersect(ray, this);

    if (distance < 0) {
      return null;
    }

    SceneNode target = getOwner();
    Vector3f point = new Vector3f(ray.getDirection()).multLocal(distance).addLocal(ray.getOrigin());

    return new RaycastHit(target, point, distance);
  }

  public float getRadius() {
    return radius;
  }

  public Vector3f getCenter() {
    return getOwner().getTransform().getPosition();
  }
}
