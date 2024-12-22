package workspace.ui.event;

import java.util.ArrayList;
import java.util.List;

/** Central event manager for handling mouse events. */
public class EventManager {

  private List<MouseEventListener> listeners = new ArrayList<>();

  /**
   * Adds a listener to receive mouse events.
   *
   * @param listener Listener to add.
   */
  public void addListener(MouseEventListener listener) {
    if (listener != null && !listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  /**
   * Removes a listener from receiving mouse events.
   *
   * @param listener Listener to remove.
   */
  public void removeListener(MouseEventListener listener) {
    listeners.remove(listener);
  }

  public void dispatchMouseClicked(MouseEvent e) {
    for (MouseEventListener listener : listeners) {
      listener.onMouseClicked(e);
    }
  }

  public void dispatchMouseDragged(MouseEvent e) {
    for (MouseEventListener listener : listeners) {
      listener.onMouseDragged(e);
    }
  }

  public void dispatchMouseReleased(MouseEvent e) {
    for (MouseEventListener listener : listeners) {
      listener.onMouseReleased(e);
    }
  }

  public void dispatchMousePressed(MouseEvent e) {
    for (MouseEventListener listener : listeners) {
      listener.onMousePressed(e);
    }
  }
}
