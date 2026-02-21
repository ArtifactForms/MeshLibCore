package engine.components;

/**
 * Base class for components that apply a sinusoidal (oscillating) effect over time.
 *
 * <p>An {@code OscillationComponent} encapsulates the common logic for time-based sine-wave
 * modulation, such as floating, pulsing, shaking, or flickering effects. Subclasses are expected to
 * sample the oscillation value each frame and apply it to a specific property (e.g. position,
 * scale, color, light intensity).
 *
 * <h2>Oscillation Model</h2>
 *
 * The sampled value is computed as:
 *
 * <pre>
 * sin(time * speed + phase) * amplitude
 * </pre>
 *
 * where:
 *
 * <ul>
 *   <li><b>amplitude</b> defines the maximum deviation from the base value
 *   <li><b>speed</b> controls how fast the oscillation progresses
 *   <li><b>phase</b> offsets the oscillation to avoid synchronization
 * </ul>
 *
 * <p>The internal time accumulator is advanced using the frame time ({@code tpf}), making the
 * oscillation frame-rate independent.
 *
 * <h2>Typical Use Cases</h2>
 *
 * <ul>
 *   <li>Floating objects (position Y modulation)
 *   <li>Pulsing objects (uniform scale modulation)
 *   <li>Light intensity or color pulsing
 *   <li>Camera shake or subtle motion effects
 * </ul>
 *
 * <p>This class is intentionally abstract and does not modify any scene state by itself.
 */
public abstract class OscillationComponent extends AbstractComponent {

  /** Maximum deviation of the oscillation from the base value. */
  protected float amplitude;

  /** Speed factor controlling how fast the oscillation progresses. */
  protected float speed;

  /** Phase offset used to desynchronize multiple oscillators. */
  protected float phase;

  /** Internal time accumulator used for sampling the sine wave. */
  protected float time;

  /**
   * Creates a new oscillation component with the given amplitude and speed. The phase offset
   * defaults to {@code 0}.
   *
   * @param amplitude maximum oscillation offset
   * @param speed oscillation speed factor
   */
  protected OscillationComponent(float amplitude, float speed) {
    this(amplitude, speed, 0);
  }

  /**
   * Creates a new oscillation component with the given amplitude, speed, and phase.
   *
   * @param amplitude maximum oscillation offset
   * @param speed oscillation speed factor
   * @param phase phase offset in radians
   */
  protected OscillationComponent(float amplitude, float speed, float phase) {
    this.amplitude = amplitude;
    this.speed = speed;
    this.phase = phase;
  }

  /**
   * Samples the oscillation value for the current frame.
   *
   * <p>This method advances the internal time accumulator using the provided time-per-frame value
   * and returns the current oscillation offset.
   *
   * @param tpf time per frame in seconds
   * @return the current oscillation value
   */
  protected float sample(float tpf) {
    time += tpf;
    return (float) Math.sin(time * speed + phase) * amplitude;
  }

  /**
   * Sets the oscillation amplitude.
   *
   * @param amplitude maximum oscillation offset
   */
  public void setAmplitude(float amplitude) {
    this.amplitude = amplitude;
  }

  /**
   * Sets the oscillation speed.
   *
   * @param speed oscillation speed factor
   */
  public void setSpeed(float speed) {
    this.speed = speed;
  }

  /**
   * Sets the phase offset of the oscillation.
   *
   * @param phase phase offset in radians
   */
  public void setPhase(float phase) {
    this.phase = phase;
  }
}
