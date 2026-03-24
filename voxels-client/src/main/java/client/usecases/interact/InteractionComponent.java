package client.usecases.interact;

import client.ray.RaycastResult;
import client.rendering.BlockHighlightRenderer;
import common.interaction.BlockTarget;
import common.interaction.InteractionTarget;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.Input;
import engine.scene.CameraMode;
import math.Vector3f;

public class InteractionComponent extends AbstractComponent implements RenderableComponent {

  private final Input input;
  private final InteractionController controller;
  private final TargetingService targeting;

  private InteractionTarget currentTarget;

  public InteractionComponent(Input input,
                              InteractionController controller,
                              TargetingService targeting) {
    this.input = input;
    this.controller = controller;
    this.targeting = targeting;
  }

  @Override
  public void update(float tpf) {
	  if (isGameplayBlocked()) {
		  return;
	  }
	 
    RaycastResult result = targeting.raycast(getOwner());
    currentTarget = result.target;

    if (currentTarget == null) {
      return;
    }

    if (input.isMousePressed(Input.LEFT)) {
      controller.breakTarget(currentTarget);
    }

    if (input.isMousePressed(Input.RIGHT)) {
      controller.placeTarget(currentTarget);
    }

    if (input.isMousePressed(Input.CENTER)) {
      controller.pickTarget(currentTarget);
    }
  }

  @Override
  public void render(Graphics g) {

    if (!(currentTarget instanceof BlockTarget block)) {
      return;
    }

    Vector3f camPos = getOwner()
        .getScene()
        .getActiveCamera()
        .getTransform()
        .getPosition();

    g.pushMatrix();

    float x = block.x;
    float y = -block.y;
    float z = block.z;

    if (getOwner().getScene().getCameraMode() == CameraMode.CAMERA_RELATIVE) {
      g.translate(
          x - camPos.x,
          y - camPos.y,
          z - camPos.z
      );
    } else {
      g.translate(x, y, z);
    }

    BlockHighlightRenderer.render(g);

    g.popMatrix();
  }
  
  private boolean isGameplayBlocked() {
	  return getOwner().getScene().getTopScreen().blocksGameplay();
  }
}