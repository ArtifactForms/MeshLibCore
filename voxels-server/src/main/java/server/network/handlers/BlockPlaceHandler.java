package server.network.handlers;

import common.network.packets.BlockPlacePacket;
import common.network.packets.BlockUpdatePacket;
import common.network.packets.SoundEffectPacket;
import common.world.SoundEffect;
import server.events.events.BlockPlaceEvent;
import server.network.ServerConnection;
import server.player.ServerPlayer;

public class BlockPlaceHandler {

  private final ServerConnection connection;

  public BlockPlaceHandler(ServerConnection connection) {
    this.connection = connection;
  }

  public void handle(BlockPlacePacket packet) {

	  ServerPlayer player = connection.getPlayer();
	  if (player == null) return;

	  var server = connection.getServer();

	  BlockPlaceEvent event =
	      new BlockPlaceEvent(
	          player,
	          packet.getX(),
	          packet.getY(),
	          packet.getZ(),
	          packet.getBlockId());

	  server.getEventBus().fire(event);

	  if (event.isCancelled()) {
	    return;
	  }

	  server.getWorld()
	        .setBlock(event.getX(), event.getY(), event.getZ(), event.getBlockId());

	  connection.send(new SoundEffectPacket(SoundEffect.BLOCK_PLACE));

	  server.getPlayerManager().broadcast(
	      new BlockUpdatePacket(
	          event.getX(),
	          event.getY(),
	          event.getZ(),
	          event.getBlockId()));
	}
}
