package server.network;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import common.network.Packet;
import common.network.packets.ChatMessagePacket;
import common.network.packets.PlayerQuitPacket;
import server.events.events.PlayerQuitEvent;
import server.player.ServerPlayer;

/**
 * Thread-safe manager for active network connections and players. Uses a CopyOnWriteArrayList to
 * allow concurrent broadcasting without explicit synchronization.
 */
public class PlayerManager {

  /**
   * * List of all active server connections. Thread-safe for concurrent iteration and modification.
   */
  private static final List<ServerConnection> connections = new CopyOnWriteArrayList<>();

  /**
   * Registers a new connection.
   *
   * @param conn The ServerConnection to add.
   */
  public void addConnection(ServerConnection conn) {
    if (!connections.contains(conn)) {
      connections.add(conn);
    }
  }

  /**
   * Removes a connection and broadcasts a quit message if a player was associated.
   *
   * @param conn The ServerConnection to remove.
   */
  public void removeConnection(ServerConnection conn) {
    if (connections.remove(conn)) {
      ServerPlayer player = conn.getPlayer();
      if (player != null) {
        handleDisconnect(player);
      }
    }
  }

  /**
   * Retrieves all currently active and non-null player objects.
   *
   * @return A list of all active ServerPlayers.
   */
  public List<ServerPlayer> getAllPlayers() {
    return connections
        .stream()
        .map(ServerConnection::getPlayer)
        .filter(player -> player != null)
        .collect(Collectors.toList());
  }

  /**
   * Sends a packet to all running connections. Safe to call while the connection list is being
   * modified elsewhere.
   *
   * @param packet The packet to be broadcasted.
   */
  public void broadcast(Packet packet) {
    for (ServerConnection conn : connections) {
      if (conn.isRunning()) {
        conn.send(packet);
      }
    }
  }

  public void removePlayer(ServerPlayer player) {
    // Finde die Verbindung, die zu diesem Player gehört
    for (ServerConnection conn : connections) {
      if (conn.getPlayer() != null && conn.getPlayer().equals(player)) {
        connections.remove(conn);
        break;
      }
    }
  }

  public void handleDisconnect(ServerPlayer player) {
    String defaultQuitMessage = "§e" + player.getName() + " left the game.";
    PlayerQuitEvent event = new PlayerQuitEvent(player.getUuid(), defaultQuitMessage);

    player.getConnection().getServer().getEventBus().fire(event);

    removePlayer(player);
    broadcast(new PlayerQuitPacket(player.getUuid()));

    if (event.getQuitMessage() != null && !event.getQuitMessage().isEmpty()) {
      broadcast(new ChatMessagePacket(event.getQuitMessage()));
    }

    System.out.println("[Server] Player " + player.getName() + " disconnected.");
  }

  public ServerPlayer getPlayer(UUID uuid) {
    for (ServerConnection conn : connections) {
      if (conn.getPlayer().getUuid().equals(uuid)) return conn.getPlayer();
    }
    return null;
  }
}
