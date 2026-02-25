package demos.collision.debug;

import engine.components.ToggleActiveOnKey;
import engine.runtime.debug.DebugColliderComponent;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.scene.SceneNode;
import math.Color;

public class DebugCollisionRenderer extends SceneNode {

  public DebugCollisionRenderer(Input input) {
    setName("Debug-Collision");

    SceneNode debug = new SceneNode();

    debug.addComponent(new DebugColliderComponent(Color.YELLOW));
    debug.addComponent(new DebugCapsuleSweepComponent());
    debug.setActive(false);

    addChild(debug);

    addComponent(new ToggleActiveOnKey(input, debug, Key.NUM_3));
  }
}
