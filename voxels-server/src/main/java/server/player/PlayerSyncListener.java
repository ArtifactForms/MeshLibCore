package server.player;

import java.util.UUID;

import common.game.Inventory;
import common.game.ItemStack;
import common.network.packets.GameModeUpdatePacket;
import common.network.packets.PlayerInventoryFullUpdatePacket;
import server.events.events.PostPlayerInventoryClearEvent;
import server.events.events.PostGameModeChangeEvent;
import server.gateways.EventGateway;
import server.network.PlayerManager;

public class PlayerSyncListener {

  private final PlayerManager playerManager;

  public PlayerSyncListener(PlayerManager playerManager, EventGateway events) {
    this.playerManager = playerManager;
    events.register(PostPlayerInventoryClearEvent.class, e -> onInventoryClearedEvent(e));
    events.register(PostGameModeChangeEvent.class, e -> onPostGameModeChangeEvent(e));
  }

  private void onInventoryClearedEvent(PostPlayerInventoryClearEvent e) {
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

  private void onPostGameModeChangeEvent(PostGameModeChangeEvent e) {
    playerManager.send(e.getPlayerId(), new GameModeUpdatePacket(e.getGameMode()));
  }
}
