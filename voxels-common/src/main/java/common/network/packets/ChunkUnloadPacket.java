package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class ChunkUnloadPacket implements Packet {

  public int chunkX;
  public int chunkZ;

  public ChunkUnloadPacket(int chunkX, int chunkZ) {
    this.chunkX = chunkX;
    this.chunkZ = chunkZ;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeInt(chunkX);
    out.writeInt(chunkZ);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.chunkX = in.readInt();
    this.chunkZ = in.readInt();
  }

  @Override
  public int getId() {
    return PacketIds.UNLOAD_CHUNK;
  }
}
