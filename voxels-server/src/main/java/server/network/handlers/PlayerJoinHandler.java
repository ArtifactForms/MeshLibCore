package server.network.handlers;

import common.network.packets.ChatMessagePacket;
import common.network.packets.ChunkDataPacket;
import common.network.packets.PlayerJoinPacket;
import common.network.packets.PlayerPositionPacket;
import common.world.ChunkData;

import server.events.events.PlayerJoinEvent;
import server.network.GameServer;
import server.network.PlayerManager;
import server.network.ServerConnection;
import server.player.ServerPlayer;

public class PlayerJoinHandler {

  private final ServerConnection connection;

  public PlayerJoinHandler(ServerConnection connection) {
    this.connection = connection;
  }

  public void handle(PlayerJoinPacket packet) {

    // Prevent double join (connection already has a player assigned)
    if (connection.getPlayer() != null) {
      return;
    }

    // Create the server-side player object
    ServerPlayer player = new ServerPlayer(packet.getUuid(), packet.getName(), connection);
    connection.setPlayer(player);

    // Calculate spawn position
    int spawnX = 0;
    int spawnZ = 0;
    int spawnY = GameServer.getWorld().getHighestNonAirBlock(spawnX, spawnZ);

    // Default join message
    String joinMessage = "§e" + player.getName() + " ist dem Spiel beigetreten!";

    // Fire join event so other systems can modify spawn or message
    PlayerJoinEvent event = new PlayerJoinEvent(player, joinMessage, spawnX, spawnY, spawnZ);
    GameServer.getEventBus().fire(event);

    // If the event was cancelled, close the connection
    if (event.isCancelled()) {
      connection.close();
      return;
    }

    // Apply potentially modified event values
    spawnX = event.getSpawnX();
    spawnY = event.getSpawnY();
    spawnZ = event.getSpawnZ();
    joinMessage = event.getJoinMessage();

    // Move the player to the spawn position
    player.move(spawnX, spawnY, spawnZ, 0f, 0f);

    // Broadcast the join message to all connected players
    PlayerManager.broadcast(new ChatMessagePacket(joinMessage));

    // Send initial chunks around the spawn position
    sendInitialChunks();

    // Send teleport packet to synchronize client position
    connection.send(new PlayerPositionPacket(player.getUuid(), spawnX, -spawnY, spawnZ, 0f, 0f));
  }

  private void sendInitialChunks() {

    // Send a small chunk area around the spawn to the player
    for (int cx = -1; cx <= 1; cx++) {
      for (int cz = -1; cz <= 1; cz++) {

        ChunkData data = GameServer.getWorld().getOrCreateChunk(cx, cz);
        connection.send(new ChunkDataPacket(data));
      }
    }
  }
}