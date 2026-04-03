package server.permissions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SimplePermissionService implements PermissionService {

  private final Map<UUID, Set<String>> permissions = new HashMap<>();

  private final Set<UUID> operators = new HashSet<>();

  @Override
  public boolean hasPermission(UUID playerId, String permission) {

    if (operators.contains(playerId)) {
      return true;
    }

    Set<String> perms = permissions.get(playerId);

    if (perms == null) {
      return false;
    }

    return perms.contains(permission);
  }

  @Override
  public boolean isOp(UUID playerId) {
    return operators.contains(playerId);
  }

  public void setOp(UUID playerId, boolean value) {
    if (value) {
      operators.add(playerId);
    } else {
      operators.remove(playerId);
    }
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
