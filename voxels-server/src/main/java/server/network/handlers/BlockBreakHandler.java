package server.network.handlers;

import java.util.List;

import common.game.ReachDistance;
import common.game.block.BlockRegistry;
import common.game.block.BlockType;
import common.game.block.Blocks;
import common.network.packets.BlockBreakPacket;
import common.network.packets.BlockUpdatePacket;
import common.network.packets.SoundEffectPacket;
import common.world.SoundEffect;
import server.events.events.BlockBreakEvent;
import server.network.GameServer;
import server.network.PlayerManager;
import server.network.ServerConnection;
import server.network.validation.BlockActionValidator;
import server.network.validation.DistanceValidator;
import server.network.validation.MaterialValidator;
import server.player.ServerPlayer;

public class BlockBreakHandler {

  private final ServerConnection connection;

  private static final List<BlockActionValidator> RULES =
      List.<BlockActionValidator>of(
          new DistanceValidator(ReachDistance.VALUE + 0.5f), new MaterialValidator());

  public BlockBreakHandler(ServerConnection connection) {
    this.connection = connection;
  }

  public void handle(BlockBreakPacket packet) {
    ServerPlayer player = connection.getPlayer();
    if (player == null) return;

    int x = packet.getX();
    int y = packet.getY();
    int z = packet.getZ();

    // Hard-Validation (Reach, Bedrock, etc.)
    for (BlockActionValidator rule : RULES) {
      if (!rule.isValid(player, x, y, z)) {
        // send block back to client (Re-sync)
        resyncBlock(x, y, z);
        return;
      }
    }

    // Get the block type before it's gone
    short oldBlockId =
        GameServer.getWorld().getBlock(packet.getX(), packet.getY(), packet.getZ()).getId();

    BlockType oldType = BlockRegistry.get(oldBlockId);

    // Event
    BlockBreakEvent event =
        new BlockBreakEvent(player, packet.getX(), packet.getY(), packet.getZ());

    GameServer.getEventBus().fire(event);

    if (event.isCancelled()) {
      return;
    }

    // Bedrock protect
    if (oldType == Blocks.BEDROCK) {
      return;
    }

    GameServer.getWorld().setBlock(event.getX(), event.getY(), event.getZ(), Blocks.AIR.getId());

    //    // 3. SPAWN THE ITEM
    //    if (oldType != BlockType.AIR) {
    //      // Position the item in the center of the block
    //      Vector3f spawnPos =
    //          new Vector3f(event.getX() + 0.5f, event.getY() + 0.5f, event.getZ() + 0.5f);
    //
    //      // Generate a unique ID (you'll need an ID generator or use a timestamp)
    //      long entityId = System.currentTimeMillis();
    //
    //      ItemEntity drop = new ItemEntity(entityId, oldType, spawnPos);
    //
    //      // Give it a little "pop" velocity so it doesn't just sit there
    //      drop.getVelocity().y = 0.15f;
    //      drop.getVelocity().x = (float) (Math.random() * 0.1f - 0.05f);
    //      drop.getVelocity().z = (float) (Math.random() * 0.1f - 0.05f);
    //
    //      // Add to server tracking and notify clients
    //      EntityManager.addEntity(drop);
    //      PlayerManager.broadcast(new ItemSpawnPacket(drop));
    //    }

    // 4. Notify everyone about the block and sound
    connection.send(new SoundEffectPacket(SoundEffect.BLOCK_BREAK));
    PlayerManager.broadcast(
        new BlockUpdatePacket(event.getX(), event.getY(), event.getZ(), Blocks.AIR.getId()));
  }

  private void resyncBlock(int x, int y, int z) {
    short currentId = GameServer.getWorld().getBlock(x, y, z).getId();
    connection.send(new BlockUpdatePacket(x, y, z, currentId));
  }
}
