package common.network.packets;

import java.io.IOException;

import common.game.GameMode;
import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;
import common.network.ServerToClientPacket;

public class GameModeUpdatePacket implements Packet, ServerToClientPacket {

  private GameMode gameMode;

  public GameModeUpdatePacket() {}

  public GameModeUpdatePacket(GameMode gameMode) {
    this.gameMode = gameMode;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    if (gameMode == null) {
      throw new IOException("GameMode cannot be null");
    }
    out.writeByte((byte) gameMode.getId());
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    int id = in.readByte() & 0xFF;

    GameMode mode = GameMode.fromId(id);
    if (mode == null) {
      throw new IOException("Invalid GameMode id: " + id);
    }

    this.gameMode = mode;
  }

  @Override
  public int getId() {
    return PacketIds.SYNC_GAMEMODE;
  }

  public GameMode getGameMode() {
    return gameMode;
  }
}
