package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;
import common.world.BlockFace;

public class StartMiningPacket implements Packet {

  private int x;

  private int y;

  private int z;

  private int selectedSlot;

  private BlockFace face;

  private float pitch;

  private float yaw;

  public StartMiningPacket() {}

  public StartMiningPacket(
      int x, int y, int z, int selectedSlot, BlockFace face, float pitch, float yaw) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.selectedSlot = selectedSlot;
    this.face = face;
    this.pitch = pitch;
    this.yaw = yaw;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeInt(x);
    out.writeInt(y);
    out.writeInt(z);
    out.writeInt(selectedSlot);

    out.writeByte((byte) face.ordinal());

    out.writeFloat(pitch);
    out.writeFloat(yaw);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.x = in.readInt();
    this.y = in.readInt();
    this.z = in.readInt();
    this.selectedSlot = in.readInt();

    int faceIndex = in.readByte() & 0xFF;
    if (faceIndex < 0 || faceIndex >= BlockFace.values().length) {
      throw new IOException("Invalid BlockFace index: " + faceIndex);
    }
    this.face = BlockFace.values()[faceIndex];

    this.pitch = in.readFloat();
    this.yaw = in.readFloat();
  }

  @Override
  public int getId() {
    return PacketIds.START_MINING;
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

  public BlockFace getFace() {
    return face;
  }

  public float getPitch() {
    return pitch;
  }

  public float getYaw() {
    return yaw;
  }
}
