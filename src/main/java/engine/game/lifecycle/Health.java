package engine.game.lifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the health state of an entity in the game domain.
 *
 * <p><strong>Health is a pure game-lifecycle object.</strong> It contains only state and rules
 * related to hit points:
 *
 * <ul>
 *   <li>current health
 *   <li>maximum health
 *   <li>damage and healing rules
 *   <li>death detection
 * </ul>
 *
 * <p>This class is intentionally <strong>engine-agnostic</strong>:
 *
 * <ul>
 *   <li>No knowledge of scene graphs, components, or entities
 *   <li>No rendering or UI logic
 *   <li>No references to who caused damage or why
 * </ul>
 *
 * <p>Instead of directly triggering effects, {@code Health} emits lifecycle events via {@link
 * HealthListener}. This allows multiple systems to react independently, such as:
 *
 * <ul>
 *   <li>UI (health bars, hit flashes)
 *   <li>Audio (hit sounds, death sounds)
 *   <li>Gameplay logic (death handling, respawn, scoring)
 * </ul>
 *
 * <p>Typical usage:
 *
 * <pre>{@code
 * Health health = new Health(100);
 * health.addListener(new HealthListener() {
 *   public void onDamaged(float amount, float current) { ... }
 *   public void onDeath() { ... }
 * });
 * }</pre>
 *
 * <p>In practice, {@code Health} is usually wrapped by an engine-facing component (e.g. {@code
 * HealthComponent}) which adapts it to the scene graph or entity-component system.
 */
public class Health {

  /** Current health value, clamped between 0 and {@link #max}. */
  private float current;

  /** Maximum health value. Immutable after construction. */
  private final float max;

  /**
   * Registered listeners that react to health lifecycle events.
   *
   * <p>Listeners are notified when:
   *
   * <ul>
   *   <li>damage is applied
   *   <li>health is restored
   *   <li>the entity dies
   * </ul>
   */
  private final List<HealthListener> listeners = new ArrayList<>();

  /**
   * Creates a new {@code Health} instance with the given maximum value.
   *
   * @param max the maximum health value
   */
  public Health(float max) {
    this.max = max;
    this.current = max;
  }

  /**
   * Registers a listener for health lifecycle events.
   *
   * @param listener the listener to add
   */
  public void addListener(HealthListener listener) {
    listeners.add(listener);
  }

  /**
   * Applies damage to this health instance.
   *
   * <p>If the entity is already dead, this method has no effect. Damage is clamped so that health
   * never drops below zero.
   *
   * @param amount the amount of damage to apply
   */
  public void applyDamage(float amount) {
    if (isDead()) return;

    current = Math.max(0, current - amount);
    notifyDamaged(amount);

    if (current == 0) {
      notifyDeath();
    }
  }

  /**
   * Restores health by the given amount.
   *
   * <p>If the entity is dead, healing has no effect. Health is clamped to {@link #max}.
   *
   * @param amount the amount of health to restore
   */
  public void heal(float amount) {
    if (isDead()) return;

    float old = current;
    current = Math.min(max, current + amount);
    notifyHealed(current - old);
  }

  /**
   * Returns whether this health instance is dead.
   *
   * @return {@code true} if health is zero or below
   */
  public boolean isDead() {
    return current <= 0;
  }

  /**
   * Returns whether this health instance is alive.
   *
   * <p>Package-private by design: external systems should usually reason in terms of {@link
   * #isDead()}.
   */
  boolean isAlive() {
    return current > 0;
  }

  /** @return the current health value */
  public float getCurrent() {
    return current;
  }

  /** @return the maximum health value */
  public float getMax() {
    return max;
  }

  /**
   * Returns the normalized health value in the range [0..1].
   *
   * @return current / max, or 0 if max is zero
   */
  public float get01() {
    return max <= 0f ? 0f : current / max;
  }

  private void notifyDamaged(float amount) {
    for (HealthListener l : listeners) {
      l.onDamaged(amount, current);
    }
  }

  private void notifyHealed(float amount) {
    for (HealthListener l : listeners) {
      l.onHealed(amount, current);
    }
  }

  private void notifyDeath() {
    for (HealthListener l : listeners) {
      l.onDeath();
    }
  }
}
