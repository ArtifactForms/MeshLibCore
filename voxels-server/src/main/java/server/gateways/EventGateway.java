package server.gateways;

import server.events.GameEvent;

public interface EventGateway {

  void fire(GameEvent event);

  <T extends GameEvent> void register(Class<T> eventType, java.util.function.Consumer<T> listener);
}
