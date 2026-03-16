package server.gateways;

import server.events.GameEvent;

public interface EventGateway {

  void fire(GameEvent event);
}
