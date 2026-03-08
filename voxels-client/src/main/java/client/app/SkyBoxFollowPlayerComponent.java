package client.app;

import engine.components.AbstractComponent;
import engine.scene.SceneNode;
import math.Vector3f;

public class SkyBoxFollowPlayerComponent extends AbstractComponent {

  private SceneNode player;

  public SkyBoxFollowPlayerComponent(SceneNode player) {
    this.player = player;
  }

  @Override
  public void onUpdate(float tpf) {
    Vector3f playerPos = player.getWorldPosition();
    getOwner().getTransform().setPosition(playerPos.x, 0, playerPos.z);
  }
}
