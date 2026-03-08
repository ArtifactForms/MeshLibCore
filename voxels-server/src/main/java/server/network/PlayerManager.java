package server.network;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import common.network.Packet;
import common.network.packets.ChatMessagePacket;
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
  public static void addConnection(ServerConnection conn) {
    connections.add(conn);
  }

  /**
   * Removes a connection and broadcasts a quit message if a player was associated.
   *
   * @param conn The ServerConnection to remove.
   */
  public static void removeConnection(ServerConnection conn) {
    if (connections.remove(conn) && conn.getPlayer() != null) {
      String quitMessage = "§e" + conn.getPlayer().getName() + " left the game.";
      broadcast(new ChatMessagePacket(quitMessage));
    }
  }

  /**
   * Retrieves all currently active and non-null player objects.
   *
   * @return A list of all active ServerPlayers.
   */
  public static List<ServerPlayer> getAllPlayers() {
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
  public static void broadcast(Packet packet) {
    for (ServerConnection conn : connections) {
      if (conn.isRunning()) {
        conn.send(packet);
      }
    }
  }
}
