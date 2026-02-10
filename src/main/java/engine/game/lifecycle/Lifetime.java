package engine.game.lifecycle;

/**
 * Represents a finite lifetime for a game-domain object.
 *
 * <p><strong>Lifetime is a pure game-layer concept.</strong> It models the idea that something
 * exists only for a limited amount of time and then expires. Typical examples include:
 *
 * <ul>
 *   <li>Projectiles
 *   <li>Temporary effects (explosions, particles, buffs)
 *   <li>Timed pickups or hazards
 * </ul>
 *
 * <p>This class is intentionally <strong>engine-agnostic</strong>:
 *
 * <ul>
 *   <li>No knowledge of scene graphs or entities
 *   <li>No automatic destruction or removal
 *   <li>No rendering, physics, or component logic
 * </ul>
 *
 * <p>{@code Lifetime} only answers one question:
 *
 * <blockquote>
 *
 * Has the lifetime expired?
 *
 * </blockquote>
 *
 * <p>How expiration is handled (e.g. destroying a scene node, disabling input, playing an effect)
 * is the responsibility of an engine- or game-facing adapter, such as a {@code LifetimeComponent}.
 *
 * <h3>Usage</h3>
 *
 * <pre>{@code
 * Lifetime lifetime = new Lifetime(2.5f); // 2.5 seconds
 *
 * if (lifetime.update(tpf)) {
 *   // lifetime expired → react accordingly
 * }
 * }</pre>
 *
 * <p>The lifetime counts down monotonically and cannot be reset or extended by default. This keeps
 * the class minimal and predictable.
 *
 * <p>If more advanced behavior is needed (pausing, resetting, extending), it should be added
 * explicitly rather than implicitly.
 */
public final class Lifetime {

  /** Remaining lifetime in seconds. */
  private float remaining;

  /**
   * Creates a new {@code Lifetime} with the given duration.
   *
   * @param seconds the total lifetime duration in seconds
   */
  public Lifetime(float seconds) {
    this.remaining = seconds;
  }

  /**
   * Advances the lifetime by the given time delta.
   *
   * <p>This method subtracts {@code tpf} (time per frame) from the remaining lifetime and reports
   * whether the lifetime has expired.
   *
   * @param tpf time delta in seconds
   * @return {@code true} if the lifetime has expired (remaining time &le; 0), {@code false}
   *     otherwise
   */
  public boolean update(float tpf) {
    remaining -= tpf;
    return remaining <= 0;
  }
}
