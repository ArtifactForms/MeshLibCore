package common.network.packets;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;
import java.io.IOException;

/** Sent by the server to inform clients that an entity should be removed from the world. */
public class EntityDestroyPacket implements Packet {

  private long entityId;

  /** Default constructor for the PacketRegistry. */
  public EntityDestroyPacket() {}

  /** @param entityId The unique ID of the entity to destroy. */
  public EntityDestroyPacket(long entityId) {
    this.entityId = entityId;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeLong(entityId);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.entityId = in.readLong();
  }

  @Override
  public int getId() {
    return PacketIds.ENTITY_DESTROY;
  }

  public long getEntityId() {
    return entityId;
  }
}
