package client.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import client.app.GameClient;
import client.player.ClientPlayer;
import client.scene.ConnectionLostScene;
import client.ui.title.Title;
import client.usecases.chat.ChatMessage;
import common.game.GameMode;
import common.game.Inventory;
import common.game.ItemStack;
import common.network.Packet;
import common.network.packets.ActionBarPacket;
import common.network.packets.BlockUpdatePacket;
import common.network.packets.ChatMessagePacket;
import common.network.packets.ChunkDataPacket;
import common.network.packets.DisconnectPacket;
import common.network.packets.EntityDestroyPacket;
import common.network.packets.GameModeUpdatePacket;
import common.network.packets.ItemSpawnPacket;
import common.network.packets.PlayerInventoryFullUpdatePacket;
import common.network.packets.PlayerPositionPacket;
import common.network.packets.PlayerQuitPacket;
import common.network.packets.PlayerSlotClearPacket;
import common.network.packets.PlayerSlotUpdatePacket;
import common.network.packets.PlayerSpawnPacket;
import common.network.packets.SoundEffectPacket;
import common.network.packets.TimeUpdatePacket;
import common.network.packets.TitlePacket;
import common.network.packets.UpdateSlotPacket;
import common.network.packets.system.PongPacket;
import engine.scene.audio.SoundManager;

/** Handles incoming packets from the server and routes them to the client-side logic. */
public class ClientPacketDispatcher {

  private final Map<Class<? extends Packet>, Consumer<Packet>> handlers = new HashMap<>();

  private GameClient client;

  public ClientPacketDispatcher(GameClient client) {
    this.client = client;

    // Registration of client-side logic
    register(ChunkDataPacket.class, this::handleChunkData);
    register(BlockUpdatePacket.class, this::handleBlockUpdate);
    register(ChatMessagePacket.class, this::handleChatMessage);
    register(SoundEffectPacket.class, this::handleSoundEffect);
    register(PlayerPositionPacket.class, this::handlePlayerPosition);
    register(ItemSpawnPacket.class, this::handleItemSpawn);
    register(EntityDestroyPacket.class, this::handleEntityDestroy);
    register(UpdateSlotPacket.class, this::handleUpdateSlot);
    register(PlayerQuitPacket.class, this::handlePlayerQuit);
    register(PlayerSpawnPacket.class, this::handlePlayerSpawn);
    register(TitlePacket.class, this::handleTitle);
    register(PlayerSlotUpdatePacket.class, this::handleSlotUpdate);
    register(PlayerInventoryFullUpdatePacket.class, this::handleInventoryFullUpdate);
    register(ActionBarPacket.class, this::handleActionBar);
    register(PlayerSlotClearPacket.class, this::handleSlotClear);
    register(TimeUpdatePacket.class, this::handleTimeUpdate);
    register(GameModeUpdatePacket.class, this::handleGameModeUpdate);
    register(PongPacket.class, this::handlePong);
    register(DisconnectPacket.class, this::handleDisconnect);
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

  private void handleDisconnect(DisconnectPacket packet) {
    String reason = packet.getReason();
    ConnectionLostScene scene = new ConnectionLostScene(client, reason);
    client.getSceneManager().setActiveScene(scene);
    System.out.println("Disconnect");
  }

  private void handlePong(PongPacket packet) {
    client.getPingTracker().update(packet.getTime());
  }

  private void handleTimeUpdate(TimeUpdatePacket packet) {
    client.getWorld().setWorldTime(packet.getWorldTime());
  }

  private void handleSlotClear(PlayerSlotClearPacket packet) {
    client.getPlayer().getInventory().setSlot(packet.getSlotIndex(), null);
  }

  private void handleActionBar(ActionBarPacket packet) {
    float duration = packet.getDurationInTicks() / 20f;
    client.getView().getActionBarView().display(packet.getText(), duration);
  }

  // --- Specific Handler Logic ---

  private void handleChunkData(ChunkDataPacket packet) {
    short[] blocks = packet.decompress();
    client.getWorld().applyChunkData(packet.getChunkX(), packet.getChunkZ(), blocks);
  }

  private void handleBlockUpdate(BlockUpdatePacket packet) {
    client
        .getWorld()
        .onServerBlockUpdate(packet.getX(), packet.getY(), packet.getZ(), packet.getBlockId());
  }

  private void handleChatMessage(ChatMessagePacket packet) {
    ChatMessage message = new ChatMessage(packet.getMessage());
    client.getView().getChatView().addMessage(message);
  }

  private void handleSoundEffect(SoundEffectPacket packet) {
    SoundManager.playEffect(packet.getSoundEffectId());
  }

  private void handlePlayerPosition(PlayerPositionPacket packet) {
    UUID id = packet.getPlayerUuid();

    if (id.equals(client.getPlayer().getUuid())) {
      ClientPlayer localPlayer = client.getPlayer();

      float serverX = (float) packet.getX();
      float serverY = (float) packet.getY();
      float serverZ = (float) packet.getZ();

      float dx = serverX - localPlayer.getPosition().x;
      float dy = serverY - localPlayer.getPosition().y;
      float dz = serverZ - localPlayer.getPosition().z;

      float distanceSq = dx * dx + dy * dy + dz * dz;

      float tolerance = 4f;

      if (packet.isTeleport()) {
        localPlayer.setPositionFromTeleport(serverX, serverY, serverZ);

      } else if (distanceSq > tolerance * tolerance) {

        localPlayer.setPosition(serverX, serverY, serverZ);

        localPlayer.setYaw(packet.getYaw());
        localPlayer.setPitch(packet.getPitch());
      }
    } else {
      client
          .getEntityManager()
          .updateRemotePlayer(
              id,
              (float) packet.getX(),
              (float) packet.getY(),
              (float) packet.getZ(),
              packet.getYaw(),
              packet.getPitch());
    }
  }

  private void handlePlayerSpawn(PlayerSpawnPacket packet) {
    client
        .getEntityManager()
        .addRemotePlayer(
            packet.getUuid(), packet.getName(), packet.getX(), packet.getY(), packet.getZ());
  }

  private void handleItemSpawn(ItemSpawnPacket packet) {
    // Add the item to the client world
    client
        .getEntityManager()
        .spawnItem(
            packet.getEntityId(),
            packet.getBlockType(),
            packet.getX(),
            packet.getY(),
            packet.getZ(),
            packet.getVelX(),
            packet.getVelY(),
            packet.getVelZ());
  }

  private void handleEntityDestroy(EntityDestroyPacket packet) {
    client.getEntityManager().removeItem(packet.getEntityId());
  }

  private void handlePlayerQuit(PlayerQuitPacket packet) {
    client.getEntityManager().removeRemotePlayer(packet.getUuid());
  }

  private void handleUpdateSlot(UpdateSlotPacket packet) {}

  private void handleTitle(TitlePacket packet) {
    float ticksPerSecond = 20f;
    float fadeInTime = packet.getFadeInTicks() / ticksPerSecond;
    float stayTime = packet.getStayTicks() / ticksPerSecond;
    float fadeOutTime = packet.getFadeOutTicks() / ticksPerSecond;
    String titleText = packet.getTitle();
    String subtitleText = packet.getSubtitle();
    Title title = new Title(titleText, subtitleText, fadeInTime, stayTime, fadeOutTime);
    client.getView().getTitleView().displayTitle(title);
  }

  private void handleSlotUpdate(PlayerSlotUpdatePacket packet) {
    Inventory inventory = client.getPlayer().getInventory();
    ItemStack itemStack = new ItemStack(packet.getItemId(), packet.getAmount());
    inventory.setSlot(packet.getSlotIndex(), itemStack);
  }

  private void handleInventoryFullUpdate(PlayerInventoryFullUpdatePacket packet) {
    Inventory inventory = client.getPlayer().getInventory();
    inventory.setItems(packet.getItems());

    int inventoryVersion = packet.getInventoryVersion();
    client.getView().getInventoryView().setInventoryVersion(inventoryVersion);
    client.getView().getInventoryView().setCursorStack(packet.getCursorStack());
  }

  private void handleGameModeUpdate(GameModeUpdatePacket packet) {
    GameMode gameMode = packet.getGameMode();
    client.getPlayer().setGameMode(gameMode);
  }
}
