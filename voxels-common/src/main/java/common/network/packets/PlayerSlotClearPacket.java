package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class PlayerSlotClearPacket implements Packet {

  private int slotIndex;
  
  public PlayerSlotClearPacket() {}

  public PlayerSlotClearPacket(int slotIndex) {
    this.slotIndex = slotIndex;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeInt(slotIndex);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.slotIndex = in.readInt();
  }

  @Override
  public int getId() {
    return PacketIds.PLAYER_SLOT_CLEAR;
  }

  public int getSlotIndex() {
    return slotIndex;
  }
}
