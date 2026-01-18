package engine.components;

import math.Vector3f;

/**
 * Component that applies a uniform pulsing (scaling) effect to a scene node.
 *
 * <p>The {@code PulseComponent} uses a sinusoidal oscillation to smoothly scale its owner up and
 * down over time. The oscillation logic is provided by {@link OscillationComponent}.
 *
 * <h2>Behavior</h2>
 *
 * <ul>
 *   <li>The base scale is captured once when the component is attached
 *   <li>A uniform scale offset is applied to all axes (X, Y, Z)
 *   <li>No cumulative scaling drift occurs over time
 * </ul>
 *
 * <p>The pulsing motion is frame-rate independent and preserves the original scale as the neutral
 * (center) state of the oscillation.
 *
 * <h2>Typical Use Cases</h2>
 *
 * <ul>
 *   <li>Pickups and collectibles
 *   <li>Interactive or highlighted objects
 *   <li>UI elements placed in 3D space
 *   <li>Visual feedback and emphasis effects
 * </ul>
 *
 * <p>{@code PulseComponent} can be safely combined with other components such as floating,
 * rotation, or look-at behavior.
 *
 * @see OscillationComponent
 * @see FloatComponent
 */
public class PulseComponent extends OscillationComponent {

  /** Base scale captured on attachment to prevent cumulative scaling drift. */
  private Vector3f baseScale;

  /**
   * Creates a new pulsing component with the given amplitude and speed. The phase offset defaults
   * to {@code 0}.
   *
   * @param amplitude maximum uniform scale offset
   * @param speed pulsing speed factor
   */
  public PulseComponent(float amplitude, float speed) {
    super(amplitude, speed);
  }

  /**
   * Creates a new pulsing component with the given amplitude, speed, and phase offset.
   *
   * @param amplitude maximum uniform scale offset
   * @param speed pulsing speed factor
   * @param phase phase offset in radians
   */
  public PulseComponent(float amplitude, float speed, float phase) {
    super(amplitude, speed, phase);
  }

  /**
   * Captures the owner's current scale as the base scale for the pulsing motion.
   *
   * <p>A copy of the scale is stored to avoid reference-based side effects.
   */
  @Override
  public void onAttach() {
    baseScale = new Vector3f(getOwner().getTransform().getScale());
  }

  /**
   * Updates the pulsing motion for the current frame.
   *
   * <p>A uniform scale offset is applied to all axes using the sampled oscillation value, while
   * preserving the original base scale.
   *
   * @param tpf time per frame in seconds
   */
  @Override
  public void onUpdate(float tpf) {
    float offset = sample(tpf);

    Vector3f scale = new Vector3f(baseScale);
    scale.addLocal(offset, offset, offset);

    getOwner().getTransform().setScale(scale);
  }

  /**
   * Called when the component is detached from its owner.
   *
   * <p>This implementation performs no action.
   */
  @Override
  public void onDetach() {}
}
