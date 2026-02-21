package engine.components;

import math.Vector3f;

/**
 * Component that applies a vertical floating motion to a {@link engine.scene.SceneNode}.
 *
 * <p>The {@code FloatComponent} uses a sinusoidal oscillation to smoothly move its owner up and
 * down along the Y-axis while preserving the original base position. The oscillation logic itself
 * is provided by {@link OscillationComponent}.
 *
 * <h2>Behavior</h2>
 *
 * <ul>
 *   <li>The base position is captured once when the component is attached
 *   <li>The Y-coordinate is offset each frame using a sine wave
 *   <li>No cumulative drift occurs over time
 * </ul>
 *
 * <p>This component does not modify X or Z coordinates and is fully frame-rate independent.
 *
 * <h2>Typical Use Cases</h2>
 *
 * <ul>
 *   <li>Pickups and collectibles
 *   <li>Floating props or decorative objects
 *   <li>UI elements placed in 3D space
 *   <li>Debug or editor visualizations
 * </ul>
 *
 * <p>{@code FloatComponent} can be safely combined with other components such as rotation, pulsing,
 * or look-at behavior.
 *
 * @see OscillationComponent
 */
public class FloatComponent extends OscillationComponent {

  /** Base position captured on attachment to prevent cumulative drift. */
  private Vector3f basePosition;

  /**
   * Creates a new floating component with the given amplitude and speed. The phase offset defaults
   * to {@code 0}.
   *
   * @param amplitude maximum vertical offset
   * @param speed floating speed factor
   */
  public FloatComponent(float amplitude, float speed) {
    super(amplitude, speed);
  }

  /**
   * Creates a new floating component with the given amplitude, speed, and phase offset.
   *
   * @param amplitude maximum vertical offset
   * @param speed floating speed factor
   * @param phase phase offset in radians
   */
  public FloatComponent(float amplitude, float speed, float phase) {
    super(amplitude, speed, phase);
  }

  /**
   * Captures the owner's current position as the base position for the floating motion.
   *
   * <p>A copy of the position is stored to avoid reference-based side effects.
   */
  @Override
  public void onAttach() {
    basePosition = new Vector3f(getOwner().getTransform().getPosition());
  }

  /**
   * Updates the floating motion for the current frame.
   *
   * <p>The Y-coordinate of the base position is offset using the sampled oscillation value, while X
   * and Z remain unchanged.
   *
   * @param tpf time per frame in seconds
   */
  @Override
  public void onUpdate(float tpf) {
    float offsetY = sample(tpf);

    Vector3f pos = new Vector3f(basePosition);
    pos.y += offsetY;

    getOwner().getTransform().setPosition(pos);
  }

  /**
   * Called when the component is detached from its owner.
   *
   * <p>This implementation performs no action.
   */
  @Override
  public void onDetach() {}
}
