package common.network.packets;

import java.io.IOException;
import java.util.UUID;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class PlayerQuitPacket implements Packet {

  private UUID uuid;

  public PlayerQuitPacket() {}

  public PlayerQuitPacket(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeUuid(uuid);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.uuid = in.readUuid();
  }

  @Override
  public int getId() {
    return PacketIds.PLAYER_QUIT;
  }

  public UUID getUuid() {
    return uuid;
  }
}
