package demos.collision;

import engine.components.AbstractComponent;
import math.Vector3f;

/**
 * Moves a platform back and forth along a local axis. Useful for testing player adhesion and sweep
 * interactions with moving objects.
 */
public class MovingPlatformComponent extends AbstractComponent {

  private Vector3f startPos;
  private float distance = 10f;
  private float speed = 2f;
  private float time = 0;

  @Override
  public void onUpdate(float dt) {
    if (startPos == null) {
      startPos = new Vector3f(getOwner().getTransform().getPosition());
    }

    time += dt * speed;
    // Simple ping-pong movement using sine
    float offset = (float) Math.sin(time) * distance;

    Vector3f newPos = startPos.add(new Vector3f(offset, 0, 0));
    getOwner().getTransform().setPosition(newPos);
  }
}
