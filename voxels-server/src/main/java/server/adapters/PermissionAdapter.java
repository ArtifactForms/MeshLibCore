package server.adapters;

import java.util.UUID;

import server.gateways.PermissionGateway;
import server.permissions.PermissionService;

public class PermissionAdapter implements PermissionGateway {

  private final PermissionService service;

  public PermissionAdapter(PermissionService service) {
    this.service = service;
  }

  @Override
  public boolean hasPermission(UUID playerId, String permission) {
    return service.hasPermission(playerId, permission);
  }

  @Override
  public boolean isOp(UUID playerId) {
    return service.isOp(playerId);
  }

  @Override
  public void setOp(UUID playerId, boolean value) {
    service.setOp(playerId, value);
  }
}
