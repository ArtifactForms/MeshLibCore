package client.player;

import client.app.ApplicationContext;
import client.ray.RaycastResult;
import client.ray.Raycasting;
import client.rendering.BlockHighlightRenderer;
import common.game.ItemStack;
import common.network.packets.BlockBreakPacket;
import common.network.packets.BlockPlacePacket;
import common.world.World;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.Input;
import engine.scene.camera.Camera;

public class BlockInteractionComponent extends AbstractComponent implements RenderableComponent {

  private boolean lastPressedLeft;
  private boolean lastPressedRight;

  private final Input input;
  private RaycastResult lastHit = RaycastResult.miss();

  public BlockInteractionComponent(Input input) {
    this.input = input;
  }

  @Override
  public void update(float tpf) {

    Camera camera = getOwner().getScene().getActiveCamera();
    World world = ApplicationContext.clientWorld;

    // Always update the current raycast hit
    lastHit = Raycasting.raycast(camera, world);

    boolean pressedLeft = input.isMousePressed(Input.LEFT);
    boolean pressedRight = input.isMousePressed(Input.RIGHT);

    if (lastHit.hit) {

      // Break block on left mouse release
      if (lastPressedLeft && !pressedLeft) {
        ApplicationContext.network.send(
            new BlockBreakPacket(lastHit.blockX, lastHit.blockY, lastHit.blockZ));
      }

      // Place block on right mouse release
      if (lastPressedRight && !pressedRight) {
    	ItemStack itemStack = ApplicationContext.hotbar.getSelected();
    	if (itemStack == null)
    		return;
    	short id = (short) itemStack.getItemId();
    	ApplicationContext.network.send(
            new BlockPlacePacket(
                lastHit.placeX, lastHit.placeY, lastHit.placeZ, id));
      }
    }

    lastPressedLeft = pressedLeft;
    lastPressedRight = pressedRight;
  }

  @Override
  public void render(Graphics g) {
    if (!lastHit.hit) return;

    int rx = lastHit.blockX;
    int ry = lastHit.blockY;
    int rz = lastHit.blockZ;

    BlockHighlightRenderer.render(g, rx, ry, rz);
  }
}
