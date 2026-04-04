package client.world;

import java.util.concurrent.ConcurrentLinkedQueue;

import common.world.World;

public class ClientWorld extends World {

  public static final int TICKS_PER_SECOND = 20;

  private float fractionalTickCounter = 0;

  private ChunkManager chunkManager;
  
  // Warteschlange für Datenpakete vom Server
  private final ConcurrentLinkedQueue<ChunkDataPacket> incomingPackets =
      new ConcurrentLinkedQueue<>();

  // Container für die Netzwerk-Daten
  private record ChunkDataPacket(int cx, int cz, short[] blocks) {}

  /** Wird vom Netzwerk-Thread aufgerufen, wenn ein Chunk-Paket ankommt. */
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

      // Referenz-Zuweisung statt Kopie (Extrem schnell!)
      chunk.setBlockData(packet.blocks);

      chunk.setDataReady();
      chunk.markDirty();
      addChunk(chunk); // Wichtig für Raycasts/Interaktion!

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
    // Wir addieren den Fortschritt auf einen float-Counter
    fractionalTickCounter += tpf * TICKS_PER_SECOND;

    // Sobald der Counter >= 1 ist, schieben wir die vollen Ticks in worldTime
    if (fractionalTickCounter >= 1.0f) {
      int fullTicks = (int) fractionalTickCounter;
//      worldTime += fullTicks;
      tick();
      fractionalTickCounter -= fullTicks;
    }
  }

//  @Override
//  public float getTimeOfDay() {
//    // worldTime (ganze Ticks) + fractionalTickCounter (Nachkommastellen)
//    float preciseTime = worldTime + fractionalTickCounter;
//    return (preciseTime % WorldTime.DAY_LENGTH) / (float) WorldTime.DAY_LENGTH;
//  }
}
