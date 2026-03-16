package server.usecases.blockbreak;

import server.usecases.blockbreak.BlockBreak.BlockBreakResponse;

public class BlockBreakPresenter implements BlockBreakResponse {
  private final BlockBreakView view;

  public BlockBreakPresenter(BlockBreakView view) {
    this.view = view;
  }

  @Override
  public void onBlockBroken(int x, int y, int z, short newId) {
    view.displayBlockUpdate(x, y, z, newId);
    view.displayBlockBrokenEffect();
  }

  @Override
  public void onCancelled(int x, int y, int z, short oldId) {
    view.displayBlockUpdate(x, y, z, oldId);
  }

  @Override
  public void onCannotBreakBedrock(int x, int y, int z, short oldId) {
    view.displayBlockUpdate(x, y, z, oldId);
    view.displayErrorMessage("This block is indestructible!");
  }

  @Override
  public void onTooFarAway(int x, int y, int z, short oldId) {
    view.displayBlockUpdate(x, y, z, oldId);
    view.displayErrorMessage("Block is out of reach!");
  }

  @Override
  public void onNoPermission(int x, int y, int z, short oldId) {
    view.displayBlockUpdate(x, y, z, oldId);
    view.displayErrorMessage("You don't have permission to break this.");
  }
}
