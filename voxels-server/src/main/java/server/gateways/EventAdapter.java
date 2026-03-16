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
}
