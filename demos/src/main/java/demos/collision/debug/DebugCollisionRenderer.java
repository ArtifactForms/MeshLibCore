package demos.collision;

import engine.runtime.debug.DebugColliderComponent;
import engine.scene.SceneNode;
import math.Color;

public class DebugCollisionRenderer extends SceneNode {

  public DebugCollisionRenderer() {
    setName("Debug-Collision");
    addComponent(new DebugColliderComponent(Color.YELLOW));
    addComponent(new DebugCapsuleSweepComponent());
  }
}
