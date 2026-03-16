package server.usecases.blockbreak;

import java.util.UUID;

import server.usecases.blockbreak.BlockBreak.BlockBreakRequest;

public class BlockBreakRequestModel implements BlockBreakRequest {

  private final UUID player;
  private final int x;
  private final int y;
  private final int z;
  private final float playerX;
  private final float playerY;
  private final float playerZ;

  public BlockBreakRequestModel(
      UUID player, int x, int y, int z, float playerX, float playerY, float playerZ) {
    this.player = player;
    this.x = x;
    this.y = y;
    this.z = z;
    this.playerX = playerX;
    this.playerY = playerY;
    this.playerZ = playerZ;
  }

  @Override
  public UUID getPlayer() {
    return player;
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public int getZ() {
    return z;
  }

  @Override
  public float getPlayerX() {
    return playerX;
  }

  @Override
  public float getPlayerY() {
    return playerY;
  }

  @Override
  public float getPlayerZ() {
    return playerZ;
  }
}
