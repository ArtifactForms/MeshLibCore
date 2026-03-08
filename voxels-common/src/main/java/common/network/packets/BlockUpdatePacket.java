package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class BlockUpdatePacket implements Packet {

  private int x;
  private int y;
  private int z;
  private short blockId;

  public BlockUpdatePacket() {}

  public BlockUpdatePacket(int x, int y, int z, short blockId) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.blockId = blockId;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeInt(x);
    out.writeInt(y);
    out.writeInt(z);
    out.writeShort((short) blockId);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.x = in.readInt();
    this.y = in.readInt();
    this.z = in.readInt();
    this.blockId = in.readShort();
  }

  @Override
  public int getId() {
    return PacketIds.BLOCK_UPDATE;
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

  public short getBlockId() {
    return blockId;
  }
}
