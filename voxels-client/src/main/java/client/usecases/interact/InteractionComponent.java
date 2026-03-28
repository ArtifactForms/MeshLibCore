package client.usecases.interact;

import client.ray.RaycastResult;
import client.rendering.BlockCrackRenderer;
import client.rendering.BlockHighlightRenderer;
import common.interaction.BlockTarget;
import common.interaction.InteractionTarget;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.rendering.Graphics;
import engine.runtime.input.Input;
import engine.scene.CameraMode;
import engine.scene.screen.GameScreen;
import math.Vector3f;

public class InteractionComponent extends AbstractComponent implements RenderableComponent {

  private final Input input;
  private final InteractionController controller;
  private final TargetingService targeting;

  private InteractionTarget currentTarget;

  private float miningTimer = 0.0f;
  private InteractionTarget lastTarget;

  public InteractionComponent(Input input,
                              InteractionController controller,
                              TargetingService targeting) {
    this.input = input;
    this.controller = controller;
    this.targeting = targeting;
  }

  public void update(float tpf) {
    if (isGameplayBlocked()) return;

    RaycastResult result = targeting.raycast(getOwner());
    currentTarget = result.target;
    
    // --- RIGHT CLICK: place block ---
    if (input.isMousePressed(Input.RIGHT)) {
      controller.placeTarget(currentTarget);
    }

    // --- MIDDLE CLICK: pick block ---
    if (input.isMousePressed(Input.CENTER)) {
      controller.pickTarget(currentTarget);
    }

    // --- RESET: stop mining if target changed or mouse released ---
    if (miningTimer > 0 &&
        (currentTarget == null ||
         !currentTarget.equals(lastTarget) ||
         !input.isMouseDown(Input.LEFT))) {

      controller.stopMining();
      miningTimer = 0.0f;
    }

    if (currentTarget == null) {
      lastTarget = null;
      return;
    }

    // --- BLOCK INTERACTION ---
    if (currentTarget instanceof BlockTarget block) {

      float breakTime = controller.getBreakTime(block);

      // ============================================
      // 🔥 INSTANT BREAK (e.g. creative mode)
      // ============================================
      if (breakTime <= 0) {
        if (input.isMousePressed(Input.LEFT)) {
          controller.breakTarget(currentTarget);
        }

        // Ensure no mining state persists
        miningTimer = 0.0f;
        lastTarget = null;
        return;
      }

      // ============================================
      // ⛏️ NORMAL MINING (hold to break)
      // ============================================
      if (input.isMouseDown(Input.LEFT)) {

        // Start mining only once
        if (miningTimer == 0.0f) {
          controller.startMining(block);
        }

        miningTimer += tpf;
        lastTarget = currentTarget;

        // Break block when timer reaches required time
        if (miningTimer >= breakTime) {
          controller.breakTarget(currentTarget);
          miningTimer = 0.0f;
        }
      }
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

    // Render block outline
    BlockHighlightRenderer.render(g, block.type.getShape());

    // Render crack overlay if currently mining
    if (miningTimer > 0) {
      float breakTime = controller.getBreakTime(block);
      float progress = Math.min(miningTimer / breakTime, 1.0f);
      BlockCrackRenderer.render(g, progress);
    }

    g.popMatrix();
  }

  private boolean isGameplayBlocked() {
	GameScreen screen = getOwner().getScene().getTopScreen();
	if (screen == null) {
		return false;
	} else {
	    return screen.blocksGameplay();
	}
  }
}