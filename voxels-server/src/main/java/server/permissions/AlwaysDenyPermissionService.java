package server.permissions;

import java.util.UUID;

public class AlwaysDenyPermissionService implements PermissionService {

  @Override
  public boolean hasPermission(UUID playerId, String permission) {
    return false;
  }

  @Override
  public boolean isOp(UUID playerId) {
    return false;
  }

  @Override
  public void setOp(UUID playerId, boolean value) {
    // Do nothing
  }
}
