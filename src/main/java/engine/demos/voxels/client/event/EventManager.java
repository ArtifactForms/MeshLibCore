package engine.demos.voxels.client.event;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

  private List<EventListener> listeners = new ArrayList<>();

  public void addListener(EventListener listener) {
    listeners.add(listener);
  }

  public void removeListener(EventListener listener) {
    listeners.remove(listener);
  }

  public void triggerEvent(Event event) {
    for (EventListener listener : listeners) {
      listener.onEvent(event);
    }
  }
}
