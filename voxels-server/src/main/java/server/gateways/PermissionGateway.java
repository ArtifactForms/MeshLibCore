package server.gateways;

import java.util.UUID;

public interface PermissionGateway {

  boolean hasPermission(UUID playerId, String permission);

  boolean isOp(UUID playerId);

  void setOp(UUID playerId, boolean value);
}
