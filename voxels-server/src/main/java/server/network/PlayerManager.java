package server.network;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import common.network.Connection;
import common.network.Packet;
import common.network.packets.ChatMessagePacket;
import common.network.packets.DisconnectPacket;
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
  private final List<ServerConnection> connections = new CopyOnWriteArrayList<>();

  private final Map<UUID, ServerPlayer> players = new ConcurrentHashMap<>();

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

  public void broadcast(Packet packet) {
    for (ServerPlayer player : players.values()) {
      if (player.getConnection() != null && player.getConnection().isRunning()) {
        player.getConnection().send(packet);
      }
    }
  }

  public Collection<ServerPlayer> getAllPlayers() {
    return players.values();
  }

  public void send(UUID playerId, Packet packet) {
    ServerPlayer player = players.get(playerId);
    if (player != null && player.getConnection() != null) {
      Connection connection = player.getConnection();
      if (connection.isRunning()) {
        connection.send(packet);
      }
    }
  }

  public void addPlayer(ServerPlayer player) {
    if (player == null) return;

    players.put(player.getUuid(), player);
  }

  public void removePlayer(ServerPlayer player) {
    if (player == null) return;

    players.remove(player.getUuid());

    connections.remove(player.getConnection());
  }

  public void kick(ServerPlayer player, String reason) {
    if (player == null) return;

    ServerConnection connection = player.getConnection();
    if (connection == null) return;

    connection.sendImmediate(new DisconnectPacket(reason));

    try {
      Thread.sleep(10);
    } catch (InterruptedException ignored) {
    }

    connection.close();
  }

  public void handleDisconnect(ServerPlayer player) {
    String defaultQuitMessage = "§e" + player.getName() + " left the game.";

    // 1. Remove player FIRST (state change)
    removePlayer(player);

    // 2. Fire event AFTER state change
    PlayerQuitEvent event = new PlayerQuitEvent(player.getUuid(), defaultQuitMessage);
    player.getConnection().getServer().getEventBus().fire(event);

    // 3. Inform clients about entity removal
    broadcast(new PlayerQuitPacket(player.getUuid()));

    // 4. Broadcast (possibly modified) quit message
    if (event.getQuitMessage() != null && !event.getQuitMessage().isEmpty()) {
      broadcast(new ChatMessagePacket(event.getQuitMessage()));
    }

    System.out.println("[Server] Player " + player.getName() + " disconnected.");
  }

  public ServerPlayer getPlayer(UUID uuid) {
    return players.get(uuid);
  }

  public ServerPlayer getPlayerByName(String name) {
    for (ServerPlayer player : players.values()) {
      if (player.getName().equals(name)) {
        return player;
      }
    }
    return null;
  }

  public List<ServerConnection> getConnections() {
    return connections;
  }
}
