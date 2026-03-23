package client.usecases.interact;

import client.ray.RaycastResult;
import client.rendering.BlockHighlightRenderer;
import common.interaction.BlockTarget;
import common.interaction.InteractionTarget;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.Input;

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

    BlockHighlightRenderer.render(g, block.x, block.y, block.z);
  }
  
  private boolean isGameplayBlocked() {
	  return getOwner().getScene().getTopScreen().blocksGameplay();
  }
}