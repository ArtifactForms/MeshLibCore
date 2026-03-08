package server.network;

import common.network.Packet;
import common.network.packets.*;
import server.network.handlers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Central dispatcher responsible for routing incoming network packets to their respective handlers.
 * This replaces the Visitor Pattern to decouple packet data from processing logic and reduce
 * boilerplate code.
 */
public class ServerPacketDispatcher {

  /**
   * * A map linking packet classes to their specific handling logic. The Consumer uses the base
   * Packet type but is safely cast during registration.
   */
  private final Map<Class<? extends Packet>, Consumer<Packet>> handlers = new HashMap<>();

  /**
   * Initializes the dispatcher and registers all available packet handlers. * @param connection The
   * active server connection associated with these handlers.
   */
  public ServerPacketDispatcher(ServerConnection connection) {
    // Register all packet-to-handler mappings
    register(PlayerJoinPacket.class, new PlayerJoinHandler(connection)::handle);
    register(PlayerMovePacket.class, new PlayerMoveHandler(connection)::handle);
    register(ChatMessagePacket.class, new ChatMessageHandler(connection)::handle);
    register(BlockUpdatePacket.class, new BlockUpdateHandler(connection)::handle);
    register(PlayerSpawnPacket.class, new PlayerSpawnHandler(connection)::handle);
    register(ChunkDataPacket.class, new ChunkDataHandler(connection)::handle);
    register(BlockPlacePacket.class, new BlockPlaceHandler(connection)::handle);
    register(BlockBreakPacket.class, new BlockBreakHandler(connection)::handle);
  }

  /**
   * Helper method to ensure type safety during registration. Uses Java Generics to wrap the
   * specific handler into a generic Packet Consumer.
   *
   * @param <T> The specific type of the Packet.
   * @param packetClass The class object of the packet type.
   * @param handler The functional interface (method reference) that processes the packet.
   */
  private <T extends Packet> void register(Class<T> packetClass, Consumer<T> handler) {
    handlers.put(packetClass, packet -> handler.accept(packetClass.cast(packet)));
  }

  /**
   * Dispatches an incoming packet to its registered handler. This method is typically called from
   * the main game loop to ensure thread-safe execution of game logic.
   *
   * @param packet The packet instance received from the network.
   */
  public void dispatch(Packet packet) {
    Consumer<Packet> handler = handlers.get(packet.getClass());

    if (handler != null) {
      handler.accept(packet);
    } else {
      // Log packets that do not have a registered handler (e.g., client-only packets)
      System.err.println(
          "[Dispatcher] No handler registered for packet type: "
              + packet.getClass().getSimpleName());
    }
  }
}
