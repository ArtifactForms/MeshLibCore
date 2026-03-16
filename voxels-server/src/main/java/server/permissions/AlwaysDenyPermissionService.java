package server.permissions;

import java.util.UUID;

public class AlwaysDenyPermissionService implements PermissionService {

  @Override
  public boolean hasPermission(UUID playerId, String permission) {
    return false;
  }
}
