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

  private double x;

  private double y;

  private double z;

  private float yaw;

  private float pitch;

  private boolean teleport;

  /** Required for PacketRegistry */
  public PlayerPositionPacket() {}

  public PlayerPositionPacket(
      UUID playerUuid, double x, double y, double z, float yaw, float pitch) {
    this(playerUuid, x, y, z, yaw, pitch, false);
  }

  public PlayerPositionPacket(
      UUID playerUuid, double x, double y, double z, float yaw, float pitch, boolean teleport) {
    this.playerUuid = playerUuid;
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
    this.teleport = teleport;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    // Write UUID
    out.writeUuid(playerUuid);

    // Write Position
    out.writeDouble(x);
    out.writeDouble(y);
    out.writeDouble(z);

    // Write Rotation
    out.writeFloat(yaw);
    out.writeFloat(pitch);

    out.writeBoolean(teleport);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    // Read UUID
    this.playerUuid = in.readUuid();

    // Read Position
    this.x = in.readDouble();
    this.y = in.readDouble();
    this.z = in.readDouble();

    // Read Rotation
    this.yaw = in.readFloat();
    this.pitch = in.readFloat();

    this.teleport = in.readBoolean();
  }

  @Override
  public int getId() {
    return PacketIds.PLAYER_POSITION;
  }

  // Getters
  public UUID getPlayerUuid() {
    return playerUuid;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

  public float getYaw() {
    return yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public boolean isTeleport() {
    return teleport;
  }
}
