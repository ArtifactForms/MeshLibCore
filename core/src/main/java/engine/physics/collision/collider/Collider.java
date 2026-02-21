package engine.physics.collision.collider;

import math.Vector3f;

public interface Collider {

  Vector3f getLocalOffset();

  void setLocalOffset(Vector3f offset);

  ColliderType getType();
}
