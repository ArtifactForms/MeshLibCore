package server.permissions;

import java.util.UUID;

public class AlwaysGrantPermissionService implements PermissionService {

  @Override
  public boolean hasPermission(UUID playerId, String permission) {
    return true;
  }
}
