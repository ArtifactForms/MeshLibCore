package client.player;

import client.app.ApplicationContext;
import client.ray.RaycastResult;
import client.ray.Raycasting;
import client.rendering.BlockHighlightRenderer;
import common.game.ItemStack;
import common.interaction.BlockTarget;
import common.interaction.InteractionTarget;
import common.network.packets.BlockBreakPacket;
import common.network.packets.BlockPlacePacket;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.Input;
import engine.scene.camera.Camera;

public class BlockInteractionComponent extends AbstractComponent implements RenderableComponent {

  private final Input input;

  private boolean lastPressedLeft;
  private boolean lastPressedRight;

  private InteractionTarget currentTarget;

  public BlockInteractionComponent(Input input) {
    this.input = input;
  }

  @Override
  public void update(float tpf) {

    RaycastResult result = raycast();
    currentTarget = result.target;

    boolean left = input.isMousePressed(Input.LEFT);
    boolean right = input.isMousePressed(Input.RIGHT);

    if (currentTarget == null) {
      updateLastState(left, right);
      return;
    }

    if (released(lastPressedLeft, left)) {
      handleBreak(currentTarget);
    }

    if (released(lastPressedRight, right)) {
      handlePlace(currentTarget);
    }

    updateLastState(left, right);
  }

  private void handleBreak(InteractionTarget target) {

    if (target instanceof BlockTarget block) {

      ApplicationContext.network.send(
          new BlockBreakPacket(block.x, block.y, block.z));
    }
  }

  private void handlePlace(InteractionTarget target) {

    if (!(target instanceof BlockTarget block)) return;

    ItemStack item = ApplicationContext.hotbar.getSelected();
    if (item == null) return;

    short id = (short) item.getItemId();

    ApplicationContext.network.send(
        new BlockPlacePacket(block.placeX, block.placeY, block.placeZ, id));
  }

  private RaycastResult raycast() {

    Camera camera = getOwner().getScene().getActiveCamera();

    switch (ApplicationContext.raycastMode) {

      case CROSS_HAIR:
        return Raycasting.raycastCrossHair(camera, 6);

      case CURSOR:
        return Raycasting.raycastScreenPoint(camera, input, 1000);

      default:
        return RaycastResult.miss();
    }
  }

  private boolean released(boolean last, boolean current) {
    return last && !current;
  }

  private void updateLastState(boolean left, boolean right) {
    lastPressedLeft = left;
    lastPressedRight = right;
  }

  @Override
  public void render(Graphics g) {

    if (!(currentTarget instanceof BlockTarget block)) return;

    BlockHighlightRenderer.render(g, block.x, block.y, block.z);
  }
}