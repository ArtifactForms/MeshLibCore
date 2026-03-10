package common.network.packets;

import java.io.IOException;
import java.util.UUID;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

/**
 * Sent by the server to sync a player's position and rotation to clients. This is used for both the
 * local player (correction) and remote players (interpolation).
 */
public class PlayerPositionPacket implements Packet {

  private UUID playerUuid;
  private float x;
  private float y;
  private float z;
  private float yaw, pitch;

  /** Required for PacketRegistry */
  public PlayerPositionPacket() {}

  public PlayerPositionPacket(UUID playerUuid, float x, float y, float z, float yaw, float pitch) {
    this.playerUuid = playerUuid;
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    // Write UUID
    out.writeUuid(playerUuid);

    // Write Position
    out.writeFloat(x);
    out.writeFloat(y);
    out.writeFloat(z);

    // Write Rotation
    out.writeFloat(yaw);
    out.writeFloat(pitch);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    // Read UUID
    this.playerUuid = in.readUuid();

    // Read Position
    this.x = in.readFloat();
    this.y = in.readFloat();
    this.z = in.readFloat();

    // Read Rotation
    this.yaw = in.readFloat();
    this.pitch = in.readFloat();
  }

  @Override
  public int getId() {
    return PacketIds.PLAYER_POSITION;
  }

  // Getters
  public UUID getPlayerUuid() {
    return playerUuid;
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
