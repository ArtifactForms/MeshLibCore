package client.usecases.interact;

import client.app.GameClient;
import client.ray.RaycastResult;
import client.ray.Raycasting;
import common.world.World;
import engine.runtime.input.Input;
import engine.scene.SceneNode;
import engine.scene.camera.Camera;

public class TargetingService {

  private final Input input;

  private final GameClient client;

  public TargetingService(Input input, GameClient client) {
    this.input = input;
    this.client = client;
  }

  public RaycastResult raycast(SceneNode node) {

    World world = client.getWorld();
    Camera camera = node.getScene().getActiveCamera();

    switch (client.getRaycastMode()) {
      case CROSS_HAIR:
        return Raycasting.raycastCrossHair(world, camera, 6);

      case CURSOR:
        return Raycasting.raycastScreenPoint(world, camera, input, 1000);

      default:
        return RaycastResult.miss();
    }
  }
}
