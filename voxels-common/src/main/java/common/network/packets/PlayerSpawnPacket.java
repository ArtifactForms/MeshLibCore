package common.network.packets;

import java.io.IOException;
import java.util.UUID;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

/**
 * Sent by the server to all clients when a new player enters the world. This tells the client to
 * create a visual representation (mesh/entity) for that player.
 */
public class PlayerSpawnPacket implements Packet {

  private UUID uuid;

  private String name;

  private float x;

  private float y;

  private float z;

  /** Required for PacketRegistry */
  public PlayerSpawnPacket() {}

  public PlayerSpawnPacket(UUID uuid, String name, float x, float y, float z) {
    this.uuid = uuid;
    this.name = name;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    // UUID writing
    out.writeUuid(uuid);

    // Data writing
    out.writeString(name);
    out.writeFloat(x);
    out.writeFloat(y);
    out.writeFloat(z);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    // UUID reading
    uuid = in.readUuid();

    // Data reading
    this.name = in.readString();
    this.x = in.readFloat();
    this.y = in.readFloat();
    this.z = in.readFloat();
  }

  @Override
  public int getId() {
    return PacketIds.PLAYER_SPAWN;
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
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
}
