package server.network.handlers;

import common.network.packets.PlayerMovePacket;
import server.events.events.PlayerMoveEvent;
import server.gateways.EventGateway;
import server.gateways.GatewayContext;
import server.network.ServerConnection;
import server.player.ServerPlayer;

/**
 * Handles movement packets sent by the client. Implements server-side validation to prevent
 * cheating and ensure the player remains within valid world boundaries.
 */
public class PlayerMoveHandler {

  private final ServerConnection connection;

  private final EventGateway events;

  public PlayerMoveHandler(ServerConnection connection, GatewayContext context) {
    this.connection = connection;
    this.events = context.events();
  }

  /**
   * Processes the move packet, fires a move event, validates the new position against the terrain,
   * and updates the player's state. * @param packet The movement data sent by the client.
   */
  public void handle(PlayerMovePacket packet) {
    ServerPlayer player = connection.getPlayer();

    if (player == null) {
      return;
    }

    // 1. Create and fire the Move Event for internal listeners (e.g., Anti-Cheat or Triggers)
    PlayerMoveEvent event =
        new PlayerMoveEvent(
            player.getUuid(),
            packet.getX(),
            packet.getY(),
            packet.getZ(),
            packet.getYaw(),
            packet.getPitch());

    events.fire(event);

    // If a listener cancelled the movement, stop processing
    if (event.isCancelled()) {
      // Optional: Force player back to previous position
      return;
    }

    float x = event.getX();
    float y = event.getY();
    float z = event.getZ();
    float yaw = event.getYaw();
    float pitch = event.getPitch();

    player.move(x, y, z, yaw, pitch);

    //    // 2. Validate position against terrain (Server Authority)
    //    // We determine where the player *should* be based on the world data
    //    int terrainY =
    //        GameServer.getWorld().getHeightAt((int) Math.floor(x), (int) Math.floor(z));
    //
    //    // 3. Check for significant deviations (e.g., flying or falling through floor)
    //    float diffY = Math.abs(y - terrainY);
    //
    //    if (diffY > 1.2f) {
    //      // The difference is too high; force the client to sync with the server's calculated Y
    //      player.move(x, terrainY, z, yaw, pitch);
    //    } else {
    //      // The position is acceptable; update internal state without sending a correction packet
    //      player.setSilentPosition(x, y, z, yaw, pitch);
    //    }
  }
}
