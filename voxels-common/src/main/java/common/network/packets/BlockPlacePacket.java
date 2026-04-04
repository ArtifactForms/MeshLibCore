package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class BlockPlacePacket implements Packet {

  private int x;

  private int y;

  private int z;

  private int selectedSlot;

  private short blockId;

  public BlockPlacePacket() {}

  public BlockPlacePacket(int x, int y, int z, int selectedSlot, short blockId) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.selectedSlot = selectedSlot;
    this.blockId = blockId;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeInt(x);
    out.writeInt(y);
    out.writeInt(z);
    out.writeInt(selectedSlot);
    out.writeShort((short) blockId);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.x = in.readInt();
    this.y = in.readInt();
    this.z = in.readInt();
    this.selectedSlot = in.readInt();
    this.blockId = in.readShort();
  }

  @Override
  public int getId() {
    return PacketIds.BLOCK_PLACE;
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

  public int getSelectedSlot() {
    return selectedSlot;
  }

  public short getBlockId() {
    return blockId;
  }
}
