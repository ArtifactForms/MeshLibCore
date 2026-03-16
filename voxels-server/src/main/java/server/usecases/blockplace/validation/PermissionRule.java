package server.usecases.blockplace.validation;

import server.gateways.PermissionGateway;
import server.permissions.Permissions;
import server.usecases.blockplace.BlockPlace.BlockPlaceRequest;
import server.usecases.blockplace.BlockPlace.BlockPlaceResponse;

public class PermissionRule implements BlockPlaceRule {

  private PermissionGateway permissions;

  public PermissionRule(PermissionGateway permissions) {
    this.permissions = permissions;
  }

  @Override
  public boolean validate(BlockPlaceRequest request, BlockPlaceResponse response, short id) {
    if (!permissions.hasPermission(request.getPlayer(), Permissions.BLOCK_BREAK)) {
      response.onNoPermission(request.getX(), request.getY(), request.getZ(), id);
      return false;
    }
    return true;
  }
}
