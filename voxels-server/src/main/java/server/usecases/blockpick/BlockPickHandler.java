package server.usecases.blockpick;

import java.util.UUID;

import common.game.Hotbar;
import common.game.ItemStack;
import common.game.block.BlockType;
import common.game.block.Blocks;
import common.logging.Log;
import common.network.packets.ActionBarPacket;
import common.network.packets.BlockPickPacket;
import common.network.packets.ChatMessagePacket;
import common.network.packets.PlayerSlotClearPacket;
import common.network.packets.PlayerSlotUpdatePacket;
import common.player.ability.Ability;
import common.player.attribute.Attribute;
import common.player.interaction.Reach;
import math.Vector3f;
import server.events.events.BlockPickEvent;
import server.gateways.EventGateway;
import server.gateways.GatewayContext;
import server.gateways.InventoryGateway;
import server.gateways.PermissionGateway;
import server.gateways.WorldGateway;
import server.network.ServerConnection;
import server.permissions.Permissions;
import server.player.ServerPlayer;

public class BlockPickHandler {

  private final ServerConnection connection;

  private final EventGateway events;

  private final PermissionGateway permissions;

  private final InventoryGateway inventory;

  private final WorldGateway world;

  public BlockPickHandler(ServerConnection connection, GatewayContext context) {
    this.connection = connection;
    this.permissions = context.permissions();
    this.inventory = context.inventory();
    this.world = context.world();
    this.events = context.events();
  }

  private boolean hasPermission(UUID uuid, String permission) {
    return permissions.hasPermission(uuid, permission);
  }

  private void resync(BlockPickPacket packet) {
    int slotIndex = packet.getSelectedSlot();
    if (slotIndex < 0 || slotIndex >= Hotbar.SIZE) {
      Log.warn("block-pick.resync.invalid-slot | slot=" + slotIndex);
      return;
    }
    ItemStack itemStack = inventory.getItem(connection.getPlayer().getUuid(), slotIndex);
    if (itemStack == null) {
      connection.send(new PlayerSlotClearPacket(slotIndex));
    } else {
      connection.send(
          new PlayerSlotUpdatePacket(slotIndex, itemStack.getItemId(), itemStack.getAmount()));
    }
  }

  private void failAndResync(BlockPickPacket packet) {
    resync(packet);
  }

  public void handle(BlockPickPacket packet) {
    ServerPlayer player = connection.getPlayer();

    if (player == null) {
      // TODO Log
      return;
    }

    UUID playerId = player.getUuid();

    // -------------------------------------
    // SLOT VALIDATION / pick only via HOTBAR
    // -------------------------------------
    int selectedSlot = packet.getSelectedSlot();
    if (selectedSlot < 0 || selectedSlot >= Hotbar.SIZE) {
      Log.warn("block-pick.invalid-slot | playerId=" + playerId + " slot=" + selectedSlot);
      return;
    }

    // -------------------------------------
    // ABILITY CHECK - AKA GAMEMODE
    // -------------------------------------
    if (!player.getAbilities().has(Ability.PICK_BLOCKS)) {
      connection.send(new ChatMessagePacket(BlockPickMessages.NO_ABILITY));
      failAndResync(packet);
      return;
    }

    // -------------------------------------
    // PERMISSION CHECK
    // -------------------------------------
    if (!hasPermission(playerId, Permissions.BLOCK_PICK)) {
      connection.send(new ChatMessagePacket(BlockPickMessages.NO_PERMISSION));
      failAndResync(packet);
      return;
    }

    // -------------------------------------
    // SELECT AND VALIDATE BLOCK
    // -------------------------------------
    int x = packet.getX();
    int y = packet.getY();
    int z = packet.getZ();

    // -------------------------------------
    // BLOCK TYPE VALIDATION
    // -------------------------------------
    // Important! Block check before reach
    BlockType blockType = world.getBlockType(x, y, z);
    if (blockType == null) {
      Log.error("block-pick.null-block | playerId=" + playerId + " x=" + x + " y=" + y + " z=" + z);
      failAndResync(packet);
      return;
    }

    if (blockType == Blocks.AIR) {
      Log.debug(
          "block-pick.air | playerId="
              + playerId
              + " slot="
              + selectedSlot
              + " x="
              + x
              + " y="
              + y
              + " z="
              + z);
      failAndResync(packet);
      return;
    }

    short blockId = blockType.getId();

    // -------------------------------------
    // REACH DISTANCE CHECK
    // -------------------------------------
    float maxReachDistance = player.getAttributes().get(Attribute.REACH_DISTANCE);
    float eyeHeight = player.getAttributes().get(Attribute.EYE_HEIGHT);
    Vector3f position = player.getPosition();
    float dist = Reach.distanceToBlock(position, eyeHeight, x, y, z);

    if (dist > maxReachDistance) {
      Log.warn(
          "block-pick.out-of-reach | playerId="
              + playerId
              + " x="
              + x
              + " y="
              + y
              + " z="
              + z
              + " dist="
              + dist
              + " maxReach="
              + maxReachDistance
              + " pos="
              + position);
      failAndResync(packet);
      return;
    }

    // -------------------------------------
    // EVENT
    // -------------------------------------
    BlockPickEvent event = new BlockPickEvent(playerId, x, y, z, blockId, selectedSlot);
    events.fire(event);

    if (event.isCancelled()) {
      Log.debug(
          "block-pick.cancelled | playerId="
              + playerId
              + " block="
              + blockId
              + " slot="
              + selectedSlot);
      failAndResync(packet);
      return;
    }

    // -------------------------------------
    // APPLY
    // -------------------------------------
    inventory.setItem(playerId, selectedSlot, blockId, 1);

    // -------------------------------------
    // RESPONSE
    // -------------------------------------
    int durationInTicks = BlockPickMessages.PICKED_MESSAGE_DURATION_TICKS;
    //    connection.send(new SoundEffectPacket(SoundEffect.BLOCK_PICK));
    connection.send(new PlayerSlotUpdatePacket(selectedSlot, blockId, 1));
    connection.send(
        new ActionBarPacket(
            BlockPickMessages.PICKED_MESSAGE.replace("%blockType%", blockType.getName()),
            durationInTicks));
    Log.debug(
        "block-pick.success | playerId="
            + playerId
            + " block="
            + blockId
            + " slot="
            + selectedSlot
            + " x="
            + x
            + " y="
            + y
            + " z="
            + z);
  }
}
