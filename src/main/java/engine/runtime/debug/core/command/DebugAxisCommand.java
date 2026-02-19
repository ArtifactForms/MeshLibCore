package engine.runtime.debug.core.command;

import engine.components.Transform;

public class DebugAxisCommand extends DebugCommand {

  public float size;
  public Transform transform;

  public DebugAxisCommand(float size, Transform transform) {
    this.size = size;
    this.transform = transform;
  }
}
