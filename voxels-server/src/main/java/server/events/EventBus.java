package server.events;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventBus {

  private final Map<Class<? extends GameEvent>, List<Consumer<? extends GameEvent>>> listeners =
      new ConcurrentHashMap<>();

  public <T extends GameEvent> void register(Class<T> eventType, Consumer<T> listener) {
    listeners
        .computeIfAbsent(eventType, k -> new java.util.concurrent.CopyOnWriteArrayList<>())
        .add(listener);
  }

  @SuppressWarnings("unchecked")
  public <T extends GameEvent> void fire(T event) {
    if (event.isCancelled()) return;

    List<Consumer<? extends GameEvent>> eventListeners = listeners.get(event.getClass());

    if (eventListeners == null) {
      return;
    }

    for (Consumer<? extends GameEvent> listener : eventListeners) {

      ((Consumer<T>) listener).accept(event);

      if (event.isCancelled()) {
        break;
      }
    }
  }
}
