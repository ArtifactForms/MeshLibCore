package server.usecases.inventoryclear;

import java.util.UUID;

import server.events.events.PlayerInventoryClearEvent;
import server.gateways.EventGateway;
import server.gateways.GatewayContext;
import server.gateways.InventoryGateway;
import server.gateways.PlayerGateway;

public class InventoryClearUseCase implements InventoryClear {

  private final InventoryGateway inventories;

  private final PlayerGateway players;

  private final EventGateway events;

  public InventoryClearUseCase(GatewayContext context) {
    this.inventories = context.inventory();
    this.players = context.players();
    this.events = context.events();
  }

  @Override
  public void execute(InventoryClearRequest request, InventoryClearResponse response) {
    UUID playerId = request.getPlayerId();
    String playerName = players.getName(playerId);

    inventories.clear(playerId);
    inventories.incrementInventoryVersion(playerId);

    PlayerInventoryClearEvent event = new PlayerInventoryClearEvent(playerId);
    events.fire(event);

    response.onInventoryCleared(playerId, playerName);
  }
}
