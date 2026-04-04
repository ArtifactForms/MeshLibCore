package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

/**
 * Sent by the client to update its position and rotation on the server. The server then broadcasts
 * this to other nearby players.
 */
public class PlayerMovePacket implements Packet {

  private float x;

  private float y;

  private float z;

  private float yaw;

  private float pitch;

  /** Required for PacketRegistry */
  public PlayerMovePacket() {}

  public PlayerMovePacket(float x, float y, float z, float yaw, float pitch) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeFloat(x);
    out.writeFloat(y);
    out.writeFloat(z);
    out.writeFloat(yaw);
    out.writeFloat(pitch);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.x = in.readFloat();
    this.y = in.readFloat();
    this.z = in.readFloat();
    this.yaw = in.readFloat();
    this.pitch = in.readFloat();
  }

  @Override
  public int getId() {
    return PacketIds.PLAYER_MOVE;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public float getZ() {
    return z;
  }

  public float getYaw() {
    return yaw;
  }

  public float getPitch() {
    return pitch;
  }
}
