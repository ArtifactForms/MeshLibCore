package server.network.validation;

import common.world.BlockType;
import server.network.GameServer;
import server.player.ServerPlayer;

public class MaterialValidator implements BlockActionValidator {
  public boolean isValid(ServerPlayer player, int x, int y, int z) {
    short id = GameServer.getWorld().getBlockId(x, y, z);
    return BlockType.fromId(id) != BlockType.BEDROCK;
  }
}
