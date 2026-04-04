package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class PlayerSlotUpdatePacket implements Packet {

  private int slotIndex;

  private short itemId;

  private int amount;

  public PlayerSlotUpdatePacket() {}

  public PlayerSlotUpdatePacket(int slotIndex, short itemId, int amount) {
    this.slotIndex = slotIndex;
    this.itemId = itemId;
    this.amount = amount;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeInt(slotIndex);
    out.writeShort(itemId);
    out.writeInt(amount);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.slotIndex = in.readInt();
    this.itemId = in.readShort();
    this.amount = in.readInt();
  }

  @Override
  public int getId() {
    return PacketIds.PLAYER_SLOT_UPDATE;
  }

  public int getSlotIndex() {
    return slotIndex;
  }

  public short getItemId() {
    return itemId;
  }

  public int getAmount() {
    return amount;
  }
}
