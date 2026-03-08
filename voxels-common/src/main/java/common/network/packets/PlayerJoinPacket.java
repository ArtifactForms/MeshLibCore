package common.network.packets;

import java.io.IOException;
import java.util.UUID;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

/**
 * Sent by the client to request joining the server. Provides the player's unique identity (UUID)
 * and display name.
 */
public class PlayerJoinPacket implements Packet {

  private UUID uuid;
  private String name;

  /** Required empty constructor for reflection-based packet instantiation. */
  public PlayerJoinPacket() {}

  public PlayerJoinPacket(UUID uuid, String name) {
    this.uuid = uuid;
    this.name = name;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeUuid(uuid);
    out.writeString(name);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.uuid = in.readUuid();
    this.name = in.readString();
  }

  @Override
  public int getId() {
    return PacketIds.PLAYER_JOIN;
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }
}
