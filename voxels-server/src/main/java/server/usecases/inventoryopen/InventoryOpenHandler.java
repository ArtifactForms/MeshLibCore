package server.usecases.inventoryopen;

import java.util.UUID;

import common.network.packets.PlayerOpenInventoryPacket;
import server.events.events.PlayerInventoryOpenEvent;
import server.gateways.EventGateway;
import server.gateways.GatewayContext;
import server.network.ServerConnection;
import server.player.ServerPlayer;

public class InventoryOpenHandler {

  private ServerConnection connection;

  private EventGateway events;

  public InventoryOpenHandler(ServerConnection connection, GatewayContext context) {
    this.connection = connection;
    this.events = context.events();
  }

  public void handle(PlayerOpenInventoryPacket packet) {
    ServerPlayer player = connection.getPlayer();

    if (player == null) {
      return;
    }

    UUID playerId = player.getUuid();
    // -------------------------------------
    // EVENT
    // -------------------------------------
    PlayerInventoryOpenEvent event = new PlayerInventoryOpenEvent(playerId);
    events.fire(event);

    if (event.isCancelled()) {
      return;
    }
  }
}
