package demos.jam26port.world;

import demos.jam26port.game.world.WorldContext;
import engine.components.AbstractComponent;
import math.Vector3f;

public class ExitTriggerComponent extends AbstractComponent {

  private float triggerRadius;
  private boolean triggered;
  private WorldContext world;

  public ExitTriggerComponent(WorldContext world) {
    this.triggerRadius = 64;
    this.triggered = false;
    this.world = world;
  }

  @Override
  public void onUpdate(float tpf) {
    if (triggered) return;

    Vector3f exitPos = getOwner().getTransform().getPosition();
    Vector3f playerPos = world.getPlayer().getPosition();

    float distSq = exitPos.distanceSquared(playerPos);
    if (distSq <= triggerRadius * triggerRadius) {
      triggered = true;
      onExit();
    }
  }

  private void onExit() {
    world.requestLevelExit();
  }
}
