package client.usecases.interact;

import client.app.GameClient;
import common.game.Hotbar;
import common.game.ItemStack;
import common.interaction.BlockTarget;
import common.interaction.InteractionTarget;
import common.network.packets.BlockBreakPacket;
import common.network.packets.BlockPlacePacket;

public class InteractionController {

  private final GameClient client;

  public InteractionController(GameClient client) {
    this.client = client;
  }
  
  public void pickTarget(InteractionTarget target) {
	    if (!(target instanceof BlockTarget block)) return;

		 short id = client.getWorld().getBlock(block.x, block.y, block.z).getId();
	  
	  Hotbar hotbar = client.getView().getHotbarView().getModel();
	  int selectedSlot = hotbar.getSelectedSlot();
	  hotbar.setSlot(selectedSlot, new ItemStack(id, 0));
  }

  public void breakTarget(InteractionTarget target) {

    if (target instanceof BlockTarget block) {
      breakBlock(block);
    }
  }

  public void placeTarget(InteractionTarget target) {

    if (!(target instanceof BlockTarget block)) return;

    Hotbar hotbar = client.getView().getHotbarView().getModel();
    ItemStack item = hotbar.getSelected();
    int selectedSlot = hotbar.getSelectedSlot();

    if (item == null) return;

    short id = (short) item.getItemId();

    client.getNetwork().send(
        new BlockPlacePacket(
            block.placeX,
            block.placeY,
            block.placeZ,
            selectedSlot,
            id));
  }

  private void breakBlock(BlockTarget block) {

    client.getNetwork().send(
        new BlockBreakPacket(
            block.x,
            block.y,
            block.z));
  }
}