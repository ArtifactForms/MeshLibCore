package server.usecases.startmining;

import java.util.UUID;

import common.world.BlockFace;

public interface StartMining {

  void execute(StartMiningRequest request, StartMiningResponse response);

  interface StartMiningRequest {

    UUID getPlayerId();

    int getX();

    int getY();

    int getZ();

    int getSelectedSlot();

    BlockFace getBlockFace();

    float getPitch();

    float getYaw();
  }

  interface StartMiningResponse {

    void onInvalidSlot(int slot);
  }
}
