package server.events;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Simple, thread-safe event bus for registering and dispatching {@link GameEvent}s.
 *
 * <p>The {@code EventBus} allows listeners to subscribe to specific event types and receive them
 * when they are fired.
 *
 * <p>Listeners are registered per event class using a {@link Consumer}, enabling type-safe handling
 * without requiring explicit casting in user code.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * eventBus.register(PlayerQuitEvent.class, event -> {
 *   System.out.println("Player left: " + event.getPlayerId());
 * });
 * }</pre>
 *
 * <p>When an event is fired, only listeners registered for the exact event class will be invoked.
 * Inheritance hierarchies are not traversed.
 *
 * <p>This event bus supports {@link Cancellable} events:
 *
 * <ul>
 *   <li>If the event is already cancelled before dispatch, it will not be delivered.
 *   <li>If a listener cancels the event during dispatch, remaining listeners will not be invoked.
 * </ul>
 *
 * <p>Thread Safety:
 *
 * <ul>
 *   <li>Listener registration is thread-safe.
 *   <li>Event dispatch is safe for concurrent reads.
 *   <li>{@code CopyOnWriteArrayList} is used to avoid {@link ConcurrentModificationException}
 *       during iteration.
 * </ul>
 *
 * <p>Note: This is a minimal implementation. It does not support:
 *
 * <ul>
 *   <li>Listener priority ordering
 *   <li>Event inheritance (e.g. listening to base classes)
 *   <li>Listener unregistration
 * </ul>
 */
public class EventBus {

  private final Map<Class<? extends GameEvent>, List<Consumer<? extends GameEvent>>> listeners =
      new ConcurrentHashMap<>();

  /**
   * Registers a listener for a specific event type.
   *
   * @param eventType The class of the event to listen for.
   * @param listener The consumer that will handle the event.
   * @param <T> The event type.
   */
  public <T extends GameEvent> void register(Class<T> eventType, Consumer<T> listener) {
    listeners
        .computeIfAbsent(eventType, k -> new java.util.concurrent.CopyOnWriteArrayList<>())
        .add(listener);
  }

  /**
   * Fires an event and notifies all registered listeners for its type.
   *
   * <p>If the event implements {@link Cancellable}, dispatch behavior is affected:
   *
   * <ul>
   *   <li>If the event is already cancelled, it will not be dispatched.
   *   <li>If a listener cancels the event, remaining listeners will not be invoked.
   * </ul>
   *
   * @param event The event to fire.
   * @param <T> The event type.
   */
  @SuppressWarnings("unchecked")
  public <T extends GameEvent> void fire(T event) {
    boolean isCancellable = event instanceof Cancellable;

    if (isCancellable && ((Cancellable) event).isCancelled()) {
      return;
    }

    List<Consumer<? extends GameEvent>> eventListeners = listeners.get(event.getClass());
    if (eventListeners == null) return;

    for (Consumer<? extends GameEvent> listener : eventListeners) {
      ((Consumer<T>) listener).accept(event);

      if (isCancellable && ((Cancellable) event).isCancelled()) {
        break;
      }
    }
  }
}
