package common.network;

import common.network.packets.*;

/**
 * Utility class to initialize the global packet protocol. This must be called on both Client and
 * Server before network communication starts.
 */
public class NetworkPackets {

  /**
   * Registers all available packets into the PacketRegistry. The IDs are automatically pulled from
   * the packet classes.
   */
  public static void register() {
    // Player & Movement
    PacketRegistry.register(PlayerMovePacket::new);
    PacketRegistry.register(PlayerJoinPacket::new);
    PacketRegistry.register(PlayerSpawnPacket::new);
    PacketRegistry.register(PlayerPositionPacket::new);
    PacketRegistry.register(PlayerQuitPacket::new);
    PacketRegistry.register(PlayerOpenInventoryPacket::new);
    PacketRegistry.register(PlayerDropItemPacket::new);
    
    // Inventory
    PacketRegistry.register(PlayerSlotUpdatePacket::new);
    PacketRegistry.register(PlayerInventoryFullUpdatePacket::new);
    PacketRegistry.register(InventoryActionPacket::new);

    // World & Blocks
    PacketRegistry.register(BlockUpdatePacket::new);
    PacketRegistry.register(ChunkDataPacket::new);
    PacketRegistry.register(BlockPlacePacket::new);
    PacketRegistry.register(BlockBreakPacket::new);
    PacketRegistry.register(BlockPickPacket::new);

    // Communication & Effects
    PacketRegistry.register(ChatMessagePacket::new);
    //    PacketRegistry.register(PrivateMessagePacket::new);
    PacketRegistry.register(SoundEffectPacket::new);

    // Items
    PacketRegistry.register(ItemPickupPacket::new);
    PacketRegistry.register(ItemSpawnPacket::new);

    PacketRegistry.register(UpdateSlotPacket::new);

    // Entities
    PacketRegistry.register(EntityDestroyPacket::new);

    // HUD
    PacketRegistry.register(ActionBarPacket::new);
    PacketRegistry.register(TitlePacket::new);
  }
}
