package server.world;

import common.network.packets.ChatMessagePacket;
import common.network.packets.TimeUpdatePacket;
import server.events.events.world.ChunkLoadedEvent;
import server.events.events.world.ChunkUnloadedEvent;
import server.events.events.world.WorldSavedEvent;
import server.events.events.world.WorldTimeChangedEvent;
import server.gateways.EventGateway;
import server.network.PlayerManager;

public class WorldNetworkSystem {

  private PlayerManager playerManager;

  public WorldNetworkSystem(EventGateway events, PlayerManager playerManager) {
    this.playerManager = playerManager;
    events.register(WorldSavedEvent.class, this::onWorldSaved);
    events.register(WorldTimeChangedEvent.class, this::onWorldTimeChanged);
    events.register(ChunkLoadedEvent.class, this::onChunkLoaded);
    events.register(ChunkUnloadedEvent.class, this::onChunkUnload);
  }

  private void onWorldSaved(WorldSavedEvent e) {
    broadcastMessage("World saved: " + e.getSavedChunksCount() + " dirty chunks saved.");
  }

  private void onWorldTimeChanged(WorldTimeChangedEvent e) {
    playerManager.broadcast(new TimeUpdatePacket(e.getTime()));
  }

  private void onChunkLoaded(ChunkLoadedEvent event) {
    //    int chunkX = event.getData().getChunkX();
    //    int chunkZ = event.getData().getChunkZ();
    //    String message = "Chunk loaded " + chunkX + "," + chunkZ;
    //    broadcastMessage(message);
  }

  private void onChunkUnload(ChunkUnloadedEvent event) {
    //    int chunkX = event.getData().getChunkX();
    //    int chunkZ = event.getData().getChunkZ();
    //    String message = "Chunk unloaded " + chunkX + "," + chunkZ + " loaded: " +
    // event.getLoadedChunksCount();
    //    broadcastMessage(message);
  }

  private void broadcastMessage(String message) {
    playerManager.broadcast(new ChatMessagePacket(message));
  }
}
