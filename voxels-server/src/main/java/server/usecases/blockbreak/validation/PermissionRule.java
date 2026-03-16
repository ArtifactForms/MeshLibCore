package server.usecases.blockbreak.validation;

import server.gateways.PermissionGateway;
import server.permissions.Permissions;
import server.usecases.blockbreak.BlockBreak.BlockBreakRequest;
import server.usecases.blockbreak.BlockBreak.BlockBreakResponse;

public class PermissionRule implements BlockBreakRule {
  private final PermissionGateway permissions;

  public PermissionRule(PermissionGateway permissions) {
    this.permissions = permissions;
  }

  @Override
  public boolean validate(BlockBreakRequest request, BlockBreakResponse response, short id) {
    if (!permissions.hasPermission(request.getPlayer(), Permissions.BLOCK_BREAK)) {
      response.onNoPermission(request.getX(), request.getY(), request.getZ(), id);
      return false;
    }
    return true;
  }
}
