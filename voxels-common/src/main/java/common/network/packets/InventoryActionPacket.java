package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class InventoryActionPacket implements Packet {

  private int slot;
  private int mouseButton;
  private int actionType;
  private int inventoryVersion;

  public InventoryActionPacket() {}

  public InventoryActionPacket(int slot, int mouseButton, int actionType, int inventoryVersion) {
    this.slot = slot;
    this.mouseButton = mouseButton;
    this.actionType = actionType;
    this.inventoryVersion = inventoryVersion;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeInt(slot);
    out.writeInt(mouseButton);
    out.writeInt(actionType);
    out.writeInt(inventoryVersion);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    slot = in.readInt();
    mouseButton = in.readInt();
    actionType = in.readInt();
    inventoryVersion = in.readInt();
  }

  public int getSlot() {
    return slot;
  }

  public int getMouseButton() {
    return mouseButton;
  }

  public int getActionType() {
    return actionType;
  }

  @Override
  public int getId() {
    return PacketIds.INVENTORY_ACTION;
  }

  public int getInventoryVersion() {
    return inventoryVersion;
  }
}
