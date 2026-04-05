package client.ray;

import common.interaction.InteractionTarget;

public class RaycastResult {

  public final boolean hit;

  public final InteractionTarget target;

  public RaycastResult(boolean hit, InteractionTarget target) {
    this.hit = hit;
    this.target = target;
  }

  public static RaycastResult miss() {
    return new RaycastResult(false, null);
  }

  public boolean isMiss() {
    return !hit;
  }

  @Override
  public String toString() {
    return "RaycastResult{hit=" + hit + ", target=" + target + "}";
  }
}
