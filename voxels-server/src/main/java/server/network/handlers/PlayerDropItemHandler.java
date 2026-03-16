package server.network.handlers;

import common.entity.ItemEntity;
import common.game.ItemStack;
import common.game.block.BlockRegistry;
import common.game.block.BlockType;
import common.network.packets.ItemSpawnPacket;
import common.network.packets.PlayerDropItemPacket;
import server.network.GameServer;
import server.network.ServerConnection;
import server.player.ServerPlayer;

public class PlayerDropItemHandler {

  private final ServerConnection connection;

  public PlayerDropItemHandler(ServerConnection connection) {
    this.connection = connection;
  }

  public void handle(PlayerDropItemPacket packet) {

    ServerPlayer player = connection.getPlayer();
    if (player == null) return;

    int slot = packet.getSlot();

    ItemStack stack = player.getInventory().removeOne(slot);

    if (stack == null) return;

    spawnDroppedItem(player, stack);
  }

  private void spawnDroppedItem(ServerPlayer player, ItemStack stack) {
    //	  Log.debug("Item spawn");

    BlockType type = BlockRegistry.get(stack.getItemId());

    GameServer server = connection.getServer();

    long id = server.getEntityManager().createEntityId();

    ItemEntity drop = new ItemEntity(id, type, player.getPosition());

    //	  item.applyDropImpulse(player.getLookDirection());

    server.getEntityManager().addEntity(drop);
    connection.getServer().getPlayerManager().broadcast(new ItemSpawnPacket(drop));
  }
}
