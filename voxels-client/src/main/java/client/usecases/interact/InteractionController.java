package client.usecases.interact;

import client.app.GameClient;
import common.game.GameMode;
import common.game.Hotbar;
import common.game.ItemStack;
import common.interaction.BlockTarget;
import common.interaction.InteractionTarget;
import common.network.packets.BlockBreakPacket;
import common.network.packets.BlockPickPacket;
import common.network.packets.BlockPlacePacket;
import common.network.packets.StartMiningPacket;
import common.player.PlayerData;

public class InteractionController {

  private final GameClient client;

  public InteractionController(GameClient client) {
    this.client = client;
  }
  
  public void pickTarget(InteractionTarget target) {
	  if (!(target instanceof BlockTarget block)) return;

	  Hotbar hotbar = client.getView().getHotbarView().getModel();
	  int selectedSlot = hotbar.getSelectedSlot();
	  
	  client.getNetwork().send(new BlockPickPacket(block.x, block.y, block.z, selectedSlot));
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
  
  public void startMining(InteractionTarget target) {
	  
	  if (!(target instanceof BlockTarget)) return;
	  
	  Hotbar hotbar = client.getView().getHotbarView().getModel();
	  int selectedSlot = hotbar.getSelectedSlot();
	  BlockTarget block = (BlockTarget) target;
	  PlayerData player = client.getPlayer();
	  
	  client.getNetwork().send(
	    new StartMiningPacket(
		    block.x, 
		    block.y, 
		    block.z, 
		    selectedSlot, 
		    block.face, 
		    player.getPitch(), 
		    player.getYaw()));
  }
  
  public void stopMining() {
	  
  }
  
  public float getBreakTime(BlockTarget block) {
	  PlayerData player = client.getPlayer();  
	  
	    if (player.getGameMode() == GameMode.CREATIVE) {
	        return 0.0f;
	    }
	    // Später: return block.getHardness(); 
	    // Blumen/Fackeln hätten hier 0.0f, Stein z.B. 1.5f
	    return 0.5f; 
	}

  private void breakBlock(BlockTarget block) {

    client.getNetwork().send(
        new BlockBreakPacket(
            block.x,
            block.y,
            block.z));
  }
}