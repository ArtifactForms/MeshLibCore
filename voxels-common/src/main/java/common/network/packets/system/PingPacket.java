package common.network.packets.system;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class PingPacket implements Packet {

  private long time;

  public PingPacket() {}

  public PingPacket(long time) {
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
    return PacketIds.PING;
  }

  public long getTime() {
    return time;
  }
}
