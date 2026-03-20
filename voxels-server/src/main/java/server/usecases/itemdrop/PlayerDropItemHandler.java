package server.usecases.itemdrop;

import java.util.UUID;

import common.entity.ItemEntity;
import common.game.Hotbar;
import common.game.ItemStack;
import common.game.block.BlockRegistry;
import common.game.block.BlockType;
import common.logging.Log;
import common.network.packets.ChatMessagePacket;
import common.network.packets.ItemSpawnPacket;
import common.network.packets.PlayerDropItemPacket;
import common.network.packets.PlayerSlotClearPacket;
import common.network.packets.PlayerSlotUpdatePacket;
import server.events.events.PlayerDropItemEvent;
import server.gateways.EventGateway;
import server.gateways.GatewayContext;
import server.gateways.InventoryGateway;
import server.gateways.PermissionGateway;
import server.network.GameServer;
import server.network.ServerConnection;
import server.permissions.Permissions;
import server.player.ServerPlayer;

public class PlayerDropItemHandler {

  private final ServerConnection connection;

  private InventoryGateway inventory;
  private EventGateway events;
  private PermissionGateway permissions;

  public PlayerDropItemHandler(ServerConnection connection, GatewayContext context) {
    this.connection = connection;
    this.inventory = context.inventory();
    this.events = context.events();
    this.permissions = context.permissions();
  }

  private boolean hasPermission(UUID uuid, String permission) {
    return permissions.hasPermission(uuid, permission);
  }

  private void resync(PlayerDropItemPacket packet) {
    int slotIndex = packet.getSelectedSlot();
    if (slotIndex < 0 || slotIndex >= Hotbar.SIZE) {
      Log.warn("item-drop.resync.invalid-slot | slot=" + slotIndex);
      return;
    }
    UUID playerId = connection.getPlayer().getUuid();
    ItemStack itemStack = inventory.getItem(playerId, slotIndex);
    if (itemStack == null) {
      connection.send(new PlayerSlotClearPacket(slotIndex));
    } else {
      connection.send(
          new PlayerSlotUpdatePacket(
              packet.getSelectedSlot(), itemStack.getItemId(), itemStack.getAmount()));
    }
  }

  private void failAndResync(PlayerDropItemPacket packet) {
    resync(packet);
  }

  public void handle(PlayerDropItemPacket packet) {
    UUID playerId = connection.getPlayer().getUuid();

    // -------------------------------------
    // SLOT VALIDATION
    // -------------------------------------
    int selectedSlot = packet.getSelectedSlot();
    if (selectedSlot < 0 || selectedSlot > inventory.getMaxSlotIndex(playerId)) {
      Log.warn("item-drop.invalid-slot | playerId=" + playerId + " slot=" + selectedSlot);
      return;
    }

    // -------------------------------------
    // PERMISSION CHECK
    // -------------------------------------
    if (!hasPermission(playerId, Permissions.ITEM_DROP)) {
      connection.send(new ChatMessagePacket(ItemDropMessages.NO_PERMISSION));
      failAndResync(packet);
      return;
    }

    // -------------------------------------
    // AMOUNT CHECK
    // -------------------------------------
    ItemStack itemStack = inventory.getItem(playerId, selectedSlot);
    if (itemStack == null) {
    	return;
    }
    
    if (itemStack.getAmount() <= 0) {
      failAndResync(packet);
      return;
    }

    // -------------------------------------
    // EVENT
    // -------------------------------------
    PlayerDropItemEvent event = new PlayerDropItemEvent(playerId, itemStack.getItemId(), 1);
    events.fire(event);

    if (event.isCancelled()) {
      Log.debug("drop-item.cancelled | playerId=" + playerId + " slot=" + selectedSlot);
      failAndResync(packet);
      return;
    }

    // -------------------------------------
    // RESPONSE
    // -------------------------------------
    inventory.consumeItem(playerId, selectedSlot, 1);
    resync(packet);

    // -------------------------------------
    // DROP
    // -------------------------------------
    //    ItemStack itemStack2 = new ItemStack(itemStack.getItemId(), 1);
    //    spawnDroppedItem(connection.getPlayer(), itemStack2);
  }

  private void spawnDroppedItem(ServerPlayer player, ItemStack stack) {
    //	  Log.debug("Item spawn");

    BlockType type = BlockRegistry.get(stack.getItemId());

    GameServer server = connection.getServer();

    long id = server.getEntityManager().createEntityId();

    ItemEntity drop = new ItemEntity(id, type, player.getPosition());

    //	  item.applyDropImpulse(player.getLookDirection())
    server.getEntityManager().addEntity(drop);
    connection.getServer().getPlayerManager().broadcast(new ItemSpawnPacket(drop));
  }
}
