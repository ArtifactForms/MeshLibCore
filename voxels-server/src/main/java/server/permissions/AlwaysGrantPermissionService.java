package server.permissions;

import java.util.UUID;

public class AlwaysGrantPermissionService implements PermissionService {

  @Override
  public boolean hasPermission(UUID playerId, String permission) {
    return true;
  }

  @Override
  public boolean isOp(UUID playerId) {
    return true;
  }

  @Override
  public void setOp(UUID playerId, boolean value) {
    // Do nothing
  }
}
