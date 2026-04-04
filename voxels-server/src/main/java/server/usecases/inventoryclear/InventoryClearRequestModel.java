package server.usecases.inventoryclear;

import java.util.UUID;

import server.usecases.inventoryclear.InventoryClear.InventoryClearRequest;

public class InventoryClearRequestModel implements InventoryClearRequest {

  private final UUID playerId;

  public InventoryClearRequestModel(UUID playerId) {
    this.playerId = playerId;
  }

  @Override
  public UUID getPlayerId() {
    return playerId;
  }
}
