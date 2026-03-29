package server.gateways;

import server.events.EventBus;
import server.events.GameEvent;

public class EventAdapter implements EventGateway {

  private EventBus eventBus;

  public EventAdapter(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void fire(GameEvent event) {
    eventBus.fire(event);
  }

  @Override
  public <T extends GameEvent> void register(
      Class<T> eventType, java.util.function.Consumer<T> listener) {
    eventBus.register(eventType, listener);
  }
}
