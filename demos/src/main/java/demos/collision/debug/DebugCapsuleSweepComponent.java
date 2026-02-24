package demos.collision.debug;

import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.physics.PhysicsQueryEvent;
import engine.physics.PhysicsQueryListener;
import engine.physics.PhysicsQuerySystem;
import engine.physics.SweepResult;
import engine.physics.collision.collider.CapsuleCollider;
import engine.physics.collision.component.ColliderComponent;
import engine.rendering.Graphics;
import engine.runtime.debug.core.DebugDraw;
import engine.scene.Scene;
import math.Color;
import math.Vector3f;

/**
 * Visualizes Capsule Sweep queries for debugging purposes. Displays the trajectory, the hit
 * position (TOI), and collision normals.
 */
public class DebugCapsuleSweepComponent extends AbstractComponent implements RenderableComponent {

  @Override
  public void onAttachToScene(Scene scene) {
    PhysicsQuerySystem querySystem = scene.getSystem(PhysicsQuerySystem.class);

    if (querySystem == null) {
      throw new IllegalStateException(
          "DebugCapsuleSweepComponent requires PhysicsQuerySystem to be registered in the Scene.");
    }

    // Listen for capsule sweep events triggered by the physics system
    querySystem.addListener(
        new PhysicsQueryListener() {
          @Override
          public void onSweepCapsule(PhysicsQueryEvent e) {
            if (!getOwner().isActive() || !isActive()) return;
            visualizeSweep(e);
          }
        });
  }

  /** Handles the visualization logic for a single sweep event. */
  private void visualizeSweep(PhysicsQueryEvent e) {
    ColliderComponent source = e.getSource();
    CapsuleCollider collider = (CapsuleCollider) source.getCollider();

    Vector3f delta = e.getDelta();
    SweepResult result = e.getResult();

    Vector3f startPos = source.getWorldPosition();
    Vector3f endPos = startPos.add(delta);

    float radius = collider.getRadius();
    float halfHeight = collider.getHalfHeight();

    // 1. Draw starting capsule state (White)
    DebugDraw.drawCapsule(radius, halfHeight, startPos, Color.WHITE);

    // 2. Draw the path of the sweep (Yellow)
    DebugDraw.drawLine(startPos, endPos, Color.YELLOW);

    if (result.hasHit()) {
      drawHitFeedback(result, startPos, delta, radius, halfHeight);
    } else {
      // 3. Draw destination capsule if no hit occurred (Green)
      DebugDraw.drawCapsule(radius, halfHeight, endPos, Color.GREEN);
    }
  }

  /** Draws specific debug info when a collision is detected during the sweep. */
  private void drawHitFeedback(
      SweepResult result, Vector3f start, Vector3f delta, float r, float hh) {
    // Calculate the capsule position at the exact Time of Impact (TOI)
    Vector3f hitCapsulePos = start.add(delta.mult(result.getTOI()));

    // Draw the capsule at the impact point (Red)
    DebugDraw.drawCapsule(r, hh, hitCapsulePos, Color.RED);

    // Draw the exact contact point
    DebugDraw.drawPoint(result.getPoint(), 3, Color.WHITE);

    // Draw the collision normal (Magenta)
    Vector3f normalEnd = result.getPoint().add(result.getNormal().mult(0.5f));
    DebugDraw.drawLine(result.getPoint(), normalEnd, Color.MAGENTA);
  }

  @Override
  public void render(Graphics g) {
    // Render logic is handled via the static DebugDraw calls during physics events
  }
}
