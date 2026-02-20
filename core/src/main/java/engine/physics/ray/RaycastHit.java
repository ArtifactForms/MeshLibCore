package engine.physics.ray;

import engine.scene.SceneNode;
import math.Vector3f;

public class RaycastHit {

  private final SceneNode target;
  private final Vector3f point;
  private final float distance;

  public RaycastHit(SceneNode target, Vector3f point, float distance) {
    this.target = target;
    this.point = point;
    this.distance = distance;
  }

  public SceneNode getTarget() {
    return target;
  }

  public Vector3f getPoint() {
    return point;
  }

  public float getDistance() {
    return distance;
  }
}
