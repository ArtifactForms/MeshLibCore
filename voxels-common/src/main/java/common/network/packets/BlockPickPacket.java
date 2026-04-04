package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class BlockPickPacket implements Packet {

  private int x;

  private int y;

  private int z;

  private int selectedSlot;

  public BlockPickPacket() {}

  public BlockPickPacket(int x, int y, int z, int selectedSlot) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.selectedSlot = selectedSlot;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeInt(x);
    out.writeInt(y);
    out.writeInt(z);
    out.writeInt(selectedSlot);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.x = in.readInt();
    this.y = in.readInt();
    this.z = in.readInt();
    this.selectedSlot = in.readInt();
  }

  @Override
  public int getId() {
    return PacketIds.BLOCK_PICK;
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
}
