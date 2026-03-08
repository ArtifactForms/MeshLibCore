package common.network.packets;

import java.io.IOException;
import java.util.UUID;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class ItemPickupPacket implements Packet {
  private long itemEntityId;
  private UUID playerUuid;

  public ItemPickupPacket() {}

  public ItemPickupPacket(long entityId, UUID uuid) {
    this.itemEntityId = entityId;
    this.playerUuid = uuid;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeUuid(playerUuid);
    out.writeLong(itemEntityId);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.playerUuid = in.readUuid();
    this.itemEntityId = in.readLong();
  }

  @Override
  public int getId() {
    return PacketIds.ITEM_PICKUP;
  }
}
