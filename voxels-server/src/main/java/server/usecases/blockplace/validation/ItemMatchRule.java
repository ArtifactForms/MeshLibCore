package server.usecases.blockplace.validation;

import server.gateways.InventoryGateway;
import server.usecases.blockplace.BlockPlace.BlockPlaceRequest;
import server.usecases.blockplace.BlockPlace.BlockPlaceResponse;

public class ItemMatchRule implements BlockPlaceRule {

  private final InventoryGateway inventoryGateway;

  public ItemMatchRule(InventoryGateway inventoryGateway) {
    this.inventoryGateway = inventoryGateway;
  }

  @Override
  public boolean validate(
      BlockPlaceRequest request, BlockPlaceResponse response, short currentBlockId) {
    boolean match =
        inventoryGateway.isItemInSlot(
            request.getPlayer(), request.getSelectedSlot(), request.getBlockId());

    if (!match) {
      response.onNoItemMatch();
      response.onCancelled(request.getX(), request.getY(), request.getZ(), currentBlockId);
      return false;
    }

    return true;
  }
}
