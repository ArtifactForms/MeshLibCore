package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class PlayerDropItemPacket implements Packet {

  private int selectedSlot;

  public PlayerDropItemPacket() {}

  public PlayerDropItemPacket(int selectedSlot) {
    this.selectedSlot = selectedSlot;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeInt(selectedSlot);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.selectedSlot = in.readInt();
  }

  @Override
  public int getId() {
    return PacketIds.PLAYER_DROP_ITEM;
  }

  public int getSelectedSlot() {
    return selectedSlot;
  }
}
