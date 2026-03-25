package server.usecases.blockplace;

import server.usecases.blockplace.BlockPlace.BlockPlaceResponse;

public class BlockPlacePresenter implements BlockPlaceResponse {

  private BlockPlaceView view;

  public BlockPlacePresenter(BlockPlaceView view) {
    this.view = view;
  }

  @Override
  public void onBlockPlaced(int x, int y, int z, short id) {
    //    view.displayBlockUpdate(x, y, z, id);
    view.broadcastBlockUpdate(x, y, z, id);
    view.displayBlockPlacedEffect();
  }

  @Override
  public void onNoPermission(int x, int y, int z, short oldId) {
    view.displayBlockUpdate(x, y, z, oldId);
    view.displayErrorMessage("You don't have permission to place blocks here.");
  }

  @Override
  public void onCancelled(int x, int y, int z, short id) {
    view.displayBlockUpdate(x, y, z, id);
  }

  @Override
  public void onNoItemMatch() {
    view.displayErrorMessage("No cheat! Item does not match your inventory!");
  }
}
