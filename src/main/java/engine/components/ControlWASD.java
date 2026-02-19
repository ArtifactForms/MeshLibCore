package engine.components;

import engine.input.Input;
import engine.input.Key;
import engine.scene.SceneNode;
import math.Vector3f;

/**
 * ControlWASD is an input-driven movement component that translates keyboard input (W/A/S/D by
 * default) into directional movement for a {@link SceneNode}.
 *
 * <p>This component can operate in two distinct modes:
 *
 * <ul>
 *   <li><b>Standalone Mode:</b> If no {@link MovementInputConsumer} is assigned, the component
 *       directly modifies the owning node's transform, applying movement in world space using a
 *       configurable speed multiplier.
 *   <li><b>Input Provider Mode:</b> If a {@link MovementInputConsumer} is set, this component does
 *       not move the node directly. Instead, it forwards normalized directional input and jump
 *       requests to the assigned consumer, allowing external systems (e.g. physics-based character
 *       controllers) to handle movement simulation.
 * </ul>
 *
 * <p>Movement input is normalized to ensure consistent speed, even when moving diagonally.
 * Frame-rate independence is achieved by scaling movement with the elapsed time per frame (tpf).
 *
 * <p>This design cleanly separates input handling from movement simulation, enabling flexible
 * architectures where input, physics, and character logic are decoupled.
 *
 * <p>Example usage:
 *
 * <pre>
 *   ControlWASD control = new ControlWASD(input, 5f);
 *   node.addComponent(control);
 *
 *   // Optional: forward input to a character controller
 *   control.setMovementInputConsumer(characterController);
 * </pre>
 *
 * <p><b>Important:</b> When operating in input provider mode (i.e. when a {@link
 * MovementInputConsumer} is assigned), the {@code speed} property of this component may be ignored.
 * In this case, the consumer is fully responsible for interpreting movement input and determining
 * the final movement speed, acceleration, and physics behavior.
 *
 * <p>The {@code speed} value only affects movement when this component directly modifies the
 * transform (standalone mode).
 */
public class ControlWASD extends AbstractComponent {

  /** The speed multiplier determines how fast the node moves per second. */
  private float speed;

  /** The Input instance to monitor keyboard events (injected dependency). */
  private Input input;

  /** Key used for moving left. Default is 'A'. */
  private Key leftKey = Key.A;

  /** Key used for moving right. Default is 'D'. */
  private Key rightKey = Key.D;

  /** Key used for moving forward. Default is 'W'. */
  private Key forwardKey = Key.W;

  /** Key used for moving backward. Default is 'S'. */
  private Key backwardKey = Key.S;

  /** Key used for jumping. Default is 'SPACE' */
  private Key jumpKey = Key.SPACE;

  /**
   * Optional movement input consumer that receives normalized directional input and jump requests.
   *
   * <p>If set, this component will no longer apply movement directly to the owning {@link
   * SceneNode}. Instead, all movement input is forwarded to the provided {@link
   * MovementInputConsumer}, allowing external systems (e.g. a physics-based character controller)
   * to process input and perform movement simulation.
   *
   * <p>If null, this component falls back to default transform-based movement.
   */
  private MovementInputConsumer movementInputConsumer;

  /**
   * Constructs a new ControlWASD component, injecting the Input logic needed for detecting key
   * presses.
   *
   * @param input The Input system used to check for keyboard input events. Must not be null.
   * @throws IllegalArgumentException if input is null.
   */
  public ControlWASD(Input input) {
    this(input, 1);
  }

  /**
   * Constructs a new ControlWASD component, injecting the Input logic needed for detecting key
   * presses and allowing a custom movement speed multiplier.
   *
   * <p>This constructor allows developers to specify custom speeds for different movement styles,
   * debugging scenarios, or game mechanics adjustments.
   *
   * @param input The Input system used to monitor keyboard input events. Must not be null.
   * @param speed The speed multiplier determining how fast the node moves per second. Must be
   *     non-negative.
   * @throws IllegalArgumentException if input is null or speed is less than 0.
   */
  public ControlWASD(Input input, float speed) {
    if (input == null) {
      throw new IllegalArgumentException("Input cannot be null.");
    }
    if (speed < 0) {
      throw new IllegalArgumentException("Speed must be non-negative.");
    }
    this.input = input;
    this.speed = speed;
  }

  /**
   * Updates the movement of the owning node based on keyboard input.
   *
   * <p>This method calculates the velocity vector by processing the input keys and applies it to
   * move the node smoothly in the 3D space by adjusting its position over time with respect to
   * `tpf` (time per frame).
   *
   * @param tpf Time per frame, used to ensure frame-rate-independent movement.
   */
  public void onUpdate(float tpf) {

    SceneNode node = getOwner();
    Vector3f direction = handleInput();

    if (movementInputConsumer != null) {

      if (direction.lengthSquared() > 0f) {
        movementInputConsumer.addMovementInput(direction);
      }

      if (input.isKeyPressed(jumpKey)) {
        movementInputConsumer.jump();
      }

      return;
    }

    // Default Movement (no Controller)
    if (direction.lengthSquared() > 0f) {
      Vector3f velocity = direction.mult(speed);
      Transform transform = node.getTransform();
      Vector3f position = transform.getPosition();
      transform.setPosition(position.add(velocity.mult(tpf)));
    }
  }

  /**
   * Processes keyboard input to determine velocity. Handles the configured keys to allow movement
   * in the 3D plane. Normalizes the vector to ensure diagonal movement doesn't lead to faster
   * speeds.
   *
   * @return A velocity vector representing the computed movement direction and speed.
   */
  private Vector3f handleInput() {
    Vector3f direction = new Vector3f();

    if (input.isKeyPressed(forwardKey)) direction.addLocal(0, 0, -1);
    if (input.isKeyPressed(leftKey)) direction.addLocal(-1, 0, 0);
    if (input.isKeyPressed(backwardKey)) direction.addLocal(0, 0, 1);
    if (input.isKeyPressed(rightKey)) direction.addLocal(1, 0, 0);

    if (direction.lengthSquared() > 0f) {
      direction.normalizeLocal();
    }

    return direction;
  }

  /**
   * Maps the movement keys to the arrow keys (Up, Down, Left, Right).
   *
   * <p>This method provides an alternative to the default WASD configuration, allowing movement to
   * be controlled using the arrow keys. Specifically:
   *
   * <ul>
   *   <li>Up Arrow - Moves forward
   *   <li>Down Arrow - Moves backward
   *   <li>Left Arrow - Moves left
   *   <li>Right Arrow - Moves right
   * </ul>
   */
  public void mapArrowKeys() {
    leftKey = Key.ARROW_LEFT;
    rightKey = Key.ARROW_RIGHT;
    forwardKey = Key.ARROW_UP;
    backwardKey = Key.ARROW_DOWN;
  }

  /**
   * Maps the movement keys to the WASD configuration (W, A, S, D).
   *
   * <p>This method restores the default key mapping for movement, commonly used in many games:
   *
   * <ul>
   *   <li>W - Moves forward
   *   <li>A - Moves left
   *   <li>S - Moves backward
   *   <li>D - Moves right
   * </ul>
   */
  public void mapWASDKeys() {
    leftKey = Key.A;
    rightKey = Key.D;
    forwardKey = Key.W;
    backwardKey = Key.S;
  }

  /**
   * Retrieves the current movement speed multiplier.
   *
   * @return The current speed value.
   */
  public float getSpeed() {
    return speed;
  }

  /**
   * Sets the movement speed multiplier for controlling how fast the node moves through the scene.
   *
   * <p>A speed of 1.0 represents the default unit speed. Increasing this value will make the node
   * move faster, while decreasing it will slow it down. Movement speed is scaled by elapsed time
   * per frame to ensure frame-rate-independent movement.
   *
   * @param speed The new speed value to set. Must be a non-negative number.
   * @throws IllegalArgumentException if the speed is less than 0.
   */
  public void setSpeed(float speed) {
    if (speed < 0) {
      throw new IllegalArgumentException("Speed must be non-negative.");
    }
    this.speed = speed;
  }

  /**
   * Sets the key used to move left.
   *
   * @param leftKey The new key to use for left movement.
   */
  public void setLeftKey(Key leftKey) {
    this.leftKey = leftKey;
  }

  /**
   * Sets the key used to move right.
   *
   * @param rightKey The new key to use for right movement.
   */
  public void setRightKey(Key rightKey) {
    this.rightKey = rightKey;
  }

  /**
   * Sets the key used to move forward.
   *
   * @param forwardKey The new key to use for forward movement.
   */
  public void setForwardKey(Key forwardKey) {
    this.forwardKey = forwardKey;
  }

  /** Sets the key used to jump. */
  public void setJumpKey(Key jumpKey) {
    this.jumpKey = jumpKey;
  }

  /**
   * Sets the {@link MovementInputConsumer} that should handle movement input.
   *
   * <p>When a consumer is assigned, this component switches from direct transform-based movement to
   * an input-provider mode. Movement directions are normalized and forwarded via {@link
   * MovementInputConsumer#addMovementInput(math.Vector3f)}, and jump requests are forwarded via
   * {@link MovementInputConsumer#jump()}.
   *
   * <p>Passing {@code null} disables input forwarding and restores the default behavior of directly
   * modifying the owning node's transform.
   *
   * @param movementInputConsumer The movement consumer responsible for handling movement input, or
   *     {@code null} to use default movement.
   */
  public void setMovementInputConsumer(MovementInputConsumer movementInputConsumer) {
    this.movementInputConsumer = movementInputConsumer;
  }

  /**
   * Sets the key used to move backward.
   *
   * @param backwardKey The new key to use for backward movement.
   */
  public void setBackwardKey(Key backwardKey) {
    this.backwardKey = backwardKey;
  }

  /** Called when the component is attached to a {@link SceneNode}. Override for custom behavior. */
  @Override
  public void onAttach() {}

  /**
   * Called when the component is detached from a {@link SceneNode}. Override for custom behavior.
   */
  @Override
  public void onDetach() {}
}
