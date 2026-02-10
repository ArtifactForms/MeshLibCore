package engine.game.time;

/**
 * Represents a reusable cooldown timer for gameplay logic.
 *
 * <p>A {@code Cooldown} models the common game mechanic where an action can only be performed again
 * after a fixed amount of time has passed, such as:
 *
 * <ul>
 *   <li>weapon fire rates
 *   <li>abilities or spells
 *   <li>dash or dodge timers
 *   <li>enemy attack intervals
 * </ul>
 *
 * <p>This class is a <strong>pure game-layer utility</strong>:
 *
 * <ul>
 *   <li>No dependency on engine timing systems
 *   <li>No scene graph or component knowledge
 *   <li>Fully deterministic and testable
 * </ul>
 *
 * <p>The cooldown is updated externally via {@link #update(float)}, typically once per frame using
 * the frame time (tpf).
 *
 * <p>Typical usage:
 *
 * <pre>{@code
 * Cooldown fireCooldown = new Cooldown(0.5f);
 *
 * void update(float tpf) {
 *   fireCooldown.update(tpf);
 *
 *   if (fireCooldown.trigger()) {
 *     shoot();
 *   }
 * }
 * }</pre>
 *
 * <p>The cooldown starts in a <em>ready</em> state and becomes unavailable immediately after {@link
 * #trigger()} succeeds.
 */
public final class Cooldown {

  /** Fixed cooldown duration in seconds. */
  private final float duration;

  /** Remaining time until the cooldown becomes ready again. */
  private float remaining = 0;

  /**
   * Creates a new cooldown with the given duration.
   *
   * @param duration cooldown length in seconds
   */
  public Cooldown(float duration) {
    this.duration = duration;
  }

  /**
   * Advances the cooldown timer.
   *
   * <p>This method should be called regularly (usually once per frame) with the elapsed time since
   * the last update.
   *
   * @param tpf time per frame (delta time) in seconds
   */
  public void update(float tpf) {
    if (remaining > 0) remaining -= tpf;
  }

  /**
   * Returns whether the cooldown is ready to be triggered.
   *
   * @return {@code true} if the cooldown has expired or was never triggered
   */
  public boolean ready() {
    return remaining <= 0;
  }

  /**
   * Attempts to trigger the cooldown.
   *
   * <p>If the cooldown is ready, it is reset to its full duration and this method returns {@code
   * true}. Otherwise, nothing happens and {@code false} is returned.
   *
   * @return {@code true} if the cooldown was successfully triggered
   */
  public boolean trigger() {
    if (!ready()) return false;
    remaining = duration;
    return true;
  }

  /**
   * Returns the normalized progress of the cooldown in the range [0..1].
   *
   * <p>A value of:
   *
   * <ul>
   *   <li>{@code 0} means just triggered
   *   <li>{@code 1} means fully ready
   * </ul>
   *
   * <p>This is especially useful for UI elements such as cooldown indicators.
   *
   * @return normalized cooldown progress
   */
  public float get01() {
    return 1f - Math.max(0, remaining) / duration;
  }
}
