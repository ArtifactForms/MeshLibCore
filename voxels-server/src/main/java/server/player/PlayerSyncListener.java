package server.player;

import java.util.UUID;

import common.game.Inventory;
import common.game.ItemStack;
import common.network.packets.PlayerInventoryFullUpdatePacket;
import server.events.events.PlayerInventoryClearedEvent;
import server.gateways.EventGateway;
import server.network.PlayerManager;

public class PlayerSyncListener {

  private final PlayerManager playerManager;

  public PlayerSyncListener(PlayerManager playerManager, EventGateway events) {
    this.playerManager = playerManager;
    events.register(PlayerInventoryClearedEvent.class, e -> onInventoryCleared(e));
  }

  public void onInventoryCleared(PlayerInventoryClearedEvent e) {
    UUID playerId = e.getPlayerId();
    ServerPlayer player = playerManager.getPlayer(playerId);

    if (player == null) {
      return;
    }

    Inventory inventory = player.getInventory();
    ItemStack cursorStack = null;
    playerManager.send(
        e.getPlayerId(),
        new PlayerInventoryFullUpdatePacket(
            inventory.getItems(), cursorStack, player.getInventoryVersion()));
  }
}
