package client.world;

import java.util.concurrent.ConcurrentLinkedQueue;

import common.world.World;

public class ClientWorld extends World {

  private final ChunkManager chunkManager;
  // Warteschlange für Datenpakete vom Server
  private final ConcurrentLinkedQueue<ChunkDataPacket> incomingPackets =
      new ConcurrentLinkedQueue<>();

  public ClientWorld(ChunkManager chunkManager) {
    this.chunkManager = chunkManager;
  }

  // Container für die Netzwerk-Daten
  private record ChunkDataPacket(int cx, int cz, short[] blocks) {}

  /** Wird vom Netzwerk-Thread aufgerufen, wenn ein Chunk-Paket ankommt. */
  public void applyChunkData(int cx, int cz, short[] blocks) {
    incomingPackets.offer(new ChunkDataPacket(cx, cz, blocks));
  }

//  /**
//   * Verarbeitet Pakete im Main-Thread innerhalb eines Zeitlimits.
//   *
//   * @param budgetInNanos Zeit in Nanosekunden (z.B. 2ms = 2_000_000)
//   */
//  public void processIncomingPackets(long budgetInNanos) {
//    long startTime = System.nanoTime();
//
//    while (!incomingPackets.isEmpty()) {
//      if (System.nanoTime() - startTime > budgetInNanos) break;
//
//      ChunkDataPacket packet = incomingPackets.poll();
//      if (packet == null) continue;
//
//      Chunk chunk = chunkManager.getOrCreateChunk(packet.cx, packet.cz);
//
//      // Daten kopieren
////      System.arraycopy(packet.blocks, 0, chunk.getRawBlockData(), 0, packet.blocks.length);
//      
//      chunk.blockData = packet.blocks;
//
//      // Diese drei Zeilen sind die "Lebensversicherung" für den Client:
//      chunk.setDataReady(); // Markiert den Chunk als valide für das Meshing
//      chunk.markDirty(); // Erzwingt den Neubau des Meshes
//      addChunk(chunk); // Registriert den Chunk in der World-Map für Raycasts/Interaktion
//
//      // Nachbarn triggern
//      chunkManager.notifyNeighborsOfDataReady(packet.cx, packet.cz);
//    }
//  }
  
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
}
