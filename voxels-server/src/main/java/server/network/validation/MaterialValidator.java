package server.network.validation;

import common.game.block.BlockRegistry;
import common.game.block.BlockType;
import common.game.block.Blocks;
import server.network.GameServer;
import server.player.ServerPlayer;

public class MaterialValidator implements BlockActionValidator {
  public boolean isValid(ServerPlayer player, int x, int y, int z) {
    short id = GameServer.getWorld().getBlock(x, y, z).getId();
    return BlockRegistry.get(id) != Blocks.BEDROCK;
  }
}
