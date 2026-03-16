package servers.views;

import common.network.packets.BlockUpdatePacket;
import common.network.packets.ChatMessagePacket;
import common.network.packets.SoundEffectPacket;
import common.world.SoundEffect;
import server.network.ServerConnection;
import server.usecases.blockbreak.BlockBreakView;

public class BlockBreakViewImpl implements BlockBreakView {

  private final ServerConnection connection;

  public BlockBreakViewImpl(ServerConnection connection) {
    this.connection = connection;
  }

  @Override
  public void displayBlockUpdate(int x, int y, int z, short id) {
    connection.send(new BlockUpdatePacket(x, y, z, id));
  }

  @Override
  public void displayErrorMessage(String message) {
    connection.send(new ChatMessagePacket("§c" + message));
  }

  @Override
  public void displaySuccessMessage(String message) {
    connection.send(new ChatMessagePacket("§a" + message));
  }

  @Override
  public void displayBlockBrokenEffect() {
    connection.send(new SoundEffectPacket(SoundEffect.BLOCK_BREAK));
  }
}
