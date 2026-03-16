package server.usecases.blockplace;

import java.util.UUID;

public interface BlockPlace {

  void execute(BlockPlaceRequest request, BlockPlaceResponse response);

  interface BlockPlaceRequest {

    UUID getPlayer();

    int getX();

    int getY();

    int getZ();
    
    int getSelectedSlot();

    float getPlayerX();

    float getPlayerY();

    float getPlayerZ();

    short getBlockId();
  }

  interface BlockPlaceResponse {

    void onBlockPlaced(int x, int y, int z, short id);

    void onNoPermission(int x, int y, int z, short oldId);

    void onCancelled(int x, int y, int z, short id);
    
    void onNoItemMatch();
  }
}
