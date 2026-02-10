package engine.game.lifecycle;

/**
 * Listener interface for reacting to {@link Health} lifecycle events.
 *
 * <p>{@code HealthListener} provides a decoupled event mechanism that allows game systems to
 * respond to changes in health state without introducing direct dependencies on {@link Health}
 * itself.
 *
 * <p>This interface is intentionally minimal and engine-agnostic. Implementations may live in:
 *
 * <ul>
 *   <li>Gameplay logic (death handling, scoring, respawn)
 *   <li>UI systems (health bars, hit flashes)
 *   <li>Audio systems (hit sounds, death sounds)
 *   <li>Visual effects (damage feedback, screen shake)
 * </ul>
 *
 * <p>Listeners should avoid mutating {@link Health} directly inside callbacks to prevent feedback
 * loops and hard-to-debug state transitions.
 *
 * <p>Typical usage:
 *
 * <pre>{@code
 * health.addListener(new HealthListener() {
 *   public void onDamaged(float amount, float newHealth) {
 *     playHitSound();
 *   }
 *
 *   public void onDeath() {
 *     triggerGameOver();
 *   }
 * });
 * }</pre>
 */
public interface HealthListener {

  /**
   * Called when damage is applied to the health instance.
   *
   * @param amount the amount of damage applied
   * @param newHealth the resulting health value after damage
   */
  void onDamaged(float amount, float newHealth);

  /**
   * Called when health is restored.
   *
   * @param amount the amount of health restored
   * @param newHealth the resulting health value after healing
   */
  void onHealed(float amount, float newHealth);

  /**
   * Called once when health reaches zero.
   *
   * <p>This method is guaranteed to be called at most once per {@link Health} instance lifecycle.
   */
  void onDeath();
}
