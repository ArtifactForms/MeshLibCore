package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class PlayerDropItemPacket implements Packet {

  private int slot;

  public PlayerDropItemPacket() {}

  public PlayerDropItemPacket(int slot) {
    this.slot = slot;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeInt(slot);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.slot = in.readInt();
  }

  @Override
  public int getId() {
    return PacketIds.PLAYER_DROP_ITEM;
  }

  public int getSlot() {
    return slot;
  }
}
