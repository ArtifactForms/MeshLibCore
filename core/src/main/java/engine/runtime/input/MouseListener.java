package engine.runtime.input;

public interface MouseListener {

  void onMouseClicked(MouseEvent e);

  void onMousePressed(MouseEvent e);

  void onMouseMoved(MouseEvent e);

  void onMouseDragged(MouseEvent e);

  void onMouseReleased(MouseEvent e);
}
