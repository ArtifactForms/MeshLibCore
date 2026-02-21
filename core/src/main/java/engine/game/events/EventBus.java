package engine.game.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal synchronous event bus.
 *
 * <p>The event bus routes game-layer events to registered listeners. It contains no game logic and
 * no knowledge about event semantics.
 */
public final class EventBus {

  private final Map<Class<?>, List<EventListener<?>>> listeners = new HashMap<>();

  /** Registers a listener for a specific event type. */
  public <E extends GameEvent> void register(Class<E> eventType, EventListener<E> listener) {

    listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
  }

  /** Publishes an event immediately to all registered listeners. */
  @SuppressWarnings("unchecked")
  public <E extends GameEvent> void post(E event) {
    List<EventListener<?>> list = listeners.get(event.getClass());
    if (list == null) return;

    for (EventListener<?> l : list) {
      ((EventListener<E>) l).onEvent(event);
    }
  }
}
