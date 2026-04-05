package server.network.validation;

import math.Vector3f;
import server.player.ServerPlayer;

public class DistanceValidator implements BlockActionValidator {

  private final float maxReach;

  public DistanceValidator(float maxReach) {
    this.maxReach = maxReach;
  }

  @Override
  public boolean isValid(ServerPlayer player, int x, int y, int z) {
    // Distance from eyes to block center
    Vector3f eyePos = player.getPosition().add(0, 1.6f, 0);
    Vector3f blockCenter = new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f);

    return eyePos.distance(blockCenter) <= maxReach;
  }
}
