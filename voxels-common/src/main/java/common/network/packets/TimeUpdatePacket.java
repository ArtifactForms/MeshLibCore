package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class TimeUpdatePacket implements Packet {

  private long worldTime;

  public TimeUpdatePacket() {}

  public TimeUpdatePacket(long worldTime) {
    this.worldTime = worldTime;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeLong(worldTime);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.worldTime = in.readLong();
  }

  @Override
  public int getId() {
    return PacketIds.TIME_UPDATE;
  }

  public long getWorldTime() {
    return worldTime;
  }
}
