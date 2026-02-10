package engine.components.time;

import engine.components.AbstractComponent;
import engine.game.time.Cooldown;

/**
 * Engine adapter component for {@link Cooldown}.
 *
 * <p>{@code CooldownComponent} bridges the pure game-layer {@link Cooldown} into the engine's
 * component lifecycle by automatically updating it once per frame.
 *
 * <p>This component:
 *
 * <ul>
 *   <li>Does NOT trigger the cooldown
 *   <li>Does NOT react to input or events
 *   <li>Only advances time using {@code tpf}
 * </ul>
 *
 * <p>Gameplay systems (shooting, abilities, AI, UI) are expected to <strong>query or trigger the
 * cooldown explicitly</strong>.
 *
 * <p>Typical usage:
 *
 * <pre>{@code
 * Cooldown cooldown = new Cooldown(0.25f);
 * node.addComponent(new CooldownComponent(cooldown));
 *
 * // elsewhere (e.g. ShootComponent)
 * if (cooldown.trigger()) {
 *   fire();
 * }
 * }</pre>
 *
 * <p>This strict separation keeps time progression engine-driven, while decision-making remains in
 * the game layer.
 */
public final class CooldownComponent extends AbstractComponent {

  private final Cooldown cooldown;

  /**
   * Creates a new component that updates the given cooldown.
   *
   * @param cooldown the game-layer cooldown instance to update
   */
  public CooldownComponent(Cooldown cooldown) {
    this.cooldown = cooldown;
  }

  @Override
  public void onUpdate(float tpf) {
    cooldown.update(tpf);
  }

  /**
   * Exposes the underlying cooldown for gameplay systems.
   *
   * <p>This allows other components on the same node to query or trigger the cooldown without
   * duplicating timing logic.
   *
   * @return the wrapped {@link Cooldown}
   */
  public Cooldown getCooldown() {
    return cooldown;
  }
}
