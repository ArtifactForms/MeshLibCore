package server.usecases.blockplace;

import java.util.UUID;

import server.usecases.blockplace.BlockPlace.BlockPlaceRequest;

public class BlockPlaceRequestModel implements BlockPlaceRequest {
  private final UUID player;
  private final int x;
  private final int y;
  private final int z;
  private final short blockId;
  private final float playerX;
  private final float playerY;
  private final float playerZ;
  private final int selectedSlot;

  public BlockPlaceRequestModel(
      UUID player,
      int x,
      int y,
      int z,
      short blockId,
      float px,
      float py,
      float pz,
      int selectedSlot) {
    this.player = player;
    this.x = x;
    this.y = y;
    this.z = z;
    this.blockId = blockId;
    this.playerX = px;
    this.playerY = py;
    this.playerZ = pz;
    this.selectedSlot = selectedSlot;
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
  public short getBlockId() {
    return blockId;
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

  @Override
  public int getSelectedSlot() {
    return selectedSlot;
  }
}
