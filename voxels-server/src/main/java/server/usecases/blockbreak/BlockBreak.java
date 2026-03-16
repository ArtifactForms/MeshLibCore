package server.usecases.blockbreak;

import java.util.UUID;

public interface BlockBreak {

  void execute(BlockBreakRequest request, BlockBreakResponse response);

  interface BlockBreakRequest {

    UUID getPlayer();

    int getX();

    int getY();

    int getZ();

    float getPlayerX();

    float getPlayerY();

    float getPlayerZ();
  }

  interface BlockBreakResponse {

    void onBlockBroken(int x, int y, int z, short newId);

    void onCancelled(int x, int y, int z, short oldId);

    void onCannotBreakBedrock(int x, int y, int z, short oldId);

    void onTooFarAway(int x, int y, int z, short oldId);

    void onNoPermission(int x, int y, int z, short oldId);
  }
}
