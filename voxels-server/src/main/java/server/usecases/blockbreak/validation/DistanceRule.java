package server.usecases.blockbreak.validation;

import server.usecases.blockbreak.BlockBreak.BlockBreakRequest;
import server.usecases.blockbreak.BlockBreak.BlockBreakResponse;

public class DistanceRule implements BlockBreakRule {

  private final double maxDistanceSquared;

  public DistanceRule(double maxDist) {
    this.maxDistanceSquared = maxDist * maxDist;
  }

  @Override
  public boolean validate(BlockBreakRequest request, BlockBreakResponse response, short id) {
    double dx = request.getPlayerX() - request.getX();
    double dy = request.getPlayerY() - request.getY();
    double dz = request.getPlayerZ() - request.getZ();

    double distSq = dx * dx + dy * dy + dz * dz;

    if (distSq > maxDistanceSquared) {
      response.onTooFarAway(request.getX(), request.getY(), request.getZ(), id);
      return false;
    }
    return true;
  }
}
