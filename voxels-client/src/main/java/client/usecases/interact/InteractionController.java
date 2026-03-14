package client.usecases.interact;

import client.app.GameClient;
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

  public void breakTarget(InteractionTarget target) {

    if (target instanceof BlockTarget block) {
      breakBlock(block);
    }
  }

  public void placeTarget(InteractionTarget target) {

    if (!(target instanceof BlockTarget block)) return;

    ItemStack item = client
        .getView()
        .getHotbarView()
        .getModel()
        .getSelected();

    if (item == null) return;

    short id = (short) item.getItemId();

    client.getNetwork().send(
        new BlockPlacePacket(
            block.placeX,
            block.placeY,
            block.placeZ,
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