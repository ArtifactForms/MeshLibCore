package demos.collision;

import engine.components.AbstractComponent;
import engine.components.MovementInputConsumer;
import engine.physics.PhysicsQuerySystem;
import engine.physics.SweepResult;
import engine.physics.collision.component.ColliderComponent;
import math.Vector3f;

public class CharacterControllerComponent4Stable extends AbstractComponent
    implements MovementInputConsumer {

  private final ColliderComponent collider;

  private final Vector3f velocity = new Vector3f();
  private final Vector3f pendingInput = new Vector3f();

  private float moveSpeed = 6f;
  private float gravity = 18.0f; // Etwas höher für besseres Gefühl
  private float jumpSpeed = 7f;

  private float skinWidth = 0.005f;
  private int maxIterations = 4;

  private float stepHeight = 0.4f; // Leicht erhöht
  private float slopeLimitCos = 0.7f;

  private boolean grounded = false;

  // 🌍 Orientierung: -Y ist OBEN
  private static final Vector3f WORLD_UP = new Vector3f(0, -1, 0);
  private static final Vector3f WORLD_DOWN = new Vector3f(0, 1, 0);

  public CharacterControllerComponent4Stable(ColliderComponent collider) {
    this.collider = collider;
  }

  @Override
  public void addMovementInput(Vector3f direction) {
    pendingInput.addLocal(direction);
  }

  @Override
  public void jump() {
    if (grounded) {
      velocity.y = -jumpSpeed; // -Y ist Up
      grounded = false;
    }
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

    // Schwerkraft (+Y ist runter)
    velocity.addLocal(WORLD_DOWN.mult(gravity * tpf));

    performMovement(physics, tpf);

    grounded = checkGrounded(physics);
    if (grounded && velocity.y > 0) {
      velocity.y = 0;
    }
  }

  private void performMovement(PhysicsQuerySystem physics, float tpf) {
    Vector3f totalMove = velocity.mult(tpf);
    Vector3f horizontalMove = new Vector3f(totalMove.x, 0, totalMove.z);

    // 1. Zuerst die vertikale Bewegung (Gravitation) separat behandeln
    // Das verhindert, dass die Fallgeschwindigkeit den Step-Up stört
    Vector3f verticalMove = new Vector3f(0, totalMove.y, 0);
    moveWithSliding(physics, verticalMove);

    // 2. Jetzt die horizontale Bewegung
    if (horizontalMove.lengthSquared() > 1e-7f) {
      SweepResult hit = physics.sweepCapsule(collider, horizontalMove);

      if (hit.hasHit()) {
        // Wir stoßen gegen etwas. Versuche Step-Up!
        if (grounded && tryStepUp(physics, horizontalMove)) {
          // Erfolg: Der Charakter wurde in tryStepUp bereits bewegt.
          return;
        }
      }

      // Wenn kein Step möglich war oder wir nicht am Boden sind: Normal sliden
      moveWithSliding(physics, horizontalMove);
    }
  }

  private boolean tryStepUp(PhysicsQuerySystem physics, Vector3f horizontal) {
    Vector3f startPos = new Vector3f(getOwner().getTransform().getPosition());

    // SCHRITT 1: Nach oben teleportieren (Testweise)
    // Wir nutzen direkt die stepHeight
    getOwner().getTransform().translate(WORLD_UP.mult(stepHeight));

    // SCHRITT 2: Horizontal bewegen (während wir "oben" sind)
    SweepResult fwHit = physics.sweepCapsule(collider, horizontal);
    float moveDist = fwHit.hasHit() ? Math.max(0, fwHit.getTOI() - skinWidth) : 1.0f;
    getOwner().getTransform().translate(horizontal.mult(moveDist));

    // SCHRITT 3: Wieder runter auf die Stufe/Boden
    // Wir casten etwas weiter runter als wir hochgegangen sind
    float dropDist = stepHeight + 0.1f;
    SweepResult downHit = physics.sweepCapsule(collider, WORLD_DOWN.mult(dropDist));

    if (downHit.hasHit()) {
      float actualDown = downHit.getTOI() * dropDist;
      getOwner().getTransform().translate(WORLD_DOWN.mult(actualDown - skinWidth));

      // Prüfen, ob wir auf einer begehbaren Fläche gelandet sind
      if (downHit.getNormal().dot(WORLD_UP) > slopeLimitCos) {
        return true; // Treppe erfolgreich bestiegen
      }
    }

    // Wenn wir hier landen, war der Step ungültig (z.B. zu steil oder blockiert)
    getOwner().getTransform().setPosition(startPos);
    return false;
  }

  private void moveWithSliding(PhysicsQuerySystem physics, Vector3f remaining) {
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

      float dot = remaining.dot(normal);
      if (dot < 0) remaining.subtractLocal(normal.mult(dot));

      float vDot = velocity.dot(normal);
      if (vDot < 0) velocity.subtractLocal(normal.mult(vDot));
    }
  }

  private boolean checkGrounded(PhysicsQuerySystem physics) {
    SweepResult hit = physics.sweepCapsule(collider, WORLD_DOWN.mult(0.1f));
    if (hit.hasHit()) {
      // Check ob die Normale gegen WORLD_UP (0, -1, 0) zeigt
      return hit.getNormal().dot(WORLD_UP) > slopeLimitCos;
    }
    return false;
  }
}
