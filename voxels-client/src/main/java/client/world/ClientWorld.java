package client.world;

import java.util.concurrent.ConcurrentLinkedQueue;

import common.world.World;

public class ClientWorld extends World {

  public static final int TICKS_PER_SECOND = 20;

  private float fractionalTickCounter = 0;

  private ChunkManager chunkManager;

  private final ConcurrentLinkedQueue<ChunkDataPacket> incomingPackets =
      new ConcurrentLinkedQueue<>();

  private record ChunkDataPacket(int cx, int cz, short[] blocks) {}

  public void applyChunkData(int cx, int cz, short[] blocks) {
    incomingPackets.offer(new ChunkDataPacket(cx, cz, blocks));
  }

  public void processIncomingPackets(long budgetInNanos) {
    long startTime = System.nanoTime();

    while (!incomingPackets.isEmpty()) {
      if (System.nanoTime() - startTime > budgetInNanos) break;

      ChunkDataPacket packet = incomingPackets.poll();
      if (packet == null) continue;

      Chunk chunk = chunkManager.getOrCreateChunk(packet.cx, packet.cz);
      chunk.setBlockData(packet.blocks);
      chunk.setDataReady();
      chunk.markDirty();
      addChunk(chunk);

      chunkManager.notifyNeighborsOfDataReady(packet.cx, packet.cz);
    }
  }

  public void onServerBlockUpdate(int x, int y, int z, short blockId) {
    chunkManager.setBlockAt(x, y, z, blockId);
    setBlock(x, y, z, blockId);
  }

  public void setChunkManager(ChunkManager chunkManager) {
    this.chunkManager = chunkManager;
  }

  public void update(float tpf) {
    fractionalTickCounter += tpf * TICKS_PER_SECOND;

    if (fractionalTickCounter >= 1.0f) {
      int fullTicks = (int) fractionalTickCounter;
      tick();
      fractionalTickCounter -= fullTicks;
    }
  }
}
