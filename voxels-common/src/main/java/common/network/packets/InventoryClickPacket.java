package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class InventoryClickPacket implements Packet {

  private int slot;

  public InventoryClickPacket() {}

  public InventoryClickPacket(int slot) {
    this.slot = slot;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeInt(slot);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    slot = in.readInt();
  }

  public int getSlot() {
    return slot;
  }

  @Override
  public int getId() {
    return PacketIds.INVENTORY_CLICK;
  }
}
