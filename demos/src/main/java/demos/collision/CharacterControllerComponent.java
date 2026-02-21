package demos.collision;

import engine.components.AbstractComponent;
import engine.components.MovementInputConsumer;
import engine.physics.PhysicsQuerySystem;
import engine.physics.SweepResult;
import engine.physics.collision.component.ColliderComponent;
import engine.scene.SceneNode;
import math.Vector3f;

/**
 * A kinematic character controller handling movement, gravity, stepping, and platform adhesion in a
 * -Y = Up coordinate system.
 */
public class CharacterControllerComponent extends AbstractComponent
    implements MovementInputConsumer {

  private final ColliderComponent collider;
  private final Vector3f velocity = new Vector3f();
  private final Vector3f pendingInput = new Vector3f();

  // --- Platform Handling ---
  private SceneNode groundNode = null;
  private final Vector3f lastGroundPosition = new Vector3f();

  // --- Configuration ---
  private float moveSpeed = 6f;
  private float gravity = 18.0f;
  private float jumpSpeed = 7f;
  private float skinWidth = 0.005f;
  private int maxIterations = 5;
  private float stepHeight = 0.4f;
  private float slopeLimitCos = 0.7f;
  private boolean grounded = false;

  private static final Vector3f WORLD_UP = new Vector3f(0, -1, 0);
  private static final Vector3f WORLD_DOWN = new Vector3f(0, 1, 0);

  public CharacterControllerComponent(ColliderComponent collider) {
    this.collider = collider;
  }

  @Override
  public void addMovementInput(Vector3f direction) {
    pendingInput.addLocal(direction);
  }

  @Override
  public void jump() {
    if (grounded) {
      PhysicsQuerySystem physics = getOwner().getScene().getSystem(PhysicsQuerySystem.class);
      if (physics != null) {
        // Check if there is space above before jumping
        SweepResult ceilingHit = physics.sweepCapsule(collider, WORLD_UP.mult(0.05f));
        if (!ceilingHit.hasHit()) {
          velocity.y = -jumpSpeed; // Negative Y is UP
          grounded = false;
          groundNode = null;
        }
      }
    }
  }

  @Override
  public void onUpdate(float tpf) {
    PhysicsQuerySystem physics = getOwner().getScene().getSystem(PhysicsQuerySystem.class);
    if (physics == null) return;

    // 1. ADHESION: Move with the platform before processing our own velocity
    handlePlatformMovement();

    // 2. INPUT PROCESSING
    if (pendingInput.lengthSquared() > 0f) {
      Vector3f wishDir = pendingInput.normalize();
      velocity.x = wishDir.x * moveSpeed;
      velocity.z = wishDir.z * moveSpeed;
    } else {
      velocity.x = 0;
      velocity.z = 0;
    }
    pendingInput.set(0, 0, 0);

    // 3. APPLY GRAVITY (Positive Y is DOWN)
    velocity.addLocal(WORLD_DOWN.mult(gravity * tpf));

    // 4. PERFORM PHYSICS MOVEMENT
    performMovement(physics, tpf);

    // 5. UPDATE STATE
    updateGroundedState(physics);

    // Snap vertical velocity to zero if grounded and trying to move downwards
    if (grounded && velocity.y > 0) {
      velocity.y = 0;
    }
  }

  /** Drags the player along with the platform they are standing on. */
  private void handlePlatformMovement() {
    if (grounded && groundNode != null) {
      Vector3f currentGroundPos = groundNode.getTransform().getPosition();

      // Calculate how much the platform shifted since last frame
      Vector3f platformDelta = currentGroundPos.subtract(lastGroundPosition);

      if (platformDelta.lengthSquared() > 1e-8f) {
        // Apply the same displacement to the player
        getOwner().getTransform().translate(platformDelta);
      }

      // Update tracking for the next frame
      lastGroundPosition.set(currentGroundPos);
    }
  }

  private void performMovement(PhysicsQuerySystem physics, float tpf) {
    Vector3f totalMove = velocity.mult(tpf);

    // A. Vertical Pass (Gravity/Jumping)
    Vector3f verticalMove = new Vector3f(0, totalMove.y, 0);
    moveWithSliding(physics, verticalMove, true);

    // B. Horizontal Pass (Walking)
    Vector3f horizontalMove = new Vector3f(totalMove.x, 0, totalMove.z);
    if (horizontalMove.lengthSquared() > 1e-7f) {
      if (grounded) {
        SweepResult hit = physics.sweepCapsule(collider, horizontalMove);
        if (hit.hasHit() && tryStepUp(physics, horizontalMove)) {
          return;
        }
      }
      moveWithSliding(physics, horizontalMove, false);
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

      // Move just shy of the collision point
      float safeTOI = Math.max(0f, hit.getTOI() - skinWidth);
      getOwner().getTransform().translate(remaining.mult(safeTOI));

      Vector3f normal = hit.getNormal();
      remaining = remaining.mult(1f - safeTOI);

      // Handle head bumps (ceiling collisions)
      boolean isCeiling = normal.dot(WORLD_DOWN) > 0.7f;
      if (isCeiling && velocity.y < 0) {
        velocity.y = 0;
        remaining.y = 0;
      }

      // Projection/Sliding logic
      if (!isVerticalPass) {
        normal.y = 0; // Prevent walls from pushing us up/down during horizontal movement
        if (normal.lengthSquared() > 1e-6f) normal.normalizeLocal();
      }

      float dot = remaining.dot(normal);
      if (dot < 0) remaining.subtractLocal(normal.mult(dot));

      float vDot = velocity.dot(normal);
      if (vDot < 0) {
        velocity.subtractLocal(normal.mult(vDot));
      }
    }
  }

  private boolean tryStepUp(PhysicsQuerySystem physics, Vector3f horizontal) {
    Vector3f startPos = new Vector3f(getOwner().getTransform().getPosition());

    // 1. Lift up
    getOwner().getTransform().translate(WORLD_UP.mult(stepHeight));

    // 2. Move forward
    SweepResult fwHit = physics.sweepCapsule(collider, horizontal);
    float moveDist = fwHit.hasHit() ? Math.max(0, fwHit.getTOI() - skinWidth) : 1.0f;
    getOwner().getTransform().translate(horizontal.mult(moveDist));

    // 3. Drop down
    float dropDist = stepHeight + 0.1f;
    SweepResult downHit = physics.sweepCapsule(collider, WORLD_DOWN.mult(dropDist));

    if (downHit.hasHit()) {
      float actualDown = downHit.getTOI() * dropDist;
      getOwner().getTransform().translate(WORLD_DOWN.mult(actualDown - skinWidth));

      // Validate the surface we landed on
      if (downHit.getNormal().dot(WORLD_UP) > slopeLimitCos) {
        return true;
      }
    }

    // Reset if step failed
    getOwner().getTransform().setPosition(startPos);
    return false;
  }

  private void updateGroundedState(PhysicsQuerySystem physics) {
    // Cast a short ray downwards to find the floor
    SweepResult hit = physics.sweepCapsule(collider, WORLD_DOWN.mult(0.1f));

    if (hit.hasHit() && hit.getNormal().dot(WORLD_UP) > slopeLimitCos) {
      grounded = true;
      SceneNode newGround = hit.getCollider().getOwner();

      // Update platform tracking if we land on something new
      if (newGround != groundNode) {
        groundNode = newGround;
        lastGroundPosition.set(groundNode.getTransform().getPosition());
      }
    } else {
      grounded = false;
      groundNode = null;
    }
  }

  public boolean isGrounded() {
    return grounded;
  }
}
