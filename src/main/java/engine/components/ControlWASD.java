package engine.components;

import engine.input.Input;
import engine.input.Key;
import engine.scene.SceneNode;
import math.Vector3f;

/**
 * ControlWASD is a movement component that allows moving a node in the scene graph using keyboard
 * input (W/A/S/D). It integrates with the scene's transformation system to control position
 * changes, allowing basic 3D navigation in a scene graph.
 *
 * <p>Movement is normalized to ensure consistent speed, even when moving diagonally. This class
 * supports velocity adjustments via the speed multiplier and works by translating the owning node
 * within the 3D world space.
 *
 * <p>Example usage: Attach this component to a {@link SceneNode} to allow movement using WASD
 * keyboard inputs. The position updates depend on the elapsed time per frame to ensure smooth and
 * consistent movement over varying frame rates.
 */
public class ControlWASD extends AbstractComponent {

  /** The speed multiplier determines how fast the node moves per second. */
  private float speed;

  /** The Input instance to monitor keyboard events (injected dependency). */
  private Input input;

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
   * <p>This method calculates the velocity vector by processing the WASD input and applies it to
   * move the node smoothly in the 3D space by adjusting its position over time with respect to
   * `tpf` (time per frame).
   *
   * @param tpf Time per frame, used to ensure frame-rate-independent movement.
   */
  @Override
  public void update(float tpf) {
    SceneNode node = getOwner();
    Vector3f velocity = handleInput();

    Transform transform = node.getTransform();
    Vector3f position = transform.getPosition();

    transform.setPosition(position.add(velocity.mult(tpf)));
  }

  /**
   * Processes keyboard input to determine velocity. Handles the W/A/S/D keys to allow movement in
   * the 3D plane. Normalizes the vector to ensure diagonal movement doesn't lead to faster speeds.
   *
   * @return A velocity vector representing the computed movement direction and speed.
   */
  private Vector3f handleInput() {
    Vector3f velocity = new Vector3f();

    // Check for movement inputs
    if (input.isKeyPressed(Key.W)) velocity.addLocal(0, 0, -1);
    if (input.isKeyPressed(Key.A)) velocity.addLocal(-1, 0, 0);
    if (input.isKeyPressed(Key.S)) velocity.addLocal(0, 0, 1);
    if (input.isKeyPressed(Key.D)) velocity.addLocal(1, 0, 0);

    // Normalize diagonal movement to prevent unintended speed boosts
    if (velocity.length() > 0) {
      velocity.normalizeLocal().multLocal(speed);
    }
    return velocity;
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

  @Override
  public void onAttach() {}

  @Override
  public void onDetach() {}
}
