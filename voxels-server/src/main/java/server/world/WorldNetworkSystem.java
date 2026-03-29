package server.world;

import common.network.packets.ChatMessagePacket;
import common.network.packets.TimeUpdatePacket;
import server.events.events.world.ChunkLoadedEvent;
import server.events.events.world.WorldSavedEvent;
import server.events.events.world.WorldTimeChangedEevent;
import server.gateways.EventGateway;
import server.network.PlayerManager;

public class WorldNetworkSystem {

  private PlayerManager playerManager;

  public WorldNetworkSystem(EventGateway events, PlayerManager playerManager) {
    this.playerManager = playerManager;
    events.register(WorldSavedEvent.class, this::onWorldSaved);
    events.register(WorldTimeChangedEevent.class, this::onWorldTimeChanged);
    events.register(ChunkLoadedEvent.class, this::onChunkLoaded);
  }

  private void onWorldSaved(WorldSavedEvent e) {
    broadcastMessage("World saved: " + e.getSavedChunksCount() + " dirty chunks saved.");
  }

  private void onWorldTimeChanged(WorldTimeChangedEevent e) {
    playerManager.broadcast(new TimeUpdatePacket(e.getTime()));
  }

  private void onChunkLoaded(ChunkLoadedEvent event) {
//    int chunkX = event.getData().getChunkX();
//    int chunkZ = event.getData().getChunkZ();
//    String message = "Chunk loaded " + chunkX + "," + chunkZ;
//    broadcastMessage(message);
  }

  private void broadcastMessage(String message) {
    playerManager.broadcast(new ChatMessagePacket(message));
  }
}
