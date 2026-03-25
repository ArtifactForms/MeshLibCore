package server.gateways;

import java.util.UUID;

import common.game.Inventory;
import common.game.ItemStack;
import server.network.PlayerManager;
import server.player.ServerPlayer;

public class InventoryAdapter implements InventoryGateway {

  private PlayerManager playerManager;

  public InventoryAdapter(PlayerManager playerManager) {
    this.playerManager = playerManager;
  }

  @Override
  public boolean isItemInSlot(UUID playerId, int slotIndex, short itemId) {
    Inventory inventory = getInventory(playerId);
    ItemStack itemStack = inventory.getSlot(slotIndex);
    if (itemStack == null) {
      return false;
    }
    return itemStack.getItemId() == itemId;
  }

  @Override
  public short getItemId(UUID playerId, int slotIndex) {
    Inventory inventory = getInventory(playerId);
    ItemStack itemStack = inventory.getSlot(slotIndex);
    return itemStack.getItemId();
  }

  @Override
  public void setItem(UUID playerId, int slotIndex, short itemId, int amount) {
    Inventory inventory = getInventory(playerId);
    inventory.setSlot(slotIndex, new ItemStack(itemId, amount));
  }

  @Override
  public boolean hasItem(UUID playerId, short itemId, int amount) {
    Inventory inventory = getInventory(playerId);
    return inventory.hasItem(itemId, amount);
  }

  @Override
  public void consumeItem(UUID playerId, int slotIndex, int amount) {
    Inventory inventory = getInventory(playerId);
    inventory.remove(slotIndex, amount);
  }

  @Override
  public ItemStack getItem(UUID playerId, int slotIndex) {
    Inventory inventory = getInventory(playerId);
    return inventory.getSlot(slotIndex);
  }

  private Inventory getInventory(UUID playerId) {
    ServerPlayer player = playerManager.getPlayer(playerId);
    Inventory inventory = player.getInventory();
    return inventory;
  }

  @Override
  public int getMaxSlotIndex(UUID playerId) {
    Inventory inventory = playerManager.getPlayer(playerId).getInventory();
    return inventory.getSize() - 1;
  }

  @Override
  public void clear(UUID playerId) {
    Inventory inventory = playerManager.getPlayer(playerId).getInventory();
    inventory.clear();
  }
}
