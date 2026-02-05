package engine.collision.collider;

import math.Vector3f;

public abstract class AbstractCollider implements Collider {

  protected Vector3f localOffset = new Vector3f();

  public Vector3f getLocalOffset() {
    return localOffset;
  }

  public void setLocalOffset(Vector3f offset) {
    this.localOffset = offset;
  }
}
