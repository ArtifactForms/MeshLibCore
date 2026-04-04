package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class BlockBreakPacket implements Packet {

  private int x;

  private int y;

  private int z;

  public BlockBreakPacket() {}

  public BlockBreakPacket(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeInt(x);
    out.writeInt(y);
    out.writeInt(z);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.x = in.readInt();
    this.y = in.readInt();
    this.z = in.readInt();
  }

  @Override
  public int getId() {
    return PacketIds.BLOCK_BREAK;
  }
}
