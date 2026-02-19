package engine.components;

import math.Vector3f;

public interface MovementInputConsumer {

  void addMovementInput(Vector3f direction);

  void jump();
}
