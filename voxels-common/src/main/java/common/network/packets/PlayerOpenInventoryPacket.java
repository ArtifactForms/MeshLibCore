package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class PlayerOpenInventoryPacket implements Packet {

  @Override
  public void write(PacketBuffer out) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public int getId() {
    return PacketIds.PLAYER_OPEN_INVENTORY;
  }
}
