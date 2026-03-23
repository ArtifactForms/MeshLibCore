package common.network;

import common.network.packets.ActionBarPacket;
import common.network.packets.BlockBreakPacket;
import common.network.packets.BlockPickPacket;
import common.network.packets.BlockPlacePacket;
import common.network.packets.BlockUpdatePacket;
import common.network.packets.ChatMessagePacket;
import common.network.packets.ChunkDataPacket;
import common.network.packets.EntityDestroyPacket;
import common.network.packets.InventoryActionPacket;
import common.network.packets.ItemPickupPacket;
import common.network.packets.ItemSpawnPacket;
import common.network.packets.PlayerDropItemPacket;
import common.network.packets.PlayerInventoryFullUpdatePacket;
import common.network.packets.PlayerJoinPacket;
import common.network.packets.PlayerMovePacket;
import common.network.packets.PlayerOpenInventoryPacket;
import common.network.packets.PlayerPositionPacket;
import common.network.packets.PlayerQuitPacket;
import common.network.packets.PlayerSlotClearPacket;
import common.network.packets.PlayerSlotUpdatePacket;
import common.network.packets.PlayerSpawnPacket;
import common.network.packets.SoundEffectPacket;
import common.network.packets.TimeUpdatePacket;
import common.network.packets.TitlePacket;
import common.network.packets.UpdateSlotPacket;

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
    PacketRegistry.register(PlayerSlotClearPacket::new);

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

    // World & Time
    PacketRegistry.register(TimeUpdatePacket::new);
  }
}
