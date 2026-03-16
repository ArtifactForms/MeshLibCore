package server.network.handlers;

import common.network.packets.InventoryClickPacket;
import common.network.packets.PlayerInventoryFullUpdatePacket;
import server.network.ServerConnection;
import server.player.ServerPlayer;

public class InventoryClickHandler {

  private final ServerConnection connection;

  public InventoryClickHandler(ServerConnection connection) {
    this.connection = connection;
  }

  public void handle(InventoryClickPacket packet) {

    ServerPlayer player = connection.getPlayer();
    if (player == null) return;

    int slot = packet.getSlot();

    var inventory = player.getInventory();
    var stack = inventory.getSlot(slot);

    if (stack == null) return;

    // Beispiel: 1 Item droppen
    inventory.removeOne(slot);

    // Sync zurück an Client
//    connection.send(new PlayerInventoryFullUpdatePacket(inventory.getItems()));
  }
}
