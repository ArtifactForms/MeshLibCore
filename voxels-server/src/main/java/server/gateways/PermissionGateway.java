package server.gateways;

import java.util.UUID;

public interface PermissionGateway {
  boolean hasPermission(UUID playerId, String permission);
}
