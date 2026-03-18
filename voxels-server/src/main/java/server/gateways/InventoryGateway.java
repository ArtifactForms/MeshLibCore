package server.gateways;

import java.util.UUID;

import common.game.ItemStack;

public interface InventoryGateway {

  short getItemId(UUID playerId, int slotIndex);

  void setItem(UUID playerId, int slotIndex, short itemId, int amount);

  boolean hasItem(UUID playerId, short itemId, int amount);

  void consumeItem(UUID playerId, int slotIndex, int amount);

  boolean isItemInSlot(UUID playerId, int slotIndex, short itemId);

  ItemStack getItem(UUID playerId, int slotIndex);

  int getMaxSlotIndex(UUID playerId);
}
