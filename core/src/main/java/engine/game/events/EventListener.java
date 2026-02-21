package engine.game.events;

/**
 * Listener for a specific type of {@link GameEvent}.
 *
 * @param <E> event type
 */
@FunctionalInterface
public interface EventListener<E extends GameEvent> {

  void onEvent(E event);
}
