package server.permissions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SimplePermissionService implements PermissionService {

  private final Map<UUID, Set<String>> permissions = new HashMap<>();

  @Override
  public boolean hasPermission(UUID playerId, String permission) {

    Set<String> perms = permissions.get(playerId);

    if (perms == null) {
      return false;
    }

    return perms.contains(permission);
  }

  public void grant(UUID playerId, String permission) {
    permissions.computeIfAbsent(playerId, id -> new HashSet<>()).add(permission);
  }

  public void revoke(UUID playerId, String permission) {

    Set<String> perms = permissions.get(playerId);

    if (perms != null) {
      perms.remove(permission);
    }
  }
}
