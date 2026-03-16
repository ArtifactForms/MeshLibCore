package server.network.handlers;

import common.game.Inventory;
import common.game.InventoryActionType;
import common.game.ItemStack;
import common.network.packets.InventoryActionPacket;
import common.network.packets.PlayerInventoryFullUpdatePacket;
import server.network.ServerConnection;
import server.player.ServerPlayer;

public class InventoryActionHandler {

  private final ServerConnection connection;

  public InventoryActionHandler(ServerConnection connection) {
    this.connection = connection;
  }

  public void handle(InventoryActionPacket packet) {

    ServerPlayer player = connection.getPlayer();
    if (player == null) return;

    Inventory inventory = player.getInventory();

    // Version Check (Anti-Desync / Anti-Dupe)
    if (packet.getInventoryVersion() != player.getInventoryVersion()) {

      connection.send(
          new PlayerInventoryFullUpdatePacket(
              inventory.getItems(), player.getCursorStack(), player.getInventoryVersion()));

      return;
    }

    // Validate ActionType
    int typeId = packet.getActionType();
    if (typeId < 0 || typeId >= InventoryActionType.values().length) return;

    InventoryActionType type = InventoryActionType.values()[typeId];

    int slot = packet.getSlot();
    if (slot < 0 || slot >= inventory.getSize()) return;

    // SHIFT CLICK
    if (type == InventoryActionType.SHIFT_CLICK) {

      handleShiftClick(player, slot);

      player.incrementInventoryVersion();

      connection.send(
          new PlayerInventoryFullUpdatePacket(
              inventory.getItems(), player.getCursorStack(), player.getInventoryVersion()));

      return;
    }

    ItemStack slotStack = inventory.getSlot(slot);
    ItemStack cursor = player.getCursorStack();

    // LEFT CLICK
    if (packet.getMouseButton() == 0) {

      if (cursor == null) {

        // Pickup stack
        if (slotStack != null) {

          player.setCursorStack(slotStack);
          inventory.setSlot(slot, null);
        }

      } else {

        if (slotStack == null) {

          // Place stack
          inventory.setSlot(slot, cursor);
          player.setCursorStack(null);

        } else if (slotStack.getItemId() == cursor.getItemId()) {

          // Stack items
          int max = 64;

          int remainder = slotStack.add(cursor.getAmount(), max);

          if (remainder == 0) {
            player.setCursorStack(null);
          } else {
            cursor.setAmount(remainder);
          }

        } else {

          // Swap
          inventory.setSlot(slot, cursor);
          player.setCursorStack(slotStack);
        }
      }
    }

    // RIGHT CLICK
    if (packet.getMouseButton() == 1) {

      if (cursor == null) {

        // Split stack
        if (slotStack != null) {

          int half = (int) Math.ceil(slotStack.getAmount() / 2f);

          player.setCursorStack(new ItemStack(slotStack.getItemId(), half));

          int remaining = slotStack.getAmount() - half;

          if (remaining <= 0) {
            inventory.setSlot(slot, null);
          } else {
            slotStack.setAmount(remaining);
          }
        }

      } else {

        if (slotStack == null) {

          // Place one item
          inventory.setSlot(slot, new ItemStack(cursor.getItemId(), 1));

          cursor.setAmount(cursor.getAmount() - 1);

        } else if (slotStack.getItemId() == cursor.getItemId() && slotStack.getAmount() < 64) {

          // Add one item
          slotStack.setAmount(slotStack.getAmount() + 1);
          cursor.setAmount(cursor.getAmount() - 1);

        } else {
          return;
        }

        if (cursor.getAmount() <= 0) {
          player.setCursorStack(null);
        }
      }
    }

    // Inventory changed → increase version
    player.incrementInventoryVersion();

    // Send full sync
    connection.send(
        new PlayerInventoryFullUpdatePacket(
            inventory.getItems(), player.getCursorStack(), player.getInventoryVersion()));
  }

  private void handleShiftClick(ServerPlayer player, int slot) {

    Inventory inv = player.getInventory();
    ItemStack stack = inv.getSlot(slot);

    if (stack == null) return;

    int size = inv.getSize();

    boolean fromHotbar = slot < 9;

    int start;
    int end;

    if (fromHotbar) {

      start = 9;
      end = size;

    } else {

      start = 0;
      end = 9;
    }

    moveStack(inv, stack, start, end);

    if (stack.getAmount() <= 0) {
      inv.setSlot(slot, null);
    }
  }

  private void moveStack(Inventory inv, ItemStack stack, int start, int end) {

    short itemId = stack.getItemId();

    // Merge into existing stacks
    for (int i = start; i < end; i++) {

      ItemStack target = inv.getSlot(i);

      if (target == null) continue;
      if (target.getItemId() != itemId) continue;
      if (target.getAmount() >= 64) continue;

      int space = 64 - target.getAmount();
      int transfer = Math.min(space, stack.getAmount());

      target.setAmount(target.getAmount() + transfer);
      stack.setAmount(stack.getAmount() - transfer);

      if (stack.getAmount() <= 0) return;
    }

    // Place into empty slot
    for (int i = start; i < end; i++) {

      if (inv.getSlot(i) != null) continue;

      inv.setSlot(i, new ItemStack(itemId, stack.getAmount()));
      stack.setAmount(0);

      return;
    }
  }
}
