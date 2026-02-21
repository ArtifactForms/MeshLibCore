package demos.collision;

import java.util.ArrayList;
import java.util.List;

import engine.components.AbstractComponent;
import engine.components.MovementInputConsumer;
import engine.physics.PhysicsQuerySystem;
import engine.physics.SweepResult;
import engine.physics.collision.component.ColliderComponent;
import math.Vector3f;

public class CharacterControllerComponent7Coyote extends AbstractComponent
    implements MovementInputConsumer {

  public enum MovementState {
    IDLE,
    WALKING,
    JUMPING,
    FALLING
  }

  private final ColliderComponent collider;
  private final Vector3f velocity = new Vector3f();
  private final Vector3f pendingInput = new Vector3f();
  private final List<CharacterControllerListener> listeners = new ArrayList<>();

  private MovementState currentState = MovementState.IDLE;
  private boolean grounded = false;

  // --- AAA Feel Parameter ---
  private float moveSpeed = 6f;
  private float acceleration = 60f;
  private float groundFriction = 12f; // Stoppt schnell am Boden
  private float airFriction = 1.5f; // Gleitet in der Luft (Momentum)
  private float airControl = 0.3f; // Wie gut man in der Luft lenken kann

  private float gravity = 18.0f;
  private float jumpSpeed = 7f;

  // --- Feature Toggles & Timers ---
  private boolean jumpBufferingEnabled = false;
  private float jumpBufferThreshold = 0.15f;
  private float jumpBufferTimer = 0f;

  private boolean coyoteTimeEnabled = true;
  private float coyoteTimeThreshold = 0.15f;
  private float coyoteTimeTimer = 0f;

  private float skinWidth = 0.005f;
  private int maxIterations = 5;
  private float stepHeight = 0.3f;
  private float slopeLimitCos = 0.7f;

  private static final Vector3f WORLD_UP = new Vector3f(0, -1, 0);
  private static final Vector3f WORLD_DOWN = new Vector3f(0, 1, 0);

  public CharacterControllerComponent7Coyote(ColliderComponent collider) {
    this.collider = collider;
  }

  public void addListener(CharacterControllerListener listener) {
    listeners.add(listener);
  }

  @Override
  public void addMovementInput(Vector3f direction) {
    pendingInput.addLocal(direction);
  }

  @Override
  public void jump() {
    if (jumpBufferingEnabled) {
      jumpBufferTimer = jumpBufferThreshold;
    }

    if (canJumpRightNow()) {
      executeJump();
    }
  }

  private boolean canJumpRightNow() {
    // Normaler Sprung oder Coyote Time Fenster
    return grounded || (coyoteTimeEnabled && coyoteTimeTimer > 0);
  }

  private void executeJump() {
    PhysicsQuerySystem physics = getOwner().getScene().getSystem(PhysicsQuerySystem.class);
    if (physics == null) return;

    SweepResult ceilingHit = physics.sweepCapsule(collider, WORLD_UP.mult(0.05f));
    if (!ceilingHit.hasHit()) {
      velocity.y = -jumpSpeed;
      grounded = false;
      coyoteTimeTimer = 0;
      jumpBufferTimer = 0;
      updateState(MovementState.JUMPING);
      listeners.forEach(CharacterControllerListener::onJump);
    }
  }

  @Override
  public void onUpdate(float tpf) {
    PhysicsQuerySystem physics = getOwner().getScene().getSystem(PhysicsQuerySystem.class);
    if (physics == null) return;

    // 1. Timers aktualisieren
    if (grounded) {
      coyoteTimeTimer = coyoteTimeThreshold;
    } else {
      coyoteTimeTimer -= tpf;
    }
    jumpBufferTimer -= tpf;

    // 2. AAA Movement & Gravity
    handleMovementAAA(tpf);
    velocity.addLocal(WORLD_DOWN.mult(gravity * tpf));

    // 3. Jump Buffer Check (Automatischer Sprung bei Landung)
    if (jumpBufferingEnabled && jumpBufferTimer > 0 && grounded) {
      executeJump();
    }

    // 4. Physics & Movement
    performMovement(physics, tpf);

    // 5. Grounding & State Logic
    boolean wasGrounded = grounded;
    grounded = checkGrounded(physics);

    if (grounded) {
      if (!wasGrounded) {
        listeners.forEach(CharacterControllerListener::onLand);
      }
      if (velocity.y > 0) velocity.y = 0;

      float horizontalSpeedSq = velocity.x * velocity.x + velocity.z * velocity.z;
      updateState(horizontalSpeedSq > 0.1f ? MovementState.WALKING : MovementState.IDLE);
    } else {
      updateState(velocity.y < 0 ? MovementState.JUMPING : MovementState.FALLING);
    }
  }

  private void handleMovementAAA(float tpf) {
    Vector3f wishDir = new Vector3f(pendingInput.x, 0, pendingInput.z);
    if (wishDir.lengthSquared() > 1.0f) wishDir.normalizeLocal();
    pendingInput.set(0, 0, 0);

    float accelRate = grounded ? acceleration : (acceleration * airControl);
    float frictionRate = grounded ? groundFriction : airFriction;

    if (wishDir.lengthSquared() > 0.01f) {
      // Beschleunigen
      float targetX = wishDir.x * moveSpeed;
      float targetZ = wishDir.z * moveSpeed;
      velocity.x = lerp(velocity.x, targetX, accelRate * tpf);
      velocity.z = lerp(velocity.z, targetZ, accelRate * tpf);
    } else {
      // Bremsen (Friction)
      velocity.x = lerp(velocity.x, 0, frictionRate * tpf);
      velocity.z = lerp(velocity.z, 0, frictionRate * tpf);
    }
  }

  private float lerp(float start, float end, float t) {
    return start + Math.min(1.0f, t) * (end - start);
  }

  private void performMovement(PhysicsQuerySystem physics, float tpf) {
    Vector3f totalMove = velocity.mult(tpf);

    // Vertikal
    moveWithSliding(physics, new Vector3f(0, totalMove.y, 0), true);

    // Horizontal
    Vector3f horizontalMove = new Vector3f(totalMove.x, 0, totalMove.z);
    if (horizontalMove.lengthSquared() > 1e-7f) {
      if (grounded && tryStepUp(physics, horizontalMove)) {
        return;
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

      float safeTOI = Math.max(0f, hit.getTOI() - skinWidth);
      getOwner().getTransform().translate(remaining.mult(safeTOI));

      Vector3f normal = hit.getNormal();
      remaining = remaining.mult(1f - safeTOI);

      if (normal.dot(WORLD_DOWN) > 0.7f && velocity.y < 0) { // Head Bump
        velocity.y = 0;
        remaining.y = 0;
      }

      if (!isVerticalPass) {
        normal.y = 0;
        if (normal.lengthSquared() > 1e-6f) normal.normalizeLocal();
      }

      float dot = remaining.dot(normal);
      if (dot < 0) remaining.subtractLocal(normal.mult(dot));

      float vDot = velocity.dot(normal);
      if (vDot < 0) {
        if (!isVerticalPass && Math.abs(normal.dot(WORLD_DOWN)) < 0.1f) {
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

    if (downHit.hasHit() && downHit.getNormal().dot(WORLD_UP) > slopeLimitCos) {
      float actualDown = downHit.getTOI() * dropDist;
      getOwner().getTransform().translate(WORLD_DOWN.mult(actualDown - skinWidth));
      return true;
    }

    getOwner().getTransform().setPosition(startPos);
    return false;
  }

  private void updateState(MovementState newState) {
    if (currentState != newState) {
      currentState = newState;
      listeners.forEach(l -> l.onStateChanged(newState));
    }
  }

  private boolean checkGrounded(PhysicsQuerySystem physics) {
    SweepResult hit = physics.sweepCapsule(collider, WORLD_DOWN.mult(0.1f));
    return hit.hasHit() && hit.getNormal().dot(WORLD_UP) > slopeLimitCos;
  }

  // --- Configuration Getters/Setters ---
  public void setJumpBufferingEnabled(boolean enabled) {
    this.jumpBufferingEnabled = enabled;
  }

  public void setCoyoteTimeEnabled(boolean enabled) {
    this.coyoteTimeEnabled = enabled;
  }

  public MovementState getCurrentState() {
    return currentState;
  }
}
