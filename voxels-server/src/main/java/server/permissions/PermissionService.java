package server.permissions;

import java.util.UUID;

public interface PermissionService {

  boolean hasPermission(UUID playerId, String permission);
}