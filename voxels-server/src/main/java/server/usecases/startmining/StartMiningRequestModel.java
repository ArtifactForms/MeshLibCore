package server.usecases.startmining;

import java.util.UUID;

import common.world.BlockFace;
import server.usecases.startmining.StartMining.StartMiningRequest;

public class StartMiningRequestModel implements StartMiningRequest {

  private final UUID playerId;

  private final int x;

  private final int y;

  private final int z;

  private final int selectedSlot;

  private BlockFace blockFace;

  private final float pitch;

  private final float yaw;

  public StartMiningRequestModel(
      UUID playerId,
      int x,
      int y,
      int z,
      int selectedSlot,
      BlockFace blockFace,
      float pitch,
      float yaw) {
    this.playerId = playerId;
    this.x = x;
    this.y = y;
    this.z = z;
    this.selectedSlot = selectedSlot;
    this.blockFace = blockFace;
    this.pitch = pitch;
    this.yaw = yaw;
  }

  @Override
  public UUID getPlayerId() {
    return playerId;
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
  public int getSelectedSlot() {
    return selectedSlot;
  }

  @Override
  public BlockFace getBlockFace() {
    return blockFace;
  }

  @Override
  public float getPitch() {
    return pitch;
  }

  @Override
  public float getYaw() {
    return yaw;
  }
}
