package engine.physics;

import engine.collision.component.ColliderComponent;
import math.Vector3f;

public class PhysicsQueryEvent {

  private ColliderComponent source;
  private Vector3f delta;
  private SweepResult result;

  public PhysicsQueryEvent(ColliderComponent source, Vector3f delta, SweepResult result) {
    this.source = source;
    this.delta = delta;
    this.result = result;
  }

  public ColliderComponent getSource() {
    return source;
  }

  public Vector3f getDelta() {
    return delta;
  }

  public SweepResult getResult() {
    return result;
  }
}
