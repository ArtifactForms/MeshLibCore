package demos.collision;

import engine.components.AbstractComponent;
import engine.components.MovementInputConsumer;
import engine.physics.PhysicsQuerySystem;
import engine.physics.SweepResult;
import engine.physics.collision.component.ColliderComponent;
import math.Vector3f;

public class CharacterControllerComponent5Stable extends AbstractComponent
    implements MovementInputConsumer {

  private final ColliderComponent collider;
  private final Vector3f velocity = new Vector3f();
  private final Vector3f pendingInput = new Vector3f();

  private float moveSpeed = 6f;
  private float gravity = 18.0f;
  private float jumpSpeed = 7f;
  private float skinWidth = 0.005f;
  private int maxIterations = 4;
  private float stepHeight = 0.4f;
  private float slopeLimitCos = 0.7f;
  private boolean grounded = false;

  private static final Vector3f WORLD_UP = new Vector3f(0, -1, 0);
  private static final Vector3f WORLD_DOWN = new Vector3f(0, 1, 0);

  public CharacterControllerComponent5Stable(ColliderComponent collider) {
    this.collider = collider;
  }

  @Override
  public void addMovementInput(Vector3f direction) {
    pendingInput.addLocal(direction);
  }

  @Override
  public void onUpdate(float tpf) {
    PhysicsQuerySystem physics = getOwner().getScene().getSystem(PhysicsQuerySystem.class);
    if (physics == null) return;

    if (pendingInput.lengthSquared() > 0f) {
      Vector3f wishDir = pendingInput.normalize();
      velocity.x = wishDir.x * moveSpeed;
      velocity.z = wishDir.z * moveSpeed;
    } else {
      velocity.x = 0;
      velocity.z = 0;
    }
    pendingInput.set(0, 0, 0);

    // Schwerkraft (+Y)
    velocity.addLocal(WORLD_DOWN.mult(gravity * tpf));

    performMovement(physics, tpf);

    grounded = checkGrounded(physics);
    if (grounded && velocity.y > 0) {
      velocity.y = 0;
    }
  }

  private void performMovement(PhysicsQuerySystem physics, float tpf) {
    Vector3f totalMove = velocity.mult(tpf);

    // 1. VERTIKALER PASS (Gravitation/Sprung)
    // Wir isolieren die vertikale Bewegung komplett
    Vector3f verticalMove = new Vector3f(0, totalMove.y, 0);
    moveWithSliding(physics, verticalMove, true);

    // 2. HORIZONTALER PASS (Laufen)
    Vector3f horizontalMove = new Vector3f(totalMove.x, 0, totalMove.z);
    if (horizontalMove.lengthSquared() > 1e-7f) {
      // Step-Up Check
      if (grounded) {
        SweepResult hit = physics.sweepCapsule(collider, horizontalMove);
        if (hit.hasHit() && tryStepUp(physics, horizontalMove)) {
          return;
        }
      }
      // Horizontales Gleiten (isVerticalPass = false)
      moveWithSliding(physics, horizontalMove, false);
    }
  }

  // ... (Rest bleibt gleich)

  @Override
  public void jump() {
    // Nur springen, wenn am Boden UND der Kopf nicht gegen eine Decke stößt
    if (grounded) {
      PhysicsQuerySystem physics = getOwner().getScene().getSystem(PhysicsQuerySystem.class);
      if (physics != null) {
        // Kurzer Check nach OBEN (-Y)
        SweepResult ceilingHit = physics.sweepCapsule(collider, WORLD_UP.mult(0.05f));
        if (!ceilingHit.hasHit()) {
          velocity.y = -jumpSpeed; // -Y ist Up
          grounded = false;
        }
      }
    }
  }

  private void moveWithSliding(
      PhysicsQuerySystem physics, Vector3f remaining, boolean isVerticalPass) {
    for (int i = 0; i < maxIterations; i++) {
      if (remaining.lengthSquared() < 1e-8f) break;

      SweepResult hit = physics.sweepCapsule(collider, remaining);
      if (!hit.hasHit()) {
        getOwner().getTransform().translate(remaining);
        break;
      }

      float safeTOI = Math.max(0f, hit.getTOI() - skinWidth);
      getOwner().getTransform().translate(remaining.mult(safeTOI));

      Vector3f normal = hit.getNormal();
      remaining = remaining.mult(1f - safeTOI);

      // Decken-Check: Normale zeigt nach unten (+Y)
      boolean isCeiling = normal.dot(WORLD_DOWN) > 0.7f;

      // --- HEAD BUMP ---
      if (isCeiling && velocity.y < 0) {
        velocity.y = 0;
        remaining.y = 0; // Wichtig: Auch den Restweg für diesen Frame killen
      }

      // (Ab hier die restliche Sliding-Logik wie zuvor...)
      if (!isVerticalPass) {
        normal.y = 0;
        if (normal.lengthSquared() > 1e-6f) normal.normalizeLocal();
      }

      float dot = remaining.dot(normal);
      if (dot < 0) remaining.subtractLocal(normal.mult(dot));

      float vDot = velocity.dot(normal);
      if (vDot < 0) {
        if (!isVerticalPass) {
          velocity.x -= normal.x * vDot;
          velocity.z -= normal.z * vDot;
        } else {
          velocity.subtractLocal(normal.mult(vDot));
        }
      }
    }
  }

  private boolean tryStepUp(PhysicsQuerySystem physics, Vector3f horizontal) {
    Vector3f startPos = new Vector3f(getOwner().getTransform().getPosition());
    getOwner().getTransform().translate(WORLD_UP.mult(stepHeight));

    SweepResult fwHit = physics.sweepCapsule(collider, horizontal);
    float moveDist = fwHit.hasHit() ? Math.max(0, fwHit.getTOI() - skinWidth) : 1.0f;
    getOwner().getTransform().translate(horizontal.mult(moveDist));

    float dropDist = stepHeight + 0.1f;
    SweepResult downHit = physics.sweepCapsule(collider, WORLD_DOWN.mult(dropDist));

    if (downHit.hasHit()) {
      float actualDown = downHit.getTOI() * dropDist;
      getOwner().getTransform().translate(WORLD_DOWN.mult(actualDown - skinWidth));
      if (downHit.getNormal().dot(WORLD_UP) > slopeLimitCos) {
        return true;
      }
    }

    getOwner().getTransform().setPosition(startPos);
    return false;
  }

  private boolean checkGrounded(PhysicsQuerySystem physics) {
    SweepResult hit = physics.sweepCapsule(collider, WORLD_DOWN.mult(0.1f));
    if (hit.hasHit()) {
      return hit.getNormal().dot(WORLD_UP) > slopeLimitCos;
    }
    return false;
  }
}
