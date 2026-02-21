package engine.components.lifecycle;

import engine.components.AbstractComponent;
import engine.game.lifecycle.Lifetime;

/**
 * Engine-side adapter component that binds a {@link Lifetime} game object to a {@link
 * engine.scene.SceneNode}.
 *
 * <p><strong>{@code LifetimeComponent} lives in the engine layer.</strong> Its sole responsibility
 * is to translate the abstract concept of a finite lifetime into a concrete engine action:
 * destroying the owning scene node when the lifetime expires.
 *
 * <p>This class intentionally contains <em>no</em> lifetime rules of its own. All timing and
 * expiration logic is delegated to {@link Lifetime}, which resides in the game layer.
 *
 * <h3>Responsibilities</h3>
 *
 * <ul>
 *   <li>Advance the associated {@link Lifetime} each update
 *   <li>Observe when the lifetime expires
 *   <li>Trigger engine-specific destruction of the owning node
 * </ul>
 *
 * <h3>Non-responsibilities</h3>
 *
 * <ul>
 *   <li>Defining how long something should live
 *   <li>Deciding <em>why</em> something expires
 *   <li>Handling effects, scoring, or gameplay consequences
 * </ul>
 *
 * <p>This strict separation keeps the game layer engine-agnostic while allowing the engine to
 * manage object lifecycles deterministically.
 *
 * <h3>Typical usage</h3>
 *
 * <pre>{@code
 * SceneNode projectile = new SceneNode("Projectile");
 * projectile.addComponent(
 *     new LifetimeComponent(new Lifetime(2.0f))
 * );
 * }</pre>
 *
 * <p>More advanced behavior (e.g. pausing, extending lifetime, or reacting to expiration events)
 * should be implemented by composing or extending {@link Lifetime}, not by adding logic here.
 *
 * @see engine.game.lifecycle.Lifetime
 */
public final class LifetimeComponent extends AbstractComponent {

  /** Game-layer lifetime object that tracks remaining existence time. */
  private final Lifetime lifetime;

  /**
   * Creates a new {@code LifetimeComponent} that adapts the given lifetime to the engine's scene
   * graph.
   *
   * @param lifetime the game-layer lifetime to observe
   */
  public LifetimeComponent(Lifetime lifetime) {
    this.lifetime = lifetime;
  }

  @Override
  public void onUpdate(float tpf) {
    if (lifetime.update(tpf)) {
      getOwner().destroy();
    }
  }
}
