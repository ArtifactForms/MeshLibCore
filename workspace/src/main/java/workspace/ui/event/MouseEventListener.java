package workspace.ui.event;

/** Interface for listening to mouse-related events. */
public interface MouseEventListener {

  void onMouseClicked(MouseEvent e);

  void onMouseDragged(MouseEvent e);

  void onMouseReleased(MouseEvent e);

  void onMousePressed(MouseEvent e);
}
