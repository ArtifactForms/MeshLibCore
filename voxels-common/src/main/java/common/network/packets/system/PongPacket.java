package common.network.packets.system;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;
import common.network.ServerToClientPacket;

public class PongPacket implements Packet, ServerToClientPacket {

  private long time;

  public PongPacket() {}

  public PongPacket(long time) {
    this.time = time;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeLong(time);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    time = in.readLong();
  }

  @Override
  public int getId() {
    return PacketIds.PONG;
  }

  public long getTime() {
    return time;
  }
}
