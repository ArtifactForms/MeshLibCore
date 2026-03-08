package client.network;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import client.app.ApplicationContext;
import client.entity.ClientEntityManager;
import client.player.ClientMovementConsumer;
import common.network.Packet;
import common.network.packets.BlockUpdatePacket;
import common.network.packets.ChatMessagePacket;
import common.network.packets.ChunkDataPacket;
import common.network.packets.EntityDestroyPacket;
import common.network.packets.ItemSpawnPacket;
import common.network.packets.PlayerPositionPacket;
import common.network.packets.SoundEffectPacket;
import common.network.packets.UpdateSlotPacket;
import engine.scene.audio.SoundManager;
import math.Vector3f;

/** Handles incoming packets from the server and routes them to the client-side logic. */
public class ClientPacketDispatcher {

  private final Map<Class<? extends Packet>, Consumer<Packet>> handlers = new HashMap<>();

  public ClientPacketDispatcher() {
    // Registration of client-side logic
    register(ChunkDataPacket.class, this::handleChunkData);
    register(BlockUpdatePacket.class, this::handleBlockUpdate);
    register(ChatMessagePacket.class, this::handleChatMessage);
    register(SoundEffectPacket.class, this::handleSoundEffect);
    register(PlayerPositionPacket.class, this::handlePlayerPosition);
    register(ItemSpawnPacket.class, this::handleItemSpawn);
    register(EntityDestroyPacket.class, this::handleEntityDestroy);
    register(UpdateSlotPacket.class, this::handleUpdateSlot);
  }

  private <T extends Packet> void register(Class<T> packetClass, Consumer<T> handler) {
    handlers.put(packetClass, packet -> handler.accept(packetClass.cast(packet)));
  }

  public void dispatch(Packet packet) {
    Consumer<Packet> handler = handlers.get(packet.getClass());
    if (handler != null) {
      handler.accept(packet);
    }
    // Unregistered packets are simply ignored on the client (e.g. PlayerMovePacket)
  }

  // --- Specific Handler Logic ---

  private void handleChunkData(ChunkDataPacket packet) {
    short[] blocks = packet.decompress();
    ApplicationContext.clientWorld.applyChunkData(packet.getChunkX(), packet.getChunkZ(), blocks);
  }

  private void handleBlockUpdate(BlockUpdatePacket packet) {
    ApplicationContext.clientWorld.onServerBlockUpdate(
        packet.getX(), packet.getY(), packet.getZ(), packet.getBlockId());
  }

  private void handleChatMessage(ChatMessagePacket packet) {
    ApplicationContext.display.setText(packet.getMessage());
  }

  private void handleSoundEffect(SoundEffectPacket packet) {
    SoundManager.playEffect(packet.getSoundEffectId());
  }

  //  private void handlePlayerPosition(PlayerPositionPacket packet) {
  //    if (packet.getPlayerUuid().equals(ApplicationContext.playerUiid)) {
  //      ClientMovementConsumer consumer = ApplicationContext.clientMovementConsumer;
  //      Vector3f clientPos = consumer.getPosition();
  //
  //      float dx = packet.getX() - clientPos.x;
  //      float dy = (-packet.getY()) - clientPos.y;
  //      float dz = packet.getZ() - clientPos.z;
  //
  //      float distanceSq = dx * dx + dy * dy + dz * dz;
  //
  //      // Hard sync if the difference is too large (Server-side authority)
  //      if (distanceSq > 0.01f) {
  //        clientPos.set(packet.getX(), -packet.getY(), packet.getZ());
  //        System.out.println("[Network] Hard sync applied (Diff: " + Math.sqrt(distanceSq) + ")");
  //      }
  //    }
  //  }

  private void handlePlayerPosition(PlayerPositionPacket packet) {
    if (packet.getPlayerUuid().equals(ApplicationContext.playerUiid)) {

      ClientMovementConsumer consumer = ApplicationContext.clientMovementConsumer;
      Vector3f clientPos = consumer.getPosition();

      float dx = packet.getX() - clientPos.x;
      float dy = packet.getY() - clientPos.y;
      float dz = packet.getZ() - clientPos.z;

      float distanceSq = dx * dx + dy * dy + dz * dz;

      if (distanceSq > 1.0f) {
        clientPos.set(packet.getX(), packet.getY(), packet.getZ());
      }
    }
  }

  // Inside your ClientPacketDispatcher or Handler
  public void handleItemSpawn(ItemSpawnPacket packet) {
    // Add the item to the client world
    ClientEntityManager.spawnItem(
        packet.getEntityId(),
        packet.getBlockType(),
        packet.getX(),
        packet.getY(),
        packet.getZ(),
        packet.getVelX(),
        packet.getVelY(),
        packet.getVelZ());

    //    System.out.println("[Client] Item spawned: " + packet.getBlockType());
  }

  public void handleEntityDestroy(EntityDestroyPacket packet) {
    // Remove the item (picked up or despawned)
    ClientEntityManager.removeItem(packet.getEntityId());
  }

  public void handleUpdateSlot(UpdateSlotPacket packet) {}
}
