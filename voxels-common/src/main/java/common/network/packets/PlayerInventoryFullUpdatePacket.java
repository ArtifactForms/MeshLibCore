package common.network.packets;

import java.io.IOException;

import common.game.ItemStack;
import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class PlayerInventoryFullUpdatePacket implements Packet {

  private ItemStack[] items;
  private ItemStack cursorStack;
  private int inventoryVersion;

  public PlayerInventoryFullUpdatePacket() {}

  public PlayerInventoryFullUpdatePacket(
      ItemStack[] items, ItemStack cursorStack, int inventoryVersion) {
    this.items = items;
    this.cursorStack = cursorStack;
    this.inventoryVersion = inventoryVersion;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {

    out.writeItems(items);

    if (cursorStack == null) {
      out.writeBoolean(false);
    } else {
      out.writeBoolean(true);
      out.writeShort(cursorStack.getItemId());
      out.writeInt(cursorStack.getAmount());
    }

    out.writeInt(inventoryVersion);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {

    items = in.readItems();

    boolean hasCursor = in.readBoolean();

    if (hasCursor) {

      short id = in.readShort();
      int amount = in.readInt();

      cursorStack = new ItemStack(id, amount);
    }

    inventoryVersion = in.readInt();
  }

  @Override
  public int getId() {
    return PacketIds.PLAYER_INVENTORY_FULL_UPDATE;
  }

  public ItemStack[] getItems() {
    return items;
  }

  public ItemStack getCursorStack() {
    return cursorStack;
  }

  public int getInventoryVersion() {
    return inventoryVersion;
  }
}
