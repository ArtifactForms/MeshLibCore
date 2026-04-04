package server.usecases.inventoryclear;

import java.util.UUID;

import server.gateways.MessageGateway;
import server.usecases.inventoryclear.InventoryClear.InventoryClearResponse;

public class InventoryClearPresenter implements InventoryClearResponse {

  private final MessageGateway messages;

  public InventoryClearPresenter(MessageGateway messages) {
    this.messages = messages;
  }

  @Override
  public void onInventoryCleared(UUID playerId, String playerName) {
    messages.sendMessage(playerId, InventoryClearMessages.INVENTORY_CLEARED);
  }
}
