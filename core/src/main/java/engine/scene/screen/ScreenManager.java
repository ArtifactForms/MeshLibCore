package engine.scene.screen;

import java.util.ArrayDeque;
import java.util.Deque;

import engine.runtime.input.Input;
import engine.runtime.input.KeyEvent;
import engine.runtime.input.KeyListener;
import engine.runtime.input.MouseEvent;
import engine.runtime.input.MouseListener;
import engine.runtime.input.MouseMode;
import engine.scene.Scene;

public class ScreenManager implements KeyListener, MouseListener {

  private Input input;

  private final Scene scene;

  private final Deque<GameScreen> stack = new ArrayDeque<>();

  public ScreenManager(Input input, Scene scene) {
    this.input = input;
    this.scene = scene;
    input.addKeyListener(this);
    input.addMouseListener(this);
  }

  public void onExit() {
    stack.clear();
    input.removeKeyListener(this);
    input.removeMouseListener(this);
  }

  public void push(GameScreen screen) {
    if (screen == null) {
      throw new IllegalArgumentException("Screen cannot be null.");
    }

    screen.onEnter();

    scene.addNode(screen.getRootNode());
    scene.getUIRoot().addChild(screen.getUiRootNode());

    stack.push(screen);

    updateCursorState();
  }

  public void pop() {
    if (stack.isEmpty()) return;

    GameScreen screen = stack.pop();
    screen.onExit();

    scene.removeNode(screen.getRootNode());
    scene.getUIRoot().removeChild(screen.getUiRootNode());

    updateCursorState();
  }

  private void updateCursorState() {
    if (stack.isEmpty()) {
      return;
    }
    input.setMouseMode(stack.peek().capturesMouse() ? MouseMode.LOCKED : MouseMode.ABSOLUTE);
  }

  public GameScreen peek() {
    return stack.peek();
  }

  @Override
  public void onMouseClicked(MouseEvent e) {
    for (GameScreen screen : stack) {
      if (screen.onMouseClicked(e)) {
        return;
      }
    }
  }

  @Override
  public void onMousePressed(MouseEvent e) {
    for (GameScreen screen : stack) {
      if (screen.onMousePressed(e)) {
        return;
      }
    }
  }

  @Override
  public void onMouseMoved(MouseEvent e) {
    for (GameScreen screen : stack) {
      if (screen.onMouseMoved(e)) {
        return;
      }
    }
  }

  @Override
  public void onMouseDragged(MouseEvent e) {
    for (GameScreen screen : stack) {
      if (screen.onMouseDragged(e)) {
        return;
      }
    }
  }

  @Override
  public void onMouseReleased(MouseEvent e) {
    for (GameScreen screen : stack) {
      if (screen.onMouseReleased(e)) {
        return;
      }
    }
  }

  @Override
  public void onKeyPressed(KeyEvent e) {
    for (GameScreen screen : stack) {
      if (screen.onKeyPressed(e)) {
        return;
      }
    }
  }

  @Override
  public void onKeyReleased(KeyEvent e) {
    for (GameScreen screen : stack) {
      if (screen.onKeyReleased(e)) {
        return;
      }
    }
  }

  @Override
  public void onKeyTyped(KeyEvent e) {
    for (GameScreen screen : stack) {
      if (screen.onKeyTyped(e)) {
        return;
      }
    }
  }
}
