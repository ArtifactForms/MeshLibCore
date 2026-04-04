package server.usecases.inventoryclear;

import java.util.UUID;

public interface InventoryClear {

  void execute(InventoryClearRequest request, InventoryClearResponse response);

  public interface InventoryClearRequest {

    UUID getPlayerId();
  }

  public interface InventoryClearResponse {

    void onInventoryCleared(UUID playerId, String playerName);
  }
}
